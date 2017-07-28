package com.sfebiz.supplychain.provider.biz;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuDeclareStateType;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.persistence.base.port.domain.PortDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncLogManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.LogisticsProviderDetailDO;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.LogisticsProviderDetailManager;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.ccb.CcbSkuSyncCommand;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuSyncCommand;
import net.pocrd.entity.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/13
 * Time: 下午8:16
 */
@Component("skuSyncBizService")
public class SkuSyncBizService implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger("CommandLogger");

    private static ApplicationContext applicationContext;

    @Resource
    private ProductDeclareManager productDeclareManager;

//    @Resource
//    private MixedSkuManager mixedSkuManager;

    @Resource
    private LogisticsProviderDetailManager logisticsProviderDetailManager;

    @Resource
    private WarehouseManager warehouseManager;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private SkuService skuService;

//    @Resource
//    private MessageProducer supplyChainMessageProducer;

    public static SkuSyncBizService getInstance() {
        return (SkuSyncBizService) applicationContext.getBean("skuSyncBizService");
    }

    /**
     * 向仓库查询商品信息
     *
     * @param skuId
     * @return
     * @throws ServiceException
     */
    public SkuEntity getProductInfoFromWms(Long skuId, Long warehouseId) throws ServiceException {

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("向仓库查询商品信息")
                .addParm("skuId", skuId)
                .addParm("warehouseId", warehouseId)
                .log();

        if (null == skuId) {
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL, LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL.getDesc());
        }

        WarehouseDO warehouseDO = warehouseManager.getById(warehouseId);
        if (null == warehouseDO) {
            throw new ServiceException(LogisticsReturnCode.WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION, LogisticsReturnCode.WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION.getDesc());
        }
        LogisticsProviderDetailDO logisticsProviderDetailDO = logisticsProviderDetailManager.getById(Long.parseLong(warehouseDO.getLogisticsProviderId()));
        if (null == logisticsProviderDetailDO) {
            throw new ServiceException(LogisticsReturnCode.PROVIDERSKU_NOT_EXIST_EXCEPTION, LogisticsReturnCode.PROVIDERSKU_NOT_EXIST_EXCEPTION.getDesc());
        }

