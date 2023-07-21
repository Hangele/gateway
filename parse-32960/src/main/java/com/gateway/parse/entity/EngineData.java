package com.gateway.parse.entity;


import com.gateway.parse.base.IStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigDecimal;
import java.nio.ByteOrder;

/**
 * 发动机数据
 */
@SuppressWarnings("all")
public class EngineData implements IStatus {

    /**
     * 发动机状态
     */
    private Short engineStatus;

    /**
     * 曲轴转速
     */
    private Integer roundSpeed;

    /**
     * 燃料消耗率
     */
    private Double fuelRate;

    public Short getEngineStatus() {
        return engineStatus;
    }

    public void setEngineStatus(Short engineStatus) {
        this.engineStatus = engineStatus;
    }

    public Integer getRoundSpeed() {
        return roundSpeed;
    }

    public void setRoundSpeed(Integer roundSpeed) {
        this.roundSpeed = roundSpeed;
    }

    public Double getFuelRate() {
        return fuelRate;
    }

    public void setFuelRate(Double fuelRate) {
        this.fuelRate = fuelRate;
    }

    @Override
    public EngineData decode(ByteBuf byteBuf) {
        EngineData engineData = new EngineData();
        engineData.setEngineStatus(byteBuf.readUnsignedByte());
        engineData.setRoundSpeed(byteBuf.readUnsignedShort());
        double fuelRate = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        engineData.setFuelRate(fuelRate);
        return engineData;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeByte(engineStatus);
        buffer.writeShort(roundSpeed);
        buffer.writeDouble(fuelRate);
        return buffer;
    }

}
