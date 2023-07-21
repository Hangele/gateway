package com.gateway.parse.entity;


import com.gateway.parse.base.IStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigDecimal;
import java.nio.ByteOrder;

/**
 * 极值数据
 */
@SuppressWarnings("all")
public class ExtremeData implements IStatus {

    /**
     * 最高电压电池子系统号
     */
    private Short maxVolSysID;

    /**
     * 最高电压单体电池代号
     */
    private Short maxVolBatID;

    /**
     * 单体电池电压最高值
     */
    private Double maxVol;

    /**
     * 最低电压电池子系统号
     */
    private Short minVolSysID;

    /**
     * 最低电压单体电池代号
     */
    private Short minVolBatID;

    /**
     * 单体电池电压最低值
     */
    private Double minVol;

    /**
     * 最高温度子系统号
     */
    private Short maxTempSysID;

    /**
     * 最高温度探针序号
     */
    private Short maxTempPinID;

    /**
     * 最高温度值
     */
    private Integer maxTemp;

    /**
     * 最低温度子系统号
     */
    private Short minTempSysID;

    /**
     * 最低温度探针序号
     */
    private Short minTempPinID;

    /**
     * 最低温度值
     */
    private Integer minTemp;

    public Short getMaxVolSysID() {
        return maxVolSysID;
    }

    public void setMaxVolSysID(Short maxVolSysID) {
        this.maxVolSysID = maxVolSysID;
    }

    public Short getMaxVolBatID() {
        return maxVolBatID;
    }

    public void setMaxVolBatID(Short maxVolBatID) {
        this.maxVolBatID = maxVolBatID;
    }

    public Double getMaxVol() {
        return maxVol;
    }

    public void setMaxVol(Double maxVol) {
        this.maxVol = maxVol;
    }

    public Short getMinVolSysID() {
        return minVolSysID;
    }

    public void setMinVolSysID(Short minVolSysID) {
        this.minVolSysID = minVolSysID;
    }

    public Short getMinVolBatID() {
        return minVolBatID;
    }

    public void setMinVolBatID(Short minVolBatID) {
        this.minVolBatID = minVolBatID;
    }

    public Double getMinVol() {
        return minVol;
    }

    public void setMinVol(Double minVol) {
        this.minVol = minVol;
    }

    public Short getMaxTempSysID() {
        return maxTempSysID;
    }

    public void setMaxTempSysID(Short maxTempSysID) {
        this.maxTempSysID = maxTempSysID;
    }

    public Short getMaxTempPinID() {
        return maxTempPinID;
    }

    public void setMaxTempPinID(Short maxTempPinID) {
        this.maxTempPinID = maxTempPinID;
    }

    public Integer getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Integer maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Short getMinTempSysID() {
        return minTempSysID;
    }

    public void setMinTempSysID(Short minTempSysID) {
        this.minTempSysID = minTempSysID;
    }

    public Short getMinTempPinID() {
        return minTempPinID;
    }

    public void setMinTempPinID(Short minTempPinID) {
        this.minTempPinID = minTempPinID;
    }

    public Integer getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Integer minTemp) {
        this.minTemp = minTemp;
    }

    @Override
    public ExtremeData decode(ByteBuf byteBuf) {
        ExtremeData extremeData = new ExtremeData();
        extremeData.setMaxVolSysID(byteBuf.readUnsignedByte());
        extremeData.setMaxVolBatID(byteBuf.readUnsignedByte());
        double maxVol = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
        extremeData.setMaxVol(maxVol);
        extremeData.setMinVolSysID(byteBuf.readUnsignedByte());
        extremeData.setMinVolBatID(byteBuf.readUnsignedByte());
        double minVol = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
        extremeData.setMinVol(minVol);
        extremeData.setMaxTempSysID(byteBuf.readUnsignedByte());
        extremeData.setMaxTempPinID(byteBuf.readUnsignedByte());
        extremeData.setMaxTemp(byteBuf.readUnsignedByte()-40);
        extremeData.setMinTempSysID(byteBuf.readUnsignedByte());
        extremeData.setMinTempPinID(byteBuf.readUnsignedByte());
        extremeData.setMinTemp(byteBuf.readUnsignedByte()-40);
        return extremeData;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeByte(maxVolSysID);
        buffer.writeByte(maxVolBatID);
        buffer.writeDouble(maxVol);
        buffer.writeByte(minVolSysID);
        buffer.writeByte(minVolBatID);
        buffer.writeDouble(minVol);
        buffer.writeByte(maxTempSysID);
        buffer.writeByte(maxTempPinID);
        buffer.writeByte(maxTemp);
        buffer.writeByte(maxTemp);
        buffer.writeByte(minTempPinID);
        buffer.writeByte(minTemp);
        return buffer;
    }

}
