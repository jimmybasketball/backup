package com.sfebiz.supplychain.service.customs.biz;

import cn.gov.zjport.newyork.ws.bo.HzPortBusinessType;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.BillType;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.protocol.ceb.order.callback.CEB312Message;
import com.sfebiz.supplychain.protocol.ceb.order.callback.OrderReturnType;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.service.customs.ftpclient.gz.GZFTPClientPool;
import com.sfebiz.supplychain.service.customs.ftpclient.gz.GZFTPConfig;
import com.sfebiz.supplychain.service.customs.ftpclient.hz.HZFTPClientPool;
import com.sfebiz.supplychain.service.customs.ftpclient.hz.HZFTPConfig;
import com.sfebiz.supplychain.service.customs.ftpclient.pt.PTFTPClientPool;
import com.sfebiz.supplychain.service.customs.ftpclient.pt.PTFTPConfig;
import com.sfebiz.supplychain.util.XMLUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/31
 * Time: 下午7:06
 */
@Component("customsOfficeService")
public class CustomsOfficeService {

    private static final Logger logger = LoggerFactory.getLogger(CustomsOfficeService.class);

    @Resource
    private PTFTPClientPool ptftpClientPool;

    @Resource
    private HZFTPClientPool hzftpClientPool;

    @Resource
    private GZFTPClientPool gzftpClientPool;

    @Resource
    private PortBillDeclareManager portBillDeclareManager;




