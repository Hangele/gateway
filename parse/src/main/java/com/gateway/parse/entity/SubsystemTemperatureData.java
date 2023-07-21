package com.gateway.parse.entity;


import com.gateway.parse.base.IStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 子系统温度数据
 */
@SuppressWarnings("all")
public class SubsystemTemperatureData implements IStatus {

    /**
     * 子系统号
     */
    private Short systemSerial;

    /**
     * 温度探针个数
     */
    private Integer batteryPinCount;

    /**
     * 探针温度值列表
     */
    private List<Integer> batteryPinTemps;

    @Override
    public SubsystemTemperatureData decode(ByteBuf byteBuf) {
        SubsystemTemperatureData subsystemTemperatureData = new SubsystemTemperatureData();
        subsystemTemperatureData.setSystemSerial(byteBuf.readUnsignedByte());
        int i1 = byteBuf.readUnsignedShort();
        subsystemTemperatureData.setBatteryPinCount(i1);
        if(i1>0){
            List<Integer> list =new ArrayList<>();
            for (int i = 0; i <i1 ; i++) {
                int i2 = byteBuf.readUnsignedByte() - 40;
                list.add(i2);
            }
            subsystemTemperatureData.setBatteryPinTemps(list);
        }
        return subsystemTemperatureData;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeByte(systemSerial);
        buffer.writeShort(batteryPinCount);
        if(batteryPinCount>0 && batteryPinTemps.size()>0){
            for (int i = 0; i <batteryPinTemps.size(); i++) {
                buffer.writeByte(batteryPinTemps.get(i) - 40);
            }
        }
        return buffer;
    }

    public Short getSystemSerial() {
        return systemSerial;
    }

    public void setSystemSerial(Short systemSerial) {
        this.systemSerial = systemSerial;
    }

    public Integer getBatteryPinCount() {
        return batteryPinCount;
    }

    public void setBatteryPinCount(Integer batteryPinCount) {
        this.batteryPinCount = batteryPinCount;
    }

    public List<Integer> getBatteryPinTemps() {
        return batteryPinTemps;
    }

    public void setBatteryPinTemps(List<Integer> batteryPinTemps) {
        this.batteryPinTemps = batteryPinTemps;
    }
}
