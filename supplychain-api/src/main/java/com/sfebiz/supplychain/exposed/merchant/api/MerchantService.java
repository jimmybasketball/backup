package com.sfebiz.supplychain.exposed.merchant.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.merchant.entity.MerchantEntity;

/**
 * 物流平台商户服务
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/7/5 10:04
 */
public interface MerchantService{

    /**
     * 创建商户
     * @param operator          操作人
     * @param merchantEntity    商户实体
     * @return                  商户id
     */
    public CommonRet<Long> createMerchant(String operator, MerchantEntity merchantEntity);

    /**
     * 修改商户信息
     * @param operator          操作人
     * @param merchantId        商户id
     * @param merchantEntity    商户实体
     * @return                  void
     */
    public CommonRet<Void> updateMerchantEntity(String operator, Long merchantId, MerchantEntity merchantEntity);



}
