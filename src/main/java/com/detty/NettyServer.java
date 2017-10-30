package com.detty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.detty.heartBeat.HeartBeatReqHandler;
import com.detty.login.LoginAuthRespHandler;

public class NettyServer {
	static {
		initCfg();
	}
	private static final Logger logger = LogManager.getLogger(NettyServer.class);

	public void bind() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		ServerBootstrap boot = new ServerBootstrap();

		boot.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
				.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws IOException {
						ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
						ch.pipeline().addLast(new NettyMessageEncoder());
						ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
						ch.pipeline().addLast(new LoginAuthRespHandler());
						ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
						logger.info("InitChannel complete");
					}
				});
		// 绑定端口，同步等待成功结果
		boot.bind(NettyConstant.REMOTE_IP, NettyConstant.REMOTE_PORT).sync();
		logger.info("Start OK,IP:" + NettyConstant.REMOTE_IP + " port: " + NettyConstant.REMOTE_PORT);
	}

	public static void main(String[] args) throws Exception {
		new NettyServer().bind();
	}
	
	private static void initCfg(){

		File file = new File("E:/SpringWork/detty/src/main/resources/log4j2.xml");
		ConfigurationSource source = null;
		BufferedInputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			source = new ConfigurationSource(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Configurator.initialize(null, source);
	}
}