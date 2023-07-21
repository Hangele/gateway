package com.gateway.parse.base;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

/**
 * 协议抽象层
 */
@SuppressWarnings("all")
public interface IProtocol extends Serializable {

    /**
     * 编码
     * @return
     */
    ByteBuf encode() throws Exception;

    /**
     * 解码
     * @param locationBytes
     * @return
     */
    IProtocol decode(ByteBuf bytes);
}
