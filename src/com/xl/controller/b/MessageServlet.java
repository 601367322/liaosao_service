package com.xl.controller.b;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sun.istack.internal.Nullable;
import com.xl.bean.MessageBean;
import com.xl.bean.UnlineMessage;
import com.xl.dao.UnlineMessageDao;
import com.xl.dao.UserDao;
import com.xl.socket.HttpHelloWorldServerHandler;
import com.xl.socket.StaticUtil;
import com.xl.util.MyUtil;
import com.xl.util.ResultCode;

@Controller
@RequestMapping(value = "/b")
public class MessageServlet {

	@Resource
	public UnlineMessageDao unlineMessageDao;

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
	Object sendMessage(@RequestParam String content,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) Integer sex) {
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
	 * @param deviceId
	 *            ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/joinqueue", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object joinQueue(HttpServletRequest request, @RequestParam String deviceId,
			@RequestParam(required = false) Integer sex) {
		JSONObject jo = new JSONObject();
		System.out.println("joinQueue\t"
				+ deviceId);
		if (HttpHelloWorldServerHandler.sessionMap.containsKey(deviceId)) {
			System.out.println("containsKey\t"
					+ deviceId);
			ChannelHandlerContext mySession = HttpHelloWorldServerHandler.sessionMap
					.get(deviceId);
			mySession.attr(AttributeKey.valueOf(StaticUtil.SEX)).set(sex);
			if (HttpHelloWorldServerHandler.queueSessionMap.size() > 0
					&& !HttpHelloWorldServerHandler.queueSessionMap
							.containsKey(deviceId)) {
				
				String key = getKeyByDeviceId(deviceId);// 随机抽取聊天对象
				ChannelHandlerContext session = HttpHelloWorldServerHandler.queueSessionMap
						.get(key);// 得到对方的session

				System.out.println("queueSessionMap\tcontainsKey\t"
						+ key);
				
				/** 将id添加到各自的session中 **/
				setAttribute(session, deviceId);
				setAttribute(mySession, key);

				HttpHelloWorldServerHandler.queueSessionMap.remove(key);// 从队列中移除

				JSONObject toJo = new JSONObject();
				toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_CONNECT_CHAT);
				toJo.put(StaticUtil.SEX,
						mySession.attr(AttributeKey.valueOf(StaticUtil.SEX))
								.get());
				toJo.put(StaticUtil.OTHERDEVICEID, deviceId);
				session.writeAndFlush(toJo.toString() + "\n");// 通知对方

				jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
				jo.put(StaticUtil.SEX,
						session.attr(AttributeKey.valueOf(StaticUtil.SEX))
								.get());
				jo.put(StaticUtil.OTHERDEVICEID, key);
			} else {
				System.out.println("queueSessionMap\tunContainsKey\t"
						+ deviceId);
				HttpHelloWorldServerHandler.queueSessionMap.put(deviceId,
						mySession);
				jo.put(ResultCode.STATUS, ResultCode.LOADING);
			}
		} else {
			System.out.println("unContainsKey\t"
					+ jo.getString(StaticUtil.DEVICEID));
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
	public @ResponseBody
	Object upload(HttpServletRequest request,
			@RequestParam("file") MultipartFile file,
			@RequestParam String deviceId, @RequestParam String toId,
			@RequestParam String msgType,
			@RequestParam(required = false) Integer sex,
			@RequestParam(required = false) Integer voiceTime) {
		JSONObject jo = new JSONObject();
		if (!file.isEmpty()) {
			ServletContext sc = request.getSession().getServletContext();
			String dir = sc.getRealPath("/upload/" + toId); // 设定文件保存的目录
			String filename = file.getOriginalFilename(); // 得到上传时的文件名
			try {
				FileUtils.writeByteArrayToFile(new File(dir, filename),
						file.getBytes());

				// //////////////
				JSONObject toJo = new JSONObject();
				toJo.put(StaticUtil.ORDER, StaticUtil.ORDER_SENDMESSAGE);
				toJo.put(StaticUtil.FROMID, deviceId);
				toJo.put(StaticUtil.TOID, toId);
				toJo.put(StaticUtil.CONTENT, filename);
				toJo.put(StaticUtil.MSGID, "");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				jo.put(ResultCode.STATUS, ResultCode.FAIL);
			}
			System.out.println("upload over. " + filename);
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
			throws FileNotFoundException, IOException {
		ServletContext sc = request.getSession().getServletContext();
		String dir = sc.getRealPath("/upload/" + deviceId);
		File downloadFile = new File(dir, fileName);
		response.setContentLength(new Long(downloadFile.length()).intValue());
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		FileCopyUtils.copy(new FileInputStream(downloadFile),
				response.getOutputStream());
	}

	@RequestMapping(value = "/getallmessage")
	public @ResponseBody
	Object getAllMyMessage(@RequestParam String deviceId) {
		JSONObject jo = new JSONObject();
		List<UnlineMessage> list = unlineMessageDao
				.getMyUnlineMessage(deviceId);
		unlineMessageDao.deleteAll(list);
		jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
		jo.put(StaticUtil.CONTENT, JSONArray.fromObject(list));
		return jo;
	}
}
