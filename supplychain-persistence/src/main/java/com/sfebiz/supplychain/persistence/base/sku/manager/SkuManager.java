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
import com.sfebiz.supplychain.persistence.base.sku.dao.SkuDao;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuDO;

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
    
    /**
     * 根据skuId列表获取商品信息map
     * @param merchantId
     * @param skuIds
     * @return 商品信息map（key：skuId，value：SkuDO）
     */
    public Map<Long, SkuDO> getSkuDOMapBySkuIds(List<Long> skuIds) {
        Map<Long, SkuDO> skuDOMap = new HashMap<Long, SkuDO>();

        SkuDO d = new SkuDO();
        BaseQuery<SkuDO> q = BaseQuery.getInstance(d);
        q.addIn("id", skuIds);
        List<SkuDO> list = this.query(q);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SkuDO skuDO : list) {
                skuDOMap.put(skuDO.getId(), skuDO);
            }
        }
        return skuDOMap;
    }
}
