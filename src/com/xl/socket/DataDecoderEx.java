package com.xl.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class DataDecoderEx extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() < 4)// 这里很关键，网上很多代码都没有这句，是用来当拆包时候剩余长度小于4的时候的保护，不加就出错咯
		{
			return false;
		}
		if (in.remaining() > 1) {
			System.out.println(in.remaining());
			in.mark();// 标记当前位置，以便reset
			int length = in.getInt(in.position());
			System.out.println(in.position());
			if (length > in.remaining() - 4) {// 如果消息内容不够，则重置，相当于不读取size
				System.out.println("package notenough  left=" + in.remaining()
						+ " length=" + length);
				in.reset();
				return false;// 接收新数据，以拼凑成完整数据
			} else {
				System.out.println("package =" + in.toString());
				in.getInt();

				byte[] bytes = new byte[length];
				in.get(bytes, 0, length);
				String str = new String(bytes, "UTF-8");
				if (null != str && str.length() > 0) {
					String strOut =str;// 别看这里的处理，这里是我的数据包解密算法~你可以直接拿str当数据
					out.write(strOut);
				}
				if (in.remaining() > 0) {// 如果读取内容后还粘了包，就让父类再给一次，进行下一次解析
					// System.out.println("package left="+in.remaining()+" data="+in.toString());
				}
				return true;// 这里有两种情况1：没数据了，那么就结束当前调用，有数据就再次调用
			}
		}
		return false;// 处理成功，让父类进行接收下个包
	}
}