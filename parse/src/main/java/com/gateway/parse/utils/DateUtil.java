package com.gateway.parse.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description: 日期处理工具类
 * @author chb
 * @date 2021-12-28 8:47
 */
@Slf4j
public class DateUtil {

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
	 * 取当前时间字符串（速度快）
	 *
	 * @return 例:20120206
	 */
	public static long getCurrentTime() {
		Calendar ca = Calendar.getInstance();
		return ca.get(Calendar.YEAR) * 10000 + (ca.get(Calendar.MONTH) + 1) * 100 + ca.get(Calendar.DATE);
	}

	/**
	 * 当前年份
	 *
	 * @return
	 */
	public static String getThisYear() {
		Calendar rightNow = Calendar.getInstance();
		return Integer.toString(rightNow.get(Calendar.YEAR));
	}

	/**
	 * 根据日期，返回字符串
	 */
	public static String getStringByDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date);
	}

	/**
	 * 根据毫秒值获取时间字符串
	 *
	 * @param time
	 * @return
	 */
	public static String getStringByLong(long time) {
		Date date = new Date(time);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}

	public static String getFormatStrByLong(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("");
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * 将日期由 20170101031122 变为 2017-01-01 03:11:22
	 *
	 * @param time
	 *            20170101031122
	 * @return
	 */
	public static String getDateFormat(String time) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new SimpleDateFormat("yyyyMMddHHmmss").parse(time));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 根据日期，返回字符串
	 */
	public static String getCurTimeString() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public static long getTimeByString(String time) {
		try {
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(time);
			return date.getTime();
		} catch (ParseException e) {
			log.error("时间格式错误！" + time, e);
			return 0;
		}
	}

	/**
	 * 计算日期时间差
	 *
	 * @param startDateStr
	 *            开始时间
	 * @param endDateStr
	 *            结束时间
	 * @param format
	 *            返回格式
	 * @return
	 */
	public static long differBetweenDate(String startDateStr, String endDateStr, String format) {
		long result = 0L;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date startDate = sdf.parse(startDateStr);

			Date endDate = sdf.parse(endDateStr);

			long millisecond = endDate.getTime() - startDate.getTime();

			switch (format) {
			case "hour":
				result = Math.abs(millisecond / (1000 * 60 * 60));
				break;
			case "minute":
				result = Math.abs(millisecond / (1000 * 60));
				break;
			case "second":
				result = Math.abs(millisecond / (1000));
				break;
			default:
				result = Math.abs(millisecond / (1000 * 60 * 60));
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String formatSecond(String date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = sf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sf.format(d);
	}

	/**
	 * yyyyMMddHHmmss -- > yyyy-MM-dd HH:mm:ss
	 *
	 * @param strDate
	 * @return
	 */
	public static String strToDate(String strDate, String format) {
		Date preDateStr;
		String lastDateStr = "";
		try {
			preDateStr = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDate);
			lastDateStr = new SimpleDateFormat(format).format(preDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lastDateStr;
	}

	/**
	 * 时间转字符串
	 */
	public static Date parseStrToDate(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date2 = null;
		try {
			date2 = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}
}
