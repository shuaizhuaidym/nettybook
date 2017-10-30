package com.detty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.detty.heartBeat.HeartBeatReqHandler;
import com.detty.login.LoginAuthReqHandler;

public class NettyClient {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private EventLoopGroup group = new NioEventLoopGroup();

	public void connect(String host, int port) throws Exception {
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
							ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
							ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
							ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
							ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
						}
					});
			// 发起异步连接操作remote local
			ChannelFuture future = boot.connect(new InetSocketAddress(host, port),
					new InetSocketAddress(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT)).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放后晴空资源，再次发起重连操作
			executor.execute(new Runnable() {
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(5);
						try {
							System.out.println("异常重连");
							connect(NettyConstant.REMOTE_IP, NettyConstant.REMOTE_PORT);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			});
		}
	}

	public static void main(String[] args) throws Exception {
		new NettyClient().connect(NettyConstant.REMOTE_IP, NettyConstant.REMOTE_PORT);
	}
}
