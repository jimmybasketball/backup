package com.sfebiz.supplychain.service.sku;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.UpdateByQuery;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuBarcodeEntity;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuBarcodeDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuBarcodeManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuManager;
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

    @Resource
    SkuManager skuManager;

    @Resource
    SkuBarcodeManager skuBarcodeManager;


    @Override
    public CommonRet<Long> createSku(String operator, SkuEntity skuEntity) {
        return null;
    }

    @Override
    public CommonRet<Void> updateSku(String operator, SkuEntity skuEntity) {
        CommonRet<Void> commonRet = new CommonRet<Void>();
            try {
            SkuDO skuDO = new SkuDO();
            BeanCopier beanCopier = BeanCopier.create(SkuEntity.class, SkuDO.class, false);
            beanCopier.copy(skuEntity, skuDO, null);
            skuDO.setModifiedBy(operator);
            skuManager.update(skuDO);

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

}