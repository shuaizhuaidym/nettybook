package com.book.timer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAppender;
import io.netty.channel.ChannelHandlerContext;

public class TimerClienHandler extends ChannelHandlerAppender {

	private ByteBuf msg;

	public TimerClienHandler() throws Exception {
		super();
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		ctx.writeAndFlush(msg);
		byte[]req="quer time order".getBytes();
		msg=Unpooled.buffer(req.length);
		msg.writeBytes(req);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
		ByteBuf buf=(ByteBuf)msg;
		byte[]req=new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body=new String(req,"UTF-8");
		System.out.println("Now is "+body);		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}
	
}
