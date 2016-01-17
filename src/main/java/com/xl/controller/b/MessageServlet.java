package com.xl.controller.b;

import com.xl.bean.*;
import com.xl.dao.UnlineMessageDao;
import com.xl.dao.UserDao;
import com.xl.dao.VipDao;
import com.xl.socket.HttpHelloWorldServerHandler;
import com.xl.socket.StaticUtil;
import com.xl.util.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
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
        MessageBean mb = MyJSONUtil.jsonToBean(content, MessageBean.class);

        Map<String, Object> jo = new HashMap<String, Object>();
        Map<String, Object> toJo = new HashMap<String, Object>();
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

            session.writeAndFlush(MyJSONUtil.beanToJson(toJo) + "\n");

        } else {
            unlineMessageDao.save(new UnlineMessage(mb.getFromId(), mb
                    .getToId(), MyJSONUtil.beanToJson(toJo).toString()));
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
    Object joinQueue(@RequestParam String deviceId, @RequestParam(required = false) Integer sex) throws Exception {
        System.out.println("joinQueueqweqweqweqwe\t" + deviceId);

        Map<String, Object> jo = new HashMap<String, Object>();

        //加入之前先删除以前的队列
        HttpHelloWorldServerHandler.queueSessionMapVip.remove(deviceId);
        HttpHelloWorldServerHandler.queueSessionMap.remove(deviceId);

        //如果已连接socket
        if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
            System.out.println("containsKey\t" + deviceId);

            ChannelHandlerContext mySession = HttpHelloWorldServerHandler.sessionMap
                    .get(deviceId);
            mySession.attr(AttributeKey.valueOf(StaticUtil.SEX)).set(sex);
            //得到session，并且将性别存储

            ChannelHandlerContext otherSession = null;
            String otherDeviceId = null;

            //优先匹配vip
            LinkedHashMap<String, ChannelHandlerContext> mapVip = HttpHelloWorldServerHandler.queueSessionMapVip;

            for (String key : mapVip.keySet()) {
                if (!key.equals(deviceId)) {//如果不是自己
                    ChannelHandlerContext session = mapVip.get(key);// 得到对方的session
                    //得到对方想匹配的性别
                    int session_wantSex = (Integer) session.attr(AttributeKey.valueOf(StaticUtil.WANTSEX)).get();
                    //如果对方相匹配的性别与我的性别相符
                    //则配对成功
                    if (session_wantSex == sex) {
                        otherSession = session;
                        otherDeviceId = key;
                    }
                }
            }

            //如果没有找到vip配对，则进行普通配对
            if (otherSession == null) {
                LinkedHashMap<String, ChannelHandlerContext> map = HttpHelloWorldServerHandler.queueSessionMap;
                for (String key : map.keySet()) {
                    if (!key.equals(deviceId)) {//随便找人配对
                        ChannelHandlerContext session = map.get(key);// 得到对方的session

                        otherSession = session;
                        otherDeviceId = key;
                    }
                }
            }

            //如果还是找不到人，则进入等待队列
            if (otherSession == null) {
                System.out.println("queueSessionMap\tunContainsKey\t"
                        + deviceId);
                HttpHelloWorldServerHandler.queueSessionMap.put(deviceId,
                        mySession);
                jo.put(ResultCode.STATUS, ResultCode.LOADING);
            } else {//如果找到人了
                jo = matchSuccess(deviceId, jo, mySession, otherSession, otherDeviceId);
            }
            return MyJSONUtil.beanToJson(jo);
        } else {
            throw new Exception("请重新登录");
        }
    }

    private Map<String, Object> matchSuccess(@RequestParam String deviceId, Map<String, Object> jo, ChannelHandlerContext mySession, ChannelHandlerContext otherSession, String otherDeviceId) {
        System.out.println("queueSessionMap\tcontainsKey\t" + otherDeviceId);

        //将聊天对象的id添加到各自的session中
        setAttribute(otherSession, deviceId);
        setAttribute(mySession, otherDeviceId);

        // 从队列中移除
        HttpHelloWorldServerHandler.queueSessionMap.remove(otherDeviceId);
        // 从队列中移除
        HttpHelloWorldServerHandler.queueSessionMapVip.remove(otherDeviceId);

        //给匹配到的对方发送消息
        Map<String, Object> toJo = new HashMap<String, Object>();
        toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CONNECT_CHAT);
        toJo.put(StaticUtil.SEX, mySession.attr(AttributeKey.valueOf(StaticUtil.SEX)).get());
        toJo.put(StaticUtil.OTHERDEVICEID, deviceId);
        otherSession.writeAndFlush(toJo.toString() + "\n");// 通知对方

        //返回成功
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.SEX, otherSession.attr(AttributeKey.valueOf(StaticUtil.SEX)).get());
        jo.put(StaticUtil.OTHERDEVICEID, otherDeviceId);

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

        Map<String, Object> jo = new HashMap<String, Object>();
        System.out.println("joinQueue\t" + deviceId);

        //加入之前先删除以前的队列
        HttpHelloWorldServerHandler.queueSessionMapVip.remove(deviceId);
        HttpHelloWorldServerHandler.queueSessionMap.remove(deviceId);

        //判断是否为vip
        Vip vip = vipDao.getVipByDeviceId(deviceId);
        if (vip == null) {
            jo.put(ResultCode.STATUS, ResultCode.NOVIP);
            return jo;
        }

        //如果已经连接socket
        if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
            System.out.println("containsKey\t" + deviceId);

            //获取到session
            ChannelHandlerContext mySession = HttpHelloWorldServerHandler.sessionMap.get(deviceId);

            //将自己的性别和想要匹配的性别存储
            mySession.attr(AttributeKey.valueOf(StaticUtil.SEX)).set(sex);
            mySession.attr(AttributeKey.valueOf(StaticUtil.WANTSEX)).set(wantSex);

            ChannelHandlerContext otherSession = null;
            String otherDeviceId = null;

            //先从vip中寻找匹配对象
            LinkedHashMap<String, ChannelHandlerContext> mapVip = HttpHelloWorldServerHandler.queueSessionMapVip;

            for (String key : mapVip.keySet()) {
                if (!key.equals(deviceId)) {
                    ChannelHandlerContext session = mapVip.get(key);// 得到对方的session
                    //得到对方性别和对方想要性别
                    int session_sex = (Integer) session.attr(AttributeKey.valueOf(StaticUtil.SEX)).get();
                    int session_wantSex = (Integer) session.attr(AttributeKey.valueOf(StaticUtil.WANTSEX)).get();
                    //判断对方性别是否是自己的想要的，并且对方想要的性别是否是自己的性别
                    if (session_sex == wantSex && session_wantSex == sex) {
                        otherSession = session;
                        otherDeviceId = key;
                    }
                }
            }

            //如果没找到，就从普通列表找人
            if (otherSession == null) {
                LinkedHashMap<String, ChannelHandlerContext> map = HttpHelloWorldServerHandler.queueSessionMap;
                for (String key : map.keySet()) {
                    if (!key.equals(deviceId)) {
                        ChannelHandlerContext session = map.get(key);// 得到对方的session
                        int session_sex = (Integer) session.attr(AttributeKey.valueOf(StaticUtil.SEX)).get();
                        //判断是不是自己想要匹配的性别
                        if (session_sex == wantSex) {
                            otherSession = session;
                            otherDeviceId = key;
                        }
                    }
                }
            }

            //如果依然没有匹配到，则进入等待列表
            if (otherSession == null) {
                System.out.println("queueSessionMap\tunContainsKey\t" + deviceId);
                HttpHelloWorldServerHandler.queueSessionMapVip.put(deviceId, mySession);
                jo.put(ResultCode.STATUS, ResultCode.LOADING);
            } else {
                jo = matchSuccess(deviceId, jo, mySession, otherSession, otherDeviceId);
            }
            return MyJSONUtil.beanToJson(jo);
        } else {
            System.out.println("unContainsKey\t" + deviceId);
            return MyJSONUtil.getErrorJsonObject();
        }
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
        return MyJSONUtil.getSuccessJsonObject();
    }

    @RequestMapping(value = "/closechat")
    public
    @ResponseBody
    Object closeChat(@RequestParam String deviceId) {
        Map<String, Object> jo = new HashMap<String, Object>();
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
                        Map<String, Object> toJo = new HashMap<String, Object>();
                        toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CLOSE_CHAT);
                        toJo.put(StaticUtil.DEVICEID, deviceId);
                        temp.writeAndFlush(MyJSONUtil.beanToJson(toJo + "\n"));// 通知对方
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

        Map<String, Object> jo = new HashMap<String, Object>();
        if (!file.isEmpty()) {
            String dir = "/mnt/" + toId;
            String filename = file.getOriginalFilename();
            try {
                FileUtils.writeByteArrayToFile(new File(dir, filename),
                        file.getBytes());

                String content = filename;
                if (thumb != null) {
                    FileUtils.writeByteArrayToFile(new File(dir, thumb.getOriginalFilename()), thumb.getBytes());
                    Map<String, Object> radioBean = new HashMap<String, Object>();
                    radioBean.put("file", filename);
                    radioBean.put("thumb", thumb.getOriginalFilename());
                    content = MyJSONUtil.beanToJson(radioBean).toString();
                }

                // //////////////
                Map<String, Object> toJo = new HashMap<String, Object>();
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
                    session.writeAndFlush(MyJSONUtil.beanToJson(toJo) + "\n");
                } else {
                    unlineMessageDao.save(new UnlineMessage(deviceId, toId,
                            MyJSONUtil.beanToJson(toJo).toString()));
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
    Object getAllMyMessage(@RequestParam String deviceId) throws Exception {
        List<UnlineMessage> list = unlineMessageDao
                .getMyUnlineMessage(deviceId);
        unlineMessageDao.deleteAll(MyRequestUtil.getUserTable(session).getDeviceId(), list);
        return MyJSONUtil.getSuccessJsonObject(list);
    }

    @RequestMapping(value = "/isvip")
    public
    @ResponseBody
    Object isVip(@RequestParam String deviceId) {
        Vip vip = vipDao.getVipByDeviceId(deviceId);
        return MyJSONUtil.getSuccessJsonObject(vip);
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
        Map<String, Object> jo = new HashMap<String, Object>();
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
                    UserBean ub = ut.getUserBean();
                    ub.setGirl(true);
                    ut.setUserBean(ub);
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
        Map<String, Object> jo = new HashMap<String, Object>();
        UserTable userTable = userDao.getUserByDeviceId(deviceId);
        if (userTable == null) {
            userTable = new UserTable();
            userTable.setDeviceId(deviceId);
        }

        UserBean userBean = null;
        if (MyUtil.isEmpty(userTable.getDetail())) {
            userBean = new UserBean();
        } else {
            userBean = userTable.getUserBean();
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
        userTable.setUserBean(userBean);

        userDao.saveOrUpdate(userTable);
        MyRequestUtil.setUserTable(session, userTable);
        return MyJSONUtil.getSuccessJsonObject(userTable);
    }


}
