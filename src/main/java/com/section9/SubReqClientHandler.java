package com.section9;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqClientHandler extends ChannelHandlerAdapter {

	public SubReqClientHandler() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		int i = 0;
		for (i = 0; i < 10; i++) {
			SubScribeReq req = request(i);
			ctx.writeAndFlush(req);
			System.out.println(req.toString()+" writed");
		}
	}

	private SubScribeReq request(int i) {
		SubScribeReq req = new SubScribeReq(i, "admin_" + i, "book", "62618866", "BeiJing");
		return req;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Receive server response : [" + msg + "]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
