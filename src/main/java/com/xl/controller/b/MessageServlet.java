package com.xl.controller.b;

import com.xl.bean.*;
import com.xl.dao.UnlineMessageDao;
import com.xl.dao.UserDao;
import com.xl.dao.VipDao;
import com.xl.socket.HttpHelloWorldServerHandler;
import com.xl.socket.StaticUtil;
import com.xl.util.DefaultDefaultValueProcessor;
import com.xl.util.MyRequestUtil;
import com.xl.util.MyUtil;
import com.xl.util.ResultCode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "/b")
public class MessageServlet {

    @Resource
    public UnlineMessageDao unlineMessageDao;
    @Resource
    public VipDao vipDao;
    @Resource
    public UserDao userDao;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;

    /**
     * 给对方发送消息
     *
     * @param content 消息内容
     * @return
     */
    @RequestMapping(value = "/sendmessage", method = {RequestMethod.GET,
            RequestMethod.POST})
    public
    @ResponseBody
    Object sendMessage(@RequestParam String content,
                       @RequestParam(required = false) Integer sex) throws Exception {
        System.out.println(content.toString());
        MessageBean mb = (MessageBean) JSONObject.toBean(
                JSONObject.fromObject(content), MessageBean.class);
        JSONObject jo = new JSONObject();
        JSONObject toJo = new JSONObject();
        toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_SENDMESSAGE);
        toJo.put(StaticUtil.FROMID, mb.getFromId());
        toJo.put(StaticUtil.TOID, mb.getToId());
        toJo.put(StaticUtil.CONTENT, mb.getContent());
        toJo.put(StaticUtil.MSGID, "");
        toJo.put(StaticUtil.MSGTYPE, mb.getMsgType());
        toJo.put(StaticUtil.TIME, MyUtil.dateFormat.format(new Date()));
        toJo.put(StaticUtil.SEX, sex);

