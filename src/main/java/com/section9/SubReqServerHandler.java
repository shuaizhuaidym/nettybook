package com.section9;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SubScribeReq req = (SubScribeReq) msg;
		System.out.println("Received "+req.getProductName());
		SubScribeRes res = new SubScribeRes(req.getSubReqID(), 0, "all is ok");
		ctx.writeAndFlush(res);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
