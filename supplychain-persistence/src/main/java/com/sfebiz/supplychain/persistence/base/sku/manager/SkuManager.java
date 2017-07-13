package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.SkuDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 基本商品Manager
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 14:50
 **/
@Component("skuManager")
public class SkuManager extends BaseManager<SkuDO> {

    @Resource
    private SkuDao skuDao;

    @Override
    public BaseDao<SkuDO> getDao() {
        return skuDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("D:/development/IDEA/ifunq-supplychain/haitao-b2b-supplychain/" +
                        "supplychain-persistence/src/main/resources/base/sqlmap/sku/sc_sku_sqlmap.xml",
                SkuDao.class,
                SkuDO.class,
                "sc_sku");
    }
}
