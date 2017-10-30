package com.detty.heartBeat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.detty.MessageType;
import com.detty.prot.Header;
import com.detty.prot.NettyMessage;

/**
 * 
 * <p>
 * Title:HeatBeatReqHandler
 * </p>
 * <p>
 * Description:客户端主动发送心跳检测
 * </p>
 * 
 * @author daiyma
 * @date 2016年7月24日
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter {
	private static final Logger logger = LogManager.getLogger(HeartBeatReqHandler.class);

	private volatile ScheduledFuture<?> heartBeat;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
		NettyMessage message = (NettyMessage) msg;
		System.out.println("握手成功，");
		// 握手成功，发送心跳
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
			System.out.println("类型匹配成功,发送心跳");
			heartBeat=ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
		}else if(message.getHeader()!=null&&message.getHeader().getType()==MessageType.HEART_BEAT_RESP.value()){
			System.out.println("Client receive server heart beat"+message);
		}
		else{
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		if(heartBeat!=null){
			heartBeat.cancel(true);
			heartBeat=null;
		}
		ctx.fireExceptionCaught(cause);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}
	/*内部任务类*/
	class HeartBeatTask implements Runnable{
		/*任务上下文*/
		private ChannelHandlerContext ctx;
		/**customized constructor*/
		public HeartBeatTask(ChannelHandlerContext ctx) {
			super();
			this.ctx = ctx;
		}
		
		public void run() {
			NettyMessage nessage=buildHeartBeat();
			ctx.writeAndFlush(nessage);
			
		}
		
		private NettyMessage buildHeartBeat(){
			NettyMessage message=new NettyMessage();
			Header header=new Header();
			header.setType(MessageType.HEART_BEAT_REQ.value());
			message.setHeader(header);
			return message;
		}
	}
}
