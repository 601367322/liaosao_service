/*package com.xl.socket;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;

public class CharsetEncoder implements ProtocolEncoder {
	private final static Charset charset = Charset.forName("UTF-8");
    @Override
    public void dispose(IoSession session) throws Exception {
    }
 
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);
        buff.putString(message.toString(), charset.newEncoder());
        buff.putString(LineDelimiter.DEFAULT.getValue(), charset.newEncoder());
        buff.flip();
        out.write(buff);
    }
}*/