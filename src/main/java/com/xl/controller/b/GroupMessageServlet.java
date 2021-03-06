package com.xl.controller.b;

import com.xl.bean.MessageBean;
import com.xl.bean.UserTable;
import com.xl.dao.UserDao;
import com.xl.dao.VipDao;
import com.xl.socket.HttpHelloWorldServerHandler;
import com.xl.socket.StaticUtil;
import com.xl.util.MyRequestUtil;
import com.xl.util.MyUtil;
import com.xl.util.ResultCode;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shen on 2015/9/7.
 */
@Controller
@RequestMapping(value = "/group")
public class GroupMessageServlet {

    @Resource
    public VipDao vipDao;
    @Resource
    public UserDao userDao;
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpSession session;

    /**
     * 群聊
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/groupchat")
    public
    @ResponseBody
    Object groupChat(@RequestParam String deviceId) throws Exception {
        Map<String, Object> jo = new HashMap<String, Object>();
        //从群组中先删除
        HttpHelloWorldServerHandler.groupSessionMap.remove(deviceId);
        //如果socket连接则添加到群组中
        if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
            UserTable ut = MyRequestUtil.getUserTable(session);//查询用户信息
            //通知全组人员有人进来了
            for (String key : HttpHelloWorldServerHandler.groupSessionMap.keySet()) {
                ChannelHandlerContext session = HttpHelloWorldServerHandler.groupSessionMap.get(key);
                if (session != null) {
                    Map<String, Object> responseJson = new HashMap<String, Object>();
//                    responseJson.put(StaticUtil.ORDER, StaticUtil.ORDER_GROUP_JOIN);
                    responseJson.put(StaticUtil.CONTENT, ut);
                    session.writeAndFlush(responseJson.toString() + "\n");
                }
            }
            //加入群组队列
            HttpHelloWorldServerHandler.groupSessionMap.put(deviceId, HttpHelloWorldServerHandler.sessionMap.get(deviceId));
            jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        }
        return jo;
    }

    /**
     * 给对方发送消息
     *
     * @param content 消息内容
     * @return
     */
    @RequestMapping(value = "/sendmessage")
    public
    @ResponseBody
    Object sendMessage(@RequestParam String content,
                       @RequestParam(required = false) String deviceId) throws Exception {
        Map<String, Object> jo = new HashMap<String, Object>();

        System.out.println(content.toString());

        UserTable ut = MyRequestUtil.getUserTable(session);

        Map<String, Object> toJo = new HashMap<String, Object>();

        toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_SENDMESSAGE);
        toJo.put(StaticUtil.FROMID, deviceId);
        toJo.put(StaticUtil.CONTENT, content);
        toJo.put(StaticUtil.MSGID, "");
        toJo.put(StaticUtil.MSGTYPE, MessageBean.TEXT);
        toJo.put(StaticUtil.CHATTYPE, 1);
        toJo.put(StaticUtil.TIME, MyUtil.dateFormat.format(new Date()));
        toJo.put(StaticUtil.SEX, ut.getUserBean().sex);

        for (String key : HttpHelloWorldServerHandler.groupSessionMap.keySet()) {
            ChannelHandlerContext session = HttpHelloWorldServerHandler.groupSessionMap.get(key);
            if (session != null) {
                toJo.put(StaticUtil.TOID, key);
                session.writeAndFlush(toJo.toString() + "\n");
            }
        }

        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.TIME, new Date());
        return jo;
    }
}
