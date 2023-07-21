package com.gateway.receive.server;

import com.gateway.receive.codec.GBTDataDecoder;
import com.gateway.receive.codec.GBTDataEncoder;
import com.gateway.receive.utils.ConfigUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述: 服务端Initializer
 * @Author: Hangele
 */

@Component
public class GBTServerInitializer extends ChannelInitializer<Channel> {

	@Autowired
	private GBTServerOutHandler outHandler;

	@Autowired
	private GBTServerInHandler inHandler;

	@Override
	protected void initChannel(Channel ch) {
		// pipeline管理channel中的Handler，在channel队列中添加一个handler来处理业务
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("idleStateHandler", new IdleStateHandler(
				ConfigUtil.READER_IDLE, ConfigUtil.WRITER_IDLE,
				ConfigUtil.ALL_IDLE));
		pipeline.addLast("encoder", new GBTDataEncoder());
		pipeline.addLast("decoder", new GBTDataDecoder());
		// ChannelOutboundHandler
		// 在注册的时候需要放在最后一个ChannelInboundHandler之前，否则将无法传递到ChannelOutboundHandler。
		pipeline.addLast("outhandler", outHandler);
		pipeline.addLast("inhandler", inHandler);
	}

}
