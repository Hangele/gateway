package com.gateway.parse.entity;

import com.gateway.parse.base.IStatus;
import com.gateway.parse.constants.Constants;
import com.gateway.parse.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 报警数据
 */
@SuppressWarnings("all")
public class AlarmData implements IStatus {

    /**
     * 报警等级
     */
    private Short maxAlarmLevel;

    /**
     * 报警通用标志
     */
    private Long alarmFlag;

    /**
     * 温度差异报警
     */
    private Boolean alarm00;

    /**
     * 电池高温报警
     */
    private Boolean alarm01;

    /**
     * 车载储能装置类型过压报警
     */
    private Boolean alarm02;

    /**
     * 车载储能装置类型欠压报警
     */
    private Boolean alarm03;

    /**
     * SOC过低报警
     */
    private Boolean alarm04;

    /**
     * 单体电池过压报警
     */
    private Boolean alarm05;

    /**
     * 单体电池欠压报警
     */
    private Boolean alarm06;

    /**
     * SOC过高报警
     */
    private Boolean alarm07;

    /**
     * SOC跳变报警
     */
    private Boolean alarm08;

    /**
     * 车载储能装置类型不匹配报警
     */
    private Boolean alarm09;

    /**
     * 单体电池一致性差报警
     */
    private Boolean alarm10;

    /**
     * 绝缘报警
     */
    private Boolean alarm11;

    /**
     * DC温度报警
     */
    private Boolean alarm12;

    /**
     * 制动系统报警
     */
    private Boolean alarm13;

    /**
     * DC状态报警
     */
    private Boolean alarm14;

    /**
     * 驱动电机控制器温度报警
     */
    private Boolean alarm15;

    /**
     * 高压互锁报警
     */
    private Boolean alarm16;

    /**
     * 驱动电机温度报警
     */
    private Boolean alarm17;

    /**
     * 车载储能装置过充报警
     */
    private Boolean alarm18;

    /**
     * 可储能装置故障数N1
     */
    private Short batteryAlarmCount;

    /**
     * 可储能装置故障信息列表
     */
    private List<Long> batteryAlarms;

    /**
     * 驱动电机故障数N2
     */
    private Short motorAlarmCount;

    /**
     * 驱动电机故障信息列表
     */
    private List<Long> motorAlarms;

    /**
     * 发动机故障数N3
     */
    private Short engineAlarmCount;

    /**
     * 发动机故障信息列表
     */
    private List<Long> engineAlarms;

    /**
     * 其他故障数N4
     */
    private Short otherAlarmCount;

    /**
     * 其他故障信息列表
     */
    private List<Long> otherAlarms;

