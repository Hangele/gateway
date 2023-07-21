package com.gateway.parse.entity;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.buffer.ByteBuf;

/**
 * 数据单元
 */
@SuppressWarnings("all")
public class DataBody {

    private JSONObject json;

    @JsonIgnore
    private ByteBuf byteBuf;

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public DataBody() {
    }

    public DataBody(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }
}
