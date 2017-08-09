package com.sfebiz.supplychain.persistence.base.sku.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.sku.dao.SkuBarcodeDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuBarcodeDO;

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

    public SkuBarcodeDO getSkuBySkuIdAndBarcode(Long skuId, String barCode) {
        SkuBarcodeDO skuBarcodeDO = new SkuBarcodeDO();
        skuBarcodeDO.setSkuId(skuId);
        skuBarcodeDO.setBarcode(barCode);
        List<SkuBarcodeDO> skuBarcodeDOList = query(BaseQuery.getInstance(skuBarcodeDO));
        if (null == skuBarcodeDOList || 0 == skuBarcodeDOList.size()) {
             return null;
        } else {
            return skuBarcodeDOList.get(0);
        }
    }

    public List<SkuBarcodeDO> getSkuBySkuId(Long skuId) {
        SkuBarcodeDO skuBarcodeDO = new SkuBarcodeDO();
        skuBarcodeDO.setSkuId(skuId);
        List<SkuBarcodeDO> skuBarcodeDOList = query(BaseQuery.getInstance(skuBarcodeDO));
        if (null == skuBarcodeDOList || 0 == skuBarcodeDOList.size()) {
            return null;
        } else {
            return skuBarcodeDOList;
        }
    }
    
    public Map<String, Long> getBarcode4SkuIdMapByBarcodeList(List<String> barcodeList) {
        Map<String, Long> barcode4SkuIdMap = new HashMap<String, Long>();

        SkuBarcodeDO queryDO = new SkuBarcodeDO();
        BaseQuery<SkuBarcodeDO> query = BaseQuery.getInstance(queryDO);
        query.addIn("barcode", barcodeList);
        List<SkuBarcodeDO> skuBarcodeDOList = this.query(query);
        if (CollectionUtils.isNotEmpty(skuBarcodeDOList)) {
            for (SkuBarcodeDO skuBarcodeDO : skuBarcodeDOList) {
                barcode4SkuIdMap.put(skuBarcodeDO.getBarcode(), skuBarcodeDO.getSkuId());
            }
        }
        return barcode4SkuIdMap;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("D:/development/IDEA/ifunq-supplychain/haitao-b2b-supplychain/" +
                        "supplychain-persistence/src/main/resources/base/sqlmap/sku/sc_sku_barcode_sqlmap.xml",
                SkuBarcodeDao.class,
                SkuBarcodeDO.class,
                "sc_sku_barcode");
    }
}
