package com.xl.socket;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.AttributeKey;

@Sharable
public class HttpHelloWorldServerHandler extends
		SimpleChannelInboundHandler<String> {

	public static ConcurrentHashMap<String, ChannelHandlerContext> sessionMap = new ConcurrentHashMap<String, ChannelHandlerContext>();
	public static ConcurrentHashMap<String, ChannelHandlerContext> queueSessionMap = new ConcurrentHashMap<String, ChannelHandlerContext>();

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String str)
			throws Exception {
		// str就是消息啦，在这里就可以进行业务逻辑了
		System.out.println(str.toString());
		JSONObject jo = JSONObject.fromObject(str);
		int order = jo.getInt(StaticUtil.ORDER);
		switch (order) {
		case StaticUtil.ORDER_CONNECT:
			ctx.attr(AttributeKey.valueOf(StaticUtil.DEVICEID)).set(
					jo.getString(StaticUtil.DEVICEID));
			sessionMap.put(jo.getString(StaticUtil.DEVICEID), ctx);
			System.out.println("sessionMap.put\t"
					+ jo.getString(StaticUtil.DEVICEID));
			break;
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext session) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(session);
		sessionMap.remove(session.attr(
				AttributeKey.valueOf(StaticUtil.DEVICEID)).get());
		queueSessionMap.remove(session.attr(
				AttributeKey.valueOf(StaticUtil.DEVICEID)).get());

		if (session.attr(AttributeKey.valueOf(StaticUtil.IDS)).get() != null) {

			JSONObject jo = new JSONObject();
			jo.put(StaticUtil.ORDER, StaticUtil.ORDER_CLOSE_CHAT);
			jo.put(StaticUtil.DEVICEID, session.attr(
					AttributeKey.valueOf(StaticUtil.DEVICEID)).get());

			ArrayList<String> ids = (ArrayList<String>) session.attr(
					AttributeKey.valueOf(StaticUtil.IDS)).get();
			for (String deviceId : ids) {
				if (sessionMap.containsKey(deviceId)) {
					ChannelHandlerContext other = sessionMap.get(deviceId);
					other.writeAndFlush(jo.toString() + "\n");
				}
			}
		}
	}
}