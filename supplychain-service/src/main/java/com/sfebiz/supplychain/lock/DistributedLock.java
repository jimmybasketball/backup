package com.sfebiz.supplychain.lock;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

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

    @Resource
    private String env;

    private String lockKey = "UNIQUESUPPLYCHAIN";

    @Resource
    private JedisCluster jedisCluster;

    @Override
    public Boolean fetch(String key) {
        return fetch(key, 300000L);
    }

    @Override
    public Boolean fetch(String key, Long timeout) {

        String fullKey = env + lockKey + key;
        /**
         * 尝试获取分布式锁，超时时间默认是轮询时间的1/2
         */
        try {
            logger.debug("try lock key: " + key);
            String ret = jedisCluster.set(fullKey, fullKey, "NX", "PX", timeout);
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
        }
    }

    @Override
    public void realease(String key) {
        String fullKey = env + lockKey + key;
        jedisCluster.del(fullKey);
    }

}