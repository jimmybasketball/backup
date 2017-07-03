package com.sfebiz.supplychain.lock;

import com.sfebiz.supplychain.config.JedisProxyUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 14/11/24 下午11:56
 */
@Component("distributedLock")
public class DistributedLock implements Lock {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    private static final String lockKey = "UNIQUESUPPLYCHAIN";

    @Resource
    private JedisProxyUtil jedisProxyUtil;

    @Override
    public Boolean fetch(String key) {
        return fetch(key, 300000L);
    }

    @Override
    public Boolean fetch(String key, Long timeout) {

        String fullKey = lockKey + key;
        /**
         * 尝试获取分布式锁，超时时间默认是轮询时间的1/2
         */
        Jedis jedis = null;
        try {
            logger.debug("try lock key: " + key);
            jedis = jedisProxyUtil.getJedis();
            String ret = jedis.set(fullKey, fullKey, "NX", "PX", timeout);
            if (StringUtils.equalsIgnoreCase("OK", ret)) {
                logger.info("get lock, key: " + fullKey + " , expire in " + timeout + " seconds.");
                return Boolean.TRUE;
            } else { // 存在锁
                logger.warn("key: " + fullKey + " locked by another business!");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            logger.error("fetch lock exception: ", e);
            return Boolean.FALSE;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void realease(String key) {
        Jedis jedis = null;
        try {
            String fullKey = lockKey + key;
            jedis = jedisProxyUtil.getJedis();
            jedis.del(fullKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}