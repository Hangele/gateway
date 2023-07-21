package com.gateway.parse.enumtype;

import com.gateway.parse.base.IStatus;
import com.gateway.parse.entity.RealTimeData;
import com.gateway.parse.entity.VehicleLogin;
import com.gateway.parse.entity.VehicleLogout;
import com.gateway.parse.handler.RealTimeDataHandler;
import com.gateway.parse.handler.VehicleHandler;

public enum EvGBHandlerType {

    //上行指令
    VEHICLE_LOGIN((short)1, "车辆登入",new VehicleLogin(), VehicleHandler.class),
    VEHICLE_LOGOUT((short)4,"车辆登出",new VehicleLogout(),VehicleHandler.class),
    REALTIME_DATA_REPORTING((short)2,"实时信息上报",new RealTimeData(), RealTimeDataHandler.class),
    REPLACEMENT_DATA_REPORTING((short)3,"补发信息上报",new RealTimeData(),RealTimeDataHandler.class);

    private Short id;
    private String desc;
    private IStatus status;
    private Class handler;

    EvGBHandlerType(Short id, String desc, IStatus status, Class handler) {
        this.id = id;
        this.desc = desc;
        this.status = status;
        this.handler = handler;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public IStatus getStatus() {
        return status;
    }

    public void setStatus(IStatus status) {
        this.status = status;
    }

    public Class getHandler() {
        return handler;
    }

    public void setHandler(Class handler) {
        this.handler = handler;
    }

    public static EvGBHandlerType valuesOf(Short id) {
        for (EvGBHandlerType enums : EvGBHandlerType.values()) {
            if (enums.getId()==id) {
                return enums;
            }
        }
        return null;
    }

}
