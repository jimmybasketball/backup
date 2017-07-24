package com.sfebiz.supplychain.service.customs.ftpclient.hz;

import com.sfebiz.supplychain.service.customs.ftpclient.FTPClientException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/31
 * Time: 下午5:23
 */
public class HZFTPClientFactory extends BasePooledObjectFactory<FTPClient> {

    private static final Logger logger = LoggerFactory.getLogger(HZFTPClientFactory.class);

    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        //地址链接超时，暂时不使用，注释该内容
        
        ftpClient.setDefaultTimeout(HZFTPConfig.getDefaultTimeout());
        ftpClient.setConnectTimeout(HZFTPConfig.getClientTimeout());
        ftpClient.setDataTimeout(HZFTPConfig.getDataTimeout());
        try {
            ftpClient.connect(HZFTPConfig.getRemoteHostIp(), HZFTPConfig.getPort());
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.warn("FTPServer Refused Connection");
                return null;
            }
            boolean result = ftpClient.login(HZFTPConfig.getAccount(), HZFTPConfig.getPassword());
            if (!result) {
                throw new FTPClientException("FTP Client Login Failure, account:" + HZFTPConfig.getAccount());
            }
            ftpClient.setFileType(HZFTPConfig.getTransferFileType());
            ftpClient.setBufferSize(1024);
            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            logger.error("FTP连接失败：" + HZFTPConfig.getRemoteHostIp() + ",端口：" + HZFTPConfig.getPort());
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
