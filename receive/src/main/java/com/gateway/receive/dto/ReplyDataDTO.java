package com.gateway.gateway.dto;

import com.gateway.receive.dto.NeCommand;
import com.gateway.receive.utils.DataUtil;
import lombok.Data;

/**
 * 平台应答
 * @author Hangele
 */
@Data
public class ReplyDataDTO extends NeCommand {
	private static final long serialVersionUID = 1L;
	/**
	 * 协议数据
	 */
	private byte[] dataTime;

	public ReplyDataDTO(String vin) {
		super(vin);
	}

	public byte[] getDataTime() {
		return dataTime;
	}

	public void setDataTime(byte[] dataTime) {
		this.dataTime = dataTime;
		setDTime(DataUtil.getTime(this.dataTime));
	}

	@Override
	public byte[] getNeContent() {
		return getDataTime();
	}
}
