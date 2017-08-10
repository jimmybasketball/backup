package com.sfebiz.supplychain.persistence.base.merchant.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderDO;

/**
 *
 * 商户供应商Dao
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/7/6 19:29
 */
public interface MerchantProviderDao extends BaseDao<MerchantProviderDO>{
    
    /**
     * @param merchantProviderId
     * @return
     */
    String queryMerchantProviderIdByNationCode(Long merchantProviderId);
}
