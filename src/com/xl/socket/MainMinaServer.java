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
