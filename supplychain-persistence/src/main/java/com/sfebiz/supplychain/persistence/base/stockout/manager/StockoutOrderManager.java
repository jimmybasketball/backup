package com.sfebiz.supplychain.persistence.base.stockout.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(StockoutOrderManager.class);

    @Resource
    private StockoutOrderDao    stockoutOrderDao;

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

    /**
     * 根据身份证号，查询此用户下第一个由于口岸单数等被限制的出库单信息
     *
     * @param idNo
     * @return 如果不存在，则返回null
     */
    public StockoutOrderDO getFirstPortValidateOrderByIdNo(String idNo, Long portId) {
        // TODO matt
        return null;
    }

    public StockoutOrderDO getByBizId(String bizId) {
        return getByBizId(bizId, null);
    }

    public StockoutOrderDO getByBizId(String bizId, String orderType) {
        if (StringUtils.isBlank(bizId)) {
            return null;
        }
        StockoutOrderDO q = new StockoutOrderDO();
        q.setBizId(bizId);
        if (StringUtils.isNotBlank(orderType)) {
            q.setOrderType(Integer.valueOf(orderType));
        }
        BaseQuery<StockoutOrderDO> qs = BaseQuery.getInstance(q);
        List<StockoutOrderDO> list = this.query(qs);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else if (list != null && list.size() > 1) {
            LOGGER.error("订单：" + bizId + "，出库单存在多条（重复），请排查。");
        }
        return null;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order-sqlmap.xml", StockoutOrderDao.class,
            StockoutOrderDO.class, "sc_stockout_order");
    }
}
