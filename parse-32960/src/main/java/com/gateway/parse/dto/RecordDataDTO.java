package com.gateway.parse.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName: RecordDataDTO
 * @Description: 数据记录VO
 * @Author: chenhb2
 * @Date: 2023-4-23 9:11
 */
@Slf4j
@Data
public class RecordDataDTO implements Serializable {
    /**
     * 命令ID
     */
    private Integer cmdId;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * 网关时间
     */
    private String gwTime;
    /**
     * 终端唯一标志
     */
    private String vin;
    /**
     * 数据时间
     */
    private String dataTime;
    /**
     * 原始数据
     */
    private String data;

}
