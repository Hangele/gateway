package com.gateway.receive.dto;

import lombok.Data;

@Data
public class SqlDTO {

	private String vin;

	private String tableName;

	private String sql;
}
