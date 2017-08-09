/*
 * @(#) com.sfebiz.logistics.service.sku/ProductDeclareServiceImpl.java
 * 
 */
package com.sfebiz.supplychain.service.sku;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.exception.DataAccessException;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.code.SkuReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.line.enums.LineType;
import com.sfebiz.supplychain.exposed.sku.api.ProductDeclareService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuDeclareEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuDeclareStateType;
import com.sfebiz.supplychain.lock.DistributedLock;
import com.sfebiz.supplychain.persistence.base.sku.domain.*;
import com.sfebiz.supplychain.persistence.base.sku.manager.*;
import com.sfebiz.supplychain.service.statemachine.Operator;
import com.taobao.tbbpm.common.persistence.ITransactionCallback;
import com.taobao.tbbpm.common.persistence.ITransactionCallbackWithoutResult;
import com.taobao.tbbpm.common.persistence.ITransactionStatus;
import com.taobao.tbbpm.common.persistence.SpringTransactionTemplate;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品备案服务
 * 
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017/7/18 22:56
 */
@Component("productDeclareService")
public class ProductDeclareServiceImpl implements ProductDeclareService {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("skurecord");

    private final static Logger logger = LoggerFactory.getLogger(ProductDeclareServiceImpl.class);

    private static final String CREATE_DECLARE_SKU_KEY = "CREATE_DECLARE_SKU_KEY:";

    private static final String DELETE_DECLARE_SKU_KEY = "DELETE_DECLARE_SKU_KEY:";

    private static final String FINISH_COLLECT_DECLARE_SKU_KEY = "FINISH_COLLECT_DECLARE_SKU_KEY:";

    private static final String NOT_PASS_DECLARE_SKU_KEY = "NOT_PASS_DECLARE_SKU_KEY:";

    @Resource
    private ProductDeclareManager productDeclareManager;

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
    private ProductDeclareXmDirectmailManager productDeclareXmDirectmailManager;

    @Resource
    private SkuManager skuManager;

    @Resource
    private DistributedLock distributedLock;

    @Resource
    private SpringTransactionTemplate springTransactionTemplate;

    /**
     * 创建备案商品
     *
     * @param skuId 商品ID
     * @param portId 口岸ID
     * @param declareMode 备案模式
     * @param userId 用户ID
     * @param operator 操作人
     * @return CommonRet<Long>
     */
    @Override
    public CommonRet<Long> createDeclareSku(Long skuId, Long portId, String declareMode, Long userId, String operator) {
        return createDeclareSku(0L, skuId, portId, declareMode, userId, operator);
    }

    /**
     * 批量创建待备案商品
     *
     * @param skuIds 待备案商品
     * @param portId 口岸ID
     * @param declareMode 备案模式
     * @param userId 用户ID
     * @param operator 操作人
     */
    @Override
    public CommonRet<Void> createBatchDeclareSku(List<Long> skuIds, Long portId, String declareMode, Long userId, String operator){

        CommonRet<Void> commonRet = new CommonRet<Void>();
        for (Long skuId : skuIds) {
            CommonRet<Long> ret = createDeclareSku(skuId, portId, declareMode, userId, operator);
            if (ret.getRetCode() != SCReturnCode.COMMON_SUCCESS.getCode()) {
                commonRet.setRetCode(ret.getRetCode());
                commonRet.setRetMsg("sku_id:" + skuId + ret.getRetMsg());
                return commonRet;
            }
        }
        return commonRet;
    }

