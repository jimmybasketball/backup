package com.sfebiz.supplychain.service.sku;

import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.sku.api.SkuMerchantService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuBarcodeEntity;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.exposed.sku.entity.SkuMerchantEntity;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuBarcodeDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuMerchantDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuBarcodeManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuMerchantManager;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 货主商品服务
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-13 11:06
 **/
@Service("skuMerchantService")
public class SkuMerchantServiceImpl implements SkuMerchantService {

    @Resource
    SkuMerchantManager skuMerchantManager;

    @Resource
    SkuManager skuManager;

    @Resource
    SkuBarcodeManager skuBarcodeManager;

    @Override
    public CommonRet<Long> createSkuMerchant(String operator, SkuMerchantEntity skuMerchantEntity) {
        CommonRet<Long> commonRet = new CommonRet<Long>();
        try {
            // skuId不存在的情况
            if (skuMerchantEntity.getSkuId() == null || skuMerchantEntity.getSkuId() == 0) {
                SkuDO skuDO = new SkuDO();
                BeanCopier beanCopier = BeanCopier.create(SkuEntity.class, SkuDO.class, false);
                beanCopier.copy(skuMerchantEntity.getSkuEntity(), skuDO, null);
                skuDO.setCreateBy(operator);
                skuDO.setModifiedBy(operator);
                skuManager.insert(skuDO);

                for (SkuBarcodeEntity barcodeEntity : skuMerchantEntity.getSkuEntity().getSkuBarcodeList()) {
                    SkuBarcodeDO barcodeDO = new SkuBarcodeDO();
                    beanCopier = BeanCopier.create(SkuBarcodeEntity.class, SkuBarcodeDO.class, false);
                    beanCopier.copy(barcodeEntity, barcodeDO, null);
                    barcodeDO.setSkuId(skuDO.getId());
                    skuBarcodeManager.insert(barcodeDO);
                }
                SkuMerchantDO skuMerchantDO = new SkuMerchantDO();
                beanCopier = BeanCopier.create(SkuMerchantEntity.class, SkuMerchantDO.class, false);
                beanCopier.copy(skuMerchantEntity, skuMerchantDO, null);
                skuMerchantDO.setSkuId(skuDO.getId());
                skuMerchantDO.setCreateBy(operator);
                skuMerchantDO.setModifiedBy(operator);
                skuMerchantManager.insert(skuMerchantDO);
                commonRet.setResult(skuMerchantDO.getId());
            // skuId存在的情况
            } else {
                SkuMerchantDO skuMerchantDO = new SkuMerchantDO();
                BeanCopier beanCopier = BeanCopier.create(SkuMerchantEntity.class, SkuMerchantDO.class, false);
                beanCopier.copy(skuMerchantEntity, skuMerchantDO, null);
                skuMerchantDO.setCreateBy(operator);
                skuMerchantDO.setModifiedBy(operator);
                skuMerchantManager.insert(skuMerchantDO);
                commonRet.setResult(skuMerchantDO.getId());
            }

        } catch (Exception e) {
            commonRet.reSet();
            commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg(e.getMessage());
        }
        return commonRet;
    }

    @Override
    public CommonRet<Void> updateSkuMerchant(String operator, SkuMerchantEntity skuMerchantEntity) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
        try {
            SkuMerchantDO skuMerchantDO = new SkuMerchantDO();
            BeanCopier beanCopier = BeanCopier.create(SkuMerchantEntity.class, SkuMerchantDO.class, false);
            beanCopier.copy(skuMerchantEntity, skuMerchantDO, null);
            skuMerchantDO.setModifiedBy(operator);
            skuMerchantManager.update(skuMerchantDO);
        } catch (Exception e) {
            commonRet.reSet();
            commonRet.setRetCode(SCReturnCode.COMMON_FAIL.getCode());
            commonRet.setRetMsg(e.getMessage());
        }
        return commonRet;
    }

    @Override
    public CommonRet<SkuMerchantEntity> selectSkuMerchantById(Long id) {
        return null;
    }
}