package com.sfebiz.supplychain.exposed.sku.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.sku.entity.SkuMerchantEntity;

/**
 * 货主商品服务
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 17:49
 **/
public interface SkuMerchantService {

    /**
     * 创建货主商品
     *
     * @param operator 操作人
     * @param skuMerchantEntity 货主商品实体
     * @return 货主商品id
     */
    public CommonRet<Long> createSkuMerchant(String operator, SkuMerchantEntity skuMerchantEntity);

    /**
     * 修改货主商品
     *
     * @param operator 操作人
     * @param skuMerchantEntity 货主商品实体
     * @return
     */
    public CommonRet<Void> updateSkuMerchant(String operator, SkuMerchantEntity skuMerchantEntity);

    /**
     * 根据获货主商品id取货主商品信息
     *
     * @param id 货主商品id
     * @return 货主商品信息
     */
    public CommonRet<SkuMerchantEntity> selectSkuMerchantById(Long id);

}