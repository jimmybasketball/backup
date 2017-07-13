package com.sfebiz.supplychain.service.sku;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.sku.api.SkuMerchantService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuMerchantEntity;

/**
 * 货主商品服务
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-13 11:06
 **/
public class SkuMerchantServiceImpl implements SkuMerchantService {

    @Override
    public CommonRet<Long> createSkuMerchant(String operator, SkuMerchantEntity skuMerchantEntity) {
        return null;
    }

    @Override
    public CommonRet<Void> updateSkuMerchant(String operator, SkuMerchantEntity skuMerchantEntity) {
        return null;
    }

    @Override
    public CommonRet<SkuMerchantEntity> selectSkuMerchantById(Long id) {
        return null;
    }
}