package com.xl.socket;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class Handler extends IoHandlerAdapter {
	
	public static ConcurrentHashMap<String, IoSession> sessionMap=new ConcurrentHashMap<String, IoSession>();
	public static ConcurrentHashMap<String, IoSession> queueSessionMap=new ConcurrentHashMap<String, IoSession>();
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		sessionMap.remove(session.getAttribute(StaticUtil.DEVICEID));
	}

	@Override
	public void messageReceived(final IoSession session, Object message)
			throws Exception {
		System.out.println(message.toString());
		JSONObject jo = JSONObject.fromObject(message);
		int order= jo.getInt(StaticUtil.ORDER);
		switch (order) {
		case StaticUtil.ORDER_CONNECT:
			session.setAttribute(StaticUtil.DEVICEID, jo.getString(StaticUtil.DEVICEID));
			sessionMap.put(jo.getString(StaticUtil.DEVICEID), session);
			break;
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		sessionMap.remove(session.getAttribute(StaticUtil.DEVICEID));
		queueSessionMap.remove(session.getAttribute(StaticUtil.DEVICEID));
		
		if(session.getAttribute(StaticUtil.IDS)!=null){
			
			JSONObject jo = new JSONObject();
			jo.put(StaticUtil.ORDER, StaticUtil.OUTLINE_OTHER);
			jo.put(StaticUtil.DEVICEID, session.getAttribute(StaticUtil.DEVICEID));
			
			ArrayList<String> ids = (ArrayList<String>) session.getAttribute(StaticUtil.IDS);
			for (String deviceId : ids) {
				if(sessionMap.containsKey(deviceId)){
					IoSession other=sessionMap.get(deviceId);
					other.write(jo.toString());
				}
			}
		}
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		session.getConfig().setUseReadOperation(true);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("IDLE" + session.getIdleCount(status));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionOpened(session);
	}
}
