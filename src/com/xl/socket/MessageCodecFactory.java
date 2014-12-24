package com.xl.socket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MessageCodecFactory implements ProtocolCodecFactory {
	 private final DataEncoderEx encoder;
	    private final DataDecoderEx decoder;
	    public MessageCodecFactory() {
	        encoder = new DataEncoderEx();
	        decoder = new DataDecoderEx();
	    }
	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		  return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		 return encoder;
	}

}
