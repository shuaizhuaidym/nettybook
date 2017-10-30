package com.coding;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	public static void main(String[] args) {
		new Server().bind(8888);
	}

	public void bind(final int port) {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap boot=new ServerBootstrap();
			boot.option(ChannelOption.TCP_NODELAY, true);
			boot.group(boss,worker).channel(NioServerSocketChannel.class);
			boot.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(com.coding.MarshallingCodeCFactory.buildMarshallingEncoder());
					ch.pipeline().addLast(com.coding.MarshallingCodeCFactory.buildMarshallingDecoder());
					ch.pipeline().addLast(new ServerChannelHanler());
				}
				
			});
			ChannelFuture f=boot.bind(port).sync();
			System.out.println("服务已经启动");
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
