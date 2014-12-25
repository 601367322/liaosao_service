/*package com.xl.socket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class KeepAlive implements KeepAliveMessageFactory {

	private static final String HEARTBEATREQUEST = "#";
	private static final String HEARTBEATRESPONSE = "*";

	@Override
	public boolean isRequest(IoSession session, Object message) {
		if (message.equals(HEARTBEATREQUEST))
			return true;
		return false;
	}

	@Override
	public boolean isResponse(IoSession session, Object message) {
		if (message.equals(HEARTBEATRESPONSE))
			return true;
		return false;
	}

	@Override
	public Object getRequest(IoSession session) {
		return HEARTBEATREQUEST;
	}
	@Override
	public Object getResponse(IoSession session, Object request) {
		return HEARTBEATRESPONSE;
	}

}*/