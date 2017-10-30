package com.coding;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	public static void main(String[] args) {
		try {
			new Client().connect("127.0.0.1", 8888);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void connect(String host, int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap boot = new Bootstrap();
		boot.group(group).channel(NioSocketChannel.class);
		boot.option(ChannelOption.TCP_NODELAY, true);
		boot.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				ch.pipeline().addLast(com.coding.MarshallingCodeCFactory.buildMarshallingDecoder());
				ch.pipeline().addLast(com.coding.MarshallingCodeCFactory.buildMarshallingEncoder());
				ch.pipeline().addLast(new ClientChannelHandler());
			}
		});
		ChannelFuture f = boot.connect(host, port);
		f.channel().closeFuture().sync();
	}
}
