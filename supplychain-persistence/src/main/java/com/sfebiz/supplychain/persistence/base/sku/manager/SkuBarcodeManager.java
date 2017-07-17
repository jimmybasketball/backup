package com.sfebiz.supplychain.persistence.base.sku.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.SkuBarcodeDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuBarcodeDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 基本商品条码Manager
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 14:50
 **/
@Component("skuBarcodeManager")
public class SkuBarcodeManager extends BaseManager<SkuBarcodeDO> {

    @Resource
    private SkuBarcodeDao skuBarcodeDao;

    @Override
    public BaseDao<SkuBarcodeDO> getDao() {
        return skuBarcodeDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("D:/development/IDEA/ifunq-supplychain/haitao-b2b-supplychain/" +
                        "supplychain-persistence/src/main/resources/base/sqlmap/sku/sc_sku_barcode_sqlmap.xml",
                SkuBarcodeDao.class,
                SkuBarcodeDO.class,
                "sc_sku_barcode");
    }
}
