package com.detty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.book.NettyMarshallingEncoder;
import com.detty.prot.NettyMessage;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
	private static final Logger logger = LogManager.getLogger(NettyMessageEncoder.class);

	private NettyMarshallingEncoder marshllingEncoder;

	public NettyMessageEncoder() {
		super();
		this.marshllingEncoder = MarshallingCodeCFactory
				.buildMarshallingEncoder();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg,
			List<Object> out) throws Exception {
		System.out.println("NettyMessageEncoder.encode() start");
		if (msg == null || msg.getHeader() == null) {
			logger.fatal("The message is null");
			throw new Exception("The message is null");
		}
		io.netty.buffer.ByteBuf sendBuffer = Unpooled.buffer();
		sendBuffer.writeInt(msg.getHeader().getCode());
		sendBuffer.writeInt(msg.getHeader().getLength());
		sendBuffer.writeLong(msg.getHeader().getSessionID());
		sendBuffer.writeByte(msg.getHeader().getType());
		sendBuffer.writeByte(msg.getHeader().getPriority());
		sendBuffer.writeInt(msg.getHeader().getAttachment().size());

		String key = null;
		byte[] keyArray = null;
		Object value = null;

		for (Map.Entry<String, Object> param : msg.getHeader().getAttachment()
				.entrySet()) {
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuffer.writeInt(keyArray.length);
			sendBuffer.writeBytes(keyArray);
			value = param.getValue();
			this.marshllingEncoder.encode(ctx, value, sendBuffer);
		}
		key=null;
		keyArray=null;
		value=null;
		if(msg.getBody()!=null){
			marshllingEncoder.encode(ctx, msg.getBody(), sendBuffer);
		}else{
			sendBuffer.writeInt(0);
		}
		sendBuffer.setInt(4, sendBuffer.readableBytes());
		System.out.println("NettyMessageEncoder.encode() end");
	}
}
