package com.gateway.receive.server;

import com.gateway.receive.dto.NeCommand;
import com.gateway.receive.dto.RecordDataDTO;
import com.gateway.receive.rabbitmq.RabbitUtil;
import com.gateway.receive.utils.DataUtil;
import com.gateway.receive.utils.DateUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.Date;

/**
 * 服务端OutHandler
 *
 * @author chb
 *
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class GBTServerOutHandler extends ChannelOutboundHandlerAdapter {

	@Autowired
	private RabbitUtil rabbitUtil;

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		super.bind(ctx, localAddress, promise);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
			SocketAddress localAddress, ChannelPromise promise)
			throws Exception {
		super.connect(ctx, remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		super.disconnect(ctx, promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		super.close(ctx, promise);
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		super.deregister(ctx, promise);
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		super.read(ctx);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
		if (msg instanceof NeCommand) {
			NeCommand cmd = (NeCommand) msg;
			Date date = new Date();
			int cmdId = cmd.getCmdID();
			String address = ctx.channel().remoteAddress().toString();
			String gwTime = DateUtils.getStringByDate(date);
			String vin = cmd.getVin();
			String dataTime = cmd.getDTime();
			String data = DataUtil.getHexStringByBytes(cmd.getData().array());
			log.info(address + " | S | " + vin + " | " + dataTime + " | " + data);
			// 应答发送数据记录
			RecordDataDTO recordDataDTO = new RecordDataDTO();
			recordDataDTO.setCmdId(cmdId);
			recordDataDTO.setIpAddress(address);
			recordDataDTO.setGwTime(gwTime);
			recordDataDTO.setVin(vin);
			recordDataDTO.setDataTime(dataTime);
			recordDataDTO.setData(data);
			rabbitUtil.sendData(recordDataDTO);
		}

		ctx.writeAndFlush(msg, promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		super.flush(ctx);
	}
}
