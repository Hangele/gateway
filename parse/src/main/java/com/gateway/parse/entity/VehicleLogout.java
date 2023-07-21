package com.gateway.parse.entity;

import com.gateway.parse.base.IStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteOrder;

/**
 * 车辆登出
 */
@SuppressWarnings("all")
public class VehicleLogout implements IStatus {

    private static final BeanTime producer = new BeanTime();

    //车辆登出时间
    private BeanTime beanTime;
    private String dataTime;

    //车辆登出流水号
    private Integer serialNum;

    @Override
    public VehicleLogout decode(ByteBuf byteBuf) {
        VehicleLogout vehicleLogout = new VehicleLogout();
        BeanTime beanTime = producer.decode(byteBuf);
        vehicleLogout.setBeanTime(beanTime);
        vehicleLogout.setDataTime(beanTime.formatTime());
        vehicleLogout.setSerialNum(byteBuf.readUnsignedShort());
        return vehicleLogout;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeBytes(beanTime.encode());
        buffer.writeShort(serialNum);
        return buffer;
    }

    public BeanTime getBeanTime() {
        return beanTime;
    }

    public void setBeanTime(BeanTime beanTime) {
        this.beanTime = beanTime;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
}
