package com.xl.controller.b;

import com.xl.bean.*;
import com.xl.dao.AccountDao;
import com.xl.dao.ChatRoomDao;
import com.xl.dao.ChatRoomRequestDao;
import com.xl.dao.PayDao;
import com.xl.socket.HttpHelloWorldServerHandler;
import com.xl.socket.StaticUtil;
import com.xl.util.MyJSONUtil;
import com.xl.util.MyRequestUtil;
import com.xl.util.MyUtil;
import com.xl.util.ResultCode;
import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Shen on 2016/1/3.
 */
@Controller
@RequestMapping(value = "/chat")
public class ChatRoomServlet {

    @Autowired
    HttpSession session;

    @Autowired
    ChatRoomDao chatRoomDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    ChatRoomRequestDao chatRoomRequestDao;

    @Autowired
    PayDao payDao;

    @RequestMapping(value = "/createroom")
    public
    @ResponseBody
    Object createChatRoom(@ModelAttribute ChatRoom chatRoom) throws Exception {
        UserTable user = MyRequestUtil.getUserTable(session);
        ChatRoom room = chatRoomDao.findByDeviceId(user.getDeviceId());
        if (room != null) {
            throw new Exception("只能创建一个聊天室");
        }
        chatRoom.setCreateTime(new Date());
        chatRoomDao.save(chatRoom);
        return MyJSONUtil.getSuccessJsonObject(chatRoom);
    }

    @RequestMapping(value = "/deleteroom")
    public
    @ResponseBody
    Object deleteChatRoom(@RequestParam Integer roomId) throws Exception {
        UserTable user = MyRequestUtil.getUserTable(session);
        ChatRoom room = chatRoomDao.findChatRoomById(roomId);
        if (!user.getDeviceId().equals(room.getDeviceId())) {
            throw new Exception("这不是你的哦");
        }
        chatRoomDao.deleteByDeviceId(user.getDeviceId());
        return MyJSONUtil.getSuccessJsonObject();
    }

    @RequestMapping(value = "/roomlist")
    public
    @ResponseBody
    Object getChatRoomList(@RequestParam int page) throws Exception {
        return MyJSONUtil.getSuccessJsonObject(chatRoomDao.findChatRoomByPage(page));
    }

    @RequestMapping(value = "/sendchatrequest")
    public
    @ResponseBody
    Object sendChatRequest(@RequestParam Integer times, @RequestParam Integer roomId) throws Exception {
        UserTable userTable = MyRequestUtil.getUserTable(session);//获取当前用户
        ChatRoom room = chatRoomDao.findChatRoomById(roomId);//获取房间

        ChatRoomRequest requestTemp = chatRoomRequestDao.findChatRoomRequestByRoom(userTable.getDeviceId(), room);
        if (requestTemp != null) {
            return MyJSONUtil.getErrorInfoJsonObject("已经申请过了哟，正在等待对方同意。");
        }

        if (room.getMaxTime() < times) {//超出时间
            return MyJSONUtil.getErrorInfoJsonObject("你他喵在逗我？！");
        }

        Account account = accountDao.getAccountByDeviceId(userTable.getDeviceId());//获取账户
        int totalPrice = (int) (times * room.getPrice());
        if (account == null || account.getCoin() < totalPrice) {
            return MyJSONUtil.getErrorInfoJsonObject(ResultCode.NOMONEY, "钱不够，赶紧充值！");
        }

        //创建一个请求
        ChatRoomRequest request = new ChatRoomRequest();
        request.setCreateTime(new Date());
        request.setDeviceIdForMaster(room.getDeviceId());
        request.setDeviceIdForRequester(userTable.getDeviceId());
        request.setRoomId(roomId);
        request.setState(0);
        request.setTimes(times);

        request = (ChatRoomRequest) chatRoomRequestDao.save(request);

        //冻结金额
        double coin = times * room.getPrice();
        account.setColdCoin(account.getColdCoin() + coin);
        account.setCoin(account.getCoin() - coin);
        accountDao.update(account);

        //生成订单
        Pay pay = new Pay();
        pay.setOut_trade_no(UUID.randomUUID().toString());
        pay.setName("付费聊天");
        pay.setBody(userTable.getDeviceId());
        pay.setCreate_time(MyUtil.dateFormat.format(new Date()));
        pay.setPay_type(Pay.PayType.CHATREQUEST.getResult());
        pay.setTotal_fee(coin);
        pay.setTransaction_id(String.valueOf(request.getId()));
        pay.setTrade_state("WAITING");
        payDao.save(pay);

        //向房主发送聊天请求 socket
        ChannelHandlerContext session = HttpHelloWorldServerHandler.sessionMap.get(room.getDeviceId());

        JSONObject responseJson = new JSONObject();
        responseJson.put(StaticUtil.ORDER, StaticUtil.ORDER_CHATROOM_REQUEST);
        responseJson.put(StaticUtil.CONTENT, request);

        session.writeAndFlush(responseJson.toString() + "\n");

        //返回
        return MyJSONUtil.getSuccessJsonObject(request);
    }

    @RequestMapping(value = "/getchatrequestlist")
    public
    @ResponseBody
    Object getChatRequestList(@RequestParam int page) throws Exception {
        UserTable user = MyRequestUtil.getUserTable(session);
        return MyJSONUtil.getSuccessJsonObject(chatRoomRequestDao.findRequestListByDeviceId(user.getDeviceId(), page));
    }

    @RequestMapping(value = "/chatrequestagree")
    public
    @ResponseBody
    Object chatRequestAgree(@RequestParam int requestId) throws Exception {

        return null;
    }

    @RequestMapping(value = "/chatrequestdisagree")
    public
    @ResponseBody
    Object chatRequestDisAgree(@RequestParam int requestId) throws Exception {
        return null;
    }
}
