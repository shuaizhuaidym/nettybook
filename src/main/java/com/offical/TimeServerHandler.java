package com.offical;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class TimeServerHandler extends ChannelHandlerAdapter {

	private int counter;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		String body = (String) msg;
		System.out.println("recive request body:" + body + counter++);

		String now = new Date(System.currentTimeMillis()).toString();
		String rst = "query time order".equalsIgnoreCase(body) ? now + System.getProperty("line.separator")
				: "bad reqeust";
		ByteBuf rsp = Unpooled.copiedBuffer(rst.getBytes());
		ctx.writeAndFlush(rsp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}
}
