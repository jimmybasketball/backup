/*
 * @(#) com.sfebiz.logistics.service.sku/ProductDeclareServiceImpl.java
 * 
 */
package com.sfebiz.supplychain.service.sku;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.line.enums.LineType;
import com.sfebiz.supplychain.exposed.sku.api.ProductDeclareService;
import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuDeclareEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuDeclareStateType;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.line.dao.LogisticsLineDao;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.sku.domain.*;
import com.sfebiz.supplychain.persistence.base.sku.manager.*;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.statemachine.EngineService;
import com.sfebiz.supplychain.service.statemachine.Operator;
import com.taobao.tbbpm.common.persistence.ITransactionCallbackWithoutResult;
import com.taobao.tbbpm.common.persistence.ITransactionStatus;
import com.taobao.tbbpm.common.persistence.SpringTransactionTemplate;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品备案服务
 * 
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017/7/18 22:56
 */
@Component("productDeclareService")
public class ProductDeclareServiceImpl implements ProductDeclareService {

    private static final HaitaoTraceLogger traceLogger =
            HaitaoTraceLoggerFactory.getTraceLogger("skurecord");

    private final static Logger logger = LoggerFactory.getLogger(ProductDeclareServiceImpl.class);

    private static final String CREATE_DECLARE_SKU_KEY = "CREATE_DECLARE_SKU_KEY:";

    private static final String DELETE_DECLARE_SKU_KEY = "DELETE_DECLARE_SKU_KEY:";

    private static final String START_COLLECT_DECLARE_SKU_KEY = "START_COLLECT_DECLARE_SKU_KEY:";

    private static final String FINISH_COLLECT_DECLARE_SKU_KEY = "FINISH_COLLECT_DECLARE_SKU_KEY:";

    private static final String PASS_DECLARE_SKU_KEY = "PASS_DECLARE_SKU_KEY:";

    private static final String NOT_PASS_DECLARE_SKU_KEY = "NOT_PASS_DECLARE_SKU_KEY:";

    private static final String DECLARING_SKU_KEY = "DECLARING_SKU_KEY:";

    static Map<Long, LogisticsLineDO> lineDOMap = new HashMap<Long, LogisticsLineDO>(16);
    static Map<Long, WarehouseDO> warehouseDOMap = new HashMap<Long, WarehouseDO>(16);
    @Resource
    private LogisticsLineDao logisticsLineDao;

    @Resource
    private LogisticsLineManager logisticsLineManager;

    @Resource
    private ProductDeclareManager productDeclareManager;

//    @Resource
//    private CostCalculateService costCalculateService;
//    @Resource
//    private StockoutOrderSkuManager stockoutOrderSkuManager;

    @Resource
    private WarehouseManager warehouseManager;

    @Resource
    private ProductDeclareGzBondedManager productDeclareGzBondedManager;

    @Resource
    private ProductDeclareGzDirectmailManager productDeclareGzDirectmailManager;

    @Resource
    private ProductDeclarePtDirectmailManager productDeclarePtDirectmailManager;

    @Resource
    private ProductDeclareStDirectmailManager productDeclareStDirectmailManager;

    @Resource
    private ProductDeclareCqBondedManager productDeclareCqBondedManager;

    @Resource
    private ProductDeclareQdDirectmailManager productDeclareQdDirectmailManager;

    @Resource
    private ProductDeclareHzBondedManager productDeclareHzBondedManager;

    @Resource
    private ProductDeclareHsCodeManager productDeclareHsCodeManager;

    @Resource
    private ProductDeclareHzDirectmailManager productDeclareHzDirectmailManager;

    @Resource
    private ProductDeclareNbBondedManager productDeclareNbBondedManager;

    @Resource
    private ProductDeclareNbDirectmailManager productDeclareNbDirectmailManager;

    @Resource
    private ProductDeclareJnDirectmailManager productDeclareJnDirectmailManager;

    @Resource
    private ProductDeclareXmDirectmailManager productDeclareXMDirectmailManager;

//    @Resource
//    private ProductDeclareFinishCollectProcessor productDeclareFinishCollectProcessor;
//
//    @Resource
//    private ProductDeclareDeclaringProcessor productDeclareDeclaringProcessor;
//
//    @Resource
//    private ProductDeclarePassProcessor productDeclarePassProcessor;

    @Resource
    private SkuManager skuManager;

//    @Resource
//    private SkuLineCostModelManager skuLineCostModelManager;
//
//    @Resource
//    private PortManager portManager;

    @Resource
    private DistributedLock distributedLock;

    @Resource
    private EngineService engineService;

    @Resource
    private SkuService skuService;

    @Resource
    private SpringTransactionTemplate springTransactionTemplate;

//    @Resource
//    private LineServiceImpl lineService;

    /**
     * 采购单Manager
     */
//    @Resource
//    private PurchaseOrderManager purchaseOrderManager;

    /**
     * 供应商Manager
     */
//    @Resource
//    private ProviderManager providerManager;

//    @Resource
//    private BatchStockLockLogManager batchStockLockLogManager;

//    @Resource
//    private StockService stockService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 创建备案商品 add by cg 2015-9-8 重载
     *
     * @param skuId 商品ID
     * @param portId 口岸ID
     * @param declareMode 备案模式
     * @param userId 用户ID
     * @param operator 操作人
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDeclareSku(Long skuId, Long portId, String declareMode, Long userId,
            String operator) throws ServiceException {
        return createDeclareSku(0L, skuId, portId, declareMode, userId, operator);
    }