//        WmsOrderProductQueryCommand wmsOrderProductQueryCommand = new LscmProductQueryCommand();
//        wmsOrderProductQueryCommand.setLogisticsProviderDetailDO(logisticsProviderDetailDO);
//        wmsOrderProductQueryCommand.setSkuId(skuId);
//        if (wmsOrderProductQueryCommand.execute()) {
//            return wmsOrderProductQueryCommand.getSkuEntity();
//        }
        return null;
    }


    /**
     * 发送基础商品信息给仓库,异步发送
     *
     * @return
     */
    public BaseResult sendProductBasicInfo2Wms(List<Long> skuIds, Long warehouseId, WmsOperaterType type) throws ServiceException {
        return sendProductBasicInfo2Wms(skuIds, warehouseId, type, true);
    }

    /**
     * 发送基础商品信息给仓库,同步发送
     *
     * @return
     */
    public BaseResult sendProductBasicInfo2WmsNotSync(List<Long> skuIds, Long warehouseId, WmsOperaterType type) throws ServiceException {
        return sendProductBasicInfo2Wms(skuIds, warehouseId, type, false);
    }

    /**
     * 发送基础商品信息给仓库,支持批量同步,如果是bom的主件，也是需要同步的。
     * isSync 是否同步执行
     *
     * @return
     */
    public BaseResult sendProductBasicInfo2Wms(List<Long> skuIds, Long warehouseId, WmsOperaterType type, boolean isSync) throws ServiceException {
        BaseResult baseResult = new BaseResult();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("向仓库同步基础商品信息")
                .addParm("skuIds", skuIds)
                .addParm("warehouseId", warehouseId)
                .addParm("WmsOperaterType", type)
                .log();

        if (skuIds == null || skuIds.size() == 0) {
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL, LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL.getDesc());
        }
        List<SkuEntity> skuEntities = new ArrayList<SkuEntity>();
        for (Long skuId : skuIds) {
            SkuEntity skuEntity = skuService.getSkuOnlySkuInfo(skuId);
            if (skuEntity == null) {
                throw new ServiceException(LogisticsReturnCode.SKU_NOT_EXIST, LogisticsReturnCode.SKU_NOT_EXIST.getDesc());
            }
            skuEntities.add(skuEntity);
        }

        WarehouseDO warehouseDO = warehouseManager.getById(warehouseId);

        if (null == warehouseDO) {
            throw new ServiceException(LogisticsReturnCode.WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION, LogisticsReturnCode.WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION.getDesc());
        }
        LogisticsProviderDetailDO logisticsProviderDetailDO = logisticsProviderDetailManager.getById(warehouseDO.getId());
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("向仓库同步基础商品信息")
                .addParm("skuIds", skuIds)
                .addParm("warehouseId", warehouseId)
                .addParm("物流供应商信息", logisticsProviderDetailDO)
                .log();
        if (null == logisticsProviderDetailDO) {
            throw new ServiceException(LogisticsReturnCode.PROVIDERSKU_NOT_EXIST_EXCEPTION, LogisticsReturnCode.PROVIDERSKU_NOT_EXIST_EXCEPTION.getDesc());
        }

        // 如果不存在仓库和和sku对应同步数据，就创建
        for (SkuEntity skuEntity : skuEntities) {
            SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager", SkuWarehouseSyncManager.class);
            SkuWarehouseSyncDO warehouseSyncDO = skuWarehouseSyncManager.getBySkuIdAndWarehouseId(skuEntity.getId(), warehouseDO.getId());
            if (warehouseSyncDO == null) {
                SkuWarehouseSyncDO skuWarehouseSyncDO = new SkuWarehouseSyncDO();
                skuWarehouseSyncDO.setSkuId(skuEntity.getId());
                skuWarehouseSyncDO.setWarehouseId(warehouseDO.getId());
                skuWarehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_FAIL.value);
                skuWarehouseSyncDO.setSyncUpdateState(SkuWarehouseSyncStateType.SYNC_UPDATE_FAIL.value);
                skuWarehouseSyncManager.insert(skuWarehouseSyncDO);
            } else if (WmsOperaterType.ADD.equals(type) && !warehouseSyncDO.getSyncState().equals(SkuWarehouseSyncStateType.SYNC_FAIL.value)) {
                SkuWarehouseSyncDO skuWarehouseSyncDO = new SkuWarehouseSyncDO();
                skuWarehouseSyncDO.setSkuId(skuEntity.getId());
                skuWarehouseSyncDO.setWarehouseId(warehouseDO.getId());
                skuWarehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_FAIL.value);
                skuWarehouseSyncDO.setSyncUpdateState(SkuWarehouseSyncStateType.SYNC_UPDATE_FAIL.value);
                skuWarehouseSyncManager.update(skuWarehouseSyncDO);
            } else if (WmsOperaterType.UPDATE.equals(type) && !warehouseSyncDO.getSyncUpdateState().equals(SkuWarehouseSyncStateType.SYNC_UPDATE_FAIL.value)) {
                SkuWarehouseSyncDO skuWarehouseSyncDO = new SkuWarehouseSyncDO();
                skuWarehouseSyncDO.setSkuId(skuEntity.getId());
                skuWarehouseSyncDO.setWarehouseId(warehouseDO.getId());
                skuWarehouseSyncDO.setSyncUpdateState(SkuWarehouseSyncStateType.SYNC_UPDATE_FAIL.value);
                skuWarehouseSyncManager.update(skuWarehouseSyncDO);
            }
        }


        // mock所有仓库返回同步成功 start
        boolean isMockSkuSync = MockConfig.isMocked("command", "skuSyncCommand");
        if (isMockSkuSync) {
            //直接返回仓库已发货
            logger.info("MOCK：COE仓库 商品同步 采用MOCK实现");
            SkuWarehouseSyncManager skuWarehouseSyncManager = SpringBeanFactory.getBean("skuWarehouseSyncManager", SkuWarehouseSyncManager.class);
            SkuWarehouseSyncLogManager skuWarehouseSyncLogManager = SpringBeanFactory.getBean("skuWarehouseSyncLogManager", SkuWarehouseSyncLogManager.class);
            for (SkuEntity skuEntity : skuEntities) {
                SkuWarehouseSyncDO warehouseSyncDO = skuWarehouseSyncManager.getBySkuIdAndWarehouseId(skuEntity.getId(), warehouseDO.getId());
                if (warehouseSyncDO != null) {
                    if (WmsOperaterType.ADD.equals(type)) {
                        warehouseSyncDO.setSyncState(SkuWarehouseSyncStateType.SYNC_SUCCESS.value);
                    } else {
                        warehouseSyncDO.setSyncUpdateState(SkuWarehouseSyncStateType.SYNC_UPDATE_SUCCESS.value);
                    }
                    skuWarehouseSyncManager.update(warehouseSyncDO);
                } else {
                    logger.error("skuId:{},warehouseId:{} 不存在", skuEntity.id, warehouseDO.getId());
                }
                skuWarehouseSyncLogManager.createLog(skuEntity.id, warehouseDO.getId(), "成功", "mock", 1);
            }
            baseResult.setSuccess(Boolean.TRUE);
            return baseResult;
        }
        // mock所有仓库返回同步成功 end

        // 根据command-config.xml配置执行对应的Command
        try {
            ProviderCommand cmd = CommandFactory.createCommand(logisticsProviderDetailDO.getInterfaceType().toString(), WmsMessageType.SKU_SYNC.getValue());
            WmsOrderSkuSyncCommand productSyncCommand = (WmsOrderSkuSyncCommand) cmd;
            productSyncCommand.setWarehouseDO(warehouseDO);
            productSyncCommand.setSkuEntities(skuEntities);
            productSyncCommand.setLogisticsProviderDetailDO(logisticsProviderDetailDO);
            productSyncCommand.setWmsOperaterType(type);
            if (isSync) {
                boolean isSuccess = productSyncCommand.execute();
                baseResult.setSuccess(isSuccess);
                if (!isSuccess) {
                    baseResult.setResultMessage(productSyncCommand.getErrorMessage());
                }
                return baseResult;
            } else {
                threadPoolTaskExecutor.execute(productSyncCommand);
            }
        } catch (ServiceException e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setErrorMsg("构建商品BOM物料清单命令异常")
                    .setException(e)
                    .setParms(warehouseDO.getId())
                    .log();
        }
        baseResult.setSuccess(Boolean.TRUE);

        return baseResult;
    }

    /**
     * 发送基础商品信息给清关公司,支持批量同步
     * isSync 是否同步执行
     *
     * @return
     */
    public BaseResult sendProductBasicInfo2Ccb(List<Long> skuIds, Long ccbId, boolean isSync) throws ServiceException {
        BaseResult baseResult = new BaseResult();

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("向仓库同步基础商品信息")
                .addParm("skuIds", skuIds)
                .addParm("ccbId", ccbId)
                .log();

        if (skuIds == null || skuIds.size() == 0) {
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL, LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL.getDesc());
        }
        List<SkuEntity> skuEntities = new ArrayList<SkuEntity>();
        for (Long skuId : skuIds) {
            SkuEntity skuEntity = skuService.getSkuOnlySkuInfo(skuId);
            if (skuEntity == null) {
                throw new ServiceException(LogisticsReturnCode.SKU_NOT_EXIST, LogisticsReturnCode.SKU_NOT_EXIST.getDesc());
            }
            skuEntities.add(skuEntity);
        }

        LogisticsProviderDetailDO logisticsProviderDetailDO = logisticsProviderDetailManager.getById(ccbId);
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("向仓库同步基础商品信息")
                .addParm("skuIds", skuIds)
                .addParm("ccbId", ccbId)
                .addParm("物流供应商信息", logisticsProviderDetailDO)
                .log();
        if (null == logisticsProviderDetailDO) {
            throw new ServiceException(LogisticsReturnCode.PROVIDERSKU_NOT_EXIST_EXCEPTION, LogisticsReturnCode.PROVIDERSKU_NOT_EXIST_EXCEPTION.getDesc());
        }

        try {
            ProviderCommand cmd = CommandFactory.createCommand(logisticsProviderDetailDO.getInterfaceType().toString(), WmsMessageType.CCB_SKU_SYNC.getValue());
            CcbSkuSyncCommand ccbSkuSyncCommand = (CcbSkuSyncCommand) cmd;
            ccbSkuSyncCommand.setLogisticsProviderDetailDO(logisticsProviderDetailDO);
            ccbSkuSyncCommand.setSkuEntities(skuEntities);
            if (isSync) {
                boolean isSuccess = ccbSkuSyncCommand.execute();
                baseResult.setSuccess(isSuccess);
                if (!isSuccess) {
                    baseResult.setResultMessage(ccbSkuSyncCommand.getErrorMessage());
                }
                return baseResult;
            } else {
                threadPoolTaskExecutor.execute(ccbSkuSyncCommand);
            }
        } catch (ServiceException e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setErrorMsg("构建商品BOM物料清单命令异常")
                    .setException(e)
                    .setParms(ccbId)
                    .log();
        }
        baseResult.setSuccess(Boolean.TRUE);

        return baseResult;
    }