        if (HttpHelloWorldServerHandler.sessionMap.containsKey(mb.getToId())) {// 查看对方是否连接

            ChannelHandlerContext session = HttpHelloWorldServerHandler.sessionMap
                    .get(mb.getToId());// 获取对方session

            session.writeAndFlush(toJo.toString() + "\n");

        } else {
            unlineMessageDao.save(new UnlineMessage(mb.getFromId(), mb
                    .getToId(), toJo.toString()));
        }
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.TIME, new Date());
        return jo;
    }

    /**
     * 加入等待聊天列队
     *
     * @param deviceId ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/joinqueue", method = {RequestMethod.GET,
            RequestMethod.POST})
    public
    @ResponseBody
    Object joinQueue(HttpServletRequest request, @RequestParam String deviceId,
                     @RequestParam(required = false) Integer sex) {
        JSONObject jo = new JSONObject();
        System.out.println("joinQueueqweqweqweqwe\t" + deviceId);

        HttpHelloWorldServerHandler.queueSessionMapVip.remove(deviceId);
        HttpHelloWorldServerHandler.queueSessionMap.remove(deviceId);

        if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
            System.out.println("containsKey\t" + deviceId);

            ChannelHandlerContext mySession = HttpHelloWorldServerHandler.sessionMap
                    .get(deviceId);
            mySession.attr(AttributeKey.valueOf(StaticUtil.SEX)).set(sex);

            ChannelHandlerContext otherSession = null;
            String otherDeviceId = null;

            LinkedHashMap<String, ChannelHandlerContext> mapVip = HttpHelloWorldServerHandler.queueSessionMapVip;

            for (String key : mapVip.keySet()) {
                if (!key.equals(deviceId)) {
                    ChannelHandlerContext session = mapVip.get(key);// 得到对方的session
                    int session_sex = (Integer) session.attr(
                            AttributeKey.valueOf(StaticUtil.SEX)).get();
                    int session_wantSex = (Integer) session.attr(
                            AttributeKey.valueOf(StaticUtil.WANTSEX)).get();

                    if (session_wantSex == sex) {
                        otherSession = session;
                        otherDeviceId = key;
                    }
                }
            }

            if (otherSession == null) {
                LinkedHashMap<String, ChannelHandlerContext> map = HttpHelloWorldServerHandler.queueSessionMap;
                for (String key : map.keySet()) {
                    if (!key.equals(deviceId)) {
                        ChannelHandlerContext session = map.get(key);// 得到对方的session
                        int session_sex = (Integer) session.attr(
                                AttributeKey.valueOf(StaticUtil.SEX)).get();

                        otherSession = session;
                        otherDeviceId = key;
                    }
                }
            }

            if (otherSession == null) {
                System.out.println("queueSessionMap\tunContainsKey\t"
                        + deviceId);
                HttpHelloWorldServerHandler.queueSessionMap.put(deviceId,
                        mySession);
                jo.put(ResultCode.STATUS, ResultCode.LOADING);
            } else {
                System.out.println("queueSessionMap\tcontainsKey\t"
                        + otherDeviceId);

                /** 将id添加到各自的session中 **/
                setAttribute(otherSession, deviceId);
                setAttribute(mySession, otherDeviceId);

                HttpHelloWorldServerHandler.queueSessionMap
                        .remove(otherDeviceId);// 从队列中移除
                HttpHelloWorldServerHandler.queueSessionMapVip
                        .remove(otherDeviceId);// 从队列中移除

                JSONObject toJo = new JSONObject();
                toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CONNECT_CHAT);
                toJo.put(StaticUtil.SEX,
                        mySession.attr(AttributeKey.valueOf(StaticUtil.SEX))
                                .get());
                toJo.put(StaticUtil.OTHERDEVICEID, deviceId);
                otherSession.writeAndFlush(toJo.toString() + "\n");// 通知对方

                jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
                jo.put(StaticUtil.SEX,
                        otherSession.attr(AttributeKey.valueOf(StaticUtil.SEX))
                                .get());
                jo.put(StaticUtil.OTHERDEVICEID, otherDeviceId);
            }

        } else {
            System.out.println("unContainsKey\t" + deviceId);
            jo.put(ResultCode.STATUS, ResultCode.FAIL);
        }
        return jo;
    }

    /**
     * 加入VIP等待聊天列队
     *
     * @param deviceId ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/joinqueuevip", method = {RequestMethod.GET,
            RequestMethod.POST})
    public
    @ResponseBody
    Object joinQueue(HttpServletRequest request, @RequestParam String deviceId,
                     @RequestParam(required = false) Integer sex,
                     @RequestParam(required = false) Integer wantSex) {
        JSONObject jo = new JSONObject();
        System.out.println("joinQueue\t" + deviceId);

        HttpHelloWorldServerHandler.queueSessionMapVip.remove(deviceId);
        HttpHelloWorldServerHandler.queueSessionMap.remove(deviceId);

        Vip vip = vipDao.getVipByDeviceId(deviceId);
        if (vip == null) {
            jo.put(ResultCode.STATUS, ResultCode.NOVIP);
            return jo;
        }

        if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
            System.out.println("containsKey\t" + deviceId);

            ChannelHandlerContext mySession = HttpHelloWorldServerHandler.sessionMap
                    .get(deviceId);
            mySession.attr(AttributeKey.valueOf(StaticUtil.SEX)).set(sex);
            mySession.attr(AttributeKey.valueOf(StaticUtil.WANTSEX)).set(
                    wantSex);

            ChannelHandlerContext otherSession = null;
            String otherDeviceId = null;

            LinkedHashMap<String, ChannelHandlerContext> mapVip = HttpHelloWorldServerHandler.queueSessionMapVip;

            for (String key : mapVip.keySet()) {
                if (!key.equals(deviceId)) {
                    ChannelHandlerContext session = mapVip.get(key);// 得到对方的session
                    int session_sex = (Integer) session.attr(
                            AttributeKey.valueOf(StaticUtil.SEX)).get();
                    int session_wantSex = (Integer) session.attr(
                            AttributeKey.valueOf(StaticUtil.WANTSEX)).get();

                    if (session_sex == wantSex && session_wantSex == sex) {
                        otherSession = session;
                        otherDeviceId = key;
                    }
                }
            }

            if (otherSession == null) {
                LinkedHashMap<String, ChannelHandlerContext> map = HttpHelloWorldServerHandler.queueSessionMap;
                for (String key : map.keySet()) {
                    if (!key.equals(deviceId)) {
                        ChannelHandlerContext session = map.get(key);// 得到对方的session
                        int session_sex = (Integer) session.attr(
                                AttributeKey.valueOf(StaticUtil.SEX)).get();

                        if (session_sex == wantSex) {
                            otherSession = session;
                            otherDeviceId = key;
                        }
                    }
                }
            }

            if (otherSession == null) {
                System.out.println("queueSessionMap\tunContainsKey\t"
                        + deviceId);
                HttpHelloWorldServerHandler.queueSessionMapVip.put(deviceId,
                        mySession);
                jo.put(ResultCode.STATUS, ResultCode.LOADING);
            } else {
                System.out.println("queueSessionMap\tcontainsKey\t"
                        + otherDeviceId);

                /** 将id添加到各自的session中 **/
                setAttribute(otherSession, deviceId);
                setAttribute(mySession, otherDeviceId);

                HttpHelloWorldServerHandler.queueSessionMap
                        .remove(otherDeviceId);// 从队列中移除
                HttpHelloWorldServerHandler.queueSessionMapVip
                        .remove(otherDeviceId);// 从队列中移除

                JSONObject toJo = new JSONObject();
                toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CONNECT_CHAT);
                toJo.put(StaticUtil.SEX,
                        mySession.attr(AttributeKey.valueOf(StaticUtil.SEX))
                                .get());
                toJo.put(StaticUtil.OTHERDEVICEID, deviceId);
                otherSession.writeAndFlush(toJo.toString() + "\n");// 通知对方

                jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
                jo.put(StaticUtil.SEX,
                        otherSession.attr(AttributeKey.valueOf(StaticUtil.SEX))
                                .get());
                jo.put(StaticUtil.OTHERDEVICEID, otherDeviceId);
            }

        } else {
            System.out.println("unContainsKey\t" + deviceId);
            jo.put(ResultCode.STATUS, ResultCode.FAIL);
        }
        return jo;
    }

    /**
     * 退出等待聊天列队
     *
     * @param deviceId ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/exitqueue")
    public
    @ResponseBody
    Object exitQueue(@RequestParam String deviceId) {
        System.out.println("exitQueue" + deviceId);
        HttpHelloWorldServerHandler.queueSessionMap.remove(deviceId);
        HttpHelloWorldServerHandler.queueSessionMapVip.remove(deviceId);
        JSONObject jo = new JSONObject();
        jo.put(ResultCode.STATUS, ResultCode.LOADING);
        return jo;
    }

    @RequestMapping(value = "/closechat")
    public
    @ResponseBody
    Object closeChat(@RequestParam String deviceId) {
        JSONObject jo = new JSONObject();
        if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
            ChannelHandlerContext session = HttpHelloWorldServerHandler.sessionMap
                    .get(deviceId);
            ArrayList<String> ids = (ArrayList<String>) session.attr(
                    AttributeKey.valueOf(StaticUtil.IDS)).get();
            if (ids != null) {
                for (String string : ids) {
                    ChannelHandlerContext temp = HttpHelloWorldServerHandler.sessionMap
                            .get(string);
                    if (temp != null) {
                        temp.attr(AttributeKey.valueOf(StaticUtil.IDS)).set(
                                null);
                        JSONObject toJo = new JSONObject();
                        toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CLOSE_CHAT);
                        toJo.put(StaticUtil.DEVICEID, deviceId);
                        temp.writeAndFlush(toJo.toString() + "\n");// 通知对方
                    }
                }
                session.attr(AttributeKey.valueOf(StaticUtil.IDS)).set(null);
            }
            jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        } else {
            jo.put(ResultCode.STATUS, ResultCode.FAIL);
        }
        return jo;
    }

    public void setAttribute(ChannelHandlerContext session, String deviceId) {
        ArrayList<String> ids = new ArrayList<String>();
        if (session.attr(AttributeKey.valueOf(StaticUtil.IDS)).get() != null) {
            ids = (ArrayList<String>) session.attr(
                    AttributeKey.valueOf(StaticUtil.IDS)).get();
        }
        ids.remove(deviceId);
        ids.add(deviceId);
        session.attr(AttributeKey.valueOf(StaticUtil.IDS)).set(ids);
    }

    public void getAttribute(ChannelHandlerContext session, String deviceId) {

    }

    public String getKeyByDeviceId(String deviceId) {
        int radom = (int) (Math.random() * HttpHelloWorldServerHandler.queueSessionMap
                .size());
        String key = HttpHelloWorldServerHandler.queueSessionMap.keySet()
                .toArray(
                        new String[HttpHelloWorldServerHandler.queueSessionMap
                                .size()])[radom];

        if (key.equals(deviceId)) {
            return getKeyByDeviceId(deviceId);
        } else {
            return key;
        }
    }

    @RequestMapping(value = "/uploadfile")
    public
    @ResponseBody
    Object upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, value = "thumb") MultipartFile thumb,
            @RequestParam String deviceId, @RequestParam String toId,
            @RequestParam String msgType,
            @RequestParam(required = false) Integer sex,
            @RequestParam(required = false) Integer voiceTime) throws Exception {
        JSONObject jo = new JSONObject();
        if (!file.isEmpty()) {
            String dir = "/mnt/" + toId;
            String filename = file.getOriginalFilename();
            try {
                FileUtils.writeByteArrayToFile(new File(dir, filename),
                        file.getBytes());

                String content = filename;
                if (thumb != null) {
                    FileUtils.writeByteArrayToFile(new File(dir, thumb.getOriginalFilename()), thumb.getBytes());
                    JSONObject radioBean = new JSONObject();
                    radioBean.put("file", filename);
                    radioBean.put("thumb", thumb.getOriginalFilename());
                    content = "\"" + radioBean + "\"";
                }

                // //////////////
                JSONObject toJo = new JSONObject();
                toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_SENDMESSAGE);
                toJo.put(StaticUtil.FROMID, deviceId);
                toJo.put(StaticUtil.TOID, toId);
                toJo.put(StaticUtil.CONTENT, content);
                toJo.put(StaticUtil.MSGID, new Date().getTime());
                toJo.put(StaticUtil.MSGTYPE, msgType);
                toJo.put(StaticUtil.TIME, MyUtil.dateFormat.format(new Date()));
                toJo.put(StaticUtil.VOICETIME, voiceTime);
                toJo.put(StaticUtil.SEX, sex);

                if (HttpHelloWorldServerHandler.sessionMap.containsKey(toId)) {// 查看对方是否连接
                    ChannelHandlerContext session = HttpHelloWorldServerHandler.sessionMap
                            .get(toId);// 获取对方session
                    session.writeAndFlush(toJo.toString() + "\n");
                } else {
                    unlineMessageDao.save(new UnlineMessage(deviceId, toId,
                            toJo.toString()));
                }
                jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
                jo.put(StaticUtil.TIME, new Date());
                return jo;
            } catch (IOException e) {
                throw new Exception("卧槽，上传失败");
            }
        } else {
            jo.put(ResultCode.STATUS, ResultCode.FAIL);
        }
        return jo;

    }

    @RequestMapping(value = "/download/{deviceId}/{fileName}")
    public void download(HttpServletRequest request,
                         HttpServletResponse response,
                         @PathVariable("fileName") String fileName,
                         @PathVariable("deviceId") String deviceId)
            throws IOException {
        String dir = "/mnt/" + deviceId;
        File downloadFile = new File(dir, fileName);
        response.setContentLength(new Long(downloadFile.length()).intValue());
        response.setHeader("Content-Disposition", "attachment; filename="
                + fileName);
        FileCopyUtils.copy(new FileInputStream(downloadFile),
                response.getOutputStream());
    }

    /**
     * 获取所有未读消息
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getallmessage")
    public
    @ResponseBody
    Object getAllMyMessage(@RequestParam String deviceId) {
        JSONObject jo = new JSONObject();
        List<UnlineMessage> list = unlineMessageDao
                .getMyUnlineMessage(deviceId);
        unlineMessageDao.deleteAll(list);
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.CONTENT, JSONArray.fromObject(list));
        return jo;
    }

    @RequestMapping(value = "/isvip")
    public
    @ResponseBody
    Object isVip(@RequestParam String deviceId) {
        JSONObject jo = new JSONObject();
        Vip vip = vipDao
                .getVipByDeviceId(deviceId);
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.CONTENT, vip);
        return jo;
    }

    /**
     * 充值会员
     *
     * @param deviceId
     * @param month
     * @return
     */
    @RequestMapping(value = "/setvip")
    public
    @ResponseBody
    Object setVip(@RequestParam String deviceId,
                  @RequestParam(required = false) Integer month,
                  @RequestParam(required = false) Boolean girl) {
        JSONObject jo = new JSONObject();
        try {
            Vip vip = vipDao
                    .getVipByDeviceIdAll(deviceId);
            if (month == null) {
                month = 1;
            }
            if (vip == null) {
                vip = new Vip();
                vip.setDeviceId(MyUtil.getmd5DeviceId(deviceId));
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
            if (girl != null && girl) {
                UserTable ut = userDao.getUserByDeviceId(deviceId);
                if (ut != null) {
                    UserBean ub = (UserBean) JSONObject.toBean(
                            JSONObject.fromObject(ut.getDetail()),
                            UserBean.class);
                    ub.setGirl(true);
                    ut.setDetail(JSONObject.fromObject(ub,
                            DefaultDefaultValueProcessor.getJsonConfig())
                            .toString());
                    userDao.update(ut);
                }
            }
            request.removeAttribute(deviceId);
            jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        } catch (Exception e) {
            jo.put(ResultCode.STATUS, ResultCode.FAIL);
            jo.put(StaticUtil.CONTENT, e.toString());
        }
        return jo;
    }

    /**
     * 设置用户信息
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/setuserdetail")
    public
    @ResponseBody
    Object setUserDetail(@RequestParam String deviceId,
                         @ModelAttribute UserBean user) throws Exception {
        JSONObject jo = new JSONObject();
        UserTable userTable = userDao.getUserByDeviceId(deviceId);
        if (userTable == null) {
            userTable = new UserTable();
            userTable.setDeviceId(deviceId);
        }

        UserBean userBean = null;
        if (MyUtil.isEmpty(userTable.getDetail())) {
            userBean = new UserBean();
        } else {
            userBean = (UserBean) JSONObject.toBean(
                    JSONObject.fromObject(userTable.getDetail()),
                    UserBean.class);
        }
        if (user != null) {
            if (user.sex != null) {
                if (userBean.sex != null) {
                    jo.put(ResultCode.STATUS, ResultCode.FAIL);
                    return jo;
                }
                userBean.sex = user.sex;
            }
            if (user.lat != null)
                userBean.lat = user.lat;
            if (user.lng != null)
                userBean.lng = user.lng;
            if (user.province != null)
                userBean.province = user.province;
            if (user.city != null)
                userBean.city = user.city;
            if (user.nickname != null) {
                if (MyUtil.isOverStepLength(user.nickname, 16)) {
                    throw new Exception("昵称不能超过8个汉字");
                }
                userBean.nickname = user.nickname;
            }
            if (user.birthday != null)
                userBean.birthday = user.birthday;
            if (user.desc != null) {
                if (MyUtil.isOverStepLength(user.desc, 300)) {

                    throw new Exception("个性签名不能超过150个汉字");
                }
                userBean.desc = user.desc;
            }
            if (user.zhifubao != null)
                userBean.zhifubao = user.zhifubao;
            if (user.weixin != null)
                userBean.weixin = user.weixin;
        }
        userTable.setDetail(MyUtil.toJson(userBean));

        userDao.saveOrUpdate(userTable);
        MyRequestUtil.setUserTable(session, userTable);
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.CONTENT, MyUtil.toJsonNoNull(userTable));
        return jo;
    }


}
