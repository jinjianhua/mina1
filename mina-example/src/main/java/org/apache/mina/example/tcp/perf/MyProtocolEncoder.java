package org.apache.mina.example.tcp.perf;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MyProtocolEncoder implements ProtocolEncoder{

	public void encode(IoSession session, Object message,ProtocolEncoderOutput out) throws Exception {
		System.out.println("MyProtocolEncoder  encode");
//		byte[] m = (byte[])message;
//		IoBuffer  ioBuffer = IoBuffer.allocate(m.length);
//		ioBuffer.put(m);
//		ioBuffer.flip();
		out.write(message);
	}

	public void dispose(IoSession session) throws Exception {
		
		
	}

}