//
//    /**
//     * 发送商品BOM物料信息给仓库
//     *
//     * @return
//     */
//    public BaseResult sendProductBomInfo2Wms(Long mixedSkuId, Long warehouseId) throws ServiceException {
//        BaseResult baseResult = new BaseResult();
//        LogBetter.instance(logger)
//                .setLevel(LogLevel.INFO)
//                .setMsg("向仓库同步组合商品信息")
//                .addParm("skuId", mixedSkuId)
//                .addParm("warehouseId", warehouseId)
//                .log();
//
//        MixedSkuDO mixedSkuDO = new MixedSkuDO();
//        mixedSkuDO.setMixedSkuId(mixedSkuId);
//        List<MixedSkuDO> mixedSkuDOs = mixedSkuManager.query(BaseQuery.getInstance(mixedSkuDO));
//        if (mixedSkuDOs == null || mixedSkuDOs.size() == 0) {
//            throw new ServiceException(LogisticsReturnCode.MIXED_SKU_NOT_EXIST, LogisticsReturnCode.MIXED_SKU_NOT_EXIST.getDesc());
//        }
//        for (MixedSkuDO mixedSku : mixedSkuDOs) {
//            if (mixedSku.getCount() == null || mixedSku.getCount() == 0 || mixedSku.getBaseSkuId() == null) {
//                throw new ServiceException(LogisticsReturnCode.MIXED_SKU_PARAMS_ILLAGEL, LogisticsReturnCode.MIXED_SKU_PARAMS_ILLAGEL.getDesc());
//            }
//        }
//
//        WarehouseDO warehouseDO = warehouseManager.getById(warehouseId);
//        if (null == warehouseDO) {
//            throw new ServiceException(LogisticsReturnCode.WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION, LogisticsReturnCode.WAREHOUSE_NOT_EXISTS_ERROR_EXCEPTION.getDesc());
//        }
//        LogisticsProviderDetailDO logisticsProviderDetailDO = logisticsProviderDetailManager.getById(warehouseDO.getLogisticsProviderId());
//        if (null == logisticsProviderDetailDO) {
//            throw new ServiceException(LogisticsReturnCode.PROVIDERSKU_NOT_EXIST_EXCEPTION, LogisticsReturnCode.PROVIDERSKU_NOT_EXIST_EXCEPTION.getDesc());
//        }
//
//        try {
//            ProviderCommand cmd = CommandFactory.createCommand(logisticsProviderDetailDO.getInterfaceType().toString(), WmsMessageType.BOM_SYNC.getValue());
//            WmsOrderBomSyncCommand bomSyncCommand = (WmsOrderBomSyncCommand) cmd;
//            bomSyncCommand.setWarehouseDO(warehouseDO);
//            bomSyncCommand.setMixedSkuDOs(mixedSkuDOs);
//            bomSyncCommand.setLogisticsProviderDetailDO(logisticsProviderDetailDO);
//            bomSyncCommand.setMixedSkuId(mixedSkuId.toString());
//            boolean isSuccess = bomSyncCommand.execute();
//            baseResult.setSuccess(isSuccess);
//            if (!isSuccess) {
//                baseResult.setResultMessage(bomSyncCommand.getErrorMessage());
//            }
//            return baseResult;
//        } catch (CommandException e) {
//            LogBetter.instance(logger).setLevel(LogLevel.WARN)
//                    .addParm("[供应链-WMS]商品BOM同步仓库异常，SKUID", mixedSkuId)
//                    .addParm("仓库id", warehouseId)
//                    .setException(e)
//                    .log();
//            baseResult.setResultMessage("商品BOM同步仓库内部异常");
//            return baseResult;
//        }
//    }

    /**
     * 提交商品到国检结果
     *
     * @param productDeclareDO
     * @return
     */
    public boolean saveProductDeclareRecord(ProductDeclareDO productDeclareDO) {
        if (productDeclareDO.getId() == null) {
            productDeclareManager.insert(productDeclareDO);
            return false;
        } else {
            productDeclareManager.update(productDeclareDO);
        }
        return true;
    }

    /**
     * 口岸商品备案国检成功，保存国检备案编号
     *
     * @param skuId
     * @param portNId
     * @param recordNo
     * @param remark
     * @return
     */
    public boolean productDeclareCallbackSuccess(Long skuId, String portNId, String recordNo, String remark) {
        PortDO portDO = getPortByPortNid(portNId);
        if (portDO == null) {
            return false;
        }
        ProductDeclareDO productDeclareDO = getProductDeclare(skuId, portDO.getId());
        if (productDeclareDO == null) {
            productDeclareDO = new ProductDeclareDO();
            productDeclareDO.setSkuId(skuId);
            productDeclareDO.setPortId(portDO.getId());
            productDeclareDO.setRecordNo(recordNo);
            productDeclareDO.setRemark(remark);
            productDeclareDO.setState(SkuDeclareStateType.DECLARE_PASS.getValue());
            productDeclareDO.setFinishTime(new Date());
            productDeclareManager.insert(productDeclareDO);
        } else {
            productDeclareDO.setRecordNo(recordNo);
            productDeclareDO.setRemark(remark);
            productDeclareDO.setState(SkuDeclareStateType.DECLARE_PASS.getValue());
            productDeclareDO.setFinishTime(new Date());
            productDeclareManager.update(productDeclareDO);
        }

        return true;
    }

    /**
     * 口岸商品备案国检失败
     *
     * @param skuId
     * @param portNId
     * @param remark
     * @return
     */
    public boolean productDeclareCallbackFailure(Long skuId, String portNId, String remark) {
        PortDO portDO = getPortByPortNid(portNId);
        if (portDO == null) {
            return false;
        }
        ProductDeclareDO productDeclareDO = getProductDeclare(skuId, portDO.getId());
        if (productDeclareDO == null) {
            productDeclareDO = new ProductDeclareDO();
            productDeclareDO.setSkuId(skuId);
            productDeclareDO.setPortId(portDO.getId());
            productDeclareDO.setRemark(remark);
//            productDeclareDO.setState(SkuDeclareStateType.FINISHED_COLLECTING.getValue());
            productDeclareDO.setState(SkuDeclareStateType.DECLARE_NOT_PASS.getValue());
            productDeclareManager.insert(productDeclareDO);
        } else {
            productDeclareDO.setRemark(remark);
//            productDeclareDO.setState(SkuDeclareStateType.FINISHED_COLLECTING.getValue());
            productDeclareDO.setState(SkuDeclareStateType.DECLARE_NOT_PASS.getValue());
            productDeclareManager.update(productDeclareDO);
        }

        return true;
    }

    /**
     * 获取提交的商品备案信息
     *
     * @param skuId
     * @param portId
     * @return
     */
    public ProductDeclareDO getProductDeclare(Long skuId, Long portId) {
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        productDeclareDO.setPortId(portId);
        productDeclareDO.setSkuId(skuId);
        List<ProductDeclareDO> productDeclareDOList = productDeclareManager.query(BaseQuery.getInstance(productDeclareDO));
        if (productDeclareDOList != null && productDeclareDOList.size() > 0) {
            return productDeclareDOList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据口岸Nid获取口岸实体
     *
     * @return
     */
    public PortDO getPortByPortNid(String portNid) {
        PortDO portDO = new PortDO();
        portDO.setPortNid(portNid);
        portDO.setId(PortNid.getCodeByNid(portNid));
        return portDO;
    }
//
//    public void sendEmail4ProductDeclare(ProviderDO providerDO, List<Long> skuIdList, Long portId, String declareMode) {
//        try {
//            LogBetter.instance(logger)
//                    .setLevel(LogLevel.INFO)
//                    .setMsg("[供应链-创建备案商品]:发送备案商品邮件")
//                    .addParm("商品编码列表:", skuIdList)
//                    .addParm("口岸ID:", portId)
//                    .addParm("备案模式:", declareMode)
//                    .log();
//            List<ProductDeclareDO> declareSkuInfo = productDeclareManager
//                    .getWaitDeclareSkuInfo(skuIdList, portId, declareMode);
//            if (CollectionUtils.isEmpty(declareSkuInfo)) {
//                return;
//            }
//
//            Message msg = new Message();
//            msg.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_EVENT);
//            msg.setTag(MessageConstants.TAG_PRODUCT_DECLARE_INIT);
//            //消息体没有内容
//            msg.setBody(" ".getBytes("UTF-8"));//BODY不能为空
//
//            //向用户属性中赋值
//            Properties properties = new Properties();
//            properties.put("portId", portId);
//            properties.put("declareMode", declareMode);
//            properties.put("provider", null != providerDO ? providerDO.getName() : "");
//            properties.put("skuInfo", JSON.toJSONString(declareSkuInfo));
//            msg.setUserProperties(properties);
//            supplyChainMessageProducer.send(msg);
//        } catch (Exception e) {
//            LogBetter.instance(logger)
//                    .setLevel(LogLevel.WARN)
//                    .setException(e)
//                    .setErrorMsg("[[供应链-审核采购单创建备案商品]:发送备案商品邮件异常]: " + e.getMessage())
//                    .addParm("商品编码列表:", skuIdList)
//                    .addParm("口岸ID:", portId)
//                    .addParm("备案模式:", declareMode)
//                    .log();
//        }
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
