package com.sfebiz.supplychain.service.port.ftpclient.gz;

import com.sfebiz.supplychain.service.port.ftpclient.FTPClientException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ztc on 2016/11/28.
 */
public class GZFTPClientFactory extends BasePooledObjectFactory<FTPClient> {

    private static final Logger logger = LoggerFactory.getLogger(GZFTPClientFactory.class);

    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        //地址链接超时，暂时不使用，注释该内容

        ftpClient.setDefaultTimeout(GZFTPConfig.getDefaultTimeout());
        ftpClient.setConnectTimeout(GZFTPConfig.getClientTimeout());
        ftpClient.setDataTimeout(GZFTPConfig.getDataTimeout());
        try {
            ftpClient.connect(GZFTPConfig.getRemoteHostIp(), GZFTPConfig.getPort());
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.warn("FTPServer Refused Connection");
                return null;
            }

            logger.info("ftp返回码："+reply);
            boolean result = ftpClient.login(GZFTPConfig.getAccount(), GZFTPConfig.getPassword());
            logger.info("ftp登陆结果："+result+",返回码："+ftpClient.getReplyCode());
            if (!result) {
                throw new FTPClientException("FTP Client Login Failure, account:" + GZFTPConfig.getAccount());
            }
            ftpClient.setFileType(GZFTPConfig.getTransferFileType());
            ftpClient.setBufferSize(1024);
            ftpClient.enterLocalPassiveMode();

        } catch (IOException e) {
            logger.error("FTP连接失败：" + GZFTPConfig.getRemoteHostIp() + ",端口：" + GZFTPConfig.getPort());
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<FTPClient>(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> pooledObject) throws Exception {
        try {
            if (pooledObject != null && pooledObject.getObject().isConnected()) {
                pooledObject.getObject().logout();
            }
        } catch (IOException io) {
            logger.error("销毁FTP Client对象异常", io);
        } finally {
            try {
                pooledObject.getObject().disconnect();
            } catch (IOException io) {
                logger.error("销毁FTP Client对象异常", io);
            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        try {
            FTPClient ftpClient = p.getObject();
            return ftpClient.sendNoOp();
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            return false;
        }
    }

}
