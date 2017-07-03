package com.sfebiz.supplychain.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 16/6/14 上午11:20
 */
public class JedisProxyUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisProxyUtil.class);

    private JedisPool jedisPool;

    public JedisProxyUtil(String redisHost, String redisPassWord, int redisPort, int redisMaxTotal, int redisMinIdle,
                          int redisMaxWaitTime, int redisMaxIdle, boolean redisTestOnBorrow, int redisTimeout) {
        JedisPoolConfig poolConfig = getJedisPoolConfig(redisMaxTotal, redisMinIdle, redisMaxWaitTime, redisMaxIdle, redisTestOnBorrow);
        if (StringUtils.isNotBlank(redisPassWord)) {
            this.jedisPool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout, redisPassWord);
        } else {
            this.jedisPool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout);
        }
        LOGGER.info("redisProxy初始化完成! redisHost = {}, port = {}", redisHost, redisPort);
    }

    public Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

    public void releaseJedis(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }
    public boolean set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } catch (Throwable e) {
            LOGGER.warn("RedisCacheServiceImpl op exception!", e);
        } finally {
            releaseJedis(jedis);
        }
        return Boolean.TRUE;
    }
    public String get(String key) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.get(key);
        } catch (Throwable e) {
            LOGGER.warn("RedisCacheServiceImpl op exception!", e);
        } finally {
            releaseJedis(jedis);
        }
        return result;
    }
    public void closeJedisProxy() {
        LOGGER.info("closeJedisProxy start!");
        try {
            if (jedisPool != null) {
                jedisPool.close();
                LOGGER.warn("JedisProxyUtil 关闭redis连接!");
            }
        } catch (Throwable t) {
            LOGGER.error("closeJedisProxy exception", t);
        }
    }
    public boolean del(String key) {
        boolean result = Boolean.TRUE;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.del(key) != 0;
        } catch (Throwable e) {
            LOGGER.warn("RedisCacheServiceImpl op exception!", e);
        } finally {
            releaseJedis(jedis);
        }
        return result;
    }
    private JedisPoolConfig getJedisPoolConfig(int redisMaxTotal, int redisMinIdle, int redisMaxWaitTime, int redisMaxIdle, boolean redisTestOnBorrow) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //最大连接数, 默认20个
        poolConfig.setMaxTotal(redisMaxTotal);
        //最小空闲连接数, 默认0
        poolConfig.setMinIdle(redisMinIdle);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted), 如果超时就抛异常, 小于零:阻塞不确定的时间, 默认 - 1
        poolConfig.setMaxWaitMillis(redisMaxWaitTime);
        //最大空闲连接数, 默认20个
        poolConfig.setMaxIdle(redisMaxIdle);
        //在获取连接的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(redisTestOnBorrow);
        poolConfig.setTestOnReturn(Boolean.FALSE);
        //在空闲时检查有效性, 默认false
        poolConfig.setTestWhileIdle(Boolean.TRUE);
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        poolConfig.setMinEvictableIdleTimeMillis(1800000);
        //每次逐出检查时 逐出的最大数目 如果为负数就是: idleObjects.size / abs(n), 默认3
        poolConfig.setNumTestsPerEvictionRun(3);
        //对象空闲多久后逐出, 当空闲时间 > 该值 且 空闲连接>最大空闲数 时直接逐出, 不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
        poolConfig.setSoftMinEvictableIdleTimeMillis(1800000);
        //逐出扫描的时间间隔(毫秒) 如果为负数, 则不运行逐出线程, 默认 - 1
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);
        return poolConfig;
    }

}
