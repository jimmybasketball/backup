package com.sfebiz.supplychain.service.sku;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.UpdateByQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuBarcodeEntity;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.sku.enums.SkuDeclareStateType;
import com.sfebiz.supplychain.persistence.base.sku.domain.ProductDeclareDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuBarcodeDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuBarcodeManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuManager;
import com.taobao.tbbpm.common.persistence.ITransactionCallbackWithoutResult;
import com.taobao.tbbpm.common.persistence.ITransactionStatus;
import com.taobao.tbbpm.common.persistence.SpringTransactionTemplate;
import net.pocrd.entity.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 基本商品服务
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-13 11:07
 **/
@Service("skuService")
public class SkuServiceImpl implements SkuService {

    private final static ModelMapper modelMapper = new ModelMapper();
    private final static Logger logger = LoggerFactory.getLogger(SkuServiceImpl.class);

    @Resource
    SkuManager skuManager;

    @Resource
    SkuBarcodeManager skuBarcodeManager;

    @Resource
    private SpringTransactionTemplate springTransactionTemplate;

    @Resource
    ProductDeclareManager productDeclareManager;


    @Override
    public CommonRet<Long> createSku(String operator, SkuEntity skuEntity) {
        return null;
    }

    @Override
    public CommonRet<Void> updateSku(final String operator, final SkuEntity skuEntity) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        try {
            springTransactionTemplate.execute(new ITransactionCallbackWithoutResult() {
                @Override
                public void doInTransaction(ITransactionStatus status) throws Exception {
                    SkuDO skuDO = new SkuDO();
                    BeanCopier beanCopier = BeanCopier.create(SkuEntity.class, SkuDO.class, false);
                    beanCopier.copy(skuEntity, skuDO, null);
                    skuDO.setModifiedBy(operator);
                    skuManager.update(skuDO);

//                    SkuBarcodeDO skuBarcodeDO = new SkuBarcodeDO();
//                    skuBarcodeDO.setSkuId(skuEntity.getId());
//                    BaseQuery<SkuBarcodeDO> baseQuery = BaseQuery.getInstance(skuBarcodeDO);
//                    skuBarcodeManager.delete(baseQuery);
//
//                    for (SkuBarcodeEntity barcodeEntity : skuEntity.getSkuBarcodeList()) {
//                        skuBarcodeDO = new SkuBarcodeDO();
//                        skuBarcodeDO.setSkuId(skuEntity.getId());
//                        skuBarcodeDO.setBarcode(barcodeEntity.getBarcode());
//                        skuBarcodeManager.insertOrUpdate(skuBarcodeDO);
//                    }

                    SkuBarcodeDO skuBarcodeDO = new SkuBarcodeDO();
                    skuBarcodeDO.setSkuId(skuEntity.getId());
                    BaseQuery<SkuBarcodeDO> baseQuery = BaseQuery.getInstance(skuBarcodeDO);
                    skuBarcodeManager.delete(baseQuery);

                    for (SkuBarcodeEntity barcodeEntity : skuEntity.getSkuBarcodeList()) {
                        skuBarcodeDO.setSkuId(skuEntity.getId());
                        skuBarcodeDO.setBarcode(barcodeEntity.getBarcode());
                        baseQuery = BaseQuery.getInstance(skuBarcodeDO);
                        SkuBarcodeDO delBarcodeDO = new SkuBarcodeDO();
                        delBarcodeDO.setIsDelete(0);
                        UpdateByQuery<SkuBarcodeDO> updateQuery = new UpdateByQuery<SkuBarcodeDO>( baseQuery, delBarcodeDO);
                        skuBarcodeManager.updateByQuery(updateQuery);
                        if(skuBarcodeManager.count(baseQuery) > 0) {
                        } else {
                            SkuBarcodeDO barcodeDO = new SkuBarcodeDO();
                            beanCopier = BeanCopier.create(SkuBarcodeEntity.class, SkuBarcodeDO.class, false);
                            beanCopier.copy(barcodeEntity, barcodeDO, null);
                            skuBarcodeManager.insert(barcodeDO);
                        }
                    }
                }
            });
        } catch (Exception e) {
                commonRet.reSet();
                commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
                commonRet.setRetMsg(e.getMessage());
        }
        return commonRet;
    }

    @Override
    public CommonRet<SkuEntity> selectSkuByBarcode(String barcode) {
        return null;
    }

    @Override
    public CommonRet<SkuEntity> selectSkuBySkuId(Long skuId) {
        return null;
    }

    @Override
    public CommonRet<Long> selectSkuIdByBarcode(String barcode) {
        return null;
    }

    @Override
    public CommonRet<List<SkuEntity>> selectBarcodeBySkuId(Long skuId) {
        return null;
    }

    @Override
    public SkuEntity getSkuOnlySkuInfo(long id) throws ServiceException {
        if (id == 0) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setErrorMsg(LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL.getDesc())
                    .setParms(id)
                    .log();
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL,
                    LogisticsReturnCode.SKU_SERVICE_PARAMS_ILLEGAL.getDesc());
        }
        try {
            SkuEntity skuEntity = null;
            SkuDO skuDO = skuManager.getById(id);
            if (null != skuDO) {
                skuEntity = modelMapper.map(skuDO, SkuEntity.class);
                if (StringUtils.isNotBlank(skuDO.getAttributesDesc())) {
                    skuEntity.setAttributesDesc(skuDO.getAttributesDesc());
                } else {
                    skuEntity.setAttributesDesc(null);
                }

                LogBetter.instance(logger)
                        .addParm("skuEntity",skuEntity)
                        .addParm("skuDO",skuDO).log();
                ProductDeclareDO queryDO = new ProductDeclareDO();
                queryDO.setState(SkuDeclareStateType.DECLARE_PASS.getValue());
                queryDO.setSkuId(id);
                BaseQuery<ProductDeclareDO> baseQuery = BaseQuery.getInstance(queryDO);
                List<ProductDeclareDO> productDeclareDOList = productDeclareManager.query(baseQuery);
                if (CollectionUtils.isNotEmpty(productDeclareDOList)) {
                    ProductDeclareDO productDeclareDO = productDeclareDOList.get(0);
                    // skuEntity.customsCategoryNid = productDeclareDO.getPostTaxNo();
                    skuEntity.measuringUnit = productDeclareDO.getMeasuringUnit();
                }
            }
            return skuEntity;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setErrorMsg(LogisticsReturnCode.SKU_SERVICE_INNER_EXCEPTION.getDesc())
                    .setParms(id)
                    .setException(e)
                    .log();
            throw new ServiceException(LogisticsReturnCode.SKU_SERVICE_INNER_EXCEPTION,
                    LogisticsReturnCode.SKU_SERVICE_INNER_EXCEPTION.getDesc());
        }
    }

}