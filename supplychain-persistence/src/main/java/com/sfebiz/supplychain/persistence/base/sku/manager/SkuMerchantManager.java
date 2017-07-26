package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.SkuMerchantDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuMerchantDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 货主商品Manager
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 14:50
 **/
@Component("skuMerchantManager")
public class SkuMerchantManager extends BaseManager<SkuMerchantDO> {

    @Resource
    private SkuMerchantDao skuMerchantDao;

    @Override
    public BaseDao<SkuMerchantDO> getDao() {
        return skuMerchantDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("D:/development/IDEA/ifunq-supplychain/haitao-b2b-supplychain/" +
                        "supplychain-persistence/src/main/resources/base/sqlmap/sku/sc_sku_merchant_sqlmap.xml",
                SkuMerchantDao.class,
                SkuMerchantDO.class,
                "sc_sku_merchant");
    }
}