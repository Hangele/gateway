package com.gateway.receive.codec;

import com.gateway.receive.dto.NeCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

/**
 * 编码器
 * @author chb
 *
 */
@Component
public class GBTDataEncoder extends MessageToByteEncoder<NeCommand> {

	@Override
	protected void encode(ChannelHandlerContext ctx, NeCommand msg, ByteBuf out) {
		out.writeBytes(msg.getData());
	}
}
