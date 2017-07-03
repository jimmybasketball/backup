package com.sfebiz.supplychain.lock;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/1/9 上午10:49
 */
public interface Lock {

    /**
     * 去fetch key的锁，成功返回true, 默认超时时间5分钟
     *
     * @param key
     * @return
     */
    public Boolean fetch(String key);

    /**
     * 去fetch key的锁，成功返回true
     * @param key
     * @param timeout, 锁超时时间，单位ms
     * @return 返回结果，成功true，失败false
     */
    public Boolean fetch(String key, Long timeout);

    /**
     * 释放锁
     *
     * @param key
     */
    public void realease(String key);
}
