package com.xl.controller.b;

import com.xl.bean.ChatRoom;
import com.xl.bean.UserTable;
import com.xl.dao.ChatRoomDao;
import com.xl.util.MyJSONUtil;
import com.xl.util.MyRequestUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

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
        JSONObject json = MyJSONUtil.getSuccessJsonObject(chatRoom);
        return json;
    }
}