    /**
     * 创建备案商品 add by cg 2015-9-8 重载
     *
     * @param purchaseOrderId 采购单ID
     * @param skuId 商品ID
     * @param portId 口岸ID
     * @param declareMode 备案模式
     * @param userId 用户ID
     * @param operator 操作人
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDeclareSku(Long purchaseOrderId, Long skuId, Long portId, String declareMode,
            Long userId, String operator) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-创建备案商品]:开始")
                .addParm("商品ID", skuId).addParm("口岸ID", portId).addParm("备案模式", declareMode)
                .addParm("操作人", operator).log();

        // 判断SKU信息是否存在
        SkuDO skuDO = skuManager.getById(skuId);
        if (skuDO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                    .setMsg("[供应链-创建备案商品]:商品不存在，根据skuId查找不到对应的sku").addParm("商品ID", skuId)
                    .addParm("口岸ID", portId).addParm("备案模式", declareMode).addParm("操作人", operator)
                    .log();
            throw new ServiceException(LogisticsReturnCode.SKU_NOT_EXIST, LogisticsReturnCode.SKU_NOT_EXIST.getDesc());
        }

        // 如果已存在备案商品,直接返回
        ProductDeclareDO productDeclareDO =
                productDeclareManager.queryDeclaredSku(skuId, portId, declareMode);
        if (productDeclareDO != null) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("[供应链-创建备案商品]:备案商品已存在")
                    .addParm("商品ID", skuId).addParm("口岸ID", portId).addParm("备案模式", declareMode)
                    .addParm("操作人", operator).log();
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_DECLARE_EXIST,
                    "该SKUID:" + skuId + " 已存在备案信息，不需要重复添加！");
        }

        // 从采购单审核的时候生成备案商品
        String providerName = "";
        String purchaseMode = "";
        // 平潭专用
        String productCode = "";
        String customsDeclarationElement = "";

        // TODO 入库单数据相关
        // if (purchaseOrderId.compareTo(0L) != 0) {
        // PurchaseOrderDO purchaseOrderDO = purchaseOrderManager.getById(purchaseOrderId);
        // if (purchaseOrderDO != null) {
        // ProviderDO providerDO = providerManager.getById(purchaseOrderDO.getProviderId());
        // if (providerDO != null) {
        // providerName = providerDO.getName();// 供应商名称
        // }
        // purchaseMode = purchaseOrderDO.getPurchaseMode(); // 采买模式
        // }
        // }

        Long productDeclareId;
        if (distributedLock
                .fetch(CREATE_DECLARE_SKU_KEY + skuId + "-" + portId + "-" + declareMode)) {
            try {
                productDeclareDO = new ProductDeclareDO();
                productDeclareDO.setSkuId(skuId);
                productDeclareDO.setPortId(portId);
                productDeclareDO.setDeclareMode(declareMode);
                productDeclareDO.setState(SkuDeclareStateType.DECLARE_WAIT.getValue());
                productDeclareDO.setDeclareName(skuDO.getName());
                productDeclareDO.setAttributes(skuDO.getAttributesDesc());
                productDeclareDO.setMeasuringUnit(skuDO.getMeasuringUnit());
                // productDeclareDO.setBarCode(skuDO.getBarcode()); FIXME
                productDeclareDO.setBrand(skuDO.getBrandName());
                // productDeclareDO.setOrigin(skuDO.getOrigin());
                // productDeclareDO.setCategoryName(skuDO.getCategoryName());
                productDeclareDO.setCreateUser(operator); // 备案任务创建人
                productDeclareDO.setProviderName(providerName); // 供应商名称
                productDeclareDO.setPurchaseMode(purchaseMode); // 采买模式
                // if (skuDO.getCategoryId() == null) {
                // productDeclareDO.setCategoryId("");
                // } else {
                // productDeclareDO.setCategoryId(skuDO.getCategoryId().toString());
                // }

                productDeclareManager.insertOrUpdate(productDeclareDO);
                productDeclareId = productDeclareDO.getId();
                // 启动状态机引擎 FIXME
                // ProductDeclareRequest productDeclareRequest = ProductDeclareRequestFactory
                // .generateProductDeclareRequest(
                // ProductDeclareActionType.DECLARE_TO_CREATE, productDeclareDO, null,
                // Operator.valueOf(userId, operator));
                // engineService.startStateMachineEngine(productDeclareRequest);

                if (portId == PortNid.GUANGZHOU.getValue()
                        && LineType.BONDED.getValue().equals(declareMode)) {
                    ProductDeclareGzBondedDO gzBondedDO = new ProductDeclareGzBondedDO();
                    gzBondedDO.setProductDeclareId(productDeclareId);
                    gzBondedDO.setSalesChannel("http://www.fengqu.com/");
                    gzBondedDO.setCreateUser(operator); // 备案任务创建人
                    gzBondedDO.setProviderName(providerName); // 供应商名称
                    gzBondedDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareGzBondedManager.insertOrUpdate(gzBondedDO);
                } else if (portId == PortNid.GUANGZHOU.getValue()
                        && LineType.DIRECTMAIL.getValue().equals(declareMode)) {
                    ProductDeclareGzDirectmailDO gzDirectmailDO =
                            new ProductDeclareGzDirectmailDO();
                    gzDirectmailDO.setProductDeclareId(productDeclareId);
                    gzDirectmailDO.setCreateUser(operator); // 备案任务创建人
                    gzDirectmailDO.setProviderName(providerName); // 供应商名称
                    gzDirectmailDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareGzDirectmailManager.insertOrUpdate(gzDirectmailDO);
                } else if (portId == PortNid.HANGZHOU.getValue()
                        && LineType.BONDED.getValue().equals(declareMode)) {
                    ProductDeclareHzBondedDO hzBondedDO = new ProductDeclareHzBondedDO();
                    hzBondedDO.setProductDeclareId(productDeclareId);
                    hzBondedDO.setCreateUser(operator); // 备案任务创建人
                    hzBondedDO.setProviderName(providerName); // 供应商名称
                    hzBondedDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareHzBondedManager.insertOrUpdate(hzBondedDO);
                } else if (portId == PortNid.HANGZHOU.getValue()
                        && LineType.DIRECTMAIL.getValue().equals(declareMode)) {
                    ProductDeclareHzDirectmailDO hzDirectmailDO =
                            new ProductDeclareHzDirectmailDO();
                    hzDirectmailDO.setProductDeclareId(productDeclareId);
                    hzDirectmailDO.setCreateUser(operator); // 备案任务创建人
                    hzDirectmailDO.setProviderName(providerName); // 供应商名称
                    hzDirectmailDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareHzDirectmailManager.insertOrUpdate(hzDirectmailDO);
                } else if (portId == PortNid.NINGBO.getValue()
                        && LineType.BONDED.getValue().equals(declareMode)) {
                    ProductDeclareNbBondedDO nbBondedDO = new ProductDeclareNbBondedDO();
                    nbBondedDO.setProductDeclareId(productDeclareId);
                    nbBondedDO.setCreateUser(operator); // 备案任务创建人
                    nbBondedDO.setProviderName(providerName); // 供应商名称
                    nbBondedDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareNbBondedManager.insertOrUpdate(nbBondedDO);
                } else if (portId == PortNid.NINGBO.getValue()
                        && LineType.DIRECTMAIL.getValue().equals(declareMode)) {
                    ProductDeclareNbDirectmailDO nbDirectmailDO =
                            new ProductDeclareNbDirectmailDO();
                    nbDirectmailDO.setProductDeclareId(productDeclareId);
                    productDeclareNbDirectmailManager.insertOrUpdate(nbDirectmailDO);
                } else if (portId == PortNid.JINAN.getValue()
                        && LineType.DIRECTMAIL.getValue().equals(declareMode)) {
                    ProductDeclareJnDirectmailDO jnDirectmailDO =
                            new ProductDeclareJnDirectmailDO();
                    jnDirectmailDO.setProductDeclareId(productDeclareId);
                    jnDirectmailDO.setCreateUser(operator); // 备案任务创建人
                    jnDirectmailDO.setProviderName(providerName); // 供应商名称
                    jnDirectmailDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareJnDirectmailManager.insertOrUpdate(jnDirectmailDO);
                } else if (portId == PortNid.XIAMEN.getValue()
                        && LineType.DIRECTMAIL.getValue().equals(declareMode)) {
                    ProductDeclareXmDirectmailDO xmDirectmailDO =
                            new ProductDeclareXmDirectmailDO();
                    xmDirectmailDO.setProductDeclareId(productDeclareId);
                    xmDirectmailDO.setCreateUser(operator); // 备案任务创建人
                    xmDirectmailDO.setProviderName(providerName); // 供应商名称
                    xmDirectmailDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareXMDirectmailManager.insertOrUpdate(xmDirectmailDO);
                } else if (portId == PortNid.PINGTAN.getValue()
                        && LineType.BONDED.getValue().equals(declareMode)) {
                    ProductDeclarePtDirectmailDO ptDirectmailDO =
                            new ProductDeclarePtDirectmailDO();
                    ptDirectmailDO.setProductDeclareId(productDeclareId);
                    ptDirectmailDO.setCreateUser(operator); // 备案任务创建人
                    ptDirectmailDO.setProviderName(providerName); // 供应商名称
                    ptDirectmailDO.setPurchaseMode(purchaseMode); // 采买模式
                    ptDirectmailDO.setProductCode(productCode);// 产品号
                    ptDirectmailDO.setCustomsDeclarationElement(customsDeclarationElement);// 海关申报要素
                    productDeclarePtDirectmailManager.insertOrUpdate(ptDirectmailDO);
                } else if (portId == PortNid.SHATIAN.getValue()
                        && LineType.DIRECTMAIL.getValue().equals(declareMode)) {
                    ProductDeclareStDirectmailDO stDirectmailDO =
                            new ProductDeclareStDirectmailDO();
                    stDirectmailDO.setProductDeclareId(productDeclareId);
                    stDirectmailDO.setCreateUser(operator); // 备案任务创建人
                    stDirectmailDO.setProviderName(providerName); // 供应商名称
                    stDirectmailDO.setPurchaseMode(purchaseMode); // 采买模式
                    stDirectmailDO.setProductCode(productCode);// 产品号
                    stDirectmailDO.setCustomsDeclarationElement(customsDeclarationElement);// 海关申报要素
                    productDeclareStDirectmailManager.insertOrUpdate(stDirectmailDO);
                } else if (portId == PortNid.CHONGQING.getValue()
                        && LineType.BONDED.getValue().equals(declareMode)) {
                    ProductDeclareCqBondedDO cqBondedDO = new ProductDeclareCqBondedDO();
                    cqBondedDO.setProductDeclareId(productDeclareId);
                    cqBondedDO.setCreateUser(operator); // 备案任务创建人
                    cqBondedDO.setProviderName(providerName); // 供应商名称
                    cqBondedDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareCqBondedManager.insertOrUpdate(cqBondedDO);
                } else if (portId == PortNid.QINGDAO.getValue()
                        && LineType.DIRECTMAIL.getValue().equals(declareMode)) {
                    ProductDeclareQdDirectmailDO qdDirectmailDO =
                            new ProductDeclareQdDirectmailDO();
                    qdDirectmailDO.setProductDeclareId(productDeclareId);
                    qdDirectmailDO.setCreateUser(operator); // 备案任务创建人
                    qdDirectmailDO.setProviderName(providerName); // 供应商名称
                    qdDirectmailDO.setPurchaseMode(purchaseMode); // 采买模式
                    productDeclareQdDirectmailManager.insertOrUpdate(qdDirectmailDO);
                }
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareId,
                                "supplychain"))
                        .setMsg("[供应链-创建备案商品]:成功").addParm("商品ID", skuId).addParm("口岸ID", portId)
                        .addParm("备案模式", declareMode).addParm("操作人", operator).log();
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e)
                        .setErrorMsg("[供应链-创建备案商品异常]:异常").addParm("商品ID", skuId)
                        .addParm("口岸ID", portId).addParm("备案模式", declareMode)
                        .addParm("操作人", operator).log();
                throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
                        "[供应链-创建备案商品异常]:异常" + e.getMessage());
            } finally {
                distributedLock.realease(
                        CREATE_DECLARE_SKU_KEY + skuId + "-" + portId + "-" + declareMode);
            }
        } else {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setErrorMsg("[供应链-创建备案商品异常]: 并发异常")
                    .addParm("商品ID", skuId).addParm("口岸ID", portId).addParm("备案模式", declareMode)
                    .addParm("操作人", operator).log();

            throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
                    "[供应链-创建备案商品异常]: 并发异常");
        }

        return productDeclareId;
    }

    /**
     * 批量创建待备案商品
     *
     * @param skuIds 待备案商品
     * @param portId 口岸ID
     * @param declareMode 备案模式
     * @param userId 用户ID
     * @param operator 操作人
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBatchDeclareSku(List<Long> skuIds, Long portId, String declareMode,
            Long userId, String operator) throws ServiceException {
        for (Long skuId : skuIds) {
            createDeclareSku(skuId, portId, declareMode, userId, operator);
        }
    }

    /**
     * 开始收集备案商品信息-废弃
     * 
     * @param productDeclareId 备案主表ID
     * @param userId 用户ID
     * @param operator 操作人
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startCollect(Long productDeclareId, Long userId, String operator)
            throws ServiceException {
        // LogBetter.instance(logger).setLevel(LogLevel.INFO)
        // .setMsg("[供应链-开始收集备案商品信息]")
        // .addParm("备案ID", productDeclareId)
        // .addParm("操作人", operator)
        // .log();
        //
        // ProductDeclareDO productDeclareDO = productDeclareManager.getById(productDeclareId);
        // if (null == productDeclareDO) {
        // throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_NOT_FOUND_ERROR);
        // }
        //
        // if (distributedLock.fetch(START_COLLECT_DECLARE_SKU_KEY + productDeclareId)) {
        // try {
        // ProductDeclareRequest productDeclareRequest = ProductDeclareRequestFactory
        // .generateProductDeclareRequest(ProductDeclareActionType.DECLARE_TO_START_COLLECT,
        // productDeclareDO, null, Operator.valueOf(operator));
        // engineService.executeStateMachineEngine(productDeclareRequest, false);
        //
        // LogBetter.instance(logger).setLevel(LogLevel.INFO)
        // .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareId, "supplychain"))
        // .setMsg("[供应链-开始收集备案商品信息]:成功").addParm("备案ID", productDeclareId).addParm("操作人",
        // operator).log();
        // } catch (Exception e) {
        // LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e)
        // .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareId, "supplychain"))
        // .setErrorMsg("[供应链-开始收集备案商品信息]:流程引擎执行异常")
        // .addParm("备案ID", productDeclareId).addParm("操作人", operator).log();
        // throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
        // LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION.getDesc());
        // } finally {
        // distributedLock.realease(START_COLLECT_DECLARE_SKU_KEY + productDeclareId);
        // }
        // } else {
        // LogBetter.instance(logger)
        // .setLevel(LogLevel.ERROR)
        // .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareId, "supplychain"))
        // .setErrorMsg("[供应链-开始收集备案商品信息]:并发异常")
        // .addParm("备案ID", productDeclareId).addParm("操作人", operator).log();
        // throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
        // "[供应链-开始收集备案商品信息]: 并发异常"
        // + "[备案ID: " + productDeclareId
        // + "]");
        // }
        // LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-开始收集备案商品信息]:结束")
        // .addParm("备案ID", productDeclareId).addParm("操作人", operator).log();
    }

    /**
     * 商品备案资料收集完毕导入
     * 
     * @param skuDeclareEntity 商品备案信息
     * @param userId 用户ID
     * @param operator 操作人
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finishCollect(final SkuDeclareEntity skuDeclareEntity, final Long userId,
            final String operator) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[商品备案资料收集完毕导入]:开始")
                .addParm("商品备案信息", skuDeclareEntity).addParm("操作人", operator).log();

        // 判断是否已存在备案商品
        ProductDeclareDO productDeclareDO = productDeclareManager.queryDeclaredSku(
                skuDeclareEntity.skuId, skuDeclareEntity.portId, skuDeclareEntity.declareMode);

        // 如果不存在则初始化一条
        if (null == productDeclareDO) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                    .setMsg("[商品备案资料收集完毕导入]:备案商品不存在, 初始化一条").addParm("商品备案信息", skuDeclareEntity)
                    .addParm("操作人", operator).log();
            try {
                springTransactionTemplate.execute(new ITransactionCallbackWithoutResult() {
                    @Override
                    public void doInTransaction(ITransactionStatus status) throws Exception {
                        Long productDeclareId =
                                createDeclareSku(skuDeclareEntity.skuId, skuDeclareEntity.portId,
                                        skuDeclareEntity.declareMode, userId, operator);
                        startCollect(productDeclareId, userId, operator);
                    }
                });
            } catch (ServiceException e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e)
                        .setErrorMsg("[商品备案资料收集完毕导入]:服务异常" + e.getMsg())
                        .addParm("商品备案信息", skuDeclareEntity).log();
                throw e;
            } catch (Exception e) {
                throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
                        LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION.getDesc());
            }
        }

        // productDeclareDO不存在，才需要重新查找
        if (null == productDeclareDO) {
            productDeclareDO = productDeclareManager.queryDeclaredSku(skuDeclareEntity.skuId,
                    skuDeclareEntity.portId, skuDeclareEntity.declareMode);
        }

        // // 状态判断(资料收集完毕状态,如果有的资料信息错误,也支持再次导入去修正)
        // if (ProductDeclareState.WAIT_DECLARE.getValue().equals(productDeclareDO.getState())) {
        // LogBetter.instance(logger).setLevel(LogLevel.WARN)
        // .setMsg("[商品备案资料收集完毕导入]:待备案状态无法操作资料收集完毕导入")
        // .addParm("商品备案信息", skuDeclareEntity)
        // .addParm("操作人", operator).log();
        // throw new
        // ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_FINISHED_COLLECTING_STATE_ERROR,
        // LogisticsReturnCode.PRODUCT_DECLARE_FINISHED_COLLECTING_STATE_ERROR.getDesc());
        // }
        //
        // // 控制并发
        // if (distributedLock.fetch(FINISH_COLLECT_DECLARE_SKU_KEY + productDeclareDO.getId())) {
        // try {
        // ProductDeclareRequest productDeclareRequest = ProductDeclareRequestFactory
        // .generateProductDeclareRequest(
        // ProductDeclareActionType.DECLARE_TO_FINISH_COLLECT, productDeclareDO, null,
        // Operator.valueOf(userId, operator));
        // productDeclareRequest.setSkuDeclareEntity(skuDeclareEntity);
        // // 如果是资料收集中状态,则走流程引擎
        // if (ProductDeclareState.COLLECTING.getValue().equals(productDeclareDO.getState())) {
        // engineService.executeStateMachineEngine(productDeclareRequest, false);
        // } else if
        // (ProductDeclareState.FINISHED_COLLECTING.getValue().equals(productDeclareDO.getState()))
        // {
        // // 如果是资料收集完成状态, 则更新备案信息, 不走流程引擎，直接调用process处理
        // engineService
        // .executeStateMachineProcessor(productDeclareFinishCollectProcessor,
        // productDeclareRequest,
        // false);
        // } else if (ProductDeclareState.DECLARING.getValue().equals(productDeclareDO.getState()))
        // {
        // // 如果是备案中状态, 则更新备案信息, 不走流程引擎，直接调用process处理
        // engineService
        // .executeStateMachineProcessor(productDeclareDeclaringProcessor,
        // productDeclareRequest,
        // false);
        // } else {
        // // 如果是备案完成状态, 则更新备案信息, 不走流程引擎，直接调用process处理
        // engineService
        // .executeStateMachineProcessor(productDeclarePassProcessor,
        // productDeclareRequest,
        // false);
        // }
        // } catch (ServiceException e) {
        // throw e;
        // } catch (Exception e) {
        // LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e).setMsg("[商品备案资料收集完毕导入]:异常")
        // .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareDO.getId(),
        // SystemConstants.TRACE_APP))
        // .addParm("商品备案信息", skuDeclareEntity)
        // .addParm("操作人", operator).log();
        // throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
        // "[商品备案资料收集完毕导入]:" + e.getMessage());
        // } finally {
        // distributedLock.realease(FINISH_COLLECT_DECLARE_SKU_KEY + productDeclareDO.getId());
        // }
        // } else {
        // LogBetter.instance(logger).setLevel(LogLevel.ERROR)
        // .setTraceLogger(
        // TraceLogEntity
        // .instance(traceLogger, productDeclareDO.getId(), SystemConstants.TRACE_APP))
        // .setErrorMsg("[商品备案资料收集完毕导入]:并发异常")
        // .addParm("商品备案信息", skuDeclareEntity)
        // .addParm("操作人", operator).log();
        // throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
        // "[商品备案资料收集完毕导入]:并发异常");
        // }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean declarePass(SkuDeclareEntity skuDeclareEntity, final Long userId,
            final String operator) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[备案完成导入]:开始")
                .addParm("商品备案信息", skuDeclareEntity).addParm("操作人", operator).log();

        if (StringUtils.isNotBlank(skuDeclareEntity.getHsCode())) {
            ProductDeclareHsCodeDO productDeclareHsCodeDO =
                    productDeclareHsCodeManager.getByHsCode(skuDeclareEntity.getHsCode());
            skuDeclareEntity
                    .setConsumptionDutyRate(productDeclareHsCodeDO.getConsumptionDutyRate());
            skuDeclareEntity.setTariffRate(productDeclareHsCodeDO.getTariffRate());
            skuDeclareEntity.setAddedValueTaxRate(productDeclareHsCodeDO.getAddedValueTaxRate());
            skuDeclareEntity.setMeasuringUnit(productDeclareHsCodeDO.getMeasuringUnit());
            skuDeclareEntity
                    .setSecondaryMeasuringUnit(productDeclareHsCodeDO.getSecondMeasuringUnit());
        }
        if (StringUtils.isNotBlank(skuDeclareEntity.getPostTaxNo())) {
            ProductDeclareHsCodeDO productDeclareHsCodeDO =
                    productDeclareHsCodeManager.getByHsCode(skuDeclareEntity.getPostTaxNo());
            skuDeclareEntity.setTaxRate(productDeclareHsCodeDO.getTaxRate());
        }

        // 判断是否已存在备案商品
        final ProductDeclareDO productDeclareDO = productDeclareManager.queryDeclaredSku(
                skuDeclareEntity.skuId, skuDeclareEntity.portId, skuDeclareEntity.declareMode);
        if (null == productDeclareDO) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("[备案完成导入]:备案商品不存在")
                    .addParm("商品备案信息", skuDeclareEntity).addParm("操作人", operator).log();
            throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_EXIST,
                    LogisticsReturnCode.SKU_DECLARE_NOT_EXIST.getDesc());
        }

        // 状态判断(备案完成状态,如果有的资料信息错误,也支持再次导入去修正)
        // if
        // (!ProductDeclareState.FINISHED_COLLECTING.getValue().equals(productDeclareDO.getState())
        // && !ProductDeclareState.DECLARING.getValue().equals(productDeclareDO.getState())
        // && !ProductDeclareState.DECLARE_PASS.getValue().equals(productDeclareDO.getState())) {
        // LogBetter.instance(logger).setLevel(LogLevel.WARN)
        // .setMsg("[商品备案备案通过导入]:商品的备案状态不为资料收集完毕或备案通过,无法操作备案完毕导入")
        // .addParm("商品备案信息", skuDeclareEntity)
        // .addParm("操作人", operator).log();
        // throw new ServiceException(
        // LogisticsReturnCode.PRODUCT_DECLARE_STATE_NOT_FINISHED_COLLECTING_OR_DECLARE_PASS,
        // LogisticsReturnCode.PRODUCT_DECLARE_STATE_NOT_FINISHED_COLLECTING_OR_DECLARE_PASS.getDesc());
        // }
        //
        // final SkuDeclareEntity declareEntity = skuDeclareEntity;
        //
        // // 控制并发
        // if (distributedLock.fetch(PASS_DECLARE_SKU_KEY + productDeclareDO.getId())) {
        // try {
        // springTransactionTemplate.execute(new ITransactionCallbackWithoutResult() {
        //
        // @Override
        // public void doInTransaction(ITransactionStatus status) throws Exception {
        // ProductDeclareRequest productDeclareRequest = ProductDeclareRequestFactory
        // .generateProductDeclareRequest(
        // ProductDeclareActionType.DECLARE_TO_PASS, productDeclareDO, null,
        // Operator.valueOf(userId, operator));
        // productDeclareRequest.setSkuDeclareEntity(declareEntity);
        // // 如果是资料收集完毕或备案中状态,则走流程引擎
        // if
        // (ProductDeclareState.FINISHED_COLLECTING.getValue().equals(productDeclareDO.getState())
        // || ProductDeclareState.DECLARING.getValue().equals(productDeclareDO.getState())) {
        // engineService.executeStateMachineEngine(productDeclareRequest, false);
        // } else {
        // // 如果是备案完成状态,则更新备案信息, 不走流程引擎，直接调用process处理
        // engineService
        // .executeStateMachineProcessor(productDeclarePassProcessor, productDeclareRequest,
        // false);
        // }
        //
        // // 更新SKU表里面的CUSTOM_CATEGORY_NID
        // SkuDO skuDO = new SkuDO();
        // skuDO.setId(declareEntity.skuId);
        // skuDO.setCustomsCategoryNid(declareEntity.postTaxNo);
        // skuManager.update(skuDO);
        //
        // // 更新Redis中sku信息
        // skuService.redisSkuInfoUpdate(declareEntity.skuId);
        // }
        // });
        //
        // //解禁因备案异常而禁用的批次库存
        // unlockBatchStock(skuDeclareEntity.skuId, skuDeclareEntity.portId,
        // skuDeclareEntity.declareMode);
        //
        // } catch (ServiceException e) {
        // throw e;
        // } catch (Exception e) {
        // LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e).setMsg("[备案完成导入]:异常")
        // .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareDO.getId(),
        // SystemConstants.TRACE_APP))
        // .addParm("商品备案信息", skuDeclareEntity)
        // .addParm("操作人", operator).log();
        // throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
        // "[备案完成导入]:" + e.getMessage());
        // } finally {
        // distributedLock.realease(PASS_DECLARE_SKU_KEY + productDeclareDO.getId());
        // }
        // } else {
        // LogBetter.instance(logger).setLevel(LogLevel.ERROR)
        // .setTraceLogger(
        // TraceLogEntity.instance(traceLogger, productDeclareDO.getId(),
        // SystemConstants.TRACE_APP))
        // .setErrorMsg("[备案完成导入]:并发异常")
        // .addParm("商品备案信息", skuDeclareEntity)
        // .addParm("操作人", operator).log();
        // throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
        // "[备案完成导入]:并发异常");
        // }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean declareNotPass(Long productDeclareId, String reason, Long userId,
            String operator) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-商品备案不通过]")
                .addParm("备案ID", productDeclareId).addParm("备案不通过原因", reason)
                .addParm("用户ID", userId).addParm("操作人", operator).log();

        ProductDeclareDO productDeclareDO = productDeclareManager.getById(productDeclareId);
        if (null == productDeclareDO) {
            throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_NOT_FOUND_ERROR);
        }

        if (distributedLock.fetch(NOT_PASS_DECLARE_SKU_KEY + productDeclareId)) {
            try {
                productDeclareDO.setRemark(reason);
                // ProductDeclareRequest productDeclareRequest = ProductDeclareRequestFactory
                // .generateProductDeclareRequest(ProductDeclareActionType.DECLARE_TO_NOT_PASS,
                // productDeclareDO, null, Operator.valueOf(operator));
                // engineService.executeStateMachineEngine(productDeclareRequest, false);

                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareId,
                                "supplychain"))
                        .setMsg("[供应链-商品备案不通过操作]:成功").addParm("备案ID", productDeclareId)
                        .addParm("备案不通过原因", reason).addParm("用户ID", userId).addParm("操作人", operator)
                        .log();
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareId,
                                "supplychain"))
                        .setErrorMsg("[供应链-商品备案不通过操作]:流程引擎执行异常").addParm("备案ID", productDeclareId)
                        .addParm("备案不通过原因", reason).addParm("用户ID", userId).addParm("操作人", operator)
                        .log();
                logger.error("[供应链-商品备案不通过操作] 异常：productDeclareId={}，e={}", productDeclareId, e);
                throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
                        LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION.getDesc());
            } finally {
                distributedLock.realease(NOT_PASS_DECLARE_SKU_KEY + productDeclareId);
            }
        } else {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                            TraceLogEntity.instance(traceLogger, productDeclareId, "supplychain"))
                    .setErrorMsg("[供应链-商品备案不通过操作]:并发异常").addParm("备案ID", productDeclareId)
                    .addParm("备案不通过原因", reason).addParm("用户ID", userId).addParm("操作人", operator)
                    .log();
            throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
                    "[供应链-商品备案不通过操作]: 并发异常" + "[备案ID: " + productDeclareId + "]");
        }
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-商品备案不通过操作]:结束")
                .addParm("备案ID", productDeclareId).addParm("备案不通过原因", reason)
                .addParm("用户ID", userId).addParm("操作人", operator).log();
        return true;
    }

    // 处理各个口岸的商品备案
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean declareSku(Long skuId, Long portId) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("declareSku")
                .setParms("商品ID", skuId).setParms("口岸", portId).log();
        try {
            // SkuDO skuDO = skuManager.getById(skuId);
            // PortDO portDO = portManager.getById(portId);
            // if (skuDO == null || portDO == null) {
            // throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL,
            // LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL.getDesc());
            // }
            // ProductDeclareDO productDeclareDO = buildProductDeclare(skuDO, portDO);
            // if (ProductDeclareState.DECLARE_PASS.getValue().equals(productDeclareDO.getState()))
            // {
            // return true;
            // }
            //
            // validProductDeclareParam(productDeclareDO);
            // ProviderCommand cmd = CommandFactory
            // .createPortCommand(portDO.getType().toString(),
            // HzPortBusinessType.PRODUCT_RECORD.getType());
            // PortProductDeclareCommand productDeclareCommand = (PortProductDeclareCommand) cmd;
            // productDeclareCommand.setProductDeclareDO(productDeclareDO);
            // productDeclareCommand.setPortDO(portDO);
            // return productDeclareCommand.execute();
            return false;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e)
                    .setErrorMsg("商品备案异常").setParms("商品ID", skuId).setParms("口岸", portId).log();
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_DECLARE_INNER_EXCEPTION,
                    LogisticsReturnCode.SKU_SERVICE_DECLARE_INNER_EXCEPTION.getDesc());
        }
    }

    /**
     * 海外通商品备案信息导入-好像未使用 FIXME
     * 
     * @param skuDeclareEntity
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDeclaredSkuByImport(SkuDeclareEntity skuDeclareEntity)
            throws ServiceException {
        if (skuDeclareEntity == null || skuDeclareEntity.skuId <= 0
                || skuDeclareEntity.portId <= 0) {
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL,
                    LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL.getDesc());
        }
        SkuDO skuDO = skuManager.getById(skuDeclareEntity.getSkuId());
        if (skuDO == null) {
            throw new ServiceException(LogisticsReturnCode.SKU_NOT_EXIST,
                    LogisticsReturnCode.SKU_NOT_EXIST.getDesc());
        }
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        productDeclareDO.setSkuId(skuDeclareEntity.skuId);
        productDeclareDO.setPortId(skuDeclareEntity.portId);
        List<ProductDeclareDO> productDeclareDOs =
                productDeclareManager.query(BaseQuery.getInstance(productDeclareDO));
        if (productDeclareDOs != null && productDeclareDOs.size() > 0) {
            throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_EXIST,
                    LogisticsReturnCode.SKU_DECLARE_EXIST.getDesc());
        }
        productDeclareDO.setState(SkuDeclareStateType.DECLARE_PASS.getValue());
        productDeclareDO.setOrigin(skuDeclareEntity.origin);
        productDeclareDO.setBrand(skuDeclareEntity.brandName);
        productDeclareDO.setCategoryId(skuDeclareEntity.categoryId);
        productDeclareDO.setCategoryName(skuDeclareEntity.categoryName);
        productDeclareDO.setPostTaxNo(skuDeclareEntity.postTaxNo);
        productDeclareDO.setAttributes(skuDeclareEntity.attributes);
        productDeclareDO.setProductId(skuDeclareEntity.productId);
        productDeclareDO.setDeclareName(skuDeclareEntity.declareName);
        productDeclareDO.setBarCode(skuDeclareEntity.barcode);
        productDeclareDO.setRecordNo(skuDeclareEntity.recordNo);
        BigDecimal priceBigDecimal = new BigDecimal(skuDeclareEntity.price);// 转换为分
        long priceRmbCNF = priceBigDecimal.multiply(new BigDecimal(100)).longValue();
        productDeclareDO.setPriceRmb(priceRmbCNF);
        productDeclareDO.setRemark(skuDeclareEntity.getRemark());
        productDeclareDO.setHsCode(skuDeclareEntity.getHsCode());
        productDeclareDO.setGrossWeight(skuDeclareEntity.getGrossWeight());
        productDeclareDO.setNetWeight(skuDeclareEntity.getNetWeight());
        productDeclareDO.setTaxRate(skuDeclareEntity.getTaxRate());
        productDeclareManager.insert(productDeclareDO);
        return productDeclareDO.getId() != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeDeclareSku(Long skuId, Long portId) throws ServiceException {

        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        productDeclareDO.setSkuId(skuId);
        productDeclareDO.setPortId(portId);
        List<ProductDeclareDO> productDeclareDOs =
                productDeclareManager.query(BaseQuery.getInstance(productDeclareDO));
        if (productDeclareDOs != null && productDeclareDOs.size() > 0) {
            if (SkuDeclareStateType.DECLARE_WAIT.getValue()
                    .equalsIgnoreCase(productDeclareDOs.get(0).getState())) {
                productDeclareManager.delete(BaseQuery.getInstance(productDeclareDO));
                return true;
            } else {
                throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_ALLOW_DELETE,
                        LogisticsReturnCode.SKU_DECLARE_NOT_ALLOW_DELETE.getDesc());
            }

        } else {
            return true;
        }
    }

    /**
     * 批量删除备案商品(删除待备案或是资料收集中的商品，不删除商品备案数据，只删除广州、杭州、宁波备案数据)
     *
     * @param productDeclareIds 商品备案表IDs
     * @param portId 口岸ID
     * @param declareMode 备案模式
     * @param userId 用户ID
     * @param operator 用户名
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatchDeclareSku(String productDeclareIds, Long portId, String declareMode,
            Long userId, String operator) throws ServiceException {
        String[] productDeclareIdArr = productDeclareIds.split(",");
        // for (String productDeclareId : productDeclareIdArr) {
        // ProductDeclareDO productDeclareDO =
        // productDeclareManager.getById(Long.valueOf(productDeclareId));
        // if (productDeclareDO != null) {
        // if
        // (ProductDeclareState.WAIT_DECLARE.getValue().equalsIgnoreCase(productDeclareDO.getState())
        // ||
        // ProductDeclareState.COLLECTING.getValue().equalsIgnoreCase(productDeclareDO.getState()))
        // {
        // try {
        // if (distributedLock.fetch(DELETE_DECLARE_SKU_KEY + productDeclareId + "-" + portId + "-"
        // + declareMode)) {
        // if (portId.compareTo(2L) == 0 && "BONDED".equals(declareMode)) { // 广州保税
        // ProductDeclareGzBondedDO gz_bondedDO = new ProductDeclareGzBondedDO();
        // gz_bondedDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareGzBondedManager.delete(BaseQuery.getInstance(gz_bondedDO));
        // } else if (portId.compareTo(2L) == 0 && "DIRECTMAIL".equals(declareMode)) { //广州直邮
        // ProductDeclareGzDirectmailDO gz_dirsctmailDO = new ProductDeclareGzDirectmailDO();
        // gz_dirsctmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareGzDirectmailManager.delete(BaseQuery.getInstance(gz_dirsctmailDO));
        // } else if (portId.compareTo(1L) == 0 && "BONDED".equals(declareMode)) { //杭州保税
        // ProductDeclareHzBondedDO hz_bondedDO = new ProductDeclareHzBondedDO();
        // hz_bondedDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareHzBondedManager.delete(BaseQuery.getInstance(hz_bondedDO));
        // } else if (portId.compareTo(1L) == 0 && "DIRECTMAIL".equals(declareMode)) { //杭州直邮
        // ProductDeclareHzDirectmailDO hz_dirsctmailDO = new ProductDeclareHzDirectmailDO();
        // hz_dirsctmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareHzDirectmailManager.delete(BaseQuery.getInstance(hz_dirsctmailDO));
        // } else if (portId.compareTo(3L) == 0 && "BONDED".equals(declareMode)) { //宁波保税
        // ProductDeclareNbBondedDO nb_bondedDO = new ProductDeclareNbBondedDO();
        // nb_bondedDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareNbBondedManager.delete(BaseQuery.getInstance(nb_bondedDO));
        // } else if (portId.compareTo(3L) == 0 && "DIRECTMAIL".equals(declareMode)) { //宁波保税
        // ProductDeclareNbDirectmailDO nb_directmailDO = new ProductDeclareNbDirectmailDO();
        // nb_directmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareNbDirectmailManager.delete(BaseQuery.getInstance(nb_directmailDO));
        // } else if (portId.compareTo(5L) == 0 && "DIRECTMAIL".equals(declareMode)) { //济南直邮
        // ProductDeclareJnDirectmailDO jn_dirsctmailDO = new ProductDeclareJnDirectmailDO();
        // jn_dirsctmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareJnDirectmailManager.delete(BaseQuery.getInstance(jn_dirsctmailDO));
        // } else if (portId.compareTo(6L) == 0 && "DIRECTMAIL".equals(declareMode)) { //厦门直邮
        // ProductDeclareXmDirectmailDO xm_dirsctmailDO = new ProductDeclareXmDirectmailDO();
        // xm_dirsctmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareXMDirectmailManager.delete(BaseQuery.getInstance(xm_dirsctmailDO));
        // } else if (portId.compareTo(9L) == 0 && "BONDED".equals(declareMode)) { //重庆保税
        // ProductDeclareCqBondedDO cq_bondedDO = new ProductDeclareCqBondedDO();
        // cq_bondedDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareCqBondedManager.delete(BaseQuery.getInstance(cq_bondedDO));
        // } else if (portId.compareTo(10L) == 0 && "DIRECTMAIL".equals(declareMode)) { //青岛直邮
        // ProductDeclareQdDirectmailDO qd_declareDO = new ProductDeclareQdDirectmailDO();
        // qd_declareDO.setProductDeclareId(Long.valueOf(productDeclareId));
        // productDeclareQdDirectmailManager.delete(BaseQuery.getInstance(qd_declareDO));
        // }
        //
        //
        // // 删除备案商品主表
        // productDeclareManager.deleteById(Long.valueOf(productDeclareId));
        // }
        // } finally {
        // distributedLock.realease(DELETE_DECLARE_SKU_KEY + productDeclareId + "-" + portId + "-" +
        // declareMode);
        // }
        // } else {
        // throw new ServiceException(LogisticsReturnCode.SKU_DECLARE_NOT_ALLOW_DELETE,
        // LogisticsReturnCode.SKU_DECLARE_NOT_ALLOW_DELETE.getDesc());
        // }
        // }
        // }
        return true;
    }

    @Override
    public void updateProductDeclarePrice(Long productDeclareId, Long price)
            throws ServiceException {
        if (productDeclareId <= 0 || price < 0) {
            throw new ServiceException(new LogisticsReturnCode("参数非法", 0));
        }
        ProductDeclareDO productDeclareDO = productDeclareManager.getById(productDeclareId);
        if (productDeclareDO == null) {
            throw new ServiceException(new LogisticsReturnCode("商品备案信息不存在", 0));
        }

        productDeclareDO.setPriceRmb(price);
        productDeclareManager.update(productDeclareDO);
    }

    @Override
    public void updateProductDeclareTaxRate(Long productDeclareId, String taxRate)
            throws ServiceException {
        if (productDeclareId <= 0 || StringUtils.isBlank(taxRate) || !taxRate.startsWith("0.")) {
            throw new ServiceException(new LogisticsReturnCode("参数非法", 0));
        }
        ProductDeclareDO productDeclareDO = productDeclareManager.getById(productDeclareId);
        if (productDeclareDO == null) {
            throw new ServiceException(new LogisticsReturnCode("商品备案信息不存在", 0));
        }

        productDeclareDO.setTaxRate(taxRate);
        productDeclareManager.update(productDeclareDO);
        // skuService.redisSkuInfoUpdate(productDeclareDO.getSkuId());
    }


    @Override
    public void updateProductDeclareRate(Long productDeclareId, String consumptionDutyRate,
            String addedValueTaxRate, String tariffRate) throws ServiceException {
        if (productDeclareId <= 0 || StringUtils.isBlank(consumptionDutyRate)
                || !consumptionDutyRate.startsWith("0.") || StringUtils.isBlank(addedValueTaxRate)
                || !addedValueTaxRate.startsWith("0.") || StringUtils.isBlank(tariffRate)
                || !tariffRate.startsWith("0.")) {
            throw new ServiceException(new LogisticsReturnCode("参数非法", 0));
        }
        ProductDeclareDO productDeclareDO = productDeclareManager.getById(productDeclareId);
        if (productDeclareDO == null) {
            throw new ServiceException(new LogisticsReturnCode("商品备案信息不存在", 0));
        }

        productDeclareDO.setConsumptionDutyRate(consumptionDutyRate);
        productDeclareDO.setAddedValueTaxRate(addedValueTaxRate);
        productDeclareDO.setTariffRate(tariffRate);
        productDeclareManager.update(productDeclareDO);
        // skuService.redisSkuInfoUpdate(productDeclareDO.getSkuId());
    }


    @Override
    public void updateProductDeclare(SkuDeclareEntity skuDeclareEntity) throws ServiceException {
        if (skuDeclareEntity.productDeclareId <= 0
                || StringUtils.isBlank(skuDeclareEntity.consumptionDutyRate)
                || !skuDeclareEntity.consumptionDutyRate.startsWith("0.")
                || StringUtils.isBlank(skuDeclareEntity.addedValueTaxRate)
                || !skuDeclareEntity.addedValueTaxRate.startsWith("0.")
                || StringUtils.isBlank(skuDeclareEntity.tariffRate)
                || !skuDeclareEntity.tariffRate.startsWith("0.")) {
            throw new ServiceException(new LogisticsReturnCode("参数非法", 0));
        }
        ProductDeclareDO productDeclareDO = productDeclareManager.getById(skuDeclareEntity.productDeclareId);
        if (productDeclareDO == null) {
            throw new ServiceException(new LogisticsReturnCode("商品备案信息不存在", 0));
        }


        ProductDeclareDO doForUpdate = new ProductDeclareDO();
        doForUpdate.setId(skuDeclareEntity.productDeclareId);
        doForUpdate.setProductId(skuDeclareEntity.productId);
        doForUpdate.setDeclareName(skuDeclareEntity.declareName);
        doForUpdate.setBarCode(skuDeclareEntity.barcode);
        doForUpdate.setRecordNo(skuDeclareEntity.recordNo);
        doForUpdate.setOrigin(skuDeclareEntity.origin);
        if (skuDeclareEntity.price != null) {
            doForUpdate.setPriceRmb(Long.parseLong(skuDeclareEntity.price.toString()));
        }
        doForUpdate.setPostTaxNo(skuDeclareEntity.postTaxNo);
        doForUpdate.setConsumptionDutyRate(skuDeclareEntity.consumptionDutyRate);
        doForUpdate.setAddedValueTaxRate(skuDeclareEntity.addedValueTaxRate);
        doForUpdate.setTariffRate(skuDeclareEntity.tariffRate);
        doForUpdate.setHsCode(skuDeclareEntity.hsCode);
        doForUpdate.setNetWeight(skuDeclareEntity.netWeight);
        doForUpdate.setGrossWeight(skuDeclareEntity.grossWeight);
        doForUpdate.setMeasuringUnit(skuDeclareEntity.measuringUnit);
        doForUpdate.setFirstWeight(skuDeclareEntity.firstWeight);
        doForUpdate.setSecondWeight(skuDeclareEntity.secondWeight);
        doForUpdate.setFirstMeasuringUnit(skuDeclareEntity.firstMeasuringUnit);
        doForUpdate.setSecondMeasuringUnit(skuDeclareEntity.secondMeasuringUnit);
        productDeclareManager.update(doForUpdate);

        // 如果税率发生变化, 需要刷新Redis中商品信息
        if (!StringUtils.equals(productDeclareDO.getConsumptionDutyRate(),
                skuDeclareEntity.consumptionDutyRate)
                || !StringUtils.equals(productDeclareDO.getAddedValueTaxRate(),
                        skuDeclareEntity.addedValueTaxRate)
                || !StringUtils.equals(productDeclareDO.getTariffRate(),
                        skuDeclareEntity.tariffRate)) {
            // skuService.redisSkuInfoUpdate(skuDeclareEntity.skuId);
        }
    }

    @Override
    public List<SkuDeclareEntity> getSkuDeclareInfo(Long skuId) throws ServiceException {
        if (skuId == 0) {
            throw new ServiceException(new LogisticsReturnCode("sku不存在", 0));
        }
        ProductDeclareDO productDeclareDOForQuery = new ProductDeclareDO();
        productDeclareDOForQuery.setSkuId(skuId);
        List<ProductDeclareDO> purchaseOrderSkuDOs =
                productDeclareManager.query(BaseQuery.getInstance(productDeclareDOForQuery));
        List<SkuDeclareEntity> skuDeclareEntities = new ArrayList<SkuDeclareEntity>();
        for (ProductDeclareDO productDeclareDO1 : purchaseOrderSkuDOs) {
            SkuDeclareEntity skuDeclareEntity = new SkuDeclareEntity();
            skuDeclareEntity.portId = productDeclareDO1.getPortId();
            Long price = productDeclareDO1.getPriceRmb();
            if (price != null) {
                BigDecimal bigDecimal = new BigDecimal(price);
                skuDeclareEntity.price = bigDecimal
                        .divide(new BigDecimal(100), 2, BigDecimal.ROUND_CEILING).floatValue();
            }
            skuDeclareEntity.skuId = productDeclareDO1.getSkuId();
            // PortDO portDO = portManager.getById(productDeclareDO1.getPortId());
            // if (portDO == null) {
            // throw new ServiceException(new LogisticsReturnCode("口岸Id:" +
            // productDeclareDO1.getPortId() + "不存在", 0));
            // }
            // skuDeclareEntity.portName = portDO.getName();
            skuDeclareEntities.add(skuDeclareEntity);
        }
        return skuDeclareEntities;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult createDeclarePassSkuOfEmptyPort(Long skuId, String taxRate)
            throws ServiceException {
        BaseResult result = new BaseResult(true);
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-创建空口岸备案商品]:开始").log();
        for (LineType lineType : LineType.values()) {
            result = createDeclarePassSku(skuId, (long) PortNid.EMPTY.getValue(),
                    lineType.getValue(), taxRate, Operator.SYSTEM_OPERATOR.getName());
            if (!result.isSuccess()) {
                throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
                        "商品" + skuId + "备案失败:" + result.getResultMessage());
            }
        }
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-创建空口岸备案商品]:结束")
                .addParm("处理结果", result).log();
        return result;
    }

    /**
     * 创建空口岸备案商品
     *
     * @param skuId
     * @param portId
     * @param declareMode
     * @param taxRate
     * @param operator
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/7/18 23:54
     */
    @Override
    public BaseResult createDeclarePassSku(Long skuId, Long portId, String declareMode,
            String taxRate, String operator) {
        BaseResult result = new BaseResult();

        // 如果已存在备案商品,直接返回
        ProductDeclareDO productDeclareDO =
                productDeclareManager.queryDeclaredSku(skuId, portId, declareMode);
        if (productDeclareDO != null) {
            if (!taxRate.equals(productDeclareDO.getTaxRate())) {
                productDeclareDO.setTaxRate(taxRate);
                productDeclareManager.update(productDeclareDO);
            }
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-创建空口岸备案商品]:备案商品已存在")
                    .addParm("商品ID", skuId).addParm("口岸ID", portId).log();
            result.setSuccess(true);
            result.setResultMessage("备案商品已存在");
            return result;
        }

