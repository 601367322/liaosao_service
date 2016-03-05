package com.xl.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class MainMinaServer {
    private static final int PORT = 8181, BUF_SIZE = 2048;
    private static final int HEARTBEATRATE = 15;

    Channel channel = null;

    @Autowired
    HttpHelloWorldServerInitializer initializer;

    @PostConstruct
    public void init() {
        new Thread(new Runnable() {
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.option(ChannelOption.SO_BACKLOG, 1024);
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(initializer);

                    channel = b.bind(PORT).sync().channel();
                    channel.closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        }).start();
    }

    @PreDestroy
    public void destory() {
        if (channel != null) {
            channel.close();
        }
    }

}
