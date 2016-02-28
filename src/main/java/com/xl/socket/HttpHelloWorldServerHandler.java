package com.xl.socket;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

@Sharable
public class HttpHelloWorldServerHandler extends
        SimpleChannelInboundHandler<String> {

    public static ConcurrentHashMap<String, ChannelHandlerContext> sessionMap = new ConcurrentHashMap<String, ChannelHandlerContext>();
    public static LinkedHashMap<String, ChannelHandlerContext> queueSessionMap = new LinkedHashMap<String, ChannelHandlerContext>();
    public static LinkedHashMap<String, ChannelHandlerContext> queueSessionMapVip = new LinkedHashMap<String, ChannelHandlerContext>();
    public static LinkedHashMap<String, ChannelHandlerContext> groupSessionMap = new LinkedHashMap<String, ChannelHandlerContext>();

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String str)
            throws Exception {
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
        super.channelInactive(session);
        System.out.println("channelInactive\t" + session.attr(
                AttributeKey.valueOf(StaticUtil.DEVICEID)).get());
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