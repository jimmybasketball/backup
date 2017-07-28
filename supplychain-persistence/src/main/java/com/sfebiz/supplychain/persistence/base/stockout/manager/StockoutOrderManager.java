package com.sfebiz.supplychain.persistence.base.stockout.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 更新出库单申报支付单号
     *
     * @param stockoutOrderId
     * @param payNo
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/7/27 16:59
     */
    public int updatePayNo(long stockoutOrderId, String payNo) {
        StockoutOrderDO updateDO = new StockoutOrderDO();
        updateDO.setId(stockoutOrderId);
        updateDO.setDeclarePayNo(payNo);
        return this.update(updateDO);
    }

    /**
     * 更新出库单支付申报相关信息
     *
     * @param stockoutOrderId
     * @param payNo
     * @param payerName
     * @param payerCertNo
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/7/28 14:46
     */
    public int updatePayBillInfo(long stockoutOrderId, String payNo, String payerName, String payerCertNo){
        StockoutOrderDO updateDO = new StockoutOrderDO();
        updateDO.setId(stockoutOrderId);
        updateDO.setDeclarePayNo(payNo);
        updateDO.setDeclarePayerName(payerName);
        updateDO.setDeclarePayerCertNo(payerCertNo);
        return this.update(updateDO);
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order-sqlmap.xml", StockoutOrderDao.class,
            StockoutOrderDO.class, "sc_stockout_order");
    }
}