    @Override
    public AlarmData decode(ByteBuf byteBuf) {
        AlarmData alarmData = new AlarmData();
        alarmData.setMaxAlarmLevel(byteBuf.readUnsignedByte());
        Long l = byteBuf.readUnsignedInt();
        alarmData.setAlarmFlag(l);
        char[] chars = ByteUtil.to32BinaryString(l.intValue()).toCharArray();
        alarmData.setAlarm00(Constants.CHAR_49 == (chars[chars.length-1]));
        alarmData.setAlarm01(Constants.CHAR_49 == (chars[chars.length-2]));
        alarmData.setAlarm02(Constants.CHAR_49 == (chars[chars.length-3]));
        alarmData.setAlarm03(Constants.CHAR_49 == (chars[chars.length-4]));
        alarmData.setAlarm04(Constants.CHAR_49 == (chars[chars.length-5]));
        alarmData.setAlarm05(Constants.CHAR_49 == (chars[chars.length-6]));
        alarmData.setAlarm06(Constants.CHAR_49 == (chars[chars.length-7]));
        alarmData.setAlarm07(Constants.CHAR_49 == (chars[chars.length-8]));
        alarmData.setAlarm08(Constants.CHAR_49 == (chars[chars.length-9]));
        alarmData.setAlarm09(Constants.CHAR_49 == (chars[chars.length-10]));
        alarmData.setAlarm10(Constants.CHAR_49 == (chars[chars.length-11]));
        alarmData.setAlarm11(Constants.CHAR_49 == (chars[chars.length-12]));
        alarmData.setAlarm12(Constants.CHAR_49 == (chars[chars.length-13]));
        alarmData.setAlarm13(Constants.CHAR_49 == (chars[chars.length-14]));
        alarmData.setAlarm14(Constants.CHAR_49 == (chars[chars.length-15]));
        alarmData.setAlarm15(Constants.CHAR_49 == (chars[chars.length-16]));
        alarmData.setAlarm16(Constants.CHAR_49 == (chars[chars.length-17]));
        alarmData.setAlarm17(Constants.CHAR_49 == (chars[chars.length-18]));
        alarmData.setAlarm18(Constants.CHAR_49 == (chars[chars.length-19]));
        short deviceFailuresTotal = byteBuf.readUnsignedByte();
        alarmData.setBatteryAlarmCount(deviceFailuresTotal);
        List<Long> deviceFailuresList =new ArrayList<Long>(deviceFailuresTotal);
        for (int i = 0; i <deviceFailuresTotal; i++) {
            deviceFailuresList.add(byteBuf.readUnsignedInt());
        }
        alarmData.setBatteryAlarms(deviceFailuresList);
        short driveMotorFailuresTotal = byteBuf.readUnsignedByte();
        alarmData.setMotorAlarmCount(driveMotorFailuresTotal);
        List<Long> driveMotorFailuresList =new ArrayList<Long>(driveMotorFailuresTotal);
        for (int i = 0; i <driveMotorFailuresTotal; i++) {
            driveMotorFailuresList.add(byteBuf.readUnsignedInt());
        }
        alarmData.setMotorAlarms(driveMotorFailuresList);
        short engineFailuresTotal = byteBuf.readUnsignedByte();
        alarmData.setEngineAlarmCount(engineFailuresTotal);
        List<Long> engineFailuresList =new ArrayList<Long>(engineFailuresTotal);
        for (int i = 0; i <engineFailuresTotal; i++) {
            engineFailuresList.add(byteBuf.readUnsignedInt());
        }
        alarmData.setEngineAlarms(engineFailuresList);
        short otherFailuresTotal = byteBuf.readUnsignedByte();
        alarmData.setOtherAlarmCount(otherFailuresTotal);
        List<Long> otherFailuresTotalList =new ArrayList<Long>(otherFailuresTotal);
        for (int i = 0; i <otherFailuresTotal; i++) {
            otherFailuresTotalList.add(byteBuf.readUnsignedInt());
        }
        alarmData.setOtherAlarms(otherFailuresTotalList);
        return alarmData;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeByte(maxAlarmLevel);
        buffer.writeInt(alarmFlag.intValue());
        buffer.writeByte(batteryAlarmCount);
        if(batteryAlarmCount>0 && batteryAlarms.size()>0){
            for (int i = 0; i < batteryAlarmCount; i++) {
                buffer.writeInt(batteryAlarms.get(i).intValue());
            }
        }
        buffer.writeByte(motorAlarmCount);
        if(motorAlarmCount>0 && motorAlarms.size()>0){
            for (int i = 0; i < motorAlarmCount; i++) {
                buffer.writeInt(motorAlarms.get(i).intValue());
            }
        }
        buffer.writeByte(engineAlarmCount);
        if(engineAlarmCount>0 && engineAlarms.size()>0){
            for (int i = 0; i < engineAlarmCount; i++) {
                buffer.writeInt(engineAlarms.get(i).intValue());
            }
        }
        buffer.writeByte(otherAlarmCount);
        if(otherAlarmCount>0 && otherAlarms.size()>0){
            for (int i = 0; i < otherAlarmCount; i++) {
                buffer.writeInt(otherAlarms.get(i).intValue());
            }
        }
        return buffer;
    }

    public Short getMaxAlarmLevel() {
        return maxAlarmLevel;
    }

    public void setMaxAlarmLevel(Short maxAlarmLevel) {
        this.maxAlarmLevel = maxAlarmLevel;
    }

    public Long getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(Long alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    public Boolean getAlarm00() {
        return alarm00;
    }

    public void setAlarm00(Boolean alarm00) {
        this.alarm00 = alarm00;
    }

    public Boolean getAlarm01() {
        return alarm01;
    }

    public void setAlarm01(Boolean alarm01) {
        this.alarm01 = alarm01;
    }

    public Boolean getAlarm02() {
        return alarm02;
    }

    public void setAlarm02(Boolean alarm02) {
        this.alarm02 = alarm02;
    }

    public Boolean getAlarm03() {
        return alarm03;
    }

    public void setAlarm03(Boolean alarm03) {
        this.alarm03 = alarm03;
    }

    public Boolean getAlarm04() {
        return alarm04;
    }

    public void setAlarm04(Boolean alarm04) {
        this.alarm04 = alarm04;
    }

    public Boolean getAlarm05() {
        return alarm05;
    }

    public void setAlarm05(Boolean alarm05) {
        this.alarm05 = alarm05;
    }

    public Boolean getAlarm06() {
        return alarm06;
    }

    public void setAlarm06(Boolean alarm06) {
        this.alarm06 = alarm06;
    }

    public Boolean getAlarm07() {
        return alarm07;
    }

    public void setAlarm07(Boolean alarm07) {
        this.alarm07 = alarm07;
    }

    public Boolean getAlarm08() {
        return alarm08;
    }

    public void setAlarm08(Boolean alarm08) {
        this.alarm08 = alarm08;
    }

    public Boolean getAlarm09() {
        return alarm09;
    }

    public void setAlarm09(Boolean alarm09) {
        this.alarm09 = alarm09;
    }

    public Boolean getAlarm10() {
        return alarm10;
    }

    public void setAlarm10(Boolean alarm10) {
        this.alarm10 = alarm10;
    }

    public Boolean getAlarm11() {
        return alarm11;
    }

    public void setAlarm11(Boolean alarm11) {
        this.alarm11 = alarm11;
    }

    public Boolean getAlarm12() {
        return alarm12;
    }

    public void setAlarm12(Boolean alarm12) {
        this.alarm12 = alarm12;
    }

    public Boolean getAlarm13() {
        return alarm13;
    }

    public void setAlarm13(Boolean alarm13) {
        this.alarm13 = alarm13;
    }

    public Boolean getAlarm14() {
        return alarm14;
    }

    public void setAlarm14(Boolean alarm14) {
        this.alarm14 = alarm14;
    }

    public Boolean getAlarm15() {
        return alarm15;
    }

    public void setAlarm15(Boolean alarm15) {
        this.alarm15 = alarm15;
    }

    public Boolean getAlarm16() {
        return alarm16;
    }

    public void setAlarm16(Boolean alarm16) {
        this.alarm16 = alarm16;
    }

    public Boolean getAlarm17() {
        return alarm17;
    }

    public void setAlarm17(Boolean alarm17) {
        this.alarm17 = alarm17;
    }

    public Boolean getAlarm18() {
        return alarm18;
    }

    public void setAlarm18(Boolean alarm18) {
        this.alarm18 = alarm18;
    }

    public Short getBatteryAlarmCount() {
        return batteryAlarmCount;
    }

    public void setBatteryAlarmCount(Short batteryAlarmCount) {
        this.batteryAlarmCount = batteryAlarmCount;
    }

    public List<Long> getBatteryAlarms() {
        return batteryAlarms;
    }

    public void setBatteryAlarms(List<Long> batteryAlarms) {
        this.batteryAlarms = batteryAlarms;
    }

    public Short getMotorAlarmCount() {
        return motorAlarmCount;
    }

    public void setMotorAlarmCount(Short motorAlarmCount) {
        this.motorAlarmCount = motorAlarmCount;
    }

    public List<Long> getMotorAlarms() {
        return motorAlarms;
    }

    public void setMotorAlarms(List<Long> motorAlarms) {
        this.motorAlarms = motorAlarms;
    }

    public Short getEngineAlarmCount() {
        return engineAlarmCount;
    }

    public void setEngineAlarmCount(Short engineAlarmCount) {
        this.engineAlarmCount = engineAlarmCount;
    }

    public List<Long> getEngineAlarms() {
        return engineAlarms;
    }

    public void setEngineAlarms(List<Long> engineAlarms) {
        this.engineAlarms = engineAlarms;
    }

    public Short getOtherAlarmCount() {
        return otherAlarmCount;
    }

    public void setOtherAlarmCount(Short otherAlarmCount) {
        this.otherAlarmCount = otherAlarmCount;
    }

    public List<Long> getOtherAlarms() {
        return otherAlarms;
    }

    public void setOtherAlarms(List<Long> otherAlarms) {
        this.otherAlarms = otherAlarms;
    }
}
