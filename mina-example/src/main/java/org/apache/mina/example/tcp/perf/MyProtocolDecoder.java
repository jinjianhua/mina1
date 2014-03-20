package org.apache.mina.example.tcp.perf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyProtocolDecoder implements ProtocolDecoder{
	
	 private final AttributeKey BUFFER = new AttributeKey(getClass(), "buffer");

	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)throws Exception {
		IoBuffer buf = (IoBuffer) session.getAttribute(BUFFER);
	    if(null != buf){
	    	session.removeAttribute(BUFFER);
	    	 buf.flip();
             IoBuffer newBuf = IoBuffer.allocate(buf.remaining() + in.remaining()).setAutoExpand(true);
             newBuf.order(buf.order());
             newBuf.put(buf);
             newBuf.put(in);
             newBuf.flip();
	    	aaa(session,newBuf);
	    }else{
			aaa(session, in);
	    }
		out.write(in);
		return;
	}

	private void aaa(IoSession session, IoBuffer in) throws IOException,ClassNotFoundException {
		int limit = in.limit() - in.position();
		if (limit > 9) {
			byte[] byte9 = new byte[9];
			in.get(byte9);
			int size = getSize(byte9);
			if(limit - 9 == size){
				byte[] bytes = new byte[size];
				in.get(bytes);
				t(bytes);
			}else if(limit - 9 <size){
				in.position(0);
				session.setAttribute(BUFFER,in);
				byte[] bs = new byte[limit];
				in.get(bs);
			}else if(limit - 9 >size){
				byte[] bytes = new byte[size];
				in.get(bytes);
				t(bytes);
				aaa(session, in);
			}
		}else{
			 session.setAttribute(BUFFER,in);
			 byte[] bytes = new byte[limit];
				in.get(bytes);
		}
		
	}

	private void t(byte[] bytes) throws IOException, ClassNotFoundException {
		//print(bytes);
		InputStream is= new ByteArrayInputStream(bytes);
		ObjectInputStream ois=new ObjectInputStream(is);
		Tbean tbean =  (Tbean)ois.readObject();
		System.out.println(tbean.getName());
	}

	private void print(byte[] bytes) {
		for(int i = 0 ;i<bytes.length; i++){
			if(bytes[i] == 0)
				continue;
			System.out.print(bytes[i]+ " ");
			if((i+1)%9 == 0)
				System.out.println();
		}
	}

	private int getSize(byte[] byte9) {
		List<Byte>  byteList = new ArrayList<Byte>();
		for(Byte b:byte9){
			if(0 == b)
				continue;
			byteList.add(b);
		}
		
		byte[]  bs = new byte[byteList.size()];
		
		for(int i = 0 ;i<byteList.size();i++){
			bs[i] = byteList.get(i);
		}

		return Integer.valueOf(new String(bs));
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)throws Exception {
		
		
	}

	public void dispose(IoSession session) throws Exception {
		
		
	}

}
