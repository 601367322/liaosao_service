package com.xl.controller.b;

import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xl.bean.MessageBean;
import com.xl.socket.Handler;
import com.xl.socket.StaticUtil;
import com.xl.util.MyUtil;
import com.xl.util.ResultCode;

@Controller
@RequestMapping(value="/b")
public class MessageServlet {

	/**
	 * 给对方发送消息
	 * 
	 * @param content
	 *            消息内容
	 * @param toId
	 *            对面的ID
	 * @param fromId
	 *            自己的ID
	 * @return
	 */
	@RequestMapping(value = "/sendmessage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object sendMessage(@RequestParam String content) {
		System.out.println(content.toString());
		MessageBean mb = (MessageBean) JSONObject.toBean(
				JSONObject.fromObject(content), MessageBean.class);
		JSONObject jo = new JSONObject();
		if (Handler.sessionMap.containsKey(mb.getToId())) {
			IoSession session = Handler.sessionMap.get(mb.getToId());
			JSONObject toJo = new JSONObject();
			toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_SENDMESSAGE);
			toJo.put(StaticUtil.FROMID, mb.getFromId());
			toJo.put(StaticUtil.TOID, mb.getToId());
			toJo.put(StaticUtil.CONTENT, mb.getContent());
			toJo.put(StaticUtil.MSGID, "");
			toJo.put(StaticUtil.TIME, MyUtil.dateFormat.format(new Date()));
			session.write(toJo.toString());

			jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
			jo.put(StaticUtil.TIME, new Date());
		} else {
			jo.put(ResultCode.STATUS, ResultCode.FAIL);
		}
		return jo;
	}

	/**
	 * 加入等待聊天列队
	 * 
	 * @param deviceId
	 *            ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/joinqueue", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object joinQueue(@RequestParam String deviceId) {
		JSONObject jo = new JSONObject();
		if (Handler.sessionMap.containsKey(deviceId)) {
			if (Handler.queueSessionMap.size() > 0
					&& !Handler.queueSessionMap.contains(deviceId)) {
				String key = getKeyByDeviceId(deviceId);
				IoSession session = Handler.queueSessionMap.get(key);// 得到对方的session

				/** 将id添加到各自的session中 **/
				setAttribute(session, deviceId);
				setAttribute(Handler.sessionMap.get(deviceId), key);

				Handler.queueSessionMap.remove(key);

				JSONObject toJo = new JSONObject();
				toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CONNECT_CHAT);
				toJo.put(StaticUtil.OTHERDEVICEID, deviceId);
				session.write(toJo.toString());// 通知对方

				jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
				jo.put(StaticUtil.OTHERDEVICEID, key);
			} else {
				Handler.queueSessionMap.put(deviceId,
						Handler.sessionMap.get(deviceId));
				jo.put(ResultCode.STATUS, ResultCode.LOADING);
			}
		} else {
			jo.put(ResultCode.STATUS, ResultCode.FAIL);
		}
		return jo;
	}

	@RequestMapping(value = "/closeChat", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object closeChat() {
		return null;
	}

	public void setAttribute(IoSession session, String deviceId) {
		ArrayList<String> ids = new ArrayList<String>();
		if (session.containsAttribute(StaticUtil.IDS)) {
			ids = (ArrayList<String>) session.getAttribute(StaticUtil.IDS);
		}
		ids.remove(deviceId);
		ids.add(deviceId);
	}

	public String getKeyByDeviceId(String deviceId) {
		int radom = (int) (Math.random() * Handler.queueSessionMap.size());
		String key = Handler.queueSessionMap.keySet().toArray(
				new String[Handler.queueSessionMap.size()])[radom];

		if (key.equals(deviceId)) {
			return getKeyByDeviceId(deviceId);
		} else {
			return key;
		}
	}
}
