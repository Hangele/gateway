package com.gateway.parse.dto;

import com.gateway.parse.utils.DataUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public abstract class NeCommand implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 网关时间
	 */
	private long gwTime = System.currentTimeMillis();
	/**
	 * 数据时间
	 */
	private String dTime;
	/**
	 * 发送时间
	 */
	private long sTime;
	/**
	 * 是否为直接转发的协议数据
	 */
	private boolean isOri = false;
	/**
	 * 是否为补发的协议数据
	 */
	private boolean isDelay = false;
	/**
	 * 数据发送次数
	 */
	private int count = 1;
	/**
	 * 唯一识别码 GB16735
	 */
	private String vin;
	/**
	 * 命令标识
	 */
	private int cmdID;
	/**
	 * 应答标识
	 */
	private int replyID;
	/**
	 * 加密方式：1不加密、2RSA、3AES128
	 */
	protected int encrypt;
	/**
	 * 长度
	 */
	private int length;
	/**
	 * 数据单元
	 */
	private byte[] content;
	/**
	 * 加密后的数据单元
	 */
	private byte[] data;

	public NeCommand() {}

	public NeCommand(String vin) {
		setVin(vin);
		setEncrypt(1);
	}

	/**
	 * 获取数据体
	 * @return
	 */
	public abstract byte[] getNeContent();

	public ByteBuf getData() {
		byte[] temp = getNeContent();
		if (temp == null) {
			return null;
		}

		if (isOri) {
			if (isDelay && cmdID == 0x02) {
				// 协议头、命令码、校验位
				byte[] data03 = new byte[temp.length - 4];
				System.arraycopy(temp, 3, data03, 0, temp.length - 4);
				ByteBuf check = Unpooled.buffer(22 + getLength());
				// 命令标识
				check.writeByte((byte) 0x03);
				check.writeBytes(data03);
				byte[] checkData = check.array();
				// 计算校验码(命令单元到数据单元)
				int checkCode = DataUtil.getCheckCode(checkData);

				// 完整的指令拼装
				ByteBuf data = Unpooled.buffer(25 + getLength());
				data.writeShort((short) 0x2323);
				data.writeBytes(checkData);
				data.writeByte((byte) (checkCode));

				return data;
			} else {
				ByteBuf data = Unpooled.buffer(temp.length);
				data.writeBytes(temp);

				return data;
			}
		} else {
			setContent(temp);
			setLength((content == null ? 0 : content.length));

			// 拼装指令的校验部分
			ByteBuf check = Unpooled.buffer(22 + getLength());
			// 命令标识
			check.writeByte((byte) getCmdID());
			// 应答标识
			check.writeByte((byte) getReplyID());
			for (int i = 0; i < 17 - vin.length(); i++) {
				check.writeByte((byte) 0);
			}
			// 唯一识别码
			check.writeBytes(vin.getBytes());
			// 加密方式：1不加密、2RSA、3AES128
			check.writeByte((byte) getEncrypt());
			// 数据长度
			check.writeShort((short) getLength());
			if (getLength() > 0) {
				if (encrypt == 2 || encrypt == 3) {
					// 加密后的数据单元
					check.writeBytes(getData());
				} else {
					// 数据单元
					check.writeBytes(getContent());
				}
			}
			byte[] checkData = check.array();
			// 计算校验码(命令单元到数据单元)
			int checkCode = DataUtil.getCheckCode(checkData);
			// 完整的指令拼装
			ByteBuf data = Unpooled.buffer(25 + getLength());
			data.writeShort((short) 0x2323);
			data.writeBytes(checkData);
			data.writeByte((byte) (checkCode));

			return data;
		}
	}
}
