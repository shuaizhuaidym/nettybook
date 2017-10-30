package com.detty.login;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import com.detty.MessageType;
import com.detty.prot.Header;
import com.detty.prot.NettyMessage;

public class LoginAuthReqHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		ctx.writeAndFlush(buildLoginReq());
		System.out.println("channelActive client send auth request");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
			byte loginResult = (Byte.parseByte(message.getBody().toString()));
			if(loginResult!=(byte)0){
				System.out.println("login failed");
				ctx.close();
			}else{
				System.out.println("login ok ");
				ctx.fireChannelRead(msg);
			}
		}else{
			ctx.fireChannelRead(msg);
		}
	}

	private NettyMessage buildLoginReq() {
		NettyMessage msg = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.value());
		msg.setHeader(header);
		return msg;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
