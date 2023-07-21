package com.gateway.parse.base;

import com.gateway.parse.entity.EvGBProtocol;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现顶层方法，子类重写对应方法
 * 公用抽取
 * created by dyy
 */
@SuppressWarnings("all")
public abstract class AbstractBusinessHandler implements IHandler {

    /**
     * 公用业务处理方法
     * @param protrocol
     * @param channel
     */
    @Override
    public abstract void doBusiness(EvGBProtocol protrocol, Channel channel) throws Exception;

}
