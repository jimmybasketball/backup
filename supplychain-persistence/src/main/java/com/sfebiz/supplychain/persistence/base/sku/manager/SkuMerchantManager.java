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
import com.sfebiz.supplychain.persistence.base.sku.dao.SkuMerchantDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuMerchantDO;

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
        DaoHelper
            .genXMLWithFeature(
                "D:/development/IDEA/ifunq-supplychain/haitao-b2b-supplychain/"
                        + "supplychain-persistence/src/main/resources/base/sqlmap/sku/sc_sku_merchant_sqlmap.xml",
                SkuMerchantDao.class, SkuMerchantDO.class, "sc_sku_merchant");
    }

    /**
     * 根据skuId列表获取商品信息map
     * @param merchantId
     * @param skuIds
     * @return 商品信息map（key：skuId，value：SkuMerchantDO）
     */
    public Map<Long, SkuMerchantDO> getSkuMerchantDOMapBySkuIdsAndMerchantId(Long merchantId, List<Long> skuIds) {
        Map<Long, SkuMerchantDO> skuMerchantDOMap = new HashMap<Long, SkuMerchantDO>();

        SkuMerchantDO d = new SkuMerchantDO();
        d.setMerchantId(merchantId);
        BaseQuery<SkuMerchantDO> q = BaseQuery.getInstance(d);
        q.addIn("sku_id", skuIds);
        List<SkuMerchantDO> list = this.query(q);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SkuMerchantDO skuDO : list) {
                skuMerchantDOMap.put(skuDO.getSkuId(), skuDO);
            }
        }
        return skuMerchantDOMap;
    }
}