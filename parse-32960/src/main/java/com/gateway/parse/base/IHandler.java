package com.gateway.parse.base;

import com.gateway.parse.entity.EvGBProtocol;
import io.netty.channel.Channel;

/**
 * 业务处理类
 * created by dyy
 */
@SuppressWarnings("all")
public interface IHandler {

    void doBusiness(EvGBProtocol evGBProtocol, Channel channel) throws Exception;
}
