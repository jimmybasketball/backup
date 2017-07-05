package com.sfebiz.supplychain.persistence.base.merchant.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.merchant.dao.MerchantDao;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 物流平台商户Manager
 *
 * @author liujc
 * @create 2017-07-04 18:23
 **/
@Component("merchantManager")
public class MerchantManager extends BaseManager<MerchantDO> {

    @Resource
    private MerchantDao merchantDao;

    @Override
    public BaseDao<MerchantDO> getDao() {
        return merchantDao;
    }


    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("/Users/liujunchi/git_projects/" +
                        "supplychain/supplychain-persistence/" +
                        "src/main/resources/base/sqlmap/merchant/sc_merchant.xml",
                MerchantDao.class,
                MerchantDO.class,
                "sc_merchant");
    }

}
