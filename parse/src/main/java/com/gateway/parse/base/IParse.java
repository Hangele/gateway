package com.gateway.parse.base;

import cn.hutool.json.JSONObject;
import com.gateway.parse.enumtype.CommandType;
import io.netty.buffer.ByteBuf;

/**
 * 数据解析顶层接口
 * created by dyy
 */
@SuppressWarnings("all")
public interface IParse {

    JSONObject parseUpJson(CommandType commandType, ByteBuf body) throws Exception;

    JSONObject parseDownJson(CommandType CommandType,ByteBuf body) throws Exception;

}
