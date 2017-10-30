package com.section5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {
public void bind(int port){
	EventLoopGroup group=new NioEventLoopGroup();
	try {
		ServerBootstrap boot=new ServerBootstrap();
		boot.group(group);
		boot.channel(NioServerSocketChannel.class);
		boot.option(ChannelOption.SO_BACKLOG, 100);
		boot.handler(new LoggingHandler(LogLevel.INFO));
		boot.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel arg0) throws Exception {
				// TODO Auto-generated method stub
				
			}
		});
	} catch (Exception e) {
		// TODO: handle exception
	}
}
}
