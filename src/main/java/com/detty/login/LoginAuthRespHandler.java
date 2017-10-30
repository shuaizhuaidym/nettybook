package com.detty.login;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.detty.MessageType;
import com.detty.prot.Header;
import com.detty.prot.NettyMessage;

public class LoginAuthRespHandler extends ChannelHandlerAdapter {
	private Map<String, Boolean> nodes = new ConcurrentHashMap<String, Boolean>();

	private String[] whiteKist = { "127.0.0.1", "192.168.1.102" };

	byte failed = (byte) -1;
	byte success=(byte)0;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<receive auth request");
		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage loginResp = null;
			// 拒绝重复登陆
			if (nodes.containsKey(nodeIndex)) {
				loginResp = buildResponse(failed);
			}else{
				InetSocketAddress address=(InetSocketAddress)ctx.channel().remoteAddress();
				String ip=address.getAddress().getHostAddress();
				boolean ok=false;
				for(String wip:whiteKist){
					if(wip.equals(ip)){
						ok=true;
								break;
					}
				}
				loginResp=ok?buildResponse(success):buildResponse(failed);
				if(ok){
					nodes.put(nodeIndex, ok);
				}
				System.out.println("loging resp:"+loginResp+"body"+loginResp.getBody());
				ctx.writeAndFlush(loginResp);
			}
		}else{
			ctx.fireChannelRead(msg);//透传
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		nodes.remove(ctx.channel().remoteAddress().toString());
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}

	private NettyMessage buildResponse(byte result) {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP.value());
		message.setHeader(header);
		message.setBody(result);
		return message;
	}
}
