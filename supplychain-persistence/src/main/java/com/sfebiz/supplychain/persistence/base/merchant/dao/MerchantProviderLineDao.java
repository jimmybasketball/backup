package com.sfebiz.supplychain.persistence.base.merchant.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderLineDO;

import java.util.List;
import java.util.Map;

/**
 * 供应商线路Dao
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/7/7 18:09
 */
public interface MerchantProviderLineDao extends BaseDao<MerchantProviderLineDO>{
    public List<MerchantProviderLineDO> getByProviderAndWarehouse(Map<String, Long> paramMap);
}
