/*package com.xl.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class CharsetDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() > 0) {
			byte[] sizeBytes = new byte[4];
			in.mark();
			in.get(sizeBytes);
			int size = Integer.valueOf(toInt(sizeBytes));
			if (size > in.remaining()) {
				in.reset();
				return false;
			} else {
				byte[] bytes = new byte[size];
				in.get(bytes, 0, size);
				String str = new String(bytes, "UTF-8");
				if (null != str && str.length() > 0) {
					String strOut =str;
					out.write(strOut);
				}
			}
		}
		return false;
	}

	public static int toInt(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= ((bytes[1] << 8) & 0xFF00);
		addr |= ((bytes[2] << 16) & 0xFF0000);
		addr |= ((bytes[3] << 24) & 0xFF000000);
		return addr;
	}

}*/