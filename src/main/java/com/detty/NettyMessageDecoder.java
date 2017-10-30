package com.detty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.book.NettyMarshallingDecoder;
import com.detty.prot.Header;
import com.detty.prot.NettyMessage;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
	NettyMarshallingDecoder nettyMarshallingDecoder;

	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		nettyMarshallingDecoder = new NettyMarshallingDecoder(null);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		
		System.out.println("com.detty.NettyMessageDecoder.decode() START");
		
		ByteBuffer frame = (ByteBuffer) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCode(in.readInt());
		header.setLength(in.readInt());
		header.setSessionID(in.readLong());
		header.setType(in.readByte());
		header.setPriority(in.readByte());
		
		System.out.println(">>>>>>>>>>>>>>>>>"+header.getSessionID());
		
		int size = in.readInt();
		if (size > 0) {
			Map<String, Object> attach = new HashMap<String, Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			for (int i = 0; i < size; i++) {
				keySize = in.readInt();
				keyArray = new byte[keySize];
				in.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				attach.put(key, this.nettyMarshallingDecoder.decode(ctx, in));
			}
			keyArray = null;
			key = null;
			header.setAttachment(attach);
		}
		if (in.readableBytes() > 4) {
			message.setBody(this.nettyMarshallingDecoder.decode(ctx, in));
		}
		message.setHeader(header);
		System.out.println("com.detty.NettyMessageDecoder.decode() END");
		return message;
	}
}
