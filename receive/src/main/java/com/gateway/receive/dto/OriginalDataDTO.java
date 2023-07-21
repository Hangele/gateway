package com.gateway.receive.dto;

import com.gateway.receive.utils.DataUtil;
import lombok.Data;

@Data
public class OriginalDataDTO extends NeCommand {
	private static final long serialVersionUID = 1L;
	/**
	 * 协议数据
	 */
	private byte[] originalData;
	/**
	 * 数据时间
	 */
	private byte[] dataTime;

	public OriginalDataDTO() {
		super();
		setOri(true);
	}

	public byte[] getOriginalData() {
		return originalData;
	}

	public void setOriginalData(byte[] originalData) {
		this.originalData = originalData;

		setCmdID(originalData[2] & 0xFF);
		setReplyID(originalData[3] & 0xFF);
		byte[] bVin = new byte[17];
		System.arraycopy(originalData, 4, bVin, 0, 17);
		String vin = new String(bVin);
		setVin(vin);
		setEncrypt(originalData[21] & 0xFF);
		setLength(((originalData[22] & 0xFF) << 8) + (originalData[23] & 0xFF));

		if (getCmdID() != 0x07 && getCmdID() != 0x08 && getCmdID() != 0xfe) {
			if (originalData.length < 30) {
				setDataTime(null);
			} else {
				byte[] time = new byte[6];
				System.arraycopy(originalData, 24, time, 0, 6);
				setDTime(DataUtil.getTime(time));
				setDataTime(time);
			}

			// 目前无加密
			setContent(new byte[getLength()]);
			System.arraycopy(originalData, 24, getContent(), 0, getLength());
		}
	}

	public byte[] getDataTime() {
		return dataTime;
	}

	public void setDataTime(byte[] dataTime) {
		this.dataTime = dataTime;
	}

	@Override
	public byte[] getNeContent() {
		return getOriginalData();
	}

}
