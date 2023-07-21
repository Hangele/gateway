package com.gateway.parse.entity;


import com.gateway.parse.base.IStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigDecimal;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 子系统电压数据
 */
@SuppressWarnings("all")
public class SubsystemVoltageData implements IStatus {

    /**
     * 子系统号
     */
    private Short systemSerial;

    /**
     * 电压
     */
    private Double batteryVoltage;

    /**
     * 电流
     */
    private Double batteryCurrent;

    /**
     * 单体电池个数
     */
    private Integer batteryCount;

    /**
     * 本帧起始电池序号
     */
    private Integer frameStart;

    /**
     * 本帧单体电池总数
     */
    private Short frameCount;

    /**
     * 单体电池电压列表
     */
    private List<Double> frameVoltages;

    @Override
    public SubsystemVoltageData decode(ByteBuf byteBuf) {
        SubsystemVoltageData subsystemVoltageData = new SubsystemVoltageData();
        subsystemVoltageData.setSystemSerial(byteBuf.readUnsignedByte());
        double vol = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        subsystemVoltageData.setBatteryVoltage(vol);
        double current = new BigDecimal((byteBuf.readUnsignedShort() - 10000)).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        subsystemVoltageData.setBatteryCurrent(current);
        subsystemVoltageData.setBatteryCount(byteBuf.readUnsignedShort());
        subsystemVoltageData.setFrameStart(byteBuf.readUnsignedShort());
        short i = byteBuf.readUnsignedByte();
        subsystemVoltageData.setFrameCount(i);
        if(i>0){
            List<Double> list =new ArrayList();
            for (int j = 0; j < i; j++) {
                double i1 = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
                list.add(i1);
            }
            subsystemVoltageData.setFrameVoltages(list);
        }
        return subsystemVoltageData;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeByte(systemSerial);
        buffer.writeDouble(batteryVoltage);
        buffer.writeDouble(batteryCurrent);
        buffer.writeShort(batteryCount);
        buffer.writeShort(frameStart);
        buffer.writeByte(frameCount);
        for (int i = 0; i < frameCount; i++) {
            buffer.writeDouble(frameVoltages.get(i));
        }
        return buffer;
    }

    public Short getSystemSerial() {
        return systemSerial;
    }

    public void setSystemSerial(Short systemSerial) {
        this.systemSerial = systemSerial;
    }

    public Double getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(Double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public Double getBatteryCurrent() {
        return batteryCurrent;
    }

    public void setBatteryCurrent(Double batteryCurrent) {
        this.batteryCurrent = batteryCurrent;
    }

    public Integer getBatteryCount() {
        return batteryCount;
    }

    public void setBatteryCount(Integer batteryCount) {
        this.batteryCount = batteryCount;
    }

    public Integer getFrameStart() {
        return frameStart;
    }

    public void setFrameStart(Integer frameStart) {
        this.frameStart = frameStart;
    }

    public Short getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(Short frameCount) {
        this.frameCount = frameCount;
    }

    public List<Double> getFrameVoltages() {
        return frameVoltages;
    }

    public void setFrameVoltages(List<Double> frameVoltages) {
        this.frameVoltages = frameVoltages;
    }
}
