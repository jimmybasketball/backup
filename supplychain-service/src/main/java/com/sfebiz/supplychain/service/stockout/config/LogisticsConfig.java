package com.sfebiz.supplychain.service.stockout.config;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/1/9 下午12:11
 */
public class LogisticsConfig {

    private final static Logger          logger                                      = LoggerFactory
                                                                                         .getLogger(LogisticsConfig.class);
    private static final LogisticsConfig dynamicConfig                               = new LogisticsConfig();
    private volatile Integer             processInterval;
    private volatile Integer             updateInterval;
    private volatile Integer             syncInterval;
    private volatile Integer             msgResendInterval;
    private volatile Integer             sendStockoutDelayTime;

    private volatile Integer             sendStockoutFinishDelayTime;
    private volatile Integer             routesInterval;
    private volatile String              lockKey;
    private volatile Boolean             enableProcess;
    private volatile Boolean             enableUpdate;
    private volatile Boolean             enableSync;
    private volatile Boolean             enablePollingRoutes;
    private volatile Boolean             enableMsgResend;
    private volatile Boolean             enableAutoTask;
    private volatile Boolean             enableAutoIncr                              = true;
    // 补货提醒定时任务开关
    private volatile Boolean             enableSkuPurchaseNoticeProcess;
    private volatile Boolean             isPauseAudit;
    private volatile String              processMachines;
    private volatile String              updateMachines;
    private volatile String              syncMachines;
    private volatile String              routesMachines;
    private volatile String              monthSupplyMachines;
    private volatile String              msgResendMachines;
    private volatile String              skuPurchaseNoticeMachines;
    private volatile String              expectedExportShipOrderTimePoint;
    private volatile Integer             exportShipOrderNum;
    private volatile String              ossShipOrderTraversalTimePoint;
    private volatile List<String>        processMachineList                          = new ArrayList<String>();
    private volatile List<String>        updateMachineList                           = new ArrayList<String>();
    private volatile List<String>        syncMachineList                             = new ArrayList<String>();
    private volatile List<String>        msgResendMachineList                        = new ArrayList<String>();
    private volatile List<String>        routesMachineList                           = new ArrayList<String>();
    private volatile List<String>        skuPurchaseNoticeMachineList                = new ArrayList<String>();
    private String                       hzPortPublicKey;
    private String                       syncWarehouseList;
    private volatile String              productDeclareInitNoticeEmails;
    private volatile String              galOrderAuditEmailNotice;
    private volatile String              coeGalOrderEmailNotice;
    // 商品出库异常，短信通知列表
    private volatile String              skuStockoutExceptionNoticeSmss;
    private volatile List<String>        skuStockoutExceptionNoticeSmssList          = new ArrayList<String>();
    // 仓库业务属性支持变更通知
    private volatile String              warehouseNoticeSmss;
    private volatile List<String>        warehouseNoticeSmsList                      = new ArrayList<String>();
    // 称重回传消息延迟发送毫秒数
    private volatile Integer             weightEventMessageDelayMillis;
    // 称重消息发送延迟时间配置
    private volatile Integer             weightEventMessageDelayTimeMillis;
    // 物流人员email
    private volatile String              logisticsEmails;
    private volatile List<String>        logisticsEmailList                          = new ArrayList<String>();
    // 入库完成时不需要设置安全库存的商品类目
    private volatile String              notSetSafeStockCategorys;
    private volatile List<String>        notSetSafeStockCategoryList                 = new ArrayList<String>();
    // 用户触发获取路由时间间隔，单位为毫秒
    private volatile Integer             fetchUserRouteIntervalTimeMillis;
    // 路由信息在redis中过期时间, 单位为秒
    private volatile Integer             fetchUserRouteTimeoutSeconds;
    // 是否发送发货延迟短信通知客户开关
    private volatile Boolean             isSendDelaySms;
    // 是否向所有仓库发送延迟短信
    private volatile Boolean             isSendAllWarehouses                         = false;
    // 是否一个用户只发送一条短信
    private volatile Boolean             isSendOnlyOnce                              = false;
    // 需要发送发货延迟短信的仓库列表
    private volatile String              needSendDelaySmsWarehouses;
    private volatile List<Long>          needSendDelaySmsWarehouseList               = new ArrayList<Long>();
    // 发货延迟短信内容
    private volatile String              sendDelayMsgContent;
    // 需要定时检查出库商品数量是否超过设定阈值的仓库列表
    private volatile String              needCheckStockoutSkuCountWarehouses;
    private volatile List<Long>          needCheckStockoutSkuCountWarehouseList      = new ArrayList<Long>();
    // 仓库的已出库商品数量大于设定阈值时，需要通知的人员邮件名单
    private volatile String              stockoutNumOverLimitEmails;
    private volatile List<String>        stockoutNumOverLimitEmailList               = new ArrayList<String>();
    // 入库完成时，实收为0的是否生成记录
    private volatile Boolean             isAllowStockZero;
    // 是否启用身份证的实名认证
    private volatile Boolean             isEnableIdNoCheck;

    private volatile String              dailyReportCheckNotifySms                   = "18721616270,13301613669";
    // 退税申请：小二后台可以退税路线
    private volatile String              refundTaxLine                               = "USPS,EMS,ETK,SF";
    //效期商品预警：是否做正品转坏品
    private volatile Boolean             enableSkuExpireJob;

    // SWI供货商定时任务相关参数
    private volatile Boolean             swiAutoTask;
    private volatile long                providerId;
    private volatile long                logisticsProviderId;
    private volatile long                warehouseId;

    // 需要在出库单已出库时，设置mailNo的商户列表
    private volatile String              needAtLogisticsShippedSetMailNoMerchants;
    private volatile List<String>        needAtLogisticsShippedSetMailNoMerchantList = new ArrayList<String>();

    // 费舍尔访问地址白名单
    private volatile String              fseRequestIPWhites;
    private volatile List<String>        fseRequestIPWhiteList;

    public String getDailyReportCheckNotifySms() {
        return dailyReportCheckNotifySms;
    }

    public void setDailyReportCheckNotifySms(String dailyReportCheckNotifySms) {
        this.dailyReportCheckNotifySms = dailyReportCheckNotifySms;
    }

    public Boolean getSwiAutoTask() {
        return swiAutoTask;
    }

    public void setSwiAutoTask(Boolean swiAutoTask) {
        this.swiAutoTask = swiAutoTask;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public long getLogisticsProviderId() {
        return logisticsProviderId;
    }

    public void setLogisticsProviderId(long logisticsProviderId) {
        this.logisticsProviderId = logisticsProviderId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    private ManagerListener  dynamicConfigListener     = new ManagerListener() {
                                                           public void receiveConfigInfo(String configInfo) {
                                                               configInfo = StringUtils
                                                                   .strip(configInfo);
                                                               if (StringUtils
                                                                   .isNotBlank(configInfo)) {
                                                                   logger
                                                                       .warn("Change logistics config: "
                                                                             + configInfo);
                                                                   Properties prop = new Properties();
                                                                   try {
                                                                       prop.load(new StringReader(
                                                                           configInfo));
                                                                       updateConfig(prop);
                                                                   } catch (Exception e) {
                                                                       logger.error(
                                                                           "Parse logistics config failed:"
                                                                                   + configInfo, e);
                                                                   }
                                                               }
                                                           }

                                                           public Executor getExecutor() {
                                                               return null;
                                                           }
                                                       };
    private volatile Boolean enableGenerateDailyReport = true;

    private LogisticsConfig() {
        String groupId = "HAITAO";
        String dataId = "com.sfebiz.logistics.dynamic.config";
        DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager(groupId, dataId,
            dynamicConfigListener);
        String configInfo = defaultDiamondManager.getAvailableConfigureInfomation(1000);
        dynamicConfigListener.receiveConfigInfo(configInfo);
    }

    public static LogisticsConfig getInstance() {
        return dynamicConfig;
    }

    public static void main(String args[]) {
        System.out.println(LogisticsConfig.getInstance().getEnableMsgResend());
        System.out.println(LogisticsConfig.getInstance().getMsgResendMachineList().size());
        System.out.println(LogisticsConfig.getInstance().getMsgResendInterval());

        System.out.println(LogisticsConfig.getInstance().getSkuPurchaseNoticeMachineList());
        System.out.println(LogisticsConfig.getInstance().getEnableSkuPurchaseNoticeProcess());
        System.out.println(LogisticsConfig.getInstance().getRefundTaxLine());
        System.out.println(LogisticsConfig.getInstance().getEnableSkuExpireJob());
    }

    public String getMonthSupplyMachines() {
        return monthSupplyMachines;
    }

    public void setMonthSupplyMachines(String monthSupplyMachines) {
        this.monthSupplyMachines = monthSupplyMachines;
    }

    public String getSkuStockoutExceptionNoticeSmss() {
        return skuStockoutExceptionNoticeSmss;
    }

    public void setSkuStockoutExceptionNoticeSmss(String skuStockoutExceptionNoticeSmss) {
        this.skuStockoutExceptionNoticeSmss = skuStockoutExceptionNoticeSmss;
    }

    public List<String> getSkuStockoutExceptionNoticeSmssList() {
        if (StringUtils.isNotEmpty(skuStockoutExceptionNoticeSmss)) {
            skuStockoutExceptionNoticeSmssList = new ArrayList<String>();
            for (String tmp : skuStockoutExceptionNoticeSmss.split(",")) {
                skuStockoutExceptionNoticeSmssList.add(tmp);
            }
            return skuStockoutExceptionNoticeSmssList;
        }
        return new ArrayList<String>();
    }

    public void setSkuStockoutExceptionNoticeSmssList(List<String> skuStockoutExceptionNoticeSmssList) {
        this.skuStockoutExceptionNoticeSmssList = skuStockoutExceptionNoticeSmssList;
    }

    public Integer getWeightEventMessageDelayMillis() {
        return weightEventMessageDelayMillis;
    }

    public void setWeightEventMessageDelayMillis(Integer weightEventMessageDelayMillis) {
        this.weightEventMessageDelayMillis = weightEventMessageDelayMillis;
    }

    public Integer getWeightEventMessageDelayTimeMillis() {
        return weightEventMessageDelayTimeMillis;
    }

    public void setWeightEventMessageDelayTimeMillis(Integer weightEventMessageDelayTimeMillis) {
        this.weightEventMessageDelayTimeMillis = weightEventMessageDelayTimeMillis;
    }

    public Integer getFetchUserRouteIntervalTimeMillis() {
        return fetchUserRouteIntervalTimeMillis;
    }

    public void setFetchUserRouteIntervalTimeMillis(Integer fetchUserRouteIntervalTimeMillis) {
        this.fetchUserRouteIntervalTimeMillis = fetchUserRouteIntervalTimeMillis;
    }

    public List<String> getLogisticsEmailList() {
        if (StringUtils.isNotEmpty(logisticsEmails)) {
            logisticsEmailList = new ArrayList<String>();
            for (String tmp : logisticsEmails.split(",")) {
                logisticsEmailList.add(tmp);
            }
            return logisticsEmailList;
        }
        return new ArrayList<String>();
    }

    public void setLogisticsEmailList(List<String> logisticsEmailList) {
        this.logisticsEmailList = logisticsEmailList;
    }

    public String getLogisticsEmails() {
        return logisticsEmails;
    }

    public void setLogisticsEmails(String logisticsEmails) {
        this.logisticsEmails = logisticsEmails;
    }

    private void updateConfig(Properties properties) throws Exception {
        BeanUtils.copyProperties(this, properties);
    }

    public Integer getProcessInterval() {
        return processInterval;
    }

    public void setProcessInterval(Integer processInterval) {
        this.processInterval = processInterval;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getHzPortPublicKey() {
        return hzPortPublicKey;
    }

    public void setHzPortPublicKey(String hzPortPublicKey) {
        this.hzPortPublicKey = hzPortPublicKey;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public Boolean getEnableProcess() {
        return enableProcess;
    }

    public void setEnableProcess(Boolean enableProcess) {
        this.enableProcess = enableProcess;
    }

    public Boolean getEnableUpdate() {
        return enableUpdate;
    }

    public void setEnableUpdate(Boolean enableUpdate) {
        this.enableUpdate = enableUpdate;
    }

    public String getProcessMachines() {
        return processMachines;
    }

    public void setProcessMachines(String processMachines) {
        this.processMachines = processMachines;
        List<String> tmpProcessMachineList = new ArrayList<String>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(processMachines)) {
            String[] processMachineArray = processMachines.split(",");
            for (String processMachine : processMachineArray) {
                tmpProcessMachineList.add(processMachine.trim());
            }
        }
        processMachineList.clear();
        processMachineList = tmpProcessMachineList;
    }

    public String getMsgResendMachines() {
        return msgResendMachines;
    }

    public void setMsgResendMachines(String msgResendMachines) {
        this.msgResendMachines = msgResendMachines;
        List<String> tmpMsgResendMachines = new ArrayList<String>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(msgResendMachines)) {
            String[] processMachineArray = msgResendMachines.split(",");
            for (String processMachine : processMachineArray) {
                tmpMsgResendMachines.add(processMachine.trim());
            }
        }
        msgResendMachineList.clear();
        msgResendMachineList = tmpMsgResendMachines;
    }

    public String getSkuPurchaseNoticeMachines() {
        return skuPurchaseNoticeMachines;
    }

    public void setSkuPurchaseNoticeMachines(String skuPurchaseNoticeMachines) {
        this.skuPurchaseNoticeMachines = skuPurchaseNoticeMachines;
        List<String> tmpSkuPurchaseNoticeMachines = new ArrayList<String>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(skuPurchaseNoticeMachines)) {
            String[] tmpSkuPurchaseNoticeMachineArray = skuPurchaseNoticeMachines.split(",");
            for (String tmpSkuPurchaseNoticeMachine : tmpSkuPurchaseNoticeMachineArray) {
                tmpSkuPurchaseNoticeMachines.add(tmpSkuPurchaseNoticeMachine.trim());
            }
        }
        skuPurchaseNoticeMachineList.clear();
        skuPurchaseNoticeMachineList = tmpSkuPurchaseNoticeMachines;
    }

    public List<String> getMsgResendMachineList() {
        return msgResendMachineList;
    }

    public void setMsgResendMachineList(List<String> msgResendMachineList) {
        this.msgResendMachineList = msgResendMachineList;
    }

    public String getUpdateMachines() {
        return updateMachines;
    }

    public void setUpdateMachines(String updateMachines) {
        this.updateMachines = updateMachines;
        List<String> tmpUpdateMachineList = new ArrayList<String>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(updateMachines)) {
            String[] updateMachineArray = updateMachines.split(",");
            for (String updateMachine : updateMachineArray) {
                tmpUpdateMachineList.add(updateMachine.trim());
            }
        }
        updateMachineList.clear();
        updateMachineList = tmpUpdateMachineList;
    }

