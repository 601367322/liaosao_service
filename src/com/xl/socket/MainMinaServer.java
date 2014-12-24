package com.xl.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MainMinaServer {
	private static final int PORT = 8181, BUF_SIZE = 2048;
	private static final int HEARTBEATRATE = 15;

	public void init() {
		try {
			IoAcceptor acceptor = new NioSocketAcceptor();

			acceptor.getFilterChain().addLast("logger", new LoggingFilter());
//			acceptor.getFilterChain().addLast(
//					"codec",
//					new ProtocolCodecFilter(new CharsetCodecFactory()));
			
			acceptor.getFilterChain().addLast(
			"codec",
			new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

			KeepAliveMessageFactory heartBeatFactory = new KeepAlive();

			KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
					IdleStatus.BOTH_IDLE);

			heartBeat.setForwardEvent(true);
			heartBeat.setRequestInterval(HEARTBEATRATE);

			acceptor.getFilterChain().addLast("KeepAlive", heartBeat);

			acceptor.setHandler(new Handler());

			acceptor.getSessionConfig().setReadBufferSize(BUF_SIZE);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

			acceptor.bind(new InetSocketAddress(PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
