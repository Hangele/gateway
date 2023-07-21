package com.gateway.parse.utils;

import io.netty.buffer.ByteBuf;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 数据处理工具类
 *
 * @author chb
 *
 */
public class DataUtil {

	/**
	 * 字节数组转换成十六进制的字符串
	 *
	 * @param content
	 * @return
	 */
	public static String getHexStringByBytes(byte[] content) {
		// 测试:原始数据输出
		String hexString = "0123456789ABCDEF";
		StringBuilder sb = new StringBuilder(content.length * 2);
		for (int i = 0; i < content.length; i++) {
			sb.append(hexString.charAt((content[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((content[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}


	/** * @功能: BCD码转为10进制串(阿拉伯数据) * @参数: BCD码 * @结果: 10进制串 */
	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}


	/**
	 * 将十六进制数字字符串转为BYTE
	 *
	 * @param str
	 *            长度为2的十六进制数字字符串 如"FF"、"12"
	 * @return
	 */
	public static byte getByteByString(String str) {
		byte data= (byte) Integer.parseInt(str, 16);
//		if(data<0) {
//			return (data & 0xFF);
//		}
		return data;
	}


	/**
	 * 获得要解析的原始数据
	 *
	 * @param strData
	 * @return
	 */
	public static byte[] getBytesByHexString(String strData) {
		byte[] content = {};
		if (strData != null) {
			content = new byte[strData.length() / 2];
			for (int i = 0; i < (strData.length() / 2); i++) {
				content[i] = getByteByString(strData.substring(2 * i, 2 * i + 2));
			}
		}
		return content;
	}

	/**
	 * 计算校验码
	 *
	 * @param data
	 * @return
	 */
	public static int getCheckCode(byte[] data) {
		int CheckCode = data[0];
		for (int i = 1; i < data.length; i++) {
			CheckCode = CheckCode ^ data[i];
		}
		return CheckCode;
	}

	/**
	 * 将ASCII字节数组转换成字符串
	 *
	 * @param asc
	 * @return
	 */
	public static String getStringByAscii(byte[] asc) {
		int length = asc.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append((char) asc[i]);
		}
		return sb.toString().trim();
	}

	/**
	 * 将字节转化为无符号整数
	 *
	 * @param data
	 * @return
	 */
	public static int getNonSignByte(byte data) {
		if (data < 0) {
			int a = data + 256;
			return a;
		} else {
			return data;
		}
	}

	/**
	 * 将字节数组转换为无符号整数
	 *
	 * @param data
	 * @return
	 */
	public static long getNonSignBytes(byte[] data) {
		long value = 0;
		long length = data.length;

		for (int i = 0; i < length; i++) {
			value += getNonSignByte(data[i]) * getPower(256, length - i - 1);
		}

		return value;
	}

	/**
	 * 指数运算
	 *
	 * @param bottom
	 *            底数
	 * @param exponent
	 *            指数
	 * @return power 幂
	 */
	public static long getPower(long bottom, long exponent) {
		long power = 1;

		for (int i = 0; i < exponent; i++) {
			power = power * bottom;
		}

		return power;
	}

	/**
	 * 获取单位数据（字节位从左到右从1到8）
	 *
	 * @param data
	 * @param position
	 * @return
	 */
	public static int getBit(byte data, int position) {
		int paramData = 0;

		paramData = (data >> (8 - position)) & 1;

		return getNonSignByte((byte) paramData);
	}

	/**
	 * 获取多位数据（字节位从左到右从1到8）
	 *
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getBits(byte data, int start, int end) {
		int paramData = 0;

		for (int i = start; i <= end; i++) {
			int temp = getBit(data, i);
			paramData += temp * Math.pow(2, end - i);
		}

		return getNonSignByte((byte) paramData);
	}

	/**
	 * 获取多位数据（字节位从左到右从1到8）
	 *
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getBits(byte[] data, int start, int end) {
		int paramData = 0;
		int stbyte = start / 8;
		int offset = start % 8;
		int endbyte = end / 8;
		int offsetEnd = end % 8;
		int curpos = 0;
		for (int i = stbyte; i <= endbyte; i++) {
			int endpos = 8;
			if (stbyte == endbyte) {
				endpos = offsetEnd;
			}
			for (int i1 = offset; i1 < endpos; i1++) {
				int temp = getBit(data[i], offset);
				paramData += temp * Math.pow(2, curpos);
				curpos++;
			}
		}
		return getNonSignByte((byte) paramData);
	}

	/**
	 * 获取单字节数据（字节数组从左到右从1到高）
	 *
	 * @param data
	 * @param position
	 * @return
	 */
	public static byte getByte(byte[] data, int position) {
		byte paramData = 0;

		paramData = data[position - 1];

		return paramData;
	}

	/**
	 * 将数据解析成十六进制字符串
	 *
	 * @param data
	 * @param flag
	 * @return
	 */
	public static String parseHex(byte[] data, boolean flag) {
		String paramValue = null;

		paramValue = getHexStringByBytes(data);

		return paramValue;
	}

	/**
	 * 解析时间
	 *
	 * @param data
	 * @return
	 */
	public static String getTime(byte[] data) {
		if (data == null) {
			return "";
		}
		String time = "20";
		for (int i = 0; i < data.length; i++) {
			time += String.format("%02d", data[i]);
		}
		return time;
	}

	private static ScriptEngineManager manager = new ScriptEngineManager();
	private static ScriptEngine engine = manager.getEngineByName("JavaScript");
	public static String exception = "数据异常";
	public static String error = "数据无效";
	public static int exception_0x = 0xFE;
	public static int error_0x = 0xFF;

	/**
	 * 数据解析
	 *
	 * @param buffer
	 *            数据
	 * @param length
	 *            数据长度
	 * @param expression
	 *            表达式 可以传null
	 * @param check
	 *            是否判断有效性
	 * @return
	 */
	public static String getParamValue(ByteBuf buffer, int length, String expression, boolean check) {
		if (buffer.readableBytes() < length) {
			return null;
		}

		long temp = 0;
		switch (length) {
		case 1:
			temp = buffer.readUnsignedByte();
			if (check) {
				if (temp == 0xFF) {
					return error;
				}
				if (temp == 0xFE) {
					return exception;
				}
			}
			break;
		case 2:
			temp = buffer.readUnsignedShort();
			if (check) {
				if (temp == 0xFFFF) {
					return error;
				}
				if (temp == 0xFFFE) {
					return exception;
				}
			}
			break;
		case 4:
			temp = buffer.readUnsignedInt();
			if (check) {
				if (temp == 0xFFFFFFFF) {
					return error;
				}
				if (temp == 0xFFFFFFFE) {
					return exception;
				}
			}
			break;
		default:
			return null;
		}

		if (expression != null) {
			expression = expression.replaceAll("d", String.valueOf(temp));
			try {
				return engine.eval(expression).toString();
			} catch (ScriptException e) {
				return null;
			}
		} else {
			return String.valueOf(temp);
		}
	}

	/**
	 * 数据解析
	 *
	 * @param buffer
	 *            数据
	 * @param length
	 *            数据长度
	 * @param expression
	 *            表达式 可以传null
	 * @param check
	 *            是否判断有效性
	 * @return
	 */
	public static String getParamValueSmall(ByteBuf buffer, int length, String expression, boolean check) {
		if (buffer.readableBytes() < length) {
			return null;
		}

		long temp = 0;
		switch (length) {
		case 1:
			temp = buffer.readUnsignedByte();
			if (check) {
				if (temp == 0xFF) {
					return error;
				}
				if (temp == 0xFE) {
					return exception;
				}
			}
			break;
		case 2:
			byte[] temp2 = new byte[2];
			buffer.readBytes(temp2);
			temp = temp2[1] * 256 + temp2[0];
			if (check) {
				if (temp == 0xFFFF) {
					return error;
				}
				if (temp == 0xFFFE) {
					return exception;
				}
			}
			break;
		case 3:
			byte[] temp3 = new byte[3];
			buffer.readBytes(temp3);
			temp = temp3[2] * 256 * 256 + temp3[1] * 256 + temp3[0];
			if (check) {
				if (temp == 0xFFFF) {
					return error;
				}
				if (temp == 0xFFFE) {
					return exception;
				}
			}
			break;
		case 4:
			byte[] temp4 = new byte[4];
			buffer.readBytes(temp4);
			temp = temp4[3] * 256 * 256 * 256 + temp4[2] * 256 * 256 + temp4[1] * 256 + temp4[0];
			if (check) {
				if (temp == 0xFFFFFFFF) {
					return error;
				}
				if (temp == 0xFFFFFFFE) {
					return exception;
				}
			}
			break;
		default:
			return null;
		}

		if (expression != null) {
			expression = expression.replaceAll("d", String.valueOf(temp));
			try {
				return engine.eval(expression).toString();
			} catch (ScriptException e) {
				return null;
			}
		} else {
			return String.valueOf(temp);
		}
	}

	public static boolean checkValue(String value) {
		if (value == null || value.equals(error) || value.equals(exception)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取无符号byte
	 *
	 * @param buff
	 * @return
	 */
	public static short readUnsignedByte(ByteBuf buff) {
		return (short) (buff.readShort() & 0xff);
	}

	/**
	 * 设置无符号byte
	 *
	 * @param buff
	 * @param position
	 * @param value
	 *            设置的值
	 * @return
	 */
	public static void putUnsignedByte(ByteBuf buff, int position, int value) {
		buff.setByte(position, (byte) (value & 0xff));
	}

	/**
	 * 获取无符号short
	 *
	 * @param buff
	 * @return
	 */
	public static short readUShort(ByteBuf buff) {
		return (short) (buff.readShort() & 0xffff);
	}

	/**
	 * 获取无符号short
	 *
	 * @param buff
	 * @return
	 */
	public static short getUShort(ByteBuf buff, int pos) {
		return (short) (buff.getShort(pos) & 0xffff);
	}

	/**
	 * 设置无符号short
	 *
	 * @param buff
	 * @param position
	 * @param value
	 *            设置的值
	 * @return
	 */
	public static void putUShort(ByteBuf buff, int position, int value) {
		buff.setShort(position, (byte) (value & 0xffff));
	}

	/**
	 * 获取无符号int
	 *
	 * @param buff
	 * @return
	 */
	public static int readUInt(ByteBuf buff) {
		return (int) (buff.readInt() & 0xffffffffL);
	}

	/**
	 * 获取无符号int
	 *
	 * @param buff
	 * @return
	 */
	public static int getUInt(ByteBuf buff, int pos) {
		return (int) (buff.getInt(pos) & 0xffffffffL);
	}

	/**
	 * 设置无符号int
	 *
	 * @param buff
	 * @param position
	 * @param value
	 *            设置的值
	 * @return
	 */
	public static void putUInt(ByteBuf buff, int position, int value) {
		buff.setInt(position, (byte) (value & 0xffffffffL));
	}

	/**
	 * 判断是否是整数.
	 *
	 * @return
	 */
	public static boolean isUInteger(String input) {
		String vinput = input;
		if (input.toLowerCase().startsWith("0x")) {
			vinput = vinput.substring(2);
			return isHexNumber(vinput);
		}
		return isOctNumber(vinput);
		// Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(vinput);
		// return mer.find();
	}

	/**
	 * 判断是否是浮点数.
	 *
	 * @return
	 */
	public static boolean isDouble(String input) {
		String vinput = input;
		if (input.toLowerCase().startsWith("0x")) {
			vinput = vinput.substring(2);
			return isHexDouble(vinput);
		}
		return isOctDouble(vinput);
		// Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(vinput);
		// return mer.find();
	}

	/**
	 * 判断是否是10进制.效率高些
	 *
	 * @return
	 */
	private static boolean isOctNumber(String str) {
		boolean flag = false;
		for (int i = 0, n = str.length(); i < n; i++) {
			char c = str.charAt(i);
			if (c == '0' | c == '1' | c == '2' | c == '3' | c == '4' | c == '5' | c == '6' | c == '7' | c == '8'
					| c == '9') {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 判断是否是16进制.效率高些
	 *
	 * @return
	 */
	private static boolean isHexNumber(String str) {
		boolean flag = false;
		for (int i = 0; i < str.length(); i++) {
			char cc = str.charAt(i);
			if (cc == '0' || cc == '1' || cc == '2' || cc == '3' || cc == '4' || cc == '5' || cc == '6' || cc == '7'
					|| cc == '8' || cc == '9' || cc == 'A' || cc == 'B' || cc == 'C' || cc == 'D' || cc == 'E'
					|| cc == 'F' || cc == 'a' || cc == 'b' || cc == 'c' || cc == 'c' || cc == 'd' || cc == 'e'
					|| cc == 'f') {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 判断是否可转换10进制.效率高些
	 *
	 * @return
	 */
	private static boolean isOctDouble(String str) {
		boolean flag = false;
		for (int i = 0, n = str.length(); i < n; i++) {
			char c = str.charAt(i);
			if (c == '0' | c == '1' | c == '2' | c == '3' | c == '4' | c == '5' | c == '6' | c == '7' | c == '8'
					| c == '9' | c == '.' | c == '+' | c == '-') {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 判断是否可转换16进制.效率高些
	 *
	 * @return
	 */
	private static boolean isHexDouble(String str) {
		boolean flag = false;
		for (int i = 0; i < str.length(); i++) {
			char cc = str.charAt(i);
			if (cc == '0' || cc == '1' || cc == '2' || cc == '3' || cc == '4' || cc == '5' || cc == '6' || cc == '7'
					|| cc == '8' || cc == '9' || cc == 'A' || cc == 'B' || cc == 'C' || cc == 'D' || cc == 'E'
					|| cc == 'F' || cc == 'a' || cc == 'b' || cc == 'c' || cc == 'c' || cc == 'd' || cc == 'e'
					|| cc == 'f' || cc == '.' || cc == '+' || cc == '-') {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 字符串转整数，如果是0x开头，则按16进制方式解析，否则默认解析,如果解析失败,则返回默认值.
	 *
	 * @param input
	 * @return
	 */
	public static int ParseInt(String input, Integer defaultval) {
		if (isUInteger(input)) {
			if (!input.toLowerCase().startsWith("0x")) {
				return Integer.parseInt(input);
			}
			return Integer.parseInt(input.substring(2), 16);
		} else {
			return defaultval;
		}
	}

	/**
	 * 字符串转整数，如果是0x开头，则按16进制方式解析，否则默认解析,如果解析失败,则返回默认值.
	 *
	 * @param input
	 * @return
	 */
	public static double ParseDouble(String input, Integer defaultval) {
		if (isDouble(input)) {
			return Double.parseDouble(input);
		} else {
			return defaultval;
		}
	}

	/**
	 * 判断是否是IP地址
	 * @param str
	 * @return
	 */
    public static Boolean isIPAddressByRegex(String str) {
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        // 判断ip地址是否与正则表达式匹配
        if (str.matches(regex)) {
            String[] arr = str.split("\\.");
            for (int i = 0; i < 4; i++) {
                int temp = Integer.parseInt(arr[i]);
                //如果某个数字不是0到255之间的数 就返回false
                if (temp < 0 || temp > 255) {
					return false;
				}
            }
            return true;
        } else {
			return false;
		}
    }

	/**
	 * 获取指定元素位置
	 * @param array
	 * @param value
	 * @return
	 */
	public static int printArray(int[] array,int value){
		for(int i = 0;i<array.length;i++){
			if(array[i]==value){
				return i;
			}
		}
		//当if条件不成立时，默认返回一个负数值-1
		return -1;
	}

	/**
	 * 获取一个字节高四位
	 * @param data
	 * @return
	 */
	public static int getHeight4(byte data) {
		int height;
		height = ((data & 0xf0) >> 4);
		return height;
	}

	/**
	 * 获取一个字节的低四位
	 * @param data
	 * @return
	 */
	public static int getLow4(byte data) {
		int low;
		low = (data & 0x0f);
		return low;
	}

	/**
	 * b为传入的字节，start是起始位，length是长度，如要获取bit0-bit4的值，则start为0，length为5
	 * @param b        一个字节：10011001
	 * @param start     起始bit位。如0位
	 * @param length    需要的bit的长度。如5个bit--------> 11001
	 * @return
	 */
	public static int getByteBits(byte b, int start, int length) {
		//字节b有8位bit，右移start位，截取长度为5的bit
		//10011001 右移 0位,还是10011001
		//0xFF的二进制为  11111111（8个1），右移8-5的长度，变为：00011111
		//      10011001
		//  &   00011111
		//      00011001   --------> bit
		int bit = (b>>start)&(0xFF>>(8-length));
		return bit;
	}

	public static void main(String[] args) {
		String str = "FFF230FFD70089DA0D820D000000000000";
		byte[] content = getBytesByHexString(str);
		String vin = new String(content);
		System.err.println(vin);

	}
}
