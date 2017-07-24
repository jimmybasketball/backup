package com.sfebiz.supplychain.service.customs.ftpclient.hz;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/31
 * Time: 下午5:21
 */
@Component("hzftpClientPool")
public class HZFTPClientPool implements ObjectPool<FTPClient> {


    private static final Logger logger = LoggerFactory.getLogger(HZFTPClientPool.class);

    private static final int DEFAULT_POOL_SIZE = 8;
    private final BlockingQueue<FTPClient> pool;
    private final HZFTPClientFactory hzftpClientFactory;

    /**
     * 初始化连接池
     *
     * @throws Exception
     */
    public HZFTPClientPool() throws Exception {
        this(DEFAULT_POOL_SIZE, new HZFTPClientFactory());
    }

    /**
     * 初始化连接池，需要注入一个工厂来提供FTPClient实例
     *
     * @param hzftpClientFactory
     * @throws Exception
     */
    public HZFTPClientPool(HZFTPClientFactory hzftpClientFactory) throws Exception {
        this(DEFAULT_POOL_SIZE, hzftpClientFactory);
    }

    /**
     * @param poolSize
     * @param hzftpClientFactory
     * @throws Exception
     */
    public HZFTPClientPool(int poolSize, HZFTPClientFactory hzftpClientFactory) throws Exception {
        this.hzftpClientFactory = hzftpClientFactory;
        pool = new ArrayBlockingQueue<FTPClient>(poolSize * 2);
        initPool(poolSize);
    }

    /**
     * 初始化连接池，需要注入一个工厂来提供FTPClient实例
     *
     * @param maxPoolSize
     * @throws Exception
     */
    private void initPool(int maxPoolSize) throws Exception {
        for (int i = 0; i < maxPoolSize; i++) {
            addObject();
        }
    }


    /**
     * 从对象池中获取一个FTPClient对象
     *
     * @return
     * @throws Exception
     */
    public FTPClient borrowObject() throws Exception {
        FTPClient client = pool.take();
        if (client == null) {
            PooledObject<FTPClient> pooledObject = hzftpClientFactory.makeObject();
            client = pooledObject.getObject();
        } else if (!hzftpClientFactory.validateObject(new DefaultPooledObject<FTPClient>(client))) {
            //使对象在池中失效
            invalidateObject(client);
            //制造并添加新对象到池中
            client = hzftpClientFactory.makeObject().getObject();
            addObject();
        }

        return client;
    }


    /**
     * 回收对象
     *
     * @param client
     * @throws Exception
     */
    public void returnObject(FTPClient client) throws Exception {
        if ((client != null) && !pool.offer(client, 3, TimeUnit.SECONDS)) {
            hzftpClientFactory.destroyObject(new DefaultPooledObject<FTPClient>(client));
        }
    }

    public void invalidateObject(FTPClient client) throws Exception {
        //移除无效的客户端
        pool.remove(client);
    }

    public void addObject() throws Exception {
        pool.offer(hzftpClientFactory.makeObject().getObject(), 3, TimeUnit.SECONDS);
    }

    public int getNumIdle() throws UnsupportedOperationException {
        return 0;
    }

    public int getNumActive() throws UnsupportedOperationException {
        return pool.size();
    }

    public void clear() throws Exception {

    }

    public void close() {
        try {
            while (pool.iterator().hasNext()) {
                FTPClient ftpClient = pool.take();
                hzftpClientFactory.destroyObject(new DefaultPooledObject<FTPClient>(ftpClient));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