        // 判断SKU信息是否存在
        SkuDO skuDO = skuManager.getById(skuId);
        if (skuDO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("[供应链-创建空口岸备案商品]:商品不存在")
                    .addParm("商品ID", skuId).addParm("口岸ID", portId).log();
            result.setSuccess(false);
            result.setResultMessage("商品不存在");
            return result;
        }

        Long productDeclareId;
        if (distributedLock
                .fetch(CREATE_DECLARE_SKU_KEY + skuId + "-" + portId + "-" + declareMode)) {
            try {
                productDeclareDO = new ProductDeclareDO();
                productDeclareDO.setSkuId(skuId);
                productDeclareDO.setPortId(portId);
                productDeclareDO.setDeclareMode(declareMode);
                productDeclareDO.setState(SkuDeclareStateType.DECLARE_PASS.getValue());
                productDeclareDO.setDeclareName(skuDO.getName());
                productDeclareDO.setAttributes(skuDO.getAttributesDesc());
                productDeclareDO.setMeasuringUnit(skuDO.getMeasuringUnit());
                productDeclareDO.setTaxRate(taxRate);
                // productDeclareDO.setBarCode(skuDO.getBarcode());
                productDeclareDO.setBrand(skuDO.getBrandName());
                // productDeclareDO.setOrigin(skuDO.getOrigin());
                // productDeclareDO.setCategoryName(skuDO.getCategoryName());
                // if (skuDO.getCategoryId() == null) {
                // productDeclareDO.setCategoryId("");
                // } else {
                // productDeclareDO.setCategoryId(skuDO.getCategoryId().toString());
                // }

                productDeclareManager.insertOrUpdate(productDeclareDO);
                productDeclareId = productDeclareDO.getId();

                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, productDeclareId,
                                "supplychain"))
                        .setMsg("[供应链-创建空口岸备案商品]:备案成功").addParm("商品ID", skuId)
                        .addParm("口岸ID", portId).addParm("备案模式", declareMode)
                        .addParm("操作人", operator).log();

                result.setSuccess(true);

                // 刷新redis信息
                // skuService.redisSkuInfoUpdate(skuId);
                return result;

            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                        .setErrorMsg("[供应链-创建空口岸备案商品异常]:异常").setException(e).addParm("商品ID", skuId)
                        .addParm("口岸ID", portId).addParm("备案模式", declareMode)
                        .addParm("操作人", operator).log();
                result.setSuccess(false);
                result.setResultMessage("未知异常" + e.getMessage());
                return result;
            } finally {
                distributedLock.realease(
                        CREATE_DECLARE_SKU_KEY + skuId + "-" + portId + "-" + declareMode);
            }
        } else {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                    .setErrorMsg("[供应链-创建空口岸备案商品异常]: 并发异常").addParm("商品ID", skuId)
                    .addParm("口岸ID", portId).addParm("备案模式", declareMode).addParm("操作人", operator)
                    .log();
            result.setSuccess(false);
            result.setResultMessage("并发异常");
            return result;
        }
    }

    /**
     * 构建备案实体对象
     *
     * @param skuDO
     * @param portDO
     * @return
     * @throws ServiceException
     */
//    private ProductDeclareDO buildProductDeclare(SkuDO skuDO, PortDO portDO)
    private ProductDeclareDO buildProductDeclare(SkuDO skuDO, String portDO)
            throws ServiceException {
        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
        // productDeclareDO.setPortId(portDO.getId());
        // productDeclareDO.setSkuId(skuDO.getId());
        // List<ProductDeclareDO> productDeclareDOs =
        // productDeclareManager.query(BaseQuery.getInstance(productDeclareDO));
        // if (productDeclareDOs != null && productDeclareDOs.size() > 0) {
        // productDeclareDO = productDeclareDOs.get(0);
        // return productDeclareDO;
        // }
        // String portRecordNames = skuDO.getPortRecordNames();
        // String declareName = "";
        // if (!StringUtils.isBlank(portRecordNames)) {
        // JSONArray names = JSON.parseArray(portRecordNames);
        // for (int i = 0; i < names.size(); i++) {
        // Long declarePortId = names.getJSONObject(i).getLong("portId");
        // String portRecordName = names.getJSONObject(i).getString("portRecordName");
        // if (portDO.getId().equals(declarePortId) && StringUtils.isNotBlank(portRecordName)) {
        // declareName = portRecordName;
        // }
        // }
        // }
        // if (StringUtils.isBlank(declareName)) {
        // throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_DECLARE_NAME_NOT_EXIST,
        // LogisticsReturnCode.SKU_SERVICE_DECLARE_NAME_NOT_EXIST.getDesc());
        // }
        // productDeclareDO.setDeclareName(declareName);
        // productDeclareDO.setAttributes(skuDO.getAttributes());
        // productDeclareDO.setBarCode(skuDO.getBarcode());
        // productDeclareDO.setDeclareName(declareName);
        // productDeclareDO.setPostTaxNo(skuDO.getCustomsCategoryNid());
        // productDeclareDO.setBrand(skuDO.getBrandName());
        // productDeclareDO.setOrigin(skuDO.getOrigin());
        // productDeclareDO.setCategoryName(skuDO.getCategoryName());
        // productDeclareDO.setState(ProductDeclareState.WAIT_DECLARE.getValue());
        // if (skuDO.getCategoryId() == null) {
        // productDeclareDO.setCategoryId("");
        // } else {
        // productDeclareDO.setCategoryId(skuDO.getCategoryId().toString());
        // }

        return productDeclareDO;
    }

    /**
     * 商品备案参数验证
     *
     * @param productDeclareDO
     * @throws ServiceException
     */
    private void validProductDeclareParam(ProductDeclareDO productDeclareDO)
            throws ServiceException {
        if (StringUtils.isBlank(productDeclareDO.getPostTaxNo())
                || StringUtils.isBlank(productDeclareDO.getBrand())
                || StringUtils.isBlank(productDeclareDO.getCategoryName())
                || StringUtils.isBlank(productDeclareDO.getDeclareName())
                || StringUtils.isBlank(productDeclareDO.getCategoryId())) {
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL,
                    LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL.getDesc());
        }
    }

    protected void unlockBatchStock(final Long skuId, final Long portId, final String declareMode) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("商品备案成功，自动禁用批次库存")
                .addParm("商品ID", skuId).addParm("口岸ID", portId).addParm("备案模式", declareMode).log();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                // 1. 根据口岸和备案模式查找路线；
                // BaseQuery<LogisticsLineDO> query = new BaseQuery<LogisticsLineDO>(new
                // LogisticsLineDO());
                // query.getData().setPortId(portId);
                // query.getData().setLineType(declareMode);
                // List<LogisticsLineDO> logisticsLineDOList = logisticsLineDao.query(query);
                //
                // //2. 根据仓库ID和SKUID查找被禁用的批次库存
                // if (null != logisticsLineDOList) {
                // //聚合仓库
                // Set<Long> warehouseIdSet = new HashSet<Long>();
                // for (LogisticsLineDO logisticsLineDO : logisticsLineDOList) {
                // warehouseIdSet.add(logisticsLineDO.getWarehouseId());
                // }
                // for (Long warehouseId : warehouseIdSet) {
                // BaseQuery<BatchStockLockLogDO> lockQuery = new BaseQuery<BatchStockLockLogDO>(new
                // BatchStockLockLogDO());
                // lockQuery.getData().setSkuId(skuId);
                // lockQuery.getData().setWarehouseId(warehouseId);
                // lockQuery.getData().setLockType(BatchStockLockType.NODECLARE_GOODS.getValue());
                // lockQuery.getData().setStatus(BatchStockLockStatus.LOCK.getValue());
                //
                // List<BatchStockLockLogDO> batchStockLockLogDOList =
                // batchStockLockLogManager.query(lockQuery);
                // if (null != batchStockLockLogDOList) {
                // for (BatchStockLockLogDO batchStockLockLogDO : batchStockLockLogDOList) {
                // //判断批次禁用的原因是否匹对
                // LockBatckStockByNotDeclareEntity lockReason =
                // (LockBatckStockByNotDeclareEntity) JSON.parseObject(
                // (String)
                // batchStockLockLogDO.getFeature(LockBatckStockByNotDeclareEntity.LOCK_REASON),
                // LockBatckStockByNotDeclareEntity.class);
                // if (null != lockReason
                // && lockReason.getSkuId().equals(skuId)
                // && lockReason.getWarehouseId().equals(warehouseId)
                // && lockReason.getDeclareMode().equals(declareMode)
                // && lockReason.getPortId().equals(portId)) {
                //
                // try {
                // stockService.unlockBatchStock(skuId, batchStockLockLogDO.getBatchStockId(),
                // Operator.SYSTEM_OPERATOR.getName(), "商品备案通过");
                //
                // LogBetter.instance(logger)
                // .setLevel(LogLevel.INFO)
                // .setMsg("商品备案成功，自动禁用批次库存成功")
                // .addParm("商品ID", skuId)
                // .addParm("仓库ID", warehouseId)
                // .addParm("口岸ID", portId)
                // .addParm("备案模式", declareMode)
                // .addParm("批次号", batchStockLockLogDO.getBatchNo())
                // .log();
                //
                // } catch (ServiceException e) {
                // LogBetter.instance(logger)
                // .setLevel(LogLevel.ERROR)
                // .setMsg("商品备案成功，自动禁用批次库存异常：" + e.getMsg())
                // .addParm("商品ID", skuId)
                // .addParm("仓库ID", warehouseId)
                // .addParm("口岸ID", portId)
                // .addParm("备案模式", declareMode)
                // .addParm("批次号", batchStockLockLogDO.getBatchNo())
                // .log();
                // }
                //
                // }
                // }
                // }
                //
                // }
                // }
            }
        };
        threadPoolTaskExecutor.execute(task);
    }

    @Override
    public void declaring(List<Long> skuIdList, Long portId, String declareMode, Long userId,
            String operator) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[批量设置备案商品状态为备案中]:开始")
                .addParm("sku列表", skuIdList).addParm("口岸", portId).addParm("备案模式", declareMode)
                .addParm("操作人", operator);

