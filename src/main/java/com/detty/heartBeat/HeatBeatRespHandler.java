package com.detty.heartBeat;

import com.detty.MessageType;
import com.detty.prot.Header;
import com.detty.prot.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class HeatBeatRespHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
		NettyMessage message=(NettyMessage)msg;
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEART_BEAT_REQ.value()) {
			System.out.println("Receive client heart beat message:" + message);
			NettyMessage heartBeat = buildHeartBeat();
			System.out.println("Send heart beat response message:" + heartBeat);
			ctx.writeAndFlush(heartBeat);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
	
	NettyMessage buildHeartBeat(){
		NettyMessage msg=new NettyMessage();
		Header header=new Header();
		header.setType(MessageType.HEART_BEAT_RESP.value());
		msg.setHeader(header);
		return msg;
	}

}
