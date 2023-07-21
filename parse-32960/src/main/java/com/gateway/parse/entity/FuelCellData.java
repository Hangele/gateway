package com.gateway.parse.entity;

import com.gateway.parse.base.IStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigDecimal;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 燃料电池数据
 */
@SuppressWarnings("all")
public class FuelCellData implements IStatus {

    /**
     * 燃料电池电压
     */
    private Double batteryVoltage;

    /**
     * 燃料电池电流
     */
    private Double batteryCurrent;

    /**
     * 燃料消耗率
     */
    private Double batteryRate;

    /**
     * 燃料电池温度探针总数
     */
    private Integer batteryPinCount;

    /**
     * 探针温度值
     */
    private List<Short> batteryPinTemps;

    /**
     * 氢系统中最高温度
     */
    private Double hyMaxTemp;

    /**
     * 氢系统中最高温度探针代号
     */
    private Short hyMaxTempPin;

    /**
     * 氢气最高浓度
     */
    private Integer hyMaxChroma;

    /**
     * 氢气最高浓度探针代号
     */
    private Short hyMaxChromaPin;

    /**
     * 氢气最高压力
     */
    private Double hyMaxPress;

    /**
     * 氢气最高压力探针代号
     */
    private Short hyMaxPressPin;

    /**
     * 高压DC-DC状态
     */
    private Short highDcdcStatus;

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

    public Double getBatteryRate() {
        return batteryRate;
    }

    public void setBatteryRate(Double batteryRate) {
        this.batteryRate = batteryRate;
    }

    public Integer getBatteryPinCount() {
        return batteryPinCount;
    }

    public void setBatteryPinCount(Integer batteryPinCount) {
        this.batteryPinCount = batteryPinCount;
    }

    public List<Short> getBatteryPinTemps() {
        return batteryPinTemps;
    }

    public void setBatteryPinTemps(List<Short> batteryPinTemps) {
        this.batteryPinTemps = batteryPinTemps;
    }

    public Double getHyMaxTemp() {
        return hyMaxTemp;
    }

    public void setHyMaxTemp(Double hyMaxTemp) {
        this.hyMaxTemp = hyMaxTemp;
    }

    public Short getHyMaxTempPin() {
        return hyMaxTempPin;
    }

    public void setHyMaxTempPin(Short hyMaxTempPin) {
        this.hyMaxTempPin = hyMaxTempPin;
    }

    public Integer getHyMaxChroma() {
        return hyMaxChroma;
    }

    public void setHyMaxChroma(Integer hyMaxChroma) {
        this.hyMaxChroma = hyMaxChroma;
    }

    public Short getHyMaxChromaPin() {
        return hyMaxChromaPin;
    }

    public void setHyMaxChromaPin(Short hyMaxChromaPin) {
        this.hyMaxChromaPin = hyMaxChromaPin;
    }

    public Double getHyMaxPress() {
        return hyMaxPress;
    }

    public void setHyMaxPress(Double hyMaxPress) {
        this.hyMaxPress = hyMaxPress;
    }

    public Short getHyMaxPressPin() {
        return hyMaxPressPin;
    }

    public void setHyMaxPressPin(Short hyMaxPressPin) {
        this.hyMaxPressPin = hyMaxPressPin;
    }

    public Short getHighDcdcStatus() {
        return highDcdcStatus;
    }

    public void setHighDcdcStatus(Short highDcdcStatus) {
        this.highDcdcStatus = highDcdcStatus;
    }

    @Override
    public FuelCellData decode(ByteBuf byteBuf) {
        FuelCellData fuelCellData = new FuelCellData();
        double vol = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        fuelCellData.setBatteryVoltage(vol);
        double current = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        fuelCellData.setBatteryCurrent(current);
        double rate = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        fuelCellData.setBatteryRate(rate);
        fuelCellData.setBatteryPinCount(byteBuf.readUnsignedShort());
        List<Short> temperatureList = new ArrayList<Short>(fuelCellData.getBatteryPinCount());
        if(fuelCellData.getBatteryPinCount()>0){
            for (int i = 0; i < fuelCellData.getBatteryPinCount(); i++) {
                temperatureList.add(byteBuf.readUnsignedByte());
            }
        }
        fuelCellData.setBatteryPinTemps(temperatureList);
        double hyMaxTemp = new BigDecimal((byteBuf.readUnsignedShort() - 100)).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        fuelCellData.setHyMaxTemp(hyMaxTemp);
        fuelCellData.setHyMaxTempPin(byteBuf.readUnsignedByte());
        fuelCellData.setHyMaxChroma(byteBuf.readUnsignedShort());
        fuelCellData.setHyMaxChromaPin(byteBuf.readUnsignedByte());
        double hyMaxPress = new BigDecimal(byteBuf.readUnsignedShort()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        fuelCellData.setHyMaxPress(hyMaxPress);
        fuelCellData.setHyMaxPressPin(byteBuf.readUnsignedByte());
        fuelCellData.setHighDcdcStatus(byteBuf.readUnsignedByte());
        return fuelCellData;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeDouble(batteryVoltage);
        buffer.writeDouble(batteryCurrent);
        buffer.writeDouble(batteryRate);
        buffer.writeShort(batteryPinCount);
        if(batteryPinCount >0 && batteryPinTemps.size()>0){
            for (int i = 0; i <batteryPinTemps.size(); i++) {
                buffer.writeByte(batteryPinTemps.get(i));
            }
        }
        buffer.writeDouble(hyMaxTemp);
        buffer.writeByte(hyMaxTempPin);
        buffer.writeShort(hyMaxChroma);
        buffer.writeByte(hyMaxChromaPin);
        buffer.writeDouble(hyMaxPress);
        buffer.writeByte(hyMaxPressPin);
        buffer.writeByte(highDcdcStatus);
        return buffer;
    }
}