//        for (Long skuId : skuIdList) {
//            final ProductDeclareDO productDeclareDO =
//                    productDeclareManager.queryDeclaredSku(skuId, portId, declareMode);
//            if (null == productDeclareDO) {
//                throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_NOT_FOUND_ERROR,
//                        LogisticsReturnCode.PRODUCT_DECLARE_NOT_FOUND_ERROR.getDesc());
//            }
//
//            if (!ProductDeclareState.FINISHED_COLLECTING.getValue()
//                    .equals(productDeclareDO.getState())) {
//                throw new ServiceException(
//                        LogisticsReturnCode.PRODUCT_DECLARE_DECLARING_STATE_ERROR,
//                        LogisticsReturnCode.PRODUCT_DECLARE_DECLARING_STATE_ERROR.getDesc());
//            }
//
//            if (distributedLock.fetch(DECLARING_SKU_KEY + productDeclareDO.getId())) {
//                try {
//                    ProductDeclareRequest productDeclareRequest =
//                            ProductDeclareRequestFactory.generateProductDeclareRequest(
//                                    ProductDeclareActionType.DECLARE_TO_DECLARING, productDeclareDO,
//                                    null, Operator.valueOf(userId, operator));
//                    engineService.executeStateMachineEngine(productDeclareRequest, false);
//
//                    LogBetter.instance(logger).setLevel(LogLevel.INFO)
//                            .setTraceLogger(TraceLogEntity.instance(traceLogger,
//                                    productDeclareDO.getId(), "supplychain"))
//                            .setMsg("[供应链-设置备案商品状态为备案中]:成功")
//                            .addParm("备案ID", productDeclareDO.getId()).addParm("操作人", operator)
//                            .log();
//                } catch (Exception e) {
//                    LogBetter.instance(logger).setLevel(LogLevel.ERROR).setException(e)
//                            .setTraceLogger(TraceLogEntity.instance(traceLogger,
//                                    productDeclareDO.getId(), "supplychain"))
//                            .setErrorMsg("[供应链-设置备案商品状态为备案中]:流程引擎执行异常")
//                            .addParm("备案ID", productDeclareDO.getId()).addParm("操作人", operator)
//                            .log();
//                    throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
//                            LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION.getDesc());
//                } finally {
//                    distributedLock.realease(DECLARING_SKU_KEY + productDeclareDO.getId());
//                }
//            } else {
//                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
//                        .setTraceLogger(TraceLogEntity.instance(traceLogger,
//                                productDeclareDO.getId(), "supplychain"))
//                        .setErrorMsg("[供应链-设置备案商品状态为备案中]:并发异常")
//                        .addParm("备案ID", productDeclareDO.getId()).addParm("操作人", operator).log();
//                throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
//                        "[供应链-设置备案商品状态为备案中]: 并发异常" + "[备案ID: " + productDeclareDO.getId() + "]");
//            }
//        }
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[批量设置备案商品状态为备案中]:结束")
                .addParm("sku列表", skuIdList).addParm("口岸", portId).addParm("备案模式", declareMode)
                .addParm("操作人", operator);
    }

    @Override
    public BaseResult updateSkuGrossWeight(Long skuId, int grossWeitht, String userName)
            throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[供应链-修改商品毛重]：开始")
                .addParm("商品SKU", skuId).addParm("商品毛重", grossWeitht).addParm("操作人", userName)
                .addParm("Class", "ProductDeclareService").log();

        BaseResult result = new BaseResult(Boolean.TRUE);
