package com.gateway.parse.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gateway.parse.base.AbstractParseHandler;
import com.gateway.parse.base.IStatus;
import com.gateway.parse.enumtype.CommandType;
import com.gateway.parse.enumtype.EvGBHandlerType;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Service;

/**
 * 数据解析处理器
 * created by dyy
 */
@SuppressWarnings("all")
@Service
public class EvGBParseHandler extends AbstractParseHandler {

    /**
     * 解析Tbox的上行JSON报文方法
     * @param commandType
     * @param byteBuf
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject parseUpJson(CommandType commandType, ByteBuf byteBuf) throws Exception {
        if (null != commandType) {
            IStatus status = EvGBHandlerType.valuesOf(commandType.getId()).getStatus();
            return status == null ? null : (JSONObject) JSONUtil.parseObj(status.decode(byteBuf));
        }
        return null;
    }
}
