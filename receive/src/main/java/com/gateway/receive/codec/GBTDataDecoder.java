package com.gateway.receive.codec;
import com.gateway.receive.dto.OriginalDataDTO;
import com.gateway.receive.utils.DataUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 解码器
 *
 * @author Hangele
 *
 */
@Slf4j
@Component
public class GBTDataDecoder extends ByteToMessageDecoder {

	/**
	 * 包头
	 */
	private final static short PROTOCOL_HEAD = 0x2323;
	/**
	 * 命令标识1 应答标识1 VIN码 17 加密1
	 */
	private final static int EXCEPT_CONTENT_LENGTH = 20;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
		buf.markReaderIndex();
		boolean headOk = false;
		int messageStartIndex = 0;

		// 寻找协议头
		while (buf.readableBytes() > 1) {
			if (buf.readUnsignedShort() == PROTOCOL_HEAD) {
				messageStartIndex = buf.readerIndex() - 2;
				headOk = true;
				break;
			} else {
				messageStartIndex = buf.readerIndex() - 1;
				buf.readerIndex(messageStartIndex);
			}
		}

		if (!headOk) {
			return;
		}

		if (buf.readableBytes() < EXCEPT_CONTENT_LENGTH + 2) {
			buf.readerIndex(messageStartIndex);
			return;
		}

		// 计算校验码的开始位置
		int cmdStartIndex = buf.readerIndex();
		// 命令标识1 应答标识1 VIN码 17 加密1
		buf.skipBytes(EXCEPT_CONTENT_LENGTH);
		// 数据内容长度
		int contentLength = buf.readUnsignedShort();
		if (buf.readableBytes() < (contentLength + 1)) {
			buf.readerIndex(messageStartIndex);
			return;
		}
		// 数据内容
		buf.skipBytes(contentLength);
		// 校验码
		byte checkCode = buf.readByte();
		int endIndex = buf.readerIndex();
		// 需要校验的内容
		byte[] bytesToCheck = new byte[EXCEPT_CONTENT_LENGTH + 2
				+ contentLength];
		buf.readerIndex(cmdStartIndex);
		buf.readBytes(bytesToCheck);
		// 对比校验码
		byte actuallyCheckCode = (byte) DataUtil.getCheckCode(bytesToCheck);
		if (actuallyCheckCode != checkCode) {
			buf.readerIndex(endIndex);
			return;
		}

		// 截取协议数据
		buf.readerIndex(messageStartIndex);
		byte[] messageBytes = new byte[2 + EXCEPT_CONTENT_LENGTH + 2 + contentLength + 1];
		buf.readBytes(messageBytes);

		OriginalDataDTO cmd = new OriginalDataDTO();
		cmd.setOriginalData(messageBytes);
		out.add(cmd);
	}
}
