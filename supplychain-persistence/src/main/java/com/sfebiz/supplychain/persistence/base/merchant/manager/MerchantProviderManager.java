package com.sfebiz.supplychain.persistence.base.merchant.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.merchant.enums.MerchantProviderStateType;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantProviderDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantProviderDO;

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


    /**
     * 检查货主是否配置了供应商
     * @param merchantId  货主ID
     * @return
     */
    public boolean isMerchantSetProvider(Long merchantId) {

        if (merchantId == null) {
            return false;
        }

        MerchantProviderDO queryDO = new MerchantProviderDO();
        queryDO.setMerchantId(merchantId);
        queryDO.setState(MerchantProviderStateType.ENABLE.value);

        BaseQuery<MerchantProviderDO> baseQuery = new BaseQuery<MerchantProviderDO>(queryDO);

        return merchantProviderDao.count(baseQuery) > 0;
    }
    
    /**
     * 货主供应商id查询所属国家code
     * @param merchantProviderId
     * @return
     */
    public String queryMerchantProviderIdByNationCode(Long merchantProviderId){
        return merchantProviderDao.queryMerchantProviderIdByNationCode(merchantProviderId);
    }
}
