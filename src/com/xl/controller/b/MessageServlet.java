package com.xl.controller.b;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xl.bean.MessageBean;
import com.xl.socket.HttpHelloWorldServerHandler;
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
		if (HttpHelloWorldServerHandler.sessionMap.containsKey(mb.getToId())) {
			ChannelHandlerContext session = HttpHelloWorldServerHandler.sessionMap.get(mb.getToId());
			JSONObject toJo = new JSONObject();
			toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_SENDMESSAGE);
			toJo.put(StaticUtil.FROMID, mb.getFromId());
			toJo.put(StaticUtil.TOID, mb.getToId());
			toJo.put(StaticUtil.CONTENT, mb.getContent());
			toJo.put(StaticUtil.MSGID, "");
			toJo.put(StaticUtil.TIME, MyUtil.dateFormat.format(new Date()));
			session.writeAndFlush(toJo.toString()+"\n");

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
		if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
			if (HttpHelloWorldServerHandler.queueSessionMap.size() > 0
					&& !HttpHelloWorldServerHandler.queueSessionMap.containsKey(deviceId)) {
				String key = getKeyByDeviceId(deviceId);
				ChannelHandlerContext session = HttpHelloWorldServerHandler.queueSessionMap.get(key);// 得到对方的session

				/** 将id添加到各自的session中 **/
				setAttribute(session, deviceId);
				setAttribute(HttpHelloWorldServerHandler.sessionMap.get(deviceId), key);

				HttpHelloWorldServerHandler.queueSessionMap.remove(key);

				JSONObject toJo = new JSONObject();
				toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CONNECT_CHAT);
				toJo.put(StaticUtil.OTHERDEVICEID, deviceId);
				session.writeAndFlush(toJo.toString()+"\n");// 通知对方

				jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
				jo.put(StaticUtil.OTHERDEVICEID, key);
			} else {
				HttpHelloWorldServerHandler.queueSessionMap.put(deviceId,
						HttpHelloWorldServerHandler.sessionMap.get(deviceId));
				jo.put(ResultCode.STATUS, ResultCode.LOADING);
			}
		} else {
			jo.put(ResultCode.STATUS, ResultCode.FAIL);
		}
		return jo;
	}
	
	/**
	 * 退出等待聊天列队
	 * 
	 * @param deviceId
	 *            ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exitqueue")
	public @ResponseBody
	Object exitQueue(@RequestParam String deviceId) {
		HttpHelloWorldServerHandler.queueSessionMap.remove(deviceId);
		JSONObject jo = new JSONObject();
		jo.put(ResultCode.STATUS, ResultCode.LOADING);
		return jo;
	}

	@RequestMapping(value = "/closechat")
	public @ResponseBody
	Object closeChat(@RequestParam String deviceId) {
		JSONObject jo = new JSONObject();
		if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
			ChannelHandlerContext session = HttpHelloWorldServerHandler.sessionMap.get(deviceId);
			ArrayList<String> ids=(ArrayList<String>) session.attr(AttributeKey.valueOf(StaticUtil.IDS)).get();
			if(ids!=null){
				for (String string : ids) {
					ChannelHandlerContext temp = HttpHelloWorldServerHandler.sessionMap.get(string);
					if(temp!=null){
						JSONObject toJo = new JSONObject();
						toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CLOSE_CHAT);
						toJo.put(StaticUtil.DEVICEID, deviceId);
						temp.writeAndFlush(toJo.toString()+"\n");// 通知对方
					}
				}
			}
			jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
		} else {
			jo.put(ResultCode.STATUS, ResultCode.FAIL);
		}
		return jo;
	}

	public void setAttribute(ChannelHandlerContext session, String deviceId) {
		ArrayList<String> ids = new ArrayList<String>();
		if (session.attr(AttributeKey.valueOf(StaticUtil.IDS)).get()!=null) {
			ids = (ArrayList<String>) session.attr(AttributeKey.valueOf(StaticUtil.IDS)).get();
		}
		ids.remove(deviceId);
		ids.add(deviceId);
		session.attr(AttributeKey.valueOf(StaticUtil.IDS)).set(ids);
	}

	public String getKeyByDeviceId(String deviceId) {
		int radom = (int) (Math.random() * HttpHelloWorldServerHandler.queueSessionMap.size());
		String key = HttpHelloWorldServerHandler.queueSessionMap.keySet().toArray(
				new String[HttpHelloWorldServerHandler.queueSessionMap.size()])[radom];

		if (key.equals(deviceId)) {
			return getKeyByDeviceId(deviceId);
		} else {
			return key;
		}
	}
}
