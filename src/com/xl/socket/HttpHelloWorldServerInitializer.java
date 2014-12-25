package com.xl.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;

public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {  
      
    @Override  
    public void initChannel(SocketChannel ch) throws Exception {  
        ChannelPipeline pipeline = ch.pipeline();  
  
        pipeline.addLast("timeout", new IdleStateHandler(60, 15, 0));//定时  
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(  
                8192, Delimiters.lineDelimiter()));  
        pipeline.addLast("decoder", new StringDecoder(Charset.forName("utf-8")));  
        pipeline.addLast("encoder", new StringEncoder(Charset.forName("utf-8")));  
        pipeline.addLast("heartbeat", new MyHandler());//心跳  
        pipeline.addLast("handler",new HttpHelloWorldServerHandler());//业务  
    }  
}  