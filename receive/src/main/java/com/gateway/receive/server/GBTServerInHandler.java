package com.gateway.receive.server;

import cn.hutool.core.date.DateUtil;
import com.gateway.receive.dto.NeCommand;
import com.gateway.receive.dto.OriginalDataDTO;
import com.gateway.receive.dto.RecordDataDTO;
import com.gateway.gateway.dto.ReplyDataDTO;
import com.gateway.receive.rabbitmq.RabbitUtil;
import com.gateway.receive.utils.DataUtil;
import com.gateway.receive.utils.DateUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务端InHandler
 *
 * @author Hangele
 *
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class GBTServerInHandler extends ChannelInboundHandlerAdapter {
	@Resource
	private RabbitUtil rabbitUtil;

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			if (msg instanceof NeCommand) {
				OriginalDataDTO original = (OriginalDataDTO) msg;

				int cmdId = original.getCmdID();
				String address = ctx.channel().remoteAddress().toString();
				String vin = original.getVin();
				String dataTime = original.getDTime();
				String data = DataUtil.getHexStringByBytes(original.getData().array());
				log.info(address + " | R | " + vin + " | " + dataTime + " | " + data);
				RecordDataDTO recordDataDTO = new RecordDataDTO();
				recordDataDTO.setCmdId(cmdId);
				recordDataDTO.setGwTime(DateUtil.now());
				recordDataDTO.setIpAddress(address);
				recordDataDTO.setVin(vin);
				recordDataDTO.setDataTime(dataTime);
				recordDataDTO.setData(data);
				// 记录网关数据，发送到RabbitMQ的gateway_exchange（使用fanout交换机绑定原始数据，转发，解析三个队列）
				rabbitUtil.sendData(recordDataDTO);
				// 应答
				if (original.getReplyID() == 0xFE) {
					reply(ctx, original);
				}
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx)
			throws Exception {
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("处理GBT32960协议数据异常！", cause);
	}

	/**
	 * 向终端发送应答
	 *
	 * @param ctx
	 * @param original
	 */
	private void reply(ChannelHandlerContext ctx, OriginalDataDTO original) {
		// 下行指令，终端应答时不再返回应答
		if (original.getCmdID() == 0x80 || original.getCmdID() == 0x81 || original.getCmdID() == 0x82) {
			return;
		}
		ReplyDataDTO cmd = new ReplyDataDTO(original.getVin());
		cmd.setReplyID(0x01);
		cmd.setCmdID(original.getCmdID());
		// 07 心跳 08 终端校时
		if (original.getCmdID() != 0x07 && original.getCmdID() != 0x08) {
			cmd.setDataTime(original.getDataTime());
		} else {
			cmd.setDataTime(DateUtils.getCurrentTimeBytes());
		}

		ctx.write(cmd);
	}
}
