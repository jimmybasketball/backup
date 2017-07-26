package com.sfebiz.supplychain.persistence.base.stockout.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.ExportedShipOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;

/**
 * 
 * <p>出库单manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderManager")
public class StockoutOrderManager extends BaseManager<StockoutOrderDO> {

    @Resource
    private StockoutOrderDao stockoutOrderDao;

    @Override
    public BaseDao<StockoutOrderDO> getDao() {
        return stockoutOrderDao;
    }

    /**
     * 通过商户订单号和商户编码查询订单信息
     * 
     * @param merchantOrderNo 商户订单号
     * @param merchantId 货主ID
     * @return
     */
    public StockoutOrderDO queryByMerchantOrderNoAndMerchantId(String merchantOrderNo,
                                                               Long merchantId) {
        StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
        stockoutOrderDO.setMerchantOrderNo(merchantOrderNo);
        stockoutOrderDO.setMerchantId(merchantId);
        BaseQuery<StockoutOrderDO> baseQuery = new BaseQuery<StockoutOrderDO>(stockoutOrderDO);
        List<StockoutOrderDO> stockoutOrderDOs = stockoutOrderDao.query(baseQuery);
        if (stockoutOrderDOs == null || stockoutOrderDOs.size() == 0) {
            return null;
        } else {
            return stockoutOrderDOs.get(0);
        }
    }
    
    public List<ExportedShipOrderDO> query4Page4AutoShipOrder(List<Long> stockoutOrderIdList) {
        return stockoutOrderDao.query4Page4AutoShipOrderByStockoutOrderIds(stockoutOrderIdList);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order-sqlmap.xml", StockoutOrderDao.class,
            StockoutOrderDO.class, "sc_stockout_order");
    }
}