//        try {
//            SkuDO skuDO = skuManager.getbySkuId(skuId);
//            Map<Long, Integer> lineLogisticsCostRmb = new HashMap<Long, Integer>();
//            logger.info("开始获取路线信息......");
//            Map<Long, LogisticsLineDO> allLineDos = logisticsLineManager.getAllLineMap();
//            for (Long key : allLineDos.keySet()) {
//                LogisticsLineDO logisticsLineDO = allLineDos.get(key);
//                // 如果线路是包邮的直接跳过
//                if (logisticsLineDO != null) {
//                    if (logisticsLineDO.getIsContainFreight()) {
//                        logger.info("【开始计算logisticsCostRmb】" + "grossWeitht= " + grossWeitht + " "
//                                + logisticsLineDO.toString());
//                        int logisticsCostRmb = costCalculateService
//                                .calculateSkuLogisticsCost(grossWeitht, logisticsLineDO);
//                        lineLogisticsCostRmb.put(logisticsLineDO.getId(), logisticsCostRmb);
//                    } else {
//                        logger.info("【包邮线路】" + "grossWeitht= " + grossWeitht + " "
//                                + logisticsLineDO.toString());
//                        int logisticsCostRmb = costCalculateService
//                                .calculateSkuLogisticsCost(grossWeitht, logisticsLineDO);
//                        lineLogisticsCostRmb.put(logisticsLineDO.getId(), 0);
//                    }
//                } else {
//                    logger.info("为找到logisticsLineDO为空" + "lineId= " + key);
//                }
//            }
//            if (skuDO != null) {
//                skuDO.setGrossWeight(grossWeitht);
//                skuManager.updateIncludeDeleted(skuId, grossWeitht);
//                skuLineCostModelManager.updateSkuGrossWeight(skuId, grossWeitht,
//                        lineLogisticsCostRmb);
//
//            } else {
//                result.setSuccess(Boolean.FALSE);
//                result.setResultMessage("商品Sku不存在");
//            }
//        } catch (ServiceException e1) {
//            LogBetter.instance(logger).setLevel(LogLevel.WARN).setException(e1)
//                    .addParm("skuId", skuId).setMsg("修改商品毛重失败：" + e1.getMessage()).log();
//            result.setSuccess(Boolean.FALSE);
//            result.setResultMessage("修改商品毛重失败" + e1.getMessage());
//        } catch (Exception e) {
//            LogBetter.instance(logger).setLevel(LogLevel.WARN).setException(e)
//                    .addParm("skuId", skuId).setMsg("修改商品毛重失败：" + e.getMessage()).log();
//            result.setSuccess(Boolean.FALSE);
//            result.setResultMessage("修改商品毛重失败" + e.getMessage());
//        }


        LogBetter.instance(logger).setLevel(LogLevel.INFO).addParm("返回结果", result)
                .setMsg("[供应链-修改商品毛重]：结束").log();
        return result;
    }

    @Override
    public BaseResult recalculateSkuTax(Long skuId, Long lineId, Integer supplyCost)
            throws ServiceException {
        logger.info("开始重计算商品税费......" + "skuId=" + skuId + "  lineId=" + lineId + " supplyCost="
                + String.valueOf(supplyCost));
        BaseResult result = new BaseResult(Boolean.TRUE);
//        LogisticsLineDO lineDO = lineDOMap.get(lineId);
//
//        if (lineDO == null) {
//            lineDO = logisticsLineManager.getById(lineId);
//            lineDOMap.put(lineId, lineDO);
//        }
//
//        WarehouseDO warehouseDO = warehouseDOMap.get(lineDO.getWarehouseId());
//        if (warehouseDO == null) {
//            warehouseDO = warehouseManager.getById(lineDO.getWarehouseId());
//            warehouseDOMap.put(lineDO.getWarehouseId(), warehouseDO);
//        }
//
//        if (lineDO == null || warehouseDO == null) {
//            result.setSuccess(Boolean.FALSE);
//            result.setResultMessage("线路或者仓库不存在......");
//        }
//
//        String warehouseName = warehouseDO.getWarehouseNid();
//        SkuTaxDO skuTaxDO =
//                costCalculateService.getSkuTaxInfo(skuId, lineDO.getPortId(), lineDO.getLineType());
//        logger.info("获取到的skuTaxDO信息为：" + skuTaxDO.toString());
//        lineService.addOrUpdateSkuLineCostModel(skuTaxDO, lineDO, warehouseName, supplyCost);
        return result;
    }
}
