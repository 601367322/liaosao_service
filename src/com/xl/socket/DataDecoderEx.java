package com.xl.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class DataDecoderEx extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() < 4)// ����ܹؼ������Ϻܶ���붼û����䣬�����������ʱ��ʣ�೤��С��4��ʱ��ı��������Ӿͳ���
		{
			return false;
		}
		if (in.remaining() > 1) {
			System.out.println(in.remaining());
			in.mark();// ��ǵ�ǰλ�ã��Ա�reset
			int length = in.getInt(in.position());
			System.out.println(in.position());
			if (length > in.remaining() - 4) {// �����Ϣ���ݲ����������ã��൱�ڲ���ȡsize
				System.out.println("package notenough  left=" + in.remaining()
						+ " length=" + length);
				in.reset();
				return false;// ���������ݣ���ƴ�ճ���������
			} else {
				System.out.println("package =" + in.toString());
				in.getInt();

				byte[] bytes = new byte[length];
				in.get(bytes, 0, length);
				String str = new String(bytes, "UTF-8");
				if (null != str && str.length() > 0) {
					String strOut =str;// ������Ĵ����������ҵ����ݰ������㷨~�����ֱ����str������
					out.write(strOut);
				}
				if (in.remaining() > 0) {// �����ȡ���ݺ�ճ�˰������ø����ٸ�һ�Σ�������һ�ν���
					// System.out.println("package left="+in.remaining()+" data="+in.toString());
				}
				return true;// �������������1��û�����ˣ���ô�ͽ�����ǰ���ã������ݾ��ٴε���
			}
		}
		return false;// ����ɹ����ø�����н����¸���
	}
}