package com.sfebiz.supplychain.persistence.base.stockout.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.common.enums.PortState;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderRecordDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderRecordDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>出库单记录manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderRecordManager")
public class StockoutOrderRecordManager extends BaseManager<StockoutOrderRecordDO> {

    @Resource
    private StockoutOrderRecordDao stockoutOrderRecordDao;

    @Override
    public BaseDao<StockoutOrderRecordDO> getDao() {
        return stockoutOrderRecordDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_record-sqlmap.xml",
                StockoutOrderRecordDao.class, StockoutOrderRecordDO.class, "sc_stockout_order_record");
    }


    /**
     * 修改出库单口岸下单状态
     *
     * @param stockoutOrderId 出库单ID
     * @param toState         状态值  -1 无口岸状态 0 需口岸订单备案 1 口岸备案成功  2 口岸备案失败
     */
    public int updatePortState(long stockoutOrderId, int toState) {
        StockoutOrderRecordDO stockoutOrderRecordDO = new StockoutOrderRecordDO();
        stockoutOrderRecordDO.setStockoutOrderId(stockoutOrderId);
        stockoutOrderRecordDO.setPortState(toState);
        if (toState == PortState.SUCCESS.getState()) {
            stockoutOrderRecordDO.setPortValidatePassTime(new Date());
        }
        return stockoutOrderRecordDao.updatePortState(stockoutOrderRecordDO);
    }
}
