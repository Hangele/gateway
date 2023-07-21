package com.gateway.parse.entity;

import com.gateway.parse.base.IStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigDecimal;
import java.nio.ByteOrder;

/**
 * 整车数据
 */
@SuppressWarnings("all")
public class VehicleData implements IStatus {

    /**
     * 车辆状态
     */
    private Short vehicleStatus;

    /**
     * 充电状态
     */
    private Short chargeStatus;

    /**
     * 运行模式
     */
    private Short runStatus;

    /**
     * 车速
     */
    private Double speed;

    /**
     * 累计里程
     */
    private Double totalMillage;

    /**
     * 总电压
     */
    private Double totalVoltage;

    /**
     * 总电流
     */
    private Double totalCurrent;

    /**
     * SOC电量
     */
    private Short socStatus;

    /**
     * DC_DC状态
     */
    private Short dcdcStatus;

    /**
     * 挡位状态
     */
    private Short gearState;

    /**
     * 有无驱动力
     */
    private Integer drivingForce;

    /**
     * 有无制动力
     */
    private Integer brakingForce;

    /**
     * 挡位
     */
    private Integer gear;

    /**
     * 绝缘电阻
     */
    private Integer resistance;

    /**
     * 加速行程值
     */
    private Short accPedal;

    /**
     * 制动踏板状态
     */
    private Short breakPedal;

    public Short getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(Short vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public Short getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Short chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getTotalVoltage() {
        return totalVoltage;
    }

    public void setTotalVoltage(Double totalVoltage) {
        this.totalVoltage = totalVoltage;
    }

    public Double getTotalCurrent() {
        return totalCurrent;
    }

    public void setTotalCurrent(Double totalCurrent) {
        this.totalCurrent = totalCurrent;
    }

    public Short getGearState() {
        return gearState;
    }

    public void setGearState(Short gearState) {
        this.gearState = gearState;
    }

    public Integer getDrivingForce() {
        return drivingForce;
    }

    public void setDrivingForce(Integer drivingForce) {
        this.drivingForce = drivingForce;
    }

    public Integer getBrakingForce() {
        return brakingForce;
    }

    public void setBrakingForce(Integer brakingForce) {
        this.brakingForce = brakingForce;
    }

    public Short getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Short runStatus) {
        this.runStatus = runStatus;
    }

    public Double getTotalMillage() {
        return totalMillage;
    }

    public void setTotalMillage(Double totalMillage) {
        this.totalMillage = totalMillage;
    }

    public Short getSocStatus() {
        return socStatus;
    }

    public void setSocStatus(Short socStatus) {
        this.socStatus = socStatus;
    }

    public Short getDcdcStatus() {
        return dcdcStatus;
    }

    public void setDcdcStatus(Short dcdcStatus) {
        this.dcdcStatus = dcdcStatus;
    }

    public Integer getGear() {
        return gear;
    }

    public void setGear(Integer gear) {
        this.gear = gear;
    }

    public Integer getResistance() {
        return resistance;
    }

    public void setResistance(Integer resistance) {
        this.resistance = resistance;
    }

    public Short getAccPedal() {
        return accPedal;
    }

    public void setAccPedal(Short accPedal) {
        this.accPedal = accPedal;
    }

    public Short getBreakPedal() {
        return breakPedal;
    }

    public void setBreakPedal(Short breakPedal) {
        this.breakPedal = breakPedal;
    }

    @Override
    public VehicleData decode(ByteBuf byteBuf) {
        VehicleData vehicleData = new VehicleData();
        //车辆状态
        vehicleData.setVehicleStatus(byteBuf.readUnsignedByte());
        //充电状态
        vehicleData.setChargeStatus(byteBuf.readUnsignedByte());
        //运行模式
        vehicleData.setRunStatus(byteBuf.readUnsignedByte());
        //车速
        double speed = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        vehicleData.setSpeed(speed);
        //累计里程
        double totalMillage = new BigDecimal(byteBuf.readUnsignedInt()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        vehicleData.setTotalMillage(totalMillage);
        //总电压
        double totalVoltage = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        vehicleData.setTotalVoltage(totalVoltage);
        //总电流
        double totalCurrent = new BigDecimal((byteBuf.readUnsignedShort() -10000)).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        vehicleData.setTotalCurrent(totalCurrent);
        //SOC电量
        vehicleData.setSocStatus(byteBuf.readUnsignedByte());
        //DC-DC状态
        vehicleData.setDcdcStatus(byteBuf.readUnsignedByte());
        //档位
        short gearState = byteBuf.readUnsignedByte();
        vehicleData.setGearState(gearState);
        vehicleData.setDrivingForce((gearState >> 5) & 1);
        vehicleData.setBrakingForce((gearState >> 4) & 1);
        vehicleData.setGear(gearState & 0X0F);
        //绝缘电阻
        vehicleData.setResistance(byteBuf.readUnsignedShort());
        //加速行程值
        vehicleData.setAccPedal(byteBuf.readUnsignedByte());
        //制动踏板状态
        vehicleData.setBreakPedal(byteBuf.readUnsignedByte());
        return vehicleData;
    }

    @Override
    public ByteBuf encode(){
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeByte(vehicleStatus);
        buffer.writeByte(chargeStatus);
        buffer.writeByte(runStatus);
        buffer.writeDouble(speed);
        buffer.writeDouble(totalMillage);
        buffer.writeDouble(totalVoltage);
        buffer.writeDouble(totalCurrent);
        buffer.writeByte(socStatus);
        buffer.writeByte(dcdcStatus);
        buffer.writeByte(gearState);
        buffer.writeShort(resistance);
        buffer.writeByte(accPedal);
        buffer.writeByte(breakPedal);
        return buffer;
    }
}