    /**
     * 向远程服务器上推送报文信息
     *
     * @param messageContent
     * @param remoteFileName
     * @throws ServiceException
     */
    public void sendMessageToRemoteMQ(String messageContent, String remoteFileName , String port) throws ServiceException {
        if (StringUtils.isBlank(messageContent)) {
            //订单下发海关总署，参数不合法
            //throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL);
        }

        FTPClient ftpClient = null;
        InputStream inputStream = null;
        try {
            if (StringUtils.isBlank(remoteFileName)) {
                remoteFileName = UUID.randomUUID().toString();
            }
            if(PortNid.HANGZHOU.getNid().equals(port)){
                ftpClient = hzftpClientPool.borrowObject();
                ftpClient.changeWorkingDirectory(HZFTPConfig.getTmpFilePath());
                inputStream = new ByteArrayInputStream(messageContent.getBytes("UTF-8"));
                boolean storeFileResult = ftpClient.storeFile(remoteFileName, inputStream);
                if (!storeFileResult) {
                    //订单下发海关总署，发送异常
                    //throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR, LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR + "，Ftp 上传报文文件 失败");
                }
                boolean renameResult = ftpClient.rename(remoteFileName, "../" + HZFTPConfig.getSendFilePath() + "/" + remoteFileName);
                if (!renameResult) {
                    //订单下发海关总署，发送异常
                    //throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR, LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR + "，FtpClient rename 失败");
                }

            }else if(PortNid.PINGTAN.getNid().equals(port)){
                ftpClient = ptftpClientPool.borrowObject();
                ftpClient.changeWorkingDirectory(PTFTPConfig.getTmpFilePath());
                inputStream = new ByteArrayInputStream(messageContent.getBytes("UTF-8"));
                boolean storeFileResult = ftpClient.storeFile("/"+PTFTPConfig.getSendFilePath()+remoteFileName, inputStream);
                if (!storeFileResult) {
                    //订单下发海关总署，发送异常
                    //throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR, LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR + "，Ftp 上传报文文件 失败");
                }
            }else {
                ftpClient = gzftpClientPool.borrowObject();
                ftpClient.changeWorkingDirectory(GZFTPConfig.getTmpFilePath());
                inputStream = new ByteArrayInputStream(messageContent.getBytes("UTF-8"));
                boolean storeFileResult = ftpClient.storeFile("/"+GZFTPConfig.getSendFilePath()+remoteFileName, inputStream);
                if (!storeFileResult) {
                    //订单下发海关总署，发送异常
                    //throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR, LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR + "，Ftp 上传报文文件 失败");
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //订单下发海关总署，发送异常
            //throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR, LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (ftpClient != null) {
                try {
                    if(PortNid.HANGZHOU.getNid().equals(port)){
                        hzftpClientPool.returnObject(ftpClient);

                    }else if(PortNid.PINGTAN.getNid().equals(port)){
                        ptftpClientPool.returnObject(ftpClient);

                    }else {
                        gzftpClientPool.returnObject(ftpClient);

                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 从远程服务器上读取海关回执文件
     */
    public void readMessageFromRemoteMQ(String port) {
        FTPClient ftpClient = null;
        try {
            if(PortNid.HANGZHOU.getNid().equals(port)){
                ftpClient = hzftpClientPool.borrowObject();
                ftpClient.changeWorkingDirectory(HZFTPConfig.getReadFilePath());

            }else if(PortNid.PINGTAN.getNid().equals(port)){
                ftpClient = ptftpClientPool.borrowObject();
                ftpClient.changeWorkingDirectory(PTFTPConfig.getReadFilePath());

            }else {
                ftpClient = gzftpClientPool.borrowObject();
                ftpClient.changeWorkingDirectory(GZFTPConfig.getReadFilePath());

            }
            FTPFile[] ftpFiles = ftpClient.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                for (int i = 0; i < ftpFiles.length; i++) {
                    FTPFile ftpFile = ftpFiles[i];
                    if (ftpFile.isFile() && ftpFile.hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION)) {
                        InputStream inputStream = ftpClient.retrieveFileStream(ftpFile.getName());
                        BaseResult processResult = processOrderCallBackResult(inputStream);
                        if (processResult != null && processResult.isSuccess()) {
                            String newFileName = processResult.getResultMap().get("bizId");
                            if (StringUtils.isBlank(newFileName)) {
                                newFileName = ftpFile.getName();
                            }
                            boolean renameResult = ftpClient.deleteFile(ftpFile.getName());
                            if (!renameResult) {
                                logger.warn("删除海关回执文件：" + ftpFile.getName() + "异常。");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (ftpClient != null) {
                try {
                    if(PortNid.HANGZHOU.getNid().equals(port)){
                        hzftpClientPool.returnObject(ftpClient);

                    }else if(PortNid.PINGTAN.getNid().equals(port)){
                        ptftpClientPool.returnObject(ftpClient);

                    }else {
                        gzftpClientPool.returnObject(ftpClient);

                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 处理海关回执文件内容
     *
     * @param inputStream
     * @return
     */
    private BaseResult processOrderCallBackResult(InputStream inputStream) {
        BaseResult baseResult = new BaseResult(false);
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String tmpData;
            StringBuffer resultBuffer = new StringBuffer();
            try {
                while ((tmpData = bufferedReader.readLine()) != null) {
                    resultBuffer.append(tmpData);
                }
                String message = resultBuffer.toString().replace("http://www.chinaport.gov.cn/ceb", "");
                CEB312Message ceb312Message = XMLUtil.converyToJavaBean(message, CEB312Message.class);
                if (ceb312Message != null && ceb312Message.getOrderReturn() != null && StringUtils.isNotBlank(ceb312Message.getOrderReturn().getOrderNo())) {
                    if (!OrderReturnType.isSuccess(Integer.valueOf(ceb312Message.getOrderReturn().getReturnStatus()))) {
//                        ticketHandleService.createTicket(request.getStockoutOrderDO(), stockoutOrderTaskDO.getId(), exceptionCode);
                    }
                    baseResult.setSuccess(Boolean.TRUE);
                    Map module = new HashMap<String, String>();
                    module.put("bizId", ceb312Message.getOrderReturn().getOrderNo());
                    baseResult.setResultMap(module);
                }

                return baseResult;
            } catch (Exception e) {
                logger.error("文件读取异常" + e.getMessage(), e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }

        return baseResult;
    }

    /**
     * 获取 订单申报结果记录
     *
     * @param stockoutOrderDO
     * @return
     * @throws ServiceException
     */
    private PortBillDeclareDO getPortOrderBillDO(StockoutOrderDO stockoutOrderDO) throws ServiceException {
        if (stockoutOrderDO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .addParm("[出库单或口岸不存在]参数：", stockoutOrderDO)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PARAMS_ILLEGAL, LogisticsReturnCode.LOGISTICS_COMPANY_SERVICE_PARAMS_ILLEGAL.getDesc());
        }
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderDO.getId());
        portBillDeclareDO.setPortId(Long.valueOf(PortNid.CUSTOMSOFFICE.getValue()));
        portBillDeclareDO.setBillType(BillType.ORDER_BILL.getType());
        portBillDeclareDO.setBusinessType(HzPortBusinessType.IMPORTORDER.getType());
        List<PortBillDeclareDO> portBillDeclareDOs = portBillDeclareManager.query(BaseQuery.getInstance(portBillDeclareDO));
        if (portBillDeclareDOs != null && portBillDeclareDOs.size() > 0) {
            return portBillDeclareDOs.get(0);
        }
        return null;
    }

    /**
     * 创建订单申报记录
     *
     * @param stockoutOrderDO
     * @return
     * @throws ServiceException
     */
    private boolean createPPortOrderBill(StockoutOrderDO stockoutOrderDO) throws ServiceException {
        if (stockoutOrderDO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .addParm("[出库单或口岸不存在]参数：", stockoutOrderDO)
                    .log();
            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL, LogisticsReturnCode.STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL.getDesc());
        }
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderDO.getId());
        portBillDeclareDO.setBusinessNo(stockoutOrderDO.getBizId());
        portBillDeclareDO.setPortId(Long.valueOf(PortNid.CUSTOMSOFFICE.getValue()));
        portBillDeclareDO.setBillType(BillType.ORDER_BILL.getType());
        portBillDeclareDO.setBusinessType(HzPortBusinessType.IMPORTORDER.getType());
        portBillDeclareDO.setBillName(BillType.ORDER_BILL.getDescription());
        portBillDeclareDO.setDeclareType(DeclareType.CREATE.getType());
        portBillDeclareDO.setState(PortBillState.WAIT_SEND.getValue());
        portBillDeclareManager.insert(portBillDeclareDO);
        return portBillDeclareDO.getId() != null;
    }


}
