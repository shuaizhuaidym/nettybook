package com.section9;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubReqServer {

	public void bind(int port) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		try {

			ServerBootstrap boot = new ServerBootstrap();
			boot.group(boss, work);
			boot.channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100);
			boot.handler(new LoggingHandler(LogLevel.INFO));
			boot.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
					ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());
					ch.pipeline().addLast(new SubReqServerHandler());
				}
			});

			ChannelFuture f = boot.bind(port).sync();
			f.channel().closeFuture().sync();
			
			System.out.println("That is ok");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new SubReqServer().bind(8080);
	}
}
