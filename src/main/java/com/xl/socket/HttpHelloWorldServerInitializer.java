package com.xl.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {  

    @Autowired
    HttpHelloWorldServerHandler handler;

    @Override  
    public void initChannel(SocketChannel ch) throws Exception {  
        ChannelPipeline pipeline = ch.pipeline();  
  
        pipeline.addLast("timeout", new IdleStateHandler(60, 15, 0));
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(  
                8192, Delimiters.lineDelimiter()));  
        pipeline.addLast("decoder", new StringDecoder(Charset.forName("utf-8")));  
        pipeline.addLast("encoder", new StringEncoder(Charset.forName("utf-8")));  
        pipeline.addLast("heartbeat", new MyHandler());
        pipeline.addLast("handler",handler);
    }
}