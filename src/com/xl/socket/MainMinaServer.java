package com.xl.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MainMinaServer {
	private static final int PORT = 8181, BUF_SIZE = 2048;
	private static final int HEARTBEATRATE = 15;

	public void init() {
		/*try {
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
		}*/
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
		        EventLoopGroup bossGroup = new NioEventLoopGroup();
		        EventLoopGroup workerGroup = new NioEventLoopGroup();
		        try {
		            ServerBootstrap b = new ServerBootstrap();
		            b.option(ChannelOption.SO_BACKLOG, 1024);
		            b.group(bossGroup, workerGroup)
		             .channel(NioServerSocketChannel.class)
		             .childHandler(new HttpHelloWorldServerInitializer());

		            Channel ch = b.bind(PORT).sync().channel();
		            ch.closeFuture().sync();
		        }catch (Exception e) {
					e.printStackTrace();
				} finally {
		            bossGroup.shutdownGracefully();
		            workerGroup.shutdownGracefully();
		        }	
			}
		}).start();
	}
}