    /**
     * 创建备案商品
     *
     * @param purchaseOrderId 采购单ID
     * @param skuId 商品ID
     * @param portId 口岸ID
     * @param declareMode 备案模式
     * @param userId 用户ID
     * @param operator 操作人
     * @return CommonRet<Long>
     */
    @Override
    public CommonRet<Long> createDeclareSku(Long purchaseOrderId,
                                            final Long skuId,
                                            final Long portId,
                                            final String declareMode,
                                            final Long userId,
                                            final String operator) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-创建备案商品]:开始")
                .addParm("商品ID", skuId)
                .addParm("口岸ID", portId)
                .addParm("备案模式", declareMode)
                .addParm("操作人", operator)
                .log();

        // 判断SKU信息是否存在
        final SkuDO skuDO = skuManager.getById(skuId);
        if (skuDO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                    .setMsg("[供应链-创建备案商品]:商品不存在，根据skuId查找不到对应的sku")
                    .addParm("商品ID", skuId)
                    .addParm("口岸ID", portId)
                    .addParm("备案模式", declareMode)
                    .addParm("操作人", operator)
                    .log();
            commonRet.setRetCode(SkuReturnCode.SKU_NOT_EXIST.getCode());
            commonRet.setRetMsg(SkuReturnCode.SKU_NOT_EXIST.getDesc());
            return commonRet;
        }

        // 如果已存在备案商品,直接返回
        ProductDeclareDO productDeclareDO = productDeclareManager.queryDeclaredSku(skuId, portId, declareMode);
        if (productDeclareDO != null) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[供应链-创建备案商品]:备案商品已存在")
                    .addParm("商品ID", skuId)
                    .addParm("口岸ID", portId)
                    .addParm("备案模式", declareMode)
                    .addParm("操作人", operator)
                    .log();

            commonRet.setRetCode(SkuReturnCode.DECLARE_EXIST.getCode());
            commonRet.setRetMsg(SkuReturnCode.DECLARE_EXIST.getDesc());
            return commonRet;
        }
        Long productDeclareId = 0L; //备案记录ID
        if (distributedLock.fetch(CREATE_DECLARE_SKU_KEY + skuId + "-" + portId + "-" + declareMode)) {
            try {

                // 事务控制
                productDeclareId = (Long) springTransactionTemplate.execute(new ITransactionCallback() {
                    @Override
                    public Object doInTransaction(ITransactionStatus status) throws Exception {

                        // 从采购单审核的时候生成备案商品
                        String providerName = "";
                        String purchaseMode = "";
                        // 平潭专用
                        String productCode = "";
                        String customsDeclarationElement = "";
                        Long productDeclareId;

                        ProductDeclareDO productDeclareDO = new ProductDeclareDO();
                        productDeclareDO.setSkuId(skuId);
                        productDeclareDO.setPortId(portId);
                        productDeclareDO.setDeclareMode(declareMode);
                        productDeclareDO.setState(SkuDeclareStateType.WAIT_DECLARE.getValue());
                        productDeclareDO.setDeclareName(skuDO.getName());
                        productDeclareDO.setAttributes(skuDO.getAttributesDesc());
                        productDeclareDO.setMeasuringUnit(skuDO.getMeasuringUnit());
                        // productDeclareDO.setBarCode(skuDO.getBarcode());
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
                            productDeclareXmDirectmailManager.insertOrUpdate(xmDirectmailDO);
                        } else if (portId == PortNid.PINGTAN.getValue()
                                && LineType.DIRECTMAIL.getValue().equals(declareMode)) {
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
                        return productDeclareId;
                    }
                });
            } catch (Exception e) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setException(e)
                        .setErrorMsg("[供应链-创建备案商品异常]:异常")
                        .addParm("商品ID", skuId)
                        .addParm("口岸ID", portId)
                        .addParm("备案模式", declareMode)
                        .addParm("操作人", operator)
                        .log();
                commonRet.setRetCode(SkuReturnCode.DECLARE_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(SkuReturnCode.DECLARE_UNKNOWN_ERROR.getDesc());
            } finally {
                distributedLock.realease(CREATE_DECLARE_SKU_KEY + skuId + "-" + portId + "-" + declareMode);
            }
        } else {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链-创建备案商品异常]: 并发异常")
                    .addParm("商品ID", skuId)
                    .addParm("口岸ID", portId)
                    .addParm("备案模式", declareMode)
                    .addParm("操作人", operator)
                    .log();
            commonRet.setRetCode(SkuReturnCode.DECLARE_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg(SkuReturnCode.DECLARE_CONCURRENT_EXCEPTION.getDesc());
        }
        commonRet.setResult(productDeclareId);
        return commonRet;
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
    public boolean finishCollect(final SkuDeclareEntity skuDeclareEntity, final Long userId, final String operator) throws ServiceException {
        LogBetter.instance(logger)
                 .setLevel(LogLevel.INFO)
                 .setMsg("[商品备案资料收集完毕导入]:开始")
                 .addParm("商品备案信息", skuDeclareEntity)
                 .addParm("操作人", operator).log();

        // 判断是否已存在备案商品
        ProductDeclareDO productDeclareDO = productDeclareManager.queryDeclaredSku(skuDeclareEntity.skuId, skuDeclareEntity.portId, skuDeclareEntity.declareMode);

        // 如果不存在则初始化一条
        if (null == productDeclareDO) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[商品备案资料收集完毕导入]:备案商品不存在, 初始化一条")
                    .addParm("商品备案信息", skuDeclareEntity)
                    .addParm("操作人", operator).log();
            try {
                springTransactionTemplate.execute(new ITransactionCallbackWithoutResult() {
                    @Override
                    public void doInTransaction(ITransactionStatus status) throws Exception {
                        createDeclareSku(skuDeclareEntity.skuId, skuDeclareEntity.portId, skuDeclareEntity.declareMode, userId, operator);
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
            productDeclareDO = productDeclareManager.queryDeclaredSku(skuDeclareEntity.skuId, skuDeclareEntity.portId, skuDeclareEntity.declareMode);
        }

        if (StringUtils.isNotBlank(skuDeclareEntity.getHsCode())) {
            ProductDeclareHsCodeDO productDeclareHsCodeDO = productDeclareHsCodeManager.getByHsCode(skuDeclareEntity.getHsCode());
            skuDeclareEntity.setConsumptionDutyRate(productDeclareHsCodeDO.getConsumptionDutyRate());
            skuDeclareEntity.setTariffRate(productDeclareHsCodeDO.getTariffRate());
            skuDeclareEntity.setAddedValueTaxRate(productDeclareHsCodeDO.getAddedValueTaxRate());
            skuDeclareEntity.setMeasuringUnit(productDeclareHsCodeDO.getMeasuringUnit());
            skuDeclareEntity.setSecondaryMeasuringUnit(productDeclareHsCodeDO.getSecondMeasuringUnit());
        }
        if (StringUtils.isNotBlank(skuDeclareEntity.getPostTaxNo())) {
            ProductDeclareHsCodeDO productDeclareHsCodeDO = productDeclareHsCodeManager.getByHsCode(skuDeclareEntity.getPostTaxNo());
            skuDeclareEntity.setTaxRate(productDeclareHsCodeDO.getTaxRate());
        }

        // 控制并发
        if (distributedLock.fetch(FINISH_COLLECT_DECLARE_SKU_KEY + productDeclareDO.getId())) {
            try {
                // 调用备案通过的方法，代替以前的状态机处理
                process(productDeclareDO, skuDeclareEntity, operator);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION, "[商品备案资料收集完毕导入]:" + e.getMessage());
            } finally {
                distributedLock.realease(FINISH_COLLECT_DECLARE_SKU_KEY + productDeclareDO.getId());
            }
        } else {
            throw new ServiceException(LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
                    "[商品备案资料收集完毕导入]:并发异常");
        }
        return true;
    }

    /**
     * 商品备案不通过导入
     *
     * @param productDeclareId 商品备案主表ID
     * @param reason           备案不通过原因
     * @param userId           用户ID
     * @param operator         操作人
     * @return
     * @throws ServiceException
     */
    @Override
    public CommonRet<Void> declareNotPass(Long productDeclareId, String reason, Long userId, String operator) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-商品备案不通过]")
                .addParm("备案ID", productDeclareId)
                .addParm("备案不通过原因", reason)
                .addParm("用户ID", userId)
                .addParm("操作人", operator)
                .log();

        ProductDeclareDO productDeclareDO = productDeclareManager.getById(productDeclareId);
        if (null == productDeclareDO) {
            commonRet.setRetCode(SkuReturnCode.DECLARE_NOT_FOUND.getCode());
            commonRet.setRetMsg(SkuReturnCode.DECLARE_NOT_FOUND.getDesc());
            return commonRet;
        }

        if (distributedLock.fetch(NOT_PASS_DECLARE_SKU_KEY + productDeclareId)) {
            try {
                productDeclareDO.setRemark(reason);
                productDeclareDO = new ProductDeclareDO();
                productDeclareDO.setId(productDeclareId);
                productDeclareDO.setState(SkuDeclareStateType.DECLARE_NOT_PASS.getValue());
                productDeclareDO.setRemark(reason);
                productDeclareManager.update(productDeclareDO);
            } catch (Exception e) {
                logger.error("[供应链-商品备案不通过操作] 异常：productDeclareId={}，e={}", productDeclareId, e);
                commonRet.setRetCode(SkuReturnCode.DECLARE_UNKNOWN_ERROR.getCode());
                commonRet.setRetMsg(SkuReturnCode.DECLARE_UNKNOWN_ERROR.getDesc());
                return commonRet;
            } finally {
                distributedLock.realease(NOT_PASS_DECLARE_SKU_KEY + productDeclareId);
            }
        } else {
            commonRet.setRetCode(SkuReturnCode.DECLARE_CONCURRENT_EXCEPTION.getCode());
            commonRet.setRetMsg(SkuReturnCode.DECLARE_CONCURRENT_EXCEPTION.getDesc());
            return commonRet;
        }
        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setMsg("[供应链-商品备案不通过操作]:结束")
                .addParm("备案ID", productDeclareId)
                .addParm("备案不通过原因", reason)
                .addParm("用户ID", userId)
                .addParm("操作人", operator)
                .log();
        return commonRet;
    }

    /**
     * 批量删除备案商品(删除待备案的商品，不删除商品备案数据)
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
    public CommonRet<Void> removeBatchDeclareSku(String productDeclareIds, final Long portId, final String declareMode, Long userId, String operator) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        final String[] productDeclareIdArr = productDeclareIds.split(",");
        try {
            commonRet = (CommonRet<Void>) springTransactionTemplate.execute(new ITransactionCallback() {
                @Override
                public Object doInTransaction(ITransactionStatus status) throws Exception {
                    CommonRet<Void> commonRet = new CommonRet<Void>();
                    for (String productDeclareId : productDeclareIdArr) {
                        ProductDeclareDO productDeclareDO = productDeclareManager.getById(Long.valueOf(productDeclareId));
                        if (productDeclareDO == null) {
                            commonRet.setRetCode(SkuReturnCode.DECLARE_NOT_FOUND.getCode());
                            commonRet.setRetMsg(SkuReturnCode.DECLARE_NOT_FOUND.getDesc());
                            return commonRet;
                        }
                        if (!SkuDeclareStateType.WAIT_DECLARE.getValue().equalsIgnoreCase(productDeclareDO.getState())) {
                            commonRet.setRetCode(SkuReturnCode.DECLARE_WAIT_NOT_ALLOW_DELETE.getCode());
                            commonRet.setRetMsg(SkuReturnCode.DECLARE_WAIT_NOT_ALLOW_DELETE.getDesc());
                            return commonRet;
                        }
                    }
                    for (String productDeclareId : productDeclareIdArr) {
                        if (portId.compareTo(2L) == 0 && "BONDED".equals(declareMode)) { // 广州保税
                            ProductDeclareGzBondedDO gz_bondedDO = new ProductDeclareGzBondedDO();
                            gz_bondedDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareGzBondedManager.delete(BaseQuery.getInstance(gz_bondedDO));
                        } else if (portId.compareTo(2L) == 0 && "DIRECTMAIL".equals(declareMode)) { // 广州直邮
                            ProductDeclareGzDirectmailDO gz_dirsctmailDO = new ProductDeclareGzDirectmailDO();
                            gz_dirsctmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareGzDirectmailManager.delete(BaseQuery.getInstance(gz_dirsctmailDO));
                        } else if (portId.compareTo(1L) == 0 && "BONDED".equals(declareMode)) { // 杭州保税
                            ProductDeclareHzBondedDO hz_bondedDO = new ProductDeclareHzBondedDO();
                            hz_bondedDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareHzBondedManager.delete(BaseQuery.getInstance(hz_bondedDO));
                        } else if (portId.compareTo(1L) == 0 && "DIRECTMAIL".equals(declareMode)) { // 杭州直邮
                            ProductDeclareHzDirectmailDO hz_dirsctmailDO = new ProductDeclareHzDirectmailDO();
                            hz_dirsctmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareHzDirectmailManager.delete(BaseQuery.getInstance(hz_dirsctmailDO));
                        } else if (portId.compareTo(3L) == 0 && "BONDED".equals(declareMode)) { // 宁波保税
                            ProductDeclareNbBondedDO nb_bondedDO = new ProductDeclareNbBondedDO();
                            nb_bondedDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareNbBondedManager.delete(BaseQuery.getInstance(nb_bondedDO));
                        } else if (portId.compareTo(3L) == 0 && "DIRECTMAIL".equals(declareMode)) { // 宁波保税
                            ProductDeclareNbDirectmailDO nb_directmailDO = new ProductDeclareNbDirectmailDO();
                            nb_directmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareNbDirectmailManager.delete(BaseQuery.getInstance(nb_directmailDO));
                        } else if (portId.compareTo(5L) == 0 && "DIRECTMAIL".equals(declareMode)) { // 济南直邮
                            ProductDeclareJnDirectmailDO jn_dirsctmailDO = new ProductDeclareJnDirectmailDO();
                            jn_dirsctmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareJnDirectmailManager.delete(BaseQuery.getInstance(jn_dirsctmailDO));
                        } else if (portId.compareTo(6L) == 0 && "DIRECTMAIL".equals(declareMode)) { // 厦门直邮
                            ProductDeclareXmDirectmailDO xm_dirsctmailDO = new ProductDeclareXmDirectmailDO();
                            xm_dirsctmailDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareXmDirectmailManager.delete(BaseQuery.getInstance(xm_dirsctmailDO));
                        } else if (portId.compareTo(9L) == 0 && "BONDED".equals(declareMode)) { // 重庆保税
                            ProductDeclareCqBondedDO cq_bondedDO = new ProductDeclareCqBondedDO();
                            cq_bondedDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareCqBondedManager.delete(BaseQuery.getInstance(cq_bondedDO));
                        } else if (portId.compareTo(10L) == 0 && "DIRECTMAIL".equals(declareMode)) { // 青岛直邮
                            ProductDeclareQdDirectmailDO qd_declareDO = new ProductDeclareQdDirectmailDO();
                            qd_declareDO.setProductDeclareId(Long.valueOf(productDeclareId));
                            productDeclareQdDirectmailManager.delete(BaseQuery.getInstance(qd_declareDO));
                        }
                        // 删除备案商品主表
                        productDeclareManager.deleteById(Long.valueOf(productDeclareId));
                    }
                    return commonRet;
                }
            });
        } catch (Exception e) {
            logger.error("[供应链-批量删除备案商品] 异常：，e={}", e);
            commonRet.setRetCode(SkuReturnCode.DECLARE_UNKNOWN_ERROR.getCode());
            commonRet.setRetMsg(SkuReturnCode.DECLARE_UNKNOWN_ERROR.getDesc());
            return commonRet;
        }
        return commonRet;
    }

    /**
     * 创建空口岸的备案记录
     * @param skuId
     * @param taxRate
     * @return
     * @throws ServiceException
     */
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
    private BaseResult createDeclarePassSku(Long skuId, Long portId, String declareMode,
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
     * 更新备案信息
     * @param skuDeclareEntity
     * @throws ServiceException
     */
    @Override
    public void updateProductDeclare(SkuDeclareEntity skuDeclareEntity) throws ServiceException {
        if (skuDeclareEntity.productDeclareId <= 0) {
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
        doForUpdate.setBrand(skuDeclareEntity.brandName);
        doForUpdate.setCategoryName(skuDeclareEntity.categoryName);
        doForUpdate.setOrigin(skuDeclareEntity.origin);
        if (skuDeclareEntity.price != null) {
            doForUpdate.setPriceRmb(skuDeclareEntity.price.longValue());
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
        doForUpdate.setProviderName(skuDeclareEntity.providerName);

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

// ======================================================
    private BaseResult process(ProductDeclareDO productDeclareDO, SkuDeclareEntity skuDeclareEntity, String operator) throws ServiceException {

        Long productDeclareId = productDeclareDO.getId();
        setOperatorUser(operator);
        try {
            // 更新商品备案主表信息
            updateProductDeclare(productDeclareId, skuDeclareEntity);
            // 更新商品备案附表信息
            if (skuDeclareEntity.portId == PortNid.GUANGZHOU.getValue() && LineType.BONDED.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareGzBonded(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.GUANGZHOU.getValue() && LineType.DIRECTMAIL.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareGzDirectmail(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.HANGZHOU.getValue() && LineType.BONDED.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareHzBonded(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.HANGZHOU.getValue() && LineType.DIRECTMAIL.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareHzDirectmail(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.NINGBO.getValue() && LineType.BONDED.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareNbBonded(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.NINGBO.getValue() && LineType.DIRECTMAIL.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareNbDirectmail(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.JINAN.getValue() && LineType.DIRECTMAIL.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareJnDirectmail(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.XIAMEN.getValue() && LineType.DIRECTMAIL.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareXmDirectmail(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.PINGTAN.getValue() && LineType.DIRECTMAIL.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclarePtDirectmail(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.SHATIAN.getValue() && LineType.DIRECTMAIL.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareStDirectmail(productDeclareId, skuDeclareEntity);
            } else if (skuDeclareEntity.portId == PortNid.CHONGQING.getValue() && LineType.BONDED.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareCqBonded(productDeclareId, skuDeclareEntity);
            }else if (skuDeclareEntity.portId == PortNid.QINGDAO.getValue() && LineType.DIRECTMAIL.getValue()
                    .equals(skuDeclareEntity.declareMode)) {
                updateProductDeclareQdDirectmail(productDeclareId, skuDeclareEntity);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(
                    LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION,
                    "[供应链-备案流程操作失败]: " + LogisticsReturnCode.PRODUCT_DECLARE_INNER_EXCEPTION.getDesc() + " "
                            + "[请求参数: " + productDeclareDO
                            + "]"
            );
        }

        return BaseResult.SUCCESS_RESULT;
    }

    private void updateProductDeclare(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareDO updateProductDeclare = new ProductDeclareDO();
        updateProductDeclare.setId(productDeclareId);
        updateProductDeclare.setDeclareName(skuDeclareEntity.declareName);
        updateProductDeclare.setHsNumber(skuDeclareEntity.hsNumber);
        updateProductDeclare.setProviderAddress(skuDeclareEntity.providerAddress);
        updateProductDeclare.setAttributes(skuDeclareEntity.attributes);
        updateProductDeclare.setNetWeight(skuDeclareEntity.netWeight);
        updateProductDeclare.setGrossWeight(skuDeclareEntity.grossWeight);
        updateProductDeclare.setOrigin(skuDeclareEntity.origin);
        updateProductDeclare.setBrand(skuDeclareEntity.brandName);
        updateProductDeclare.setMeasuringUnit(skuDeclareEntity.measuringUnit);
        updateProductDeclare.setBarCode(skuDeclareEntity.barcode);
        updateProductDeclare.setCategoryName(skuDeclareEntity.categoryName);
        updateProductDeclare.setProviderName(skuDeclareEntity.providerName);
        updateProductDeclare.setRemark(skuDeclareEntity.remark);
        updateProductDeclare.setProductId(skuDeclareEntity.productId);
        updateProductDeclare.setRecordNo(skuDeclareEntity.recordNo);
        updateProductDeclare.setHsCode(skuDeclareEntity.hsCode);
        updateProductDeclare.setTaxRate(skuDeclareEntity.taxRate);
        updateProductDeclare.setConsumptionDutyRate(skuDeclareEntity.consumptionDutyRate);
        updateProductDeclare.setAddedValueTaxRate(skuDeclareEntity.addedValueTaxRate);
        updateProductDeclare.setTariffRate(skuDeclareEntity.tariffRate);
        updateProductDeclare.setPostTaxNo(skuDeclareEntity.postTaxNo);
        updateProductDeclare.setFirstMeasuringUnit(skuDeclareEntity.firstMeasuringUnit);
        updateProductDeclare.setFirstWeight(skuDeclareEntity.firstWeight);
        updateProductDeclare.setSecondMeasuringUnit(skuDeclareEntity.secondaryMeasuringUnit);
        updateProductDeclare.setSecondWeight(skuDeclareEntity.secondWeight);

        updateProductDeclare.setState(SkuDeclareStateType.DECLARE_PASS.getValue());

        // 根据操作类型更新相应的时间
        if ("finish_collect".equals(skuDeclareEntity.operateType)) {
            updateProductDeclare.setFinishCollectTime(new Date());
            updateProductDeclare.setDataCollectUser(getOperatorUser()); //资料收集完毕导入人
        } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
            updateProductDeclare.setFinishTime(new Date());
            updateProductDeclare.setFilingUser(getOperatorUser()); //备案完成导入人
        }

        // 备案价格, 需转换成分
        if (skuDeclareEntity.price != null) {
            BigDecimal tmp = new BigDecimal(skuDeclareEntity.price.toString()).multiply(new BigDecimal("100"));
            updateProductDeclare.setPriceRmb(tmp.longValue());
        }
        productDeclareManager.update(updateProductDeclare);
    }

    private void updateProductDeclareNbBonded(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareNbBondedDO nbBondedDO = productDeclareNbBondedManager
                .getByProductDeclareId(productDeclareId);

        if (nbBondedDO == null) {
            nbBondedDO = new ProductDeclareNbBondedDO();
            nbBondedDO.setProductDeclareId(productDeclareId);
            nbBondedDO.setCustomsDeclarationElement(skuDeclareEntity.customsDeclarationElement);
            nbBondedDO.setNbbsSkuId(skuDeclareEntity.nbbsSkuId);
            nbBondedDO.setCreateUser(getOperatorUser());
            nbBondedDO.setDataCollectUser(getOperatorUser());
            nbBondedDO.setFilingUser(getOperatorUser());
            productDeclareNbBondedManager.insert(nbBondedDO);
        } else {
            ProductDeclareNbBondedDO updateNbBonded = new ProductDeclareNbBondedDO();
            updateNbBonded.setId(nbBondedDO.getId());
            updateNbBonded.setNbbsSkuId(skuDeclareEntity.nbbsSkuId);
            updateNbBonded.setCustomsDeclarationElement(skuDeclareEntity.customsDeclarationElement);
            if ("finish_collect".equals(skuDeclareEntity.operateType)) {
                updateNbBonded.setDataCollectUser(getOperatorUser()); //资料收集完毕导入人
            } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
                updateNbBonded.setFilingUser(getOperatorUser()); //备案完成导入人
            }
            productDeclareNbBondedManager.update(updateNbBonded);
        }
    }

    private void updateProductDeclareHzDirectmail(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareHzDirectmailDO hzDirectmailDO = productDeclareHzDirectmailManager
                .getByProductDeclareId(productDeclareId);

        if (hzDirectmailDO == null) {
            hzDirectmailDO = new ProductDeclareHzDirectmailDO();
            hzDirectmailDO.setProductDeclareId(productDeclareId);
            hzDirectmailDO.setManufacturer(skuDeclareEntity.manufacturer);
            hzDirectmailDO.setCreateUser(getOperatorUser());
            hzDirectmailDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            hzDirectmailDO.setFilingUser(getOperatorUser());
            productDeclareHzDirectmailManager.insert(hzDirectmailDO);
        } else {
            ProductDeclareHzDirectmailDO updateHzDirectmail = new ProductDeclareHzDirectmailDO();
            updateHzDirectmail.setId(hzDirectmailDO.getId());
            updateHzDirectmail.setManufacturer(skuDeclareEntity.manufacturer);
            updateHzDirectmail.setCategoryName(skuDeclareEntity.categoryName);
            updateHzDirectmail.setProviderAddress(skuDeclareEntity.providerAddress);
            updateHzDirectmail.setIsHaveCertificate(skuDeclareEntity.isHaveCertificate);
            updateHzDirectmail.setIsRelateCertificate(skuDeclareEntity.isRelateCertificate);
            updateHzDirectmail.setIngredient(skuDeclareEntity.ingredient);
            updateHzDirectmail.setFeatures(skuDeclareEntity.features);
            if ("finish_collect".equals(skuDeclareEntity.operateType)) {
                updateHzDirectmail.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
                updateHzDirectmail.setFilingUser(getOperatorUser()); //备案完成导入人
            }
            productDeclareHzDirectmailManager.update(updateHzDirectmail);
        }
    }

    private void updateProductDeclareHzBonded(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareHzBondedDO hzBondedDO = productDeclareHzBondedManager
                .getByProductDeclareId(productDeclareId);
        if (hzBondedDO == null) {
            hzBondedDO = buildProductDeclareHzBondedDO(skuDeclareEntity);
            hzBondedDO.setProductDeclareId(productDeclareId);
            hzBondedDO.setCreateUser(getOperatorUser());
            productDeclareHzBondedManager.insert(hzBondedDO);
        } else {
            ProductDeclareHzBondedDO updateHzBonded = buildProductDeclareHzBondedDO(skuDeclareEntity);
            updateHzBonded.setId(hzBondedDO.getId());
            productDeclareHzBondedManager.update(updateHzBonded);
        }
    }

    private ProductDeclareHzBondedDO buildProductDeclareHzBondedDO(SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareHzBondedDO hzBonded = new ProductDeclareHzBondedDO();
        hzBonded.setSpecificationDeclare(skuDeclareEntity.specificationDeclare);
        hzBonded.setUnitPrice(skuDeclareEntity.unitPrice);
        hzBonded.setTotalPrice(skuDeclareEntity.totalPrice);
        hzBonded.setTradeCount(skuDeclareEntity.tradeCount);
        hzBonded.setFeatures(skuDeclareEntity.features);
        hzBonded.setSecondaryMeasuringUnit(skuDeclareEntity.secondaryMeasuringUnit);
        hzBonded.setCurrency(skuDeclareEntity.currency);
        if ("finish_collect".equals(skuDeclareEntity.operateType)) {
            hzBonded.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
        } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
            hzBonded.setFilingUser(getOperatorUser()); //备案完成导入人
        }
        return hzBonded;
    }

    private void updateProductDeclareJnDirectmail(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareJnDirectmailDO jnDirectmailDO = productDeclareJnDirectmailManager
                .getByProductDeclareId(productDeclareId);

        if (jnDirectmailDO == null) {
            jnDirectmailDO = new ProductDeclareJnDirectmailDO();
            jnDirectmailDO.setProductDeclareId(productDeclareId);
            jnDirectmailDO.setManufacturer(skuDeclareEntity.manufacturer);
            jnDirectmailDO.setCreateUser(getOperatorUser());
            jnDirectmailDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            jnDirectmailDO.setFilingUser(getOperatorUser());
            productDeclareJnDirectmailManager.insert(jnDirectmailDO);
        } else {
            ProductDeclareJnDirectmailDO updateJnDirectmail = new ProductDeclareJnDirectmailDO();
            updateJnDirectmail.setId(jnDirectmailDO.getId());
            updateJnDirectmail.setManufacturer(skuDeclareEntity.manufacturer);
            updateJnDirectmail.setCategoryName(skuDeclareEntity.categoryName);
            updateJnDirectmail.setProviderAddress(skuDeclareEntity.providerAddress);
            updateJnDirectmail.setIsHaveCertificate(skuDeclareEntity.isHaveCertificate);
            updateJnDirectmail.setIsRelateCertificate(skuDeclareEntity.isRelateCertificate);
            updateJnDirectmail.setIngredient(skuDeclareEntity.ingredient);
            updateJnDirectmail.setFeatures(skuDeclareEntity.features);
            if ("finish_collect".equals(skuDeclareEntity.operateType)) {
                updateJnDirectmail.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
                updateJnDirectmail.setFilingUser(getOperatorUser()); //备案完成导入人
            }
            productDeclareJnDirectmailManager.update(updateJnDirectmail);
        }
    }

    private void updateProductDeclareCqBonded(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareCqBondedDO cqBondedDO = productDeclareCqBondedManager
                .getByProductDeclareId(productDeclareId);

        if (cqBondedDO == null) {
            cqBondedDO = new ProductDeclareCqBondedDO();
            cqBondedDO.setProductDeclareId(productDeclareId);
            cqBondedDO.setCreateUser(getOperatorUser());
            cqBondedDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            cqBondedDO.setFilingUser(getOperatorUser());
            productDeclareCqBondedManager.insert(cqBondedDO);
        } else {
            ProductDeclareCqBondedDO updateCqBonded = new ProductDeclareCqBondedDO();
            updateCqBonded.setId(cqBondedDO.getId());
            updateCqBonded.setCustomsDeclarationElement(skuDeclareEntity.customsDeclarationElement);
            updateCqBonded.setHsCode(skuDeclareEntity.hsCode);
            if ("finish_collect".equals(skuDeclareEntity.operateType)) {
                updateCqBonded.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
                updateCqBonded.setFilingUser(getOperatorUser()); //备案完成导入人
            }
            productDeclareCqBondedManager.update(updateCqBonded);
        }
    }

    private void updateProductDeclareXmDirectmail(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareXmDirectmailDO xmDirectmailDO = productDeclareXmDirectmailManager.getByProductDeclareId(productDeclareId);

        if (xmDirectmailDO == null) {
            xmDirectmailDO = new ProductDeclareXmDirectmailDO();
            xmDirectmailDO.setProductDeclareId(productDeclareId);
            xmDirectmailDO.setManufacturer(skuDeclareEntity.manufacturer);
            xmDirectmailDO.setCreateUser(getOperatorUser());
            xmDirectmailDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            xmDirectmailDO.setFilingUser(getOperatorUser());
            productDeclareXmDirectmailManager.insert(xmDirectmailDO);
        } else {
            ProductDeclareXmDirectmailDO updateXmDirectmail = new ProductDeclareXmDirectmailDO();
            updateXmDirectmail.setId(xmDirectmailDO.getId());
            updateXmDirectmail.setManufacturer(skuDeclareEntity.manufacturer);
            updateXmDirectmail.setCategoryName(skuDeclareEntity.categoryName);
            updateXmDirectmail.setProviderAddress(skuDeclareEntity.providerAddress);
            updateXmDirectmail.setIsHaveCertificate(skuDeclareEntity.isHaveCertificate);
            updateXmDirectmail.setIsRelateCertificate(skuDeclareEntity.isRelateCertificate);
            updateXmDirectmail.setIngredient(skuDeclareEntity.ingredient);
            updateXmDirectmail.setFeatures(skuDeclareEntity.features);
            if ("finish_collect".equals(skuDeclareEntity.operateType)) {
                updateXmDirectmail.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
                updateXmDirectmail.setFilingUser(getOperatorUser()); //备案完成导入人
            }
            productDeclareXmDirectmailManager.update(updateXmDirectmail);
        }
    }

    private void updateProductDeclarePtDirectmail(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclarePtDirectmailDO ptDirectmailDO = productDeclarePtDirectmailManager
                .getByProductDeclareId(productDeclareId);

        if (ptDirectmailDO == null) {
            ptDirectmailDO = new ProductDeclarePtDirectmailDO();
            ptDirectmailDO.setProductDeclareId(productDeclareId);
            ptDirectmailDO.setManufacturer(skuDeclareEntity.manufacturer);
            ptDirectmailDO.setCreateUser(getOperatorUser());
            ptDirectmailDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            ptDirectmailDO.setFilingUser(getOperatorUser());
            ptDirectmailDO.setProductCode(skuDeclareEntity.productCode);
            ptDirectmailDO.setCustomsDeclarationElement(skuDeclareEntity.customsDeclarationElement);
            productDeclarePtDirectmailManager.insert(ptDirectmailDO);
        } else {
            ProductDeclarePtDirectmailDO updatePtDirectmail = new ProductDeclarePtDirectmailDO();
            updatePtDirectmail.setId(ptDirectmailDO.getId());
            updatePtDirectmail.setManufacturer(skuDeclareEntity.manufacturer);
            updatePtDirectmail.setCategoryName(skuDeclareEntity.categoryName);
            updatePtDirectmail.setProviderAddress(skuDeclareEntity.providerAddress);
            updatePtDirectmail.setIsHaveCertificate(skuDeclareEntity.isHaveCertificate);
            updatePtDirectmail.setIsRelateCertificate(skuDeclareEntity.isRelateCertificate);
            updatePtDirectmail.setIngredient(skuDeclareEntity.ingredient);
            updatePtDirectmail.setFeatures(skuDeclareEntity.features);
            updatePtDirectmail.setProductCode(skuDeclareEntity.productCode);
            updatePtDirectmail.setCustomsDeclarationElement(skuDeclareEntity.customsDeclarationElement);
            if ("finish_collect".equals(skuDeclareEntity.operateType)) {
                updatePtDirectmail.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
                updatePtDirectmail.setFilingUser(getOperatorUser()); //备案完成导入人
            }
            productDeclarePtDirectmailManager.update(updatePtDirectmail);
        }
    }

    private void updateProductDeclareStDirectmail(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareStDirectmailDO stDirectmailDO = productDeclareStDirectmailManager
                .getByProductDeclareId(productDeclareId);

        if (stDirectmailDO == null) {
            stDirectmailDO = new ProductDeclareStDirectmailDO();
            stDirectmailDO.setProductDeclareId(productDeclareId);
            stDirectmailDO.setManufacturer(skuDeclareEntity.manufacturer);
            stDirectmailDO.setCreateUser(getOperatorUser());
            stDirectmailDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            stDirectmailDO.setFilingUser(getOperatorUser());
            stDirectmailDO.setProductCode(skuDeclareEntity.productCode);
            stDirectmailDO.setCustomsDeclarationElement(skuDeclareEntity.customsDeclarationElement);
            productDeclareStDirectmailManager.insert(stDirectmailDO);
        } else {
            ProductDeclareStDirectmailDO updateStDirectmail = new ProductDeclareStDirectmailDO();
            updateStDirectmail.setId(stDirectmailDO.getId());
            updateStDirectmail.setManufacturer(skuDeclareEntity.manufacturer);
            updateStDirectmail.setCategoryName(skuDeclareEntity.categoryName);
            updateStDirectmail.setProviderAddress(skuDeclareEntity.providerAddress);
            updateStDirectmail.setIsHaveCertificate(skuDeclareEntity.isHaveCertificate);
            updateStDirectmail.setIsRelateCertificate(skuDeclareEntity.isRelateCertificate);
            updateStDirectmail.setIngredient(skuDeclareEntity.ingredient);
            updateStDirectmail.setFeatures(skuDeclareEntity.features);
            updateStDirectmail.setProductCode(skuDeclareEntity.productCode);
            updateStDirectmail.setCustomsDeclarationElement(skuDeclareEntity.customsDeclarationElement);
            if ("finish_collect".equals(skuDeclareEntity.operateType)) {
                updateStDirectmail.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
            } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
                updateStDirectmail.setFilingUser(getOperatorUser()); //备案完成导入人
            }
            productDeclareStDirectmailManager.update(updateStDirectmail);
        }
    }

    private void updateProductDeclareGzDirectmail(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareGzDirectmailDO gzDirectmailDO = productDeclareGzDirectmailManager
                .getByProductDeclareId(productDeclareId);

        if (gzDirectmailDO == null) {
            gzDirectmailDO = buildProductDeclareGzDirectmailDO(skuDeclareEntity);
            gzDirectmailDO.setProductDeclareId(productDeclareId);
            gzDirectmailDO.setCreateUser(getOperatorUser());
            productDeclareGzDirectmailManager.insert(gzDirectmailDO);
        } else {
            ProductDeclareGzDirectmailDO updateGzDirectmail = buildProductDeclareGzDirectmailDO(skuDeclareEntity);
            updateGzDirectmail.setId(gzDirectmailDO.getId());
            productDeclareGzDirectmailManager.update(updateGzDirectmail);
        }
    }

    private void updateProductDeclareQdDirectmail(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareQdDirectmailDO qdDirectmailDO = productDeclareQdDirectmailManager
                .getByProductDeclareId(productDeclareId);

        if (qdDirectmailDO == null) {
            qdDirectmailDO = buildProductDeclareQdDirectmailDO(skuDeclareEntity);
            qdDirectmailDO.setProductDeclareId(productDeclareId);
            qdDirectmailDO.setCreateUser(getOperatorUser());
            productDeclareQdDirectmailManager.insert(qdDirectmailDO);
        } else {
            ProductDeclareQdDirectmailDO updateQdDirectmail = buildProductDeclareQdDirectmailDO(skuDeclareEntity);
            updateQdDirectmail.setId(qdDirectmailDO.getId());
            productDeclareQdDirectmailManager.update(updateQdDirectmail);
        }
    }

    private ProductDeclareGzDirectmailDO buildProductDeclareGzDirectmailDO(SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareGzDirectmailDO gzDirectmailDO = new ProductDeclareGzDirectmailDO();
        gzDirectmailDO.setThirdSkuId(skuDeclareEntity.thirdSkuId);
        gzDirectmailDO.setManufacturer(skuDeclareEntity.manufacturer);
        gzDirectmailDO.setSkuQuality(skuDeclareEntity.skuQuality);
        gzDirectmailDO.setSecondaryMeasuringUnit(skuDeclareEntity.secondaryMeasuringUnit);
        if ("finish_collect".equals(skuDeclareEntity.operateType)) {
            gzDirectmailDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
        } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
            gzDirectmailDO.setFilingUser(getOperatorUser()); //备案完成导入人
        }
        return gzDirectmailDO;
    }

    private ProductDeclareQdDirectmailDO buildProductDeclareQdDirectmailDO(SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareQdDirectmailDO qdDirectmailDO = new ProductDeclareQdDirectmailDO();
        qdDirectmailDO.setThirdSkuId(skuDeclareEntity.thirdSkuId);
        qdDirectmailDO.setManufacturer(skuDeclareEntity.manufacturer);
        qdDirectmailDO.setSkuQuality(skuDeclareEntity.skuQuality);
        qdDirectmailDO.setSecondaryMeasuringUnit(skuDeclareEntity.secondaryMeasuringUnit);
        if ("finish_collect".equals(skuDeclareEntity.operateType)) {
            qdDirectmailDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
        } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
            qdDirectmailDO.setFilingUser(getOperatorUser()); //备案完成导入人
        }
        return qdDirectmailDO;
    }

    private void updateProductDeclareGzBonded(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareGzBondedDO gzBondedDO = productDeclareGzBondedManager
                .getByProductDeclareId(productDeclareId);

        if (gzBondedDO == null) {
            // 如果没有则初始化一条数据
            gzBondedDO = buildProductDeclareGzBondedDO(skuDeclareEntity);
            gzBondedDO.setProductDeclareId(productDeclareId);
            gzBondedDO.setCreateUser(getOperatorUser());
            productDeclareGzBondedManager.insert(gzBondedDO);
        } else {
            ProductDeclareGzBondedDO updateGzBonded = buildProductDeclareGzBondedDO(skuDeclareEntity);
            updateGzBonded.setId(gzBondedDO.getId());
            productDeclareGzBondedManager.update(updateGzBonded);
        }
    }

    private ProductDeclareGzBondedDO buildProductDeclareGzBondedDO(SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareGzBondedDO gzBondedDO = new ProductDeclareGzBondedDO();
        gzBondedDO.setThirdSkuId(skuDeclareEntity.thirdSkuId);
        gzBondedDO.setCustomsDeclarationElement(skuDeclareEntity.customsDeclarationElement);
        gzBondedDO.setManufacturer(skuDeclareEntity.manufacturer);
        gzBondedDO.setImportUnitPriceRmb(skuDeclareEntity.importUnitPriceRmb);
        gzBondedDO.setRetailPriceRmb(skuDeclareEntity.retailPriceRmb);
        gzBondedDO.setSalesChannel(skuDeclareEntity.salesChannel);
        if ("finish_collect".equals(skuDeclareEntity.operateType)) {
            gzBondedDO.setDataCollectUser(getOperatorUser());//资料收集完毕导入人
        } else if ("declare_pass".equals(skuDeclareEntity.operateType)) {
            gzBondedDO.setFilingUser(getOperatorUser()); //备案完成导入人
        }

        if (skuDeclareEntity.price != null) {
            BigDecimal tmp = new BigDecimal(skuDeclareEntity.price.toString()).multiply(new BigDecimal("100"));
            gzBondedDO.setDeclarePriceRmb(tmp);
        }
        return gzBondedDO;
    }

    private void updateProductDeclareNbDirectmail(Long productDeclareId, SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareNbDirectmailDO nbDirectmailDO = productDeclareNbDirectmailManager.getByProductDeclareId(productDeclareId);

        if (nbDirectmailDO == null) {
            // 如果没有则初始化一条数据
            nbDirectmailDO = buildProductDeclareNbDirectmailDO(skuDeclareEntity);
            nbDirectmailDO.setProductDeclareId(productDeclareId);
            productDeclareNbDirectmailManager.insert(nbDirectmailDO);
        } else {
            ProductDeclareNbDirectmailDO updateNbDirectmail = buildProductDeclareNbDirectmailDO(skuDeclareEntity);
            updateNbDirectmail.setId(nbDirectmailDO.getId());
            productDeclareNbDirectmailManager.update(updateNbDirectmail);
        }

    }

    private ProductDeclareNbDirectmailDO buildProductDeclareNbDirectmailDO(SkuDeclareEntity skuDeclareEntity) {
        ProductDeclareNbDirectmailDO nbDirectmailDO = new ProductDeclareNbDirectmailDO();
        nbDirectmailDO.setWarehouseEnterpriseCode(skuDeclareEntity.getWarehouseEnterpriseCode());
        nbDirectmailDO.setThirdSkuId(skuDeclareEntity.getThirdSkuId());
        nbDirectmailDO.setPurpose(skuDeclareEntity.getPurpose());
        nbDirectmailDO.setIngredient(skuDeclareEntity.getIngredient());
        nbDirectmailDO.setHsNumber(skuDeclareEntity.getHsNumber());
        nbDirectmailDO.setFeatures(skuDeclareEntity.getFeatures());
        nbDirectmailDO.setDescription(skuDeclareEntity.getDescription());
        nbDirectmailDO.setImgUrl(skuDeclareEntity.getImgUrl());
        return nbDirectmailDO;
    }

    private ThreadLocal<String> operatorUser = new ThreadLocal<String>();

    private String getOperatorUser() {
        return this.operatorUser.get();
    }

    private void setOperatorUser(String operatorUser) {
        this.operatorUser.set(operatorUser);
    }
}