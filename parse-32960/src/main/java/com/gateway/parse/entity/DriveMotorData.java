package com.gateway.parse.entity;

import com.gateway.parse.base.IStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigDecimal;
import java.nio.ByteOrder;

/**
 * 驱动电机数据
 */
@SuppressWarnings("all")
public class DriveMotorData implements IStatus {

    /**
     * 序号
     */
    private Short motoSerial;

    /**
     * 驱动电机状态
     */
    private Short motoStatus;

    /**
     * 驱动电机控制器温度
     */
    private Integer motoCtrlTemp;

    /**
     * 驱动电机转速
     */
    private Integer motoRoundSpeed;

    /**
     * 驱动电机转矩
     */
    private Double motoTorque;

    /**
     * 驱动电机温度
     */
    private Integer motorTemp;

    /**
     * 驱动电机控制器输入电压
     */
    private Double motoVoltage;

    /**
     * 驱动电机控制器直流母线电流
     */
    private Double motoCurrent;

    public Short getMotoSerial() {
        return motoSerial;
    }

    public void setMotoSerial(Short motoSerial) {
        this.motoSerial = motoSerial;
    }

    public Short getMotoStatus() {
        return motoStatus;
    }

    public void setMotoStatus(Short motoStatus) {
        this.motoStatus = motoStatus;
    }

    public Integer getMotoRoundSpeed() {
        return motoRoundSpeed;
    }

    public void setMotoRoundSpeed(Integer motoRoundSpeed) {
        this.motoRoundSpeed = motoRoundSpeed;
    }

    public Integer getMotoCtrlTemp() {
        return motoCtrlTemp;
    }

    public void setMotoCtrlTemp(Integer motoCtrlTemp) {
        this.motoCtrlTemp = motoCtrlTemp;
    }

    public Double getMotoTorque() {
        return motoTorque;
    }

    public void setMotoTorque(Double motoTorque) {
        this.motoTorque = motoTorque;
    }

    public Integer getMotorTemp() {
        return motorTemp;
    }

    public void setMotorTemp(Integer motorTemp) {
        this.motorTemp = motorTemp;
    }

    public Double getMotoVoltage() {
        return motoVoltage;
    }

    public void setMotoVoltage(Double motoVoltage) {
        this.motoVoltage = motoVoltage;
    }

    public Double getMotoCurrent() {
        return motoCurrent;
    }

    public void setMotoCurrent(Double motoCurrent) {
        this.motoCurrent = motoCurrent;
    }

    @Override
    public DriveMotorData decode(ByteBuf byteBuf) {
        DriveMotorData driveMotorData = new DriveMotorData();
        driveMotorData.setMotoSerial(byteBuf.readUnsignedByte());
        driveMotorData.setMotoStatus(byteBuf.readUnsignedByte());
        int motoCtrlTemp = byteBuf.readUnsignedByte() - 40;
        driveMotorData.setMotoCtrlTemp(motoCtrlTemp);
        int motoRoundSpeed = byteBuf.readUnsignedShort() - 20000;
        driveMotorData.setMotoRoundSpeed(motoRoundSpeed);
        double motoTorque = new BigDecimal((byteBuf.readUnsignedShort() - 20000)).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();;
        driveMotorData.setMotoTorque(motoTorque);
        int motorTemp = byteBuf.readUnsignedByte() - 40;
        driveMotorData.setMotorTemp(motorTemp);
        double motoVoltage = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();;
        driveMotorData.setMotoVoltage(motoVoltage);
        double motoCurrent = new BigDecimal((byteBuf.readUnsignedShort() - 10000)).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();;
        driveMotorData.setMotoCurrent(motoCurrent);
        return driveMotorData;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeByte(motoSerial);
        buffer.writeByte(motoStatus);
        buffer.writeByte(motoCtrlTemp);
        buffer.writeShort(motoRoundSpeed);
        buffer.writeDouble(motoTorque);
        buffer.writeByte(motorTemp);
        buffer.writeDouble(motoVoltage);
        buffer.writeDouble(motoCurrent);
        return buffer;
    }

}
