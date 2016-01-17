package com.xl.controller.b;

import com.xl.bean.*;
import com.xl.dao.*;
import com.xl.exception.MyException;
import com.xl.util.*;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Shen on 2015/9/19.
 */
@Controller
@RequestMapping(value = "/user")
public class UserServlet {

    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpSession session;
    @Resource
    UserDao userDao;
    @Resource
    PayDao payDao;
    @Resource
    VipDao vipDao;
    @Resource(name = "vipCoinDao")
    VipCoinDao coinDao;
    @Resource
    AlbumDao albumDao;

    /**
     * 上传头像
     *
     * @param file
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/uploadlogo")
    public
    @ResponseBody
    Object uploadLogo(@RequestParam("file") MultipartFile file, @RequestParam String deviceId) throws Exception {
        if (!file.isEmpty()) {
            UserTable ut = MyRequestUtil.getUserTable(session);//从session里获取用户信息
            if (ut == null) {
                return MyJSONUtil.getErrorInfoJsonObject(StringUtil.FAIL_PLEASE_RELOGIN);
            }
            String dir = "/mnt/logo/" + ut.getDeviceId(); // 设定文件保存的目录
            String filename = new Date().getTime() + ".jpg"; // 得到上传时的文件名
            try {

                File oldFiles[] = new File(dir).listFiles();

                //存入地址为/mtn/logo/deviceId/logo.jpg
                File logo;
                FileUtils.writeByteArrayToFile(logo = new File(dir, filename),
                        file.getBytes());
                //图片压缩
                Thumbnails.of(logo)
                        .size(160, 160)
                        .outputFormat("jpg")
                        .toFiles(new Rename() {
                            @Override
                            public String apply(String name, ThumbnailParameter param) {
                                return name + "_" + (int) param.getSize().getWidth() + "x" + (int) param.getSize().getHeight() + ".jpg";
                            }
                        });

                //保存用户信息
                UserBean ub = ut.getUserBean();
                ub.setLogo("logo/" + deviceId + "/" + filename);
                ut.setUserBean(ub);
                userDao.update(ut);

                if (oldFiles != null) {
                    for (int i = 0; i < oldFiles.length; i++) {
                        if (oldFiles[i].exists()) {
                            oldFiles[i].delete();
                        }
                    }
                }

                //返回地址http://host/img/deviceId/logo_.jpg
                return MyJSONUtil.getSuccessJsonObject("logo", MyRequestUtil.getHost(request) + ub.getLogo());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new Exception("上传失败，请重试");
            }
        } else {
            throw new Exception("上传失败，请重试");
        }
    }

    /**
     * 上传相册
     *
     * @param file
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/uploadalbum")
    public
    @ResponseBody
    Object uploadAlbum(@RequestParam("file") MultipartFile file, @RequestParam String deviceId) throws Exception {
        if (!file.isEmpty()) {
            UserTable ut = MyRequestUtil.getUserTable(session);//从session里获取用户信息
            if (ut == null) {
                return MyJSONUtil.getErrorInfoJsonObject(StringUtil.FAIL_PLEASE_RELOGIN);
            }
            String dir = "/mnt/logo/" + ut.getDeviceId() + "/album"; // 设定文件保存的目录
            String filename = new Date().getTime() + ".jpg"; // 得到上传时的文件名
            try {

                //存入地址为/mtn/logo/deviceId/album/logo.jpg
                File logo;
                FileUtils.writeByteArrayToFile(logo = new File(dir, filename),
                        file.getBytes());

                //图片压缩
                thumb(logo, 320);
                thumb(logo, 640);

                Album album = new Album();
                album.setDeviceId(deviceId);
                album.setPath("logo/" + deviceId + "/album/" + filename);
                albumDao.save(album);

                album.setPath(MyRequestUtil.getHost(request) + album.getPath());
                //返回地址http://host/img/deviceId/logo_.jpg
                return MyJSONUtil.getSuccessJsonObject(album);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new Exception("上传失败，请重试");
            }
        } else {
            throw new Exception("上传失败，请重试");
        }
    }

    public void thumb(File file, int width) {
        try {
            Thumbnails.of(file)
                    .size(width, width)
                    .outputFormat("jpg")
                    .toFiles(new Rename() {
                        @Override
                        public String apply(String name, ThumbnailParameter param) {
                            return name + "_" + (int) param.getSize().getWidth() + "x" + (int) param.getSize().getHeight() + ".jpg";
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动充值
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/pay")
    public
    @ResponseBody
    Object pay(@RequestParam String orderId) throws Exception {
        UserTable ut = MyRequestUtil.getUserTable(session);//从session里获取用户信息

        if (!Bmob.isInit()) {
            Bmob.initBmob("2c9f0c5fbeb32f1b1bce828d29514f5d",
                    "592b4d559540535e66ad45364913ec1f");
        }

        String str = Bmob.findPayOrder(orderId);

        Pay order = MyJSONUtil.jsonToBean(str, Pay.class);

        if (order != null && order.getTrade_state().equals("SUCCESS")) {
            Pay dbOrder = payDao.getPay(orderId);
            if (dbOrder == null || !dbOrder.getTrade_state().equals("SUCCESS")) {
                //可以增加会员
                payDao.save(order);
                setVip(ut.getDeviceId(), 1);
                return MyJSONUtil.getSuccessJsonObject();
            } else {
                //不可以
                return MyJSONUtil.getErrorJsonObject();
            }
        } else {
            throw new MyException(StringUtil.FAIL_PAY);
        }
    }

    public void setVip(String deviceId, Integer month) throws Exception {
        try {
            Vip vip = vipDao.getVipByDeviceIdAll(deviceId);
            if (month == null) {
                month = 1;
            }
            if (vip == null) {
                vip = new Vip();
                vip.setDeviceId(deviceId.length() > 16 ? MyUtil.getmd5DeviceId(deviceId)
                        : deviceId);
                vip.setCreateTime(new Date().getTime());
                vip.setEndTime(vip.getCreateTime() + month * 30l * 24l * 60l
                        * 60l * 1000l);
            } else {
                long time = vip.getEndTime();
                long now = new Date().getTime();
                if (time > now) {// 没有过期
                    vip.setEndTime(vip.getEndTime() + month * 30l * 24l * 60l
                            * 60l * 1000l);
                } else {// 已过期
                    vip.setCreateTime(new Date().getTime());
                    vip.setEndTime(vip.getCreateTime() + month * 30l * 24l
                            * 60l * 60l * 1000l);
                }
            }
            vipDao.saveOrUpdate(vip);
            request.removeAttribute(deviceId);
        } catch (Exception e) {
            throw new MyException(StringUtil.FAIL_PAY);
        }
    }

    @RequestMapping(value = "/vipdetail")
    public
    @ResponseBody
    Object vipDetail(@RequestParam Integer id) {
        return MyJSONUtil.getSuccessJsonObject(ResultCode.INFO, coinDao.getCoinById(id));
    }

    @RequestMapping(value = "/deletealbum")
    public
    @ResponseBody
    Object deleteAlbum(@RequestParam String ids) throws Exception {
        try {
            List<Integer> list = MyJSONUtil.jsonToList(ids, Integer.class);
            for (int i = 0; i < list.size(); i++) {
                Album album = albumDao.queryById(MyRequestUtil.getUserTable(session).getDeviceId(),list.get(i));
                File file = new File("/mnt/" + album.getPath());
                deleteThumb(file, MyUtil._320x320);
                deleteThumb(file, MyUtil._640x640);
                deleteThumb(file, "");
                albumDao.deleteAlbumById(MyRequestUtil.getUserTable(session).getDeviceId(),list.get(i));
            }
        } catch (Exception e) {
            throw new Exception("删除失败");
        }
        return MyJSONUtil.getSuccessJsonObject();
    }

    void deleteThumb(File file, String str) {
        File t = new File(file.getPath() + str);
        if (t.exists()) {
            t.delete();
        }
    }
}
