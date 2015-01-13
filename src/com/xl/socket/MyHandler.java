package com.xl.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class MyHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
//           	 System.out.println("--- Reader Idle ---");
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
//           	 System.out.println("--- Write Idle ---");
           	 ctx.writeAndFlush("#\n");
            }
        }
    }

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String str)
			throws Exception {
		if(str.equals("#")){
			return;
		}
		ctx.fireChannelRead(str);//传递到下一层，因为在HttpHelloWorldServerInitializer中注册了两个handelr，首先会进这里
	}
	
}