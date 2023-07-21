package com.gateway.parse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gateway.parse.base.IProtocol;
import com.gateway.parse.constants.Constants;
import com.gateway.parse.enumtype.CommandType;
import com.gateway.parse.enumtype.EncryptionType;
import com.gateway.parse.enumtype.ResponseType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * GB32960协议封装
 */
@SuppressWarnings("all")
public class EvGBProtocol implements IProtocol {

    /**
     * 固定##开头
     */
    @JsonIgnore
    private Boolean begin = Boolean.TRUE;

    /**
     * 命令标识
     */
    private CommandType commandType;

    /**
     * 应答标志
     */
    private ResponseType responseType = ResponseType.COMMAND;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 数据单元加密方式
     */
    private EncryptionType encryptionType = EncryptionType.NONE;

    /**
     * 数据单元长度
     */
    private Integer length;

    /**
     * 数据单元
     */
    private DataBody body;

    /**
     * BCC校验
     */
    private Boolean bcc = Boolean.TRUE;

    /**
     * 网关接收时间  协议内部不使用
     */
    private Long gatewayReceiveTime;

    /**
     * 网关转发时间  协议内部不使用
     */
    private Long gatewayForwardTime;

    /**
     * 协议编码
     * @return
     */
    @Override
    public ByteBuf encode() throws Exception {
        if(vin.length()!=17){
            throw new Exception("vin length must be 17");
        }
        ByteBuf bccBuffer = Unpooled.buffer();
        bccBuffer.order(ByteOrder.BIG_ENDIAN);
        bccBuffer.writeByte(commandType.getId());
        bccBuffer.writeByte(responseType.getId());
        bccBuffer.writeBytes(vin.getBytes(Charset.forName(Constants.UTF_8)));
        bccBuffer.writeByte(encryptionType.getId());
        if(body!=null && body.getByteBuf()!=null && body.getByteBuf().readableBytes()>0){
            bccBuffer.writeShort(body.getByteBuf().readableBytes());
            bccBuffer.writeBytes(body.getByteBuf());
        }else{
            bccBuffer.writeShort(0);
        }
        byte bcc = signBcc(bccBuffer);
        //组装数据包返回最终编码结果
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeBytes(Constants.BEGIN.getBytes(Charset.forName(Constants.UTF_8)));
        buffer.writeBytes(bccBuffer);
        buffer.writeByte(bcc);
        return buffer;
    }

    /**
     * 协议解码 只负责解码数据包
     * 数据单元解码由ParseHandler完成
     * @return
     */
    @Override
    public EvGBProtocol decode(ByteBuf byteBuf) {
        EvGBProtocol protocol = new EvGBProtocol();
        Boolean checkBcc = EvGBProtocol.checkBcc(byteBuf);
        protocol.setBcc(checkBcc);
        Boolean checkBegin = Constants.BEGIN.equals(byteBuf.readSlice(2).toString(Charset.forName(Constants.UTF_8)));
        protocol.setBegin(checkBegin);
        protocol.setCommandType(CommandType.valuesOf(byteBuf.readUnsignedByte()));
        protocol.setResponseType(ResponseType.valuesOf(byteBuf.readUnsignedByte()));
        protocol.setVin(byteBuf.readSlice(17).toString(Charset.forName(Constants.UTF_8)));
        protocol.setEncryptionType(EncryptionType.valuesOf(byteBuf.readUnsignedByte()));
        //校验码正确与起始符号正确的时候才解析数据单元
        if(checkBcc && checkBegin){
            int length = byteBuf.readUnsignedShort();
            protocol.setLength(length);
            if(length>0){
                protocol.setBody(new DataBody(byteBuf.readSlice(length).copy()));
            }
        }
        protocol.setGatewayReceiveTime(System.currentTimeMillis());
        byteBuf.readUnsignedByte();
        return protocol;
    }

    /**
     * BCC校验(异或校验)
     * @param byteBuf
     * @return
     */
    public static byte signBcc(ByteBuf byteBuf) {
        byte cs = 0;
        while (byteBuf.isReadable()){
            cs ^= byteBuf.readByte();
        }
        byteBuf.resetReaderIndex();
        return cs;
    }

    /**
     * 校验BCC是否正确
     * @param byteBuf
     * @return
     */
    public static Boolean checkBcc(ByteBuf byteBuf) {
        byte bcc = byteBuf.getByte(byteBuf.readableBytes()-1);
        ByteBuf slice = byteBuf.slice(2, byteBuf.readableBytes()-3);
        byte checkBcc = signBcc(slice);
        byteBuf.resetReaderIndex();
        return bcc == checkBcc;
    }

    public Boolean getBegin() {
        return begin;
    }

    public void setBegin(Boolean begin) {
        this.begin = begin;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public EncryptionType getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(EncryptionType encryptionType) {
        this.encryptionType = encryptionType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public DataBody getBody() {
        return body;
    }

    public void setBody(DataBody body) {
        this.body = body;
    }

    public Boolean getBcc() {
        return bcc;
    }

    public void setBcc(Boolean bcc) {
        this.bcc = bcc;
    }

    public Long getGatewayReceiveTime() {
        return gatewayReceiveTime;
    }

    public void setGatewayReceiveTime(Long gatewayReceiveTime) {
        this.gatewayReceiveTime = gatewayReceiveTime;
    }

    public Long getGatewayForwardTime() {
        return gatewayForwardTime;
    }

    public void setGatewayForwardTime(Long gatewayForwardTime) {
        this.gatewayForwardTime = gatewayForwardTime;
    }
}
