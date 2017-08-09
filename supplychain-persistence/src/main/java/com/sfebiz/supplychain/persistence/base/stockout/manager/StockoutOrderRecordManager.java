package com.sfebiz.supplychain.persistence.base.stockout.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.UpdateByQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.common.enums.PortState;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderRecordDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderRecordDO;

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

    /**
     * 更新 出库单下发流程执行完毕的时间
     * 
     * @param stockoutOrderId
     * @param stockoutCmdsSuccessSendTime
     * @return
     */
    public int updateStockoutCmdsSuccessSendTime(Long stockoutOrderId,
                                                 Date stockoutCmdsSuccessSendTime) {
        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = BaseQuery.getInstance(queryDO);
        StockoutOrderRecordDO updateDO = new StockoutOrderRecordDO();
        updateDO.setStockoutCmdsSuccessSendTime(stockoutCmdsSuccessSendTime);
        UpdateByQuery<StockoutOrderRecordDO> updateQuery = new UpdateByQuery<StockoutOrderRecordDO>(
            query, updateDO);
        return this.updateByQuery(updateQuery);
    }

    /**
     * 更新物流状态
     * 
     * @param stockoutOrderId
     * @param logisticsState
     * @return
     */
    public int updateLogisticsState(Long stockoutOrderId, Integer logisticsState) {
        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = BaseQuery.getInstance(queryDO);
        StockoutOrderRecordDO updateDO = new StockoutOrderRecordDO();
        updateDO.setLogisticsState(logisticsState);
        UpdateByQuery<StockoutOrderRecordDO> updateQuery = new UpdateByQuery<StockoutOrderRecordDO>(
            query, updateDO);
        return this.updateByQuery(updateQuery);
    }

    /**
     * 更新口岸校验通过的时间
     * 
     * @param stockoutOrderId
     * @param portValidatePassTime
     * @return
     */
    public int updatePortValidatePassTime(Long stockoutOrderId, Date portValidatePassTime) {
        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = BaseQuery.getInstance(queryDO);
        StockoutOrderRecordDO updateDO = new StockoutOrderRecordDO();
        updateDO.setPortValidatePassTime(portValidatePassTime);
        UpdateByQuery<StockoutOrderRecordDO> updateQuery = new UpdateByQuery<StockoutOrderRecordDO>(
            query, updateDO);
        return this.updateByQuery(updateQuery);
    }
}
