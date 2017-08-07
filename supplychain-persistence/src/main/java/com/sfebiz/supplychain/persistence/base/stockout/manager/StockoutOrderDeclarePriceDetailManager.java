package com.sfebiz.supplychain.persistence.base.stockout.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDeclarePriceDetailDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDetailDO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * <p>出库单申报金额明细manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderDeclarePriceDetailManager")
public class StockoutOrderDeclarePriceDetailManager extends
                                                   BaseManager<StockoutOrderDeclarePriceDetailDO> {

    @Resource
    private StockoutOrderDeclarePriceDetailDao stockoutOrderDeclarePriceDetailDao;

    @Override
    public BaseDao<StockoutOrderDeclarePriceDetailDO> getDao() {
        return stockoutOrderDeclarePriceDetailDao;
    }


    /**
     * 根据子订单号获取申报信息
     *
     * @param bizId
     * @return
     */
    public List<StockoutOrderDeclarePriceDetailDO> getByBizId(String bizId){
        if (StringUtils.isBlank(bizId)) {
            return null;
        }
        StockoutOrderDeclarePriceDetailDO d = new StockoutOrderDeclarePriceDetailDO();
        d.setBizId(bizId);
        BaseQuery<StockoutOrderDeclarePriceDetailDO> q = BaseQuery.getInstance(d);
        List<StockoutOrderDeclarePriceDetailDO> list = this.query(q);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    public StockoutOrderDeclarePriceDetailDO getSkuDeclareDOByStockoutIdAndSkuId(long stockoutOrderId, long skuId){
        StockoutOrderDeclarePriceDetailDO d = new StockoutOrderDeclarePriceDetailDO();
        d.setStockoutOrderId(stockoutOrderId);
        d.setSkuId(skuId);
        BaseQuery<StockoutOrderDeclarePriceDetailDO> q = BaseQuery.getInstance(d);
        List<StockoutOrderDeclarePriceDetailDO> skuDeclarePriceDOList = this.query(q);
        if(CollectionUtils.isEmpty(skuDeclarePriceDOList)){
            return null;
        }else{
            return skuDeclarePriceDOList.get(0);
        }
    }
    
    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_declare_price_detail-sqlmap.xml",
            StockoutOrderDeclarePriceDetailDao.class, StockoutOrderDeclarePriceDetailDO.class,
            "sc_stockout_order_declare_price_detail");
    }
}
