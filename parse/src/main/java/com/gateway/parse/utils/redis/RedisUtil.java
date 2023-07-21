package com.gateway.parse.utils.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Set;

/**
 * Redis工具类
 *
 * @author RenLong
 *
 */
@Slf4j
public class RedisUtil {
	// Redis服务器IP
	public static String ADDR_ARRAY1 = "39.105.131.113";
	// Redis的端口号
	public static int PORT1 = 6379;
	// Redis的数据库
	public static int DB1 = 8;
	// 访问密码
	public static String AUTH1 = null;
	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 200;
	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 200;
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 10000;
	// 超时时间
	private static int TIMEOUT = 2000;
	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;
	// 在return给pool时，是否提前进行validate操作；
	private static boolean TEST_ON_RETURN = true;
	private static JedisPool jedisPool = null;
	// redis过期时间,以秒为单位
	public final static int EXRP_HOUR = 60 * 60; // 一小时
	public final static int EXRP_DAY = 60 * 60 * 24; // 一天
	public final static int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月

	/**
	 * 初始化Redis连接池
	 */
	private static void initialPool() {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			config.setTestOnReturn(TEST_ON_RETURN);
			jedisPool = new JedisPool(config, ADDR_ARRAY1, PORT1, TIMEOUT,
					AUTH1, DB1);
		} catch (Exception e) {
			log.error("First create JedisPool error : " + e);
		}
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (jedisPool == null) {
			initialPool();
		}
	}

	/**
	 * 同步获取Jedis实例
	 *
	 * @return Jedis
	 */
	public synchronized static Jedis getJedis() {
		if (jedisPool == null) {
			poolInit();
		}
		Jedis jedis = null;
		try {
			if (jedisPool != null) {
				jedis = jedisPool.getResource();
			}
		} catch (Exception e) {
			log.error("Get jedis error : " + e);
			returnBrokenResource(jedis);
		}
		return jedis;
	}

	/**
	 * 释放jedis资源
	 *
	 * @param jedis
	 */
	@SuppressWarnings("deprecation")
	public static void returnResource(final Jedis jedis) {
		if (jedis != null && jedisPool != null) {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 释放jedis资源
	 *
	 * @param jedis
	 */
	@SuppressWarnings("deprecation")
	public static void returnBrokenResource(final Jedis jedis) {
		if (jedis != null && jedisPool != null) {
			jedisPool.returnBrokenResource(jedis);
		}
	}

	/**
	 * 设置 String
	 *
	 * @param key
	 * @param value
	 */
	public static void setString(Jedis jedis, String key, String value) {
		try {
			value = (value == null) ? "" : value;
			if (jedis != null && jedis.isConnected()) {
				jedis.set(key, value);
			}
		} catch (Exception e) {
			log.error("Set String error : " + e);
			returnBrokenResource(jedis);
		}
	}

	/**
	 * 设置 Map
	 *
	 * @param jedis
	 * @param key
	 * @param map
	 */
	public static void setMap(Jedis jedis, String key, Map<String, String> map) {
		try {
			if (jedis != null && jedis.isConnected()) {
				jedis.hmset(key, map);
			}
		} catch (Exception e) {
			log.error("Set Map error : " + e);
			returnBrokenResource(jedis);
		}
	}

	/**
	 * 设置 Set
	 *
	 * @param jedis
	 * @param key
	 * @param set
	 */
	public static void setSet(Jedis jedis, String key, Set<String> set) {
		try {
			if (jedis != null && jedis.isConnected()) {
				for (String s : set) {
					jedis.sadd(key, s);
				}
			}
		} catch (Exception e) {
			log.error("Set Set error : " + e);
			returnBrokenResource(jedis);
		}
	}

	/**
	 * 设置 Set
	 *
	 * @param jedis
	 * @param key
	 * @param member
	 */
	public static void setSet(Jedis jedis, String key, String member) {
		try {
			if (jedis != null && jedis.isConnected()) {
				jedis.sadd(key, member);
			}
		} catch (Exception e) {
			log.error("Set Set error : " + e);
			returnBrokenResource(jedis);
		}
	}

	/**
	 * 设置 过期时间
	 *
	 * @param key
	 * @param seconds
	 *            以秒为单位
	 * @param value
	 */
	public static void setString(Jedis jedis, String key, int seconds,
			String value) {
		try {
			value = (value == null) ? "" : value;
			if (jedis != null && jedis.isConnected()) {
				jedis.setex(key, seconds, value);
			}
		} catch (Exception e) {
			log.error("Set keyex error : " + e);
			returnBrokenResource(jedis);
		}
	}

	/**
	 * 获取String值
	 *
	 * @param key
	 * @return value
	 */
	public static String getString(Jedis jedis, String key) {
		if (jedis == null || !jedis.isConnected() || !jedis.exists(key)) {
			return null;
		}
		return jedis.get(key);
	}

	/**
	 * 判断key是否存在
	 *
	 * @param jedis
	 * @param key
	 * @return
	 */
	public static boolean existKey(Jedis jedis, String key) {
		try {
			if (jedis == null || !jedis.isConnected() || !jedis.exists(key)) {
				return false;
			}
		} catch (Exception e) {
			log.error("Exist Key error : " + e);
			returnBrokenResource(jedis);
			return false;
		}
		return true;
	}

	public static void close(Jedis jedis) {
		if (jedis != null) {
			try {
				jedis.close();
			}
			catch (Exception e) {}
		}
	}
}
