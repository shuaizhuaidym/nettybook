package com.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {
	private static final String DEFAULT_URL = "/src/main/java/com/";

	public void run(final int port, final String url) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(boss, worker).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
							ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
							ch.pipeline().addLast("http-eecoder", new HttpResponseEncoder());
							ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
							ch.pipeline().addLast("file-server-handler", new HttpFileServerHandler(url));
						}
					});
			ChannelFuture future = boot.bind("127.0.0.1", 8888).sync();
			
			System.out.println("http://127.0.0.1:8888 is ready.");
			
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		try {
			new HttpFileServer().run(8888, DEFAULT_URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
