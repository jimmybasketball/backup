package com.sfebiz.supplychain.service.sku;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.exposed.sku.entity.SkuEntity;

import java.util.List;

/**
 * 基本商品服务
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-13 11:07
 **/
public class SkuServiceImpl implements SkuService {

    @Override
    public CommonRet<Long> createSku(String operator, SkuEntity skuEntity) {
        return null;
    }

    @Override
    public CommonRet<Void> updateSku(String operator, SkuEntity skuEntity) {
        return null;
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