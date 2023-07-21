package com.gateway.receive.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 描述: 网关服务配置启动类
 * @Author: chb
 * @Date: 2023-4-21 16:39
 */
@Slf4j
@Component
public class GBTServer implements ApplicationRunner {
	public static Integer SERVER_PORT;
	@Value("${gateway.port}")
	public void setServerPort(Integer serverPort) {
		SERVER_PORT = serverPort;
	}

	/**
	 * 父线程
	 */
	private EventLoopGroup bossGroup;

	/**
	 * 子线程
	 */
	private EventLoopGroup workerGroup;

	ChannelFuture channel = null;

	@Autowired
	private GBTServerInitializer initializer;

	public GBTServer() {}

	private void bind() {
		// 通过nio方式来接收连接和处理连接
		this.bossGroup = new NioEventLoopGroup(1, r -> {
			return new Thread(r, "BossGroup");
		});
		this.workerGroup = new NioEventLoopGroup(2, r -> {
			return new Thread(r, "WorkerGroup");
		});
		try {
			ServerBootstrap svrStrap = new ServerBootstrap();
			// 设置bootstrap的线程组
			svrStrap.group(this.bossGroup, this.workerGroup);
			// 设置nio类型的channel
			svrStrap.channel(NioServerSocketChannel.class);
			svrStrap.childHandler(initializer);
			// 当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
			svrStrap.option(ChannelOption.SO_BACKLOG, 128);
			// 是否启用心跳保活机制
			svrStrap.childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
			// 缓冲区的大小建议设置为消息的平均大小，不要设置成最大消息的上限
			svrStrap.childOption(ChannelOption.RCVBUF_ALLOCATOR,
					new AdaptiveRecvByteBufAllocator(64, 1024, Integer.MAX_VALUE));
			// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功 .channel().closeFuture().sync()
			channel = svrStrap.bind(SERVER_PORT).sync();
			log.info("开始监听端口：" + SERVER_PORT);
			channel = channel.channel().closeFuture();
			channel.sync();
		} catch (Exception e) {
			log.error("启动网关出错！", e);
		} finally {
			try {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			} catch (Exception ignored) {
			}
		}
	}

	@Override
	public void run(ApplicationArguments args) {
		this.bind();
	}

	@PreDestroy
	public void destory() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		channel.channel().close();
	}
}
