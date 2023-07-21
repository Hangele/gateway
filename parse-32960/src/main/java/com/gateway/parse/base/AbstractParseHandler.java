package com.gateway.parse.base;

import cn.hutool.json.JSONObject;
import com.gateway.parse.enumtype.CommandType;
import io.netty.buffer.ByteBuf;

/**
 * 实现顶层方法，子类重写对应方法
 * created by dyy
 */
@SuppressWarnings("all")
public class AbstractParseHandler implements IParse{

    @Override
    public JSONObject parseUpJson(CommandType commandType, ByteBuf body) throws Exception {
        return null;
    }

    @Override
    public JSONObject parseDownJson(CommandType CommandType, ByteBuf body) throws Exception {
        return null;
    }
}
