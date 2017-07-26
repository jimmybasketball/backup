package com.sfebiz.supplychain.service.port.ftpclient.gz;

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
 * Created by ztc on 2016/11/28.
 */
@Component("gzftpClientPool")
public class GZFTPClientPool implements ObjectPool<FTPClient> {

    private static final Logger logger = LoggerFactory.getLogger(GZFTPClientPool.class);

    private static final int DEFAULT_POOL_SIZE = 8;
    private final BlockingQueue<FTPClient> pool;
    private final GZFTPClientFactory gzftpClientFactory;

    /**
     * 初始化连接池
     *
     * @throws Exception
     */
    public GZFTPClientPool() throws Exception {
        this(DEFAULT_POOL_SIZE, new GZFTPClientFactory());
    }

    /**
     * 初始化连接池，需要注入一个工厂来提供FTPClient实例
     *
     * @param gzftpClientFactory
     * @throws Exception
     */
    public GZFTPClientPool(GZFTPClientFactory gzftpClientFactory) throws Exception {
        this(DEFAULT_POOL_SIZE, gzftpClientFactory);
    }

    /**
     * @param poolSize
     * @param gzftpClientFactory
     * @throws Exception
     */
    public GZFTPClientPool(int poolSize, GZFTPClientFactory gzftpClientFactory) throws Exception {
        this.gzftpClientFactory = gzftpClientFactory;
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
            PooledObject<FTPClient> pooledObject = gzftpClientFactory.makeObject();
            client = pooledObject.getObject();
        } else if (!gzftpClientFactory.validateObject(new DefaultPooledObject<FTPClient>(client))) {
            //使对象在池中失效
            invalidateObject(client);
            //制造并添加新对象到池中
            client = gzftpClientFactory.makeObject().getObject();
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
            gzftpClientFactory.destroyObject(new DefaultPooledObject<FTPClient>(client));
        }
    }

    public void invalidateObject(FTPClient client) throws Exception {
        //移除无效的客户端
        pool.remove(client);
    }

    public void addObject() throws Exception {
        pool.offer(gzftpClientFactory.makeObject().getObject(), 3, TimeUnit.SECONDS);
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
                gzftpClientFactory.destroyObject(new DefaultPooledObject<FTPClient>(ftpClient));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
