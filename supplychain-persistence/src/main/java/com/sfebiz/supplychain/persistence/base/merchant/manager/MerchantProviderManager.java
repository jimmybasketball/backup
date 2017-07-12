package com.sfebiz.supplychain.persistence.base.merchant.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantProviderDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 货主供应商provider
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-06 19:32
 **/
@Component("merchantProviderManager")
public class MerchantProviderManager extends BaseManager<MerchantProviderDO>{

    @Resource
    private MerchantProviderDao merchantProviderDao;

    @Override
    public BaseDao<MerchantProviderDO> getDao() {
        return merchantProviderDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                        "supplychain/supplychain-persistence/" +
                        "src/main/resources/base/sqlmap/merchant/sc_merchant_provider_sqlmap.xml",
                MerchantProviderDao.class,
                MerchantProviderDO.class,
                "sc_merchant_provider");
    }
}