    public List<String> getProcessMachineList() {
        return processMachineList;
    }

    public void setProcessMachineList(List<String> processMachineList) {
        this.processMachineList = processMachineList;
    }

    public List<String> getUpdateMachineList() {
        return updateMachineList;
    }

    public void setUpdateMachineList(List<String> updateMachineList) {
        this.updateMachineList = updateMachineList;
    }

    public String getSyncWarehouseList() {
        return syncWarehouseList;
    }

    public void setSyncWarehouseList(String syncWarehouseList) {
        this.syncWarehouseList = syncWarehouseList;
    }

    public Integer getSyncInterval() {
        return syncInterval;
    }

    public void setSyncInterval(Integer syncInterval) {
        this.syncInterval = syncInterval;
    }

    public Integer getMsgResendInterval() {
        return msgResendInterval;
    }

    public void setMsgResendInterval(Integer msgResendInterval) {
        this.msgResendInterval = msgResendInterval;
    }

    public Boolean getEnableSync() {
        return enableSync;
    }

    public void setEnableSync(Boolean enableSync) {
        this.enableSync = enableSync;
    }

    public Boolean getEnableMsgResend() {
        return enableMsgResend;
    }

    public void setEnableMsgResend(Boolean enableMsgResend) {
        this.enableMsgResend = enableMsgResend;
    }

    public Boolean getEnableSkuPurchaseNoticeProcess() {
        return enableSkuPurchaseNoticeProcess;
    }

    public void setEnableSkuPurchaseNoticeProcess(Boolean enableSkuPurchaseNoticeProcess) {
        this.enableSkuPurchaseNoticeProcess = enableSkuPurchaseNoticeProcess;
    }

    public String getSyncMachines() {
        return syncMachines;
    }

    public void setSyncMachines(String syncMachines) {
        this.syncMachines = syncMachines;
        List<String> tmpSyncMachineList = new ArrayList<String>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(syncMachines)) {
            String[] updateMachineArray = syncMachines.split(",");
            for (String syncMachine : updateMachineArray) {
                tmpSyncMachineList.add(syncMachine.trim());
            }
        }
        syncMachineList.clear();
        syncMachineList = tmpSyncMachineList;
    }

    public Integer getRoutesInterval() {
        return routesInterval;
    }

    public void setRoutesInterval(Integer routesInterval) {
        this.routesInterval = routesInterval;
    }

    public List<String> getRoutesMachineList() {
        return routesMachineList;
    }

    public void setRoutesMachineList(List<String> routesMachineList) {
        this.routesMachineList = routesMachineList;
    }

    public String getRoutesMachines() {
        return routesMachines;
    }

    public void setRoutesMachines(String routesMachines) {
        this.routesMachines = routesMachines;
        List<String> tmpRoutesMachineList = new ArrayList<String>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(routesMachines)) {
            String[] routesMachineArray = routesMachines.split(",");
            for (String routesMachine : routesMachineArray) {
                tmpRoutesMachineList.add(routesMachine.trim());
            }
        }
        routesMachineList.clear();
        routesMachineList = tmpRoutesMachineList;
    }

    public List<String> getSkuPurchaseNoticeMachineList() {
        return skuPurchaseNoticeMachineList;
    }

    public void setSkuPurchaseNoticeMachineList(List<String> skuPurchaseNoticeMachineList) {
        this.skuPurchaseNoticeMachineList = skuPurchaseNoticeMachineList;
    }

    public String getExpectedExportShipOrderTimePoint() {
        return expectedExportShipOrderTimePoint;
    }

    public void setExpectedExportShipOrderTimePoint(String expectedExportShipOrderTimePoint) {
        this.expectedExportShipOrderTimePoint = expectedExportShipOrderTimePoint;
    }

    public Integer getExportShipOrderNum() {
        return exportShipOrderNum;
    }

    public void setExportShipOrderNum(Integer exportShipOrderNum) {
        this.exportShipOrderNum = exportShipOrderNum;
    }

    public String getOssShipOrderTraversalTimePoint() {
        return ossShipOrderTraversalTimePoint;
    }

    public void setOssShipOrderTraversalTimePoint(String ossShipOrderTraversalTimePoint) {
        this.ossShipOrderTraversalTimePoint = ossShipOrderTraversalTimePoint;
    }

    public List<String> getSyncMachineList() {
        return syncMachineList;
    }

    public void setSyncMachineList(List<String> syncMachineList) {
        this.syncMachineList = syncMachineList;
    }

    public String getWarehouseNoticeSmss() {
        return warehouseNoticeSmss;
    }

    public void setWarehouseNoticeSmss(String warehouseNoticeSmss) {
        this.warehouseNoticeSmss = warehouseNoticeSmss;
        List<String> tmp = new ArrayList<String>();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(warehouseNoticeSmss)) {
            String[] smsArray = warehouseNoticeSmss.split(",");
            for (String phoneNumber : smsArray) {
                tmp.add(phoneNumber.trim());
            }
        }
        this.warehouseNoticeSmsList.clear();
        this.warehouseNoticeSmsList = tmp;

    }

    public List<String> getWarehouseNoticeSmsList() {
        return warehouseNoticeSmsList;
    }

    public String getProductDeclareInitNoticeEmails() {
        return productDeclareInitNoticeEmails;
    }

    public void setProductDeclareInitNoticeEmails(String productDeclareInitNoticeEmails) {
        this.productDeclareInitNoticeEmails = productDeclareInitNoticeEmails;
    }

    public String getGalOrderAuditEmailNotice() {
        return galOrderAuditEmailNotice;
    }

    public void setGalOrderAuditEmailNotice(String galOrderAuditEmailNotice) {
        this.galOrderAuditEmailNotice = galOrderAuditEmailNotice;
    }

    public String getCoeGalOrderEmailNotice() {
        return coeGalOrderEmailNotice;
    }

    public void setCoeGalOrderEmailNotice(String coeGalOrderEmailNotice) {
        this.coeGalOrderEmailNotice = coeGalOrderEmailNotice;
    }

    public Integer getSendStockoutDelayTime() {
        return sendStockoutDelayTime;
    }

    public void setSendStockoutDelayTime(Integer sendStockoutDelayTime) {
        this.sendStockoutDelayTime = sendStockoutDelayTime;
    }

    public Boolean getEnablePollingRoutes() {
        return enablePollingRoutes;
    }

    public void setEnablePollingRoutes(Boolean enablePollingRoutes) {
        this.enablePollingRoutes = enablePollingRoutes;
    }

    public Integer getSendStockoutFinishDelayTime() {
        return sendStockoutFinishDelayTime;
    }

    public void setSendStockoutFinishDelayTime(Integer sendStockoutFinishDelayTime) {
        this.sendStockoutFinishDelayTime = sendStockoutFinishDelayTime;
    }

    public Boolean getIsPauseAudit() {
        return isPauseAudit;
    }

    public void setIsPauseAudit(Boolean isPauseAudit) {
        this.isPauseAudit = isPauseAudit;
    }

    public List<String> getNotSetSafeStockCategoryList() {
        if (StringUtils.isNotEmpty(notSetSafeStockCategorys)) {
            notSetSafeStockCategoryList = new ArrayList<String>();
            for (String tmp : notSetSafeStockCategorys.split(",")) {
                notSetSafeStockCategoryList.add(tmp);
            }
            return notSetSafeStockCategoryList;
        }
        return new ArrayList<String>();
    }

    public void setNotSetSafeStockCategoryList(List<String> notSetSafeStockCategoryList) {
        this.notSetSafeStockCategoryList = notSetSafeStockCategoryList;
    }

    public String getNotSetSafeStockCategorys() {
        return notSetSafeStockCategorys;
    }

    public void setNotSetSafeStockCategorys(String notSetSafeStockCategorys) {
        this.notSetSafeStockCategorys = notSetSafeStockCategorys;
    }

    public Boolean getEnableAutoTask() {
        return enableAutoTask;
    }

    public void setEnableAutoTask(Boolean enableAutoTask) {
        this.enableAutoTask = enableAutoTask;
    }

    public Integer getFetchUserRouteTimeoutSeconds() {
        return fetchUserRouteTimeoutSeconds;
    }

    public void setFetchUserRouteTimeoutSeconds(Integer fetchUserRouteTimeoutSeconds) {
        this.fetchUserRouteTimeoutSeconds = fetchUserRouteTimeoutSeconds;
    }

    public Boolean getIsSendDelaySms() {
        return isSendDelaySms;
    }

    public void setIsSendDelaySms(Boolean isSendDelaySms) {
        this.isSendDelaySms = isSendDelaySms;
    }

    public String getNeedSendDelaySmsWarehouses() {
        return needSendDelaySmsWarehouses;
    }

    public void setNeedSendDelaySmsWarehouses(String needSendDelaySmsWarehouses) {
        this.needSendDelaySmsWarehouses = needSendDelaySmsWarehouses;
    }

    public List<Long> getNeedSendDelaySmsWarehouseList() {
        if (StringUtils.isNotEmpty(needSendDelaySmsWarehouses)) {
            needSendDelaySmsWarehouseList = new ArrayList<Long>();
            for (String tmp : needSendDelaySmsWarehouses.split(",")) {
                needSendDelaySmsWarehouseList.add(Long.parseLong(tmp));
            }
            return needSendDelaySmsWarehouseList;
        }
        return new ArrayList<Long>();
    }

    public void setNeedSendDelaySmsWarehouseList(List<Long> needSendDelaySmsWarehouseList) {
        this.needSendDelaySmsWarehouseList = needSendDelaySmsWarehouseList;
    }

    public String getSendDelayMsgContent() {
        return sendDelayMsgContent;
    }

    public void setSendDelayMsgContent(String sendDelayMsgContent) {
        this.sendDelayMsgContent = sendDelayMsgContent;
    }

    public String getNeedCheckStockoutSkuCountWarehouses() {
        return needCheckStockoutSkuCountWarehouses;
    }

    public void setNeedCheckStockoutSkuCountWarehouses(String needCheckStockoutSkuCountWarehouses) {
        this.needCheckStockoutSkuCountWarehouses = needCheckStockoutSkuCountWarehouses;
    }

    public List<Long> getNeedCheckStockoutSkuCountWarehouseList() {
        if (StringUtils.isNotEmpty(needCheckStockoutSkuCountWarehouses)) {
            needCheckStockoutSkuCountWarehouseList = new ArrayList<Long>();
            for (String tmp : needCheckStockoutSkuCountWarehouses.split(",")) {
                needCheckStockoutSkuCountWarehouseList.add(Long.parseLong(tmp));
            }
            return needCheckStockoutSkuCountWarehouseList;
        }
        return new ArrayList<Long>();
    }

    public void setNeedCheckStockoutSkuCountWarehouseList(List<Long> needCheckStockoutSkuCountWarehouseList) {
        this.needCheckStockoutSkuCountWarehouseList = needCheckStockoutSkuCountWarehouseList;
    }

    public String getStockoutNumOverLimitEmails() {
        return stockoutNumOverLimitEmails;
    }

    public void setStockoutNumOverLimitEmails(String stockoutNumOverLimitEmails) {
        this.stockoutNumOverLimitEmails = stockoutNumOverLimitEmails;
    }

    public List<String> getStockoutNumOverLimitEmailList() {
        if (StringUtils.isNotEmpty(stockoutNumOverLimitEmails)) {
            stockoutNumOverLimitEmailList = new ArrayList<String>();
            for (String tmp : stockoutNumOverLimitEmails.split(",")) {
                stockoutNumOverLimitEmailList.add(tmp);
            }
            return stockoutNumOverLimitEmailList;
        }
        return new ArrayList<String>();
    }

    public void setStockoutNumOverLimitEmailList(List<String> stockoutNumOverLimitEmailList) {
        this.stockoutNumOverLimitEmailList = stockoutNumOverLimitEmailList;
    }

    public Boolean getIsAllowStockZero() {
        return isAllowStockZero;
    }

    public void setIsAllowStockZero(Boolean isAllowStockZero) {
        this.isAllowStockZero = isAllowStockZero;
    }

    public Boolean getIsEnableIdNoCheck() {
        return isEnableIdNoCheck;
    }

    public void setIsEnableIdNoCheck(Boolean isEnableIdNoCheck) {
        this.isEnableIdNoCheck = isEnableIdNoCheck;
    }

    public boolean getEnableGenerateDailyReport() {
        return enableGenerateDailyReport;
    }

    public boolean isEnableGenerateDailyReport() {
        return enableGenerateDailyReport;
    }

    public void enableGenerateDailyReport(boolean enableGenerateDailyReport) {
        this.enableGenerateDailyReport = enableGenerateDailyReport;
    }

    public Boolean getIsSendAllWarehouses() {
        return isSendAllWarehouses;
    }

    public void setIsSendAllWarehouses(Boolean isSendAllWarehouses) {
        this.isSendAllWarehouses = isSendAllWarehouses;
    }

    public Boolean getIsSendOnlyOnce() {
        return isSendOnlyOnce;
    }

    public void setIsSendOnlyOnce(Boolean isSendOnlyOnce) {
        this.isSendOnlyOnce = isSendOnlyOnce;
    }

    public String getRefundTaxLine() {
        return refundTaxLine;
    }

    public void setRefundTaxLine(String refundTaxLine) {
        this.refundTaxLine = refundTaxLine;
    }

    public Boolean getEnableAutoIncr() {
        return enableAutoIncr;
    }

    public void setEnableAutoIncr(Boolean enableAutoIncr) {
        this.enableAutoIncr = enableAutoIncr;
    }

    public Boolean getEnableSkuExpireJob() {
        return enableSkuExpireJob;
    }

    public void setEnableSkuExpireJob(Boolean enableSkuExpireJob) {
        this.enableSkuExpireJob = enableSkuExpireJob;
    }

    public String getNeedAtLogisticsShippedSetMailNoMerchants() {
        return needAtLogisticsShippedSetMailNoMerchants;
    }

    public void setNeedAtLogisticsShippedSetMailNoMerchants(String needAtLogisticsShippedSetMailNoMerchants) {
        this.needAtLogisticsShippedSetMailNoMerchants = needAtLogisticsShippedSetMailNoMerchants;
    }

    public String getFseRequestIPWhites() {
        return fseRequestIPWhites;
    }

    public void setFseRequestIPWhites(String fseRequestIPWhites) {
        this.fseRequestIPWhites = fseRequestIPWhites;
    }

    public List<String> getNeedAtLogisticsShippedSetMailNoMerchantList() {
        if (StringUtils.isNotEmpty(needAtLogisticsShippedSetMailNoMerchants)) {
            needAtLogisticsShippedSetMailNoMerchantList = new ArrayList<String>();
            for (String tmp : needAtLogisticsShippedSetMailNoMerchants.split(",")) {
                needAtLogisticsShippedSetMailNoMerchantList.add(tmp);
            }
            return needAtLogisticsShippedSetMailNoMerchantList;
        }
        return new ArrayList<String>();
    }

    public List<String> getFseRequestIPWhiteList() {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(fseRequestIPWhites)) {
            fseRequestIPWhiteList = new ArrayList<String>();
            for (String tmp : fseRequestIPWhites.split(",")) {
                fseRequestIPWhiteList.add(tmp);
            }
            return fseRequestIPWhiteList;
        }
        return null;
    }
}
