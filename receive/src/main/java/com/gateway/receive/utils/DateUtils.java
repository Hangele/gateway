package com.gateway.receive.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description: 日期处理工具类
 * @author Hangele
 */
@Slf4j
public class DateUtils {

	/**
	 * 获得当前时间的字节时间
	 */
	public static byte[] getCurrentTimeBytes() {
		return new byte[] { (byte) (Calendar.getInstance().get(Calendar.YEAR) - 2000),
				(byte) (Calendar.getInstance().get(Calendar.MONTH) + 1),
				(byte) (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)),
				(byte) (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)),
				(byte) (Calendar.getInstance().get(Calendar.MINUTE)),
				(byte) (Calendar.getInstance().get(Calendar.SECOND)) };
	}

	/**
	 * 根据日期，返回字符串
	 */
	public static String getStringByDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date);
	}

}
