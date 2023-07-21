package com.gateway.parse.handler;

import com.gateway.parse.base.AbstractBusinessHandler;
import com.gateway.parse.entity.EvGBProtocol;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 车辆登入与车辆登出处理器
 * 需要给出终端对应响应：
 * 因拆开了数据接入与解析，此处无需应答，可根据需要做对应业务处理
 * created by dyy
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class VehicleHandler extends AbstractBusinessHandler {

    @Override
    public void doBusiness(EvGBProtocol protrocol, Channel channel) {
        switch (protrocol.getCommandType()){
            case VEHICLE_LOGIN: {
                break;
            }
            case VEHICLE_LOGOUT:{
                break;
            }
            default:
                break;
        }
    }
}
