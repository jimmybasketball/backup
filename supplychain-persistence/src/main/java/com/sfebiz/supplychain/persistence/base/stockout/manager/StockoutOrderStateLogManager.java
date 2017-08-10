package com.sfebiz.supplychain.persistence.base.stockout.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderStateLogDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderStateLogDO;
import com.sfebiz.supplychain.util.DateUtil;

/**
 * 
 * <p>出库单状态变更日志manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderStateLogManager")
public class StockoutOrderStateLogManager extends BaseManager<StockoutOrderStateLogDO> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StockoutOrderStateLogManager.class);

    @Resource
    private StockoutOrderStateLogDao stockoutOrderStateLogDao;

    @Override
    public BaseDao<StockoutOrderStateLogDO> getDao() {
        return stockoutOrderStateLogDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_state_log-sqlmap.xml",
            StockoutOrderStateLogDao.class, StockoutOrderStateLogDO.class,
            "sc_stockout_order_state_log");
    }
    
    public static StockoutOrderStateLogDO newQuery() {
        StockoutOrderStateLogDO q = new StockoutOrderStateLogDO();
        q.setBizId(null);
        q.setStateDuration(null);
        q.setAlarmDuration(null);
        return q;
    }
    
    /**
     * 查询出库单最新的状态
     *
     * @param bizId
     * @return
     */
    public StockoutOrderStateLogDO queryLastState(Long stockoutOrderId, String bizId) {
        StockoutOrderStateLogDO q = newQuery();
        q.setStockoutOrderId(stockoutOrderId);
        q.setBizId(bizId);
        BaseQuery<StockoutOrderStateLogDO> qs = BaseQuery.getInstance(q);
        qs.addOrderBy("id", -1);
        List<StockoutOrderStateLogDO> stockoutOrderStateLogDOList = this.query(qs);
        if (CollectionUtils.isEmpty(stockoutOrderStateLogDOList)) {
            return null;
        }
        return stockoutOrderStateLogDOList.get(0);
    }
    
    /**
     * 订单状态日志变更 ，设置预警时间
     *
     * @param stockoutOrderDO
     * @param toState
     * @param eventTime
     * @param userName
     */
    public void updateStockOutOrderStateLog(StockoutOrderDO stockoutOrderDO, String toState, Date eventTime, Date alarmTime, String userName) {
        updateStockOutOrderStateLog(stockoutOrderDO, toState, eventTime, alarmTime, userName, null);
    }

    /**
     * 订单状态日志变更 ，设置预警时间
     *
     * @param stockoutOrderDO
     * @param toState
     * @param eventTime
     * @param userName
     * @param stateRemark
     */
    public void updateStockOutOrderStateLog(StockoutOrderDO stockoutOrderDO, String toState, Date eventTime, Date alarmTime, String userName, String stateRemark) {
        try {

            if (StockoutOrderType.SALES_STOCK_OUT.getValue() != stockoutOrderDO.getOrderType() &&
                    StockoutOrderType.TRANSPORT_STOCK_OUT.getValue() != stockoutOrderDO.getOrderType()) {
                return;
            }

            if (stockoutOrderDO.getLineId() < 200) {
                //小于200为特殊路线，不进状态流转
                return;
            }

            StockoutOrderStateLogDO stockoutOrderCurrentState = queryLastState(stockoutOrderDO.getId(), stockoutOrderDO.getBizId());

            Date currentDate = new Date();
            if (null == eventTime) {
                eventTime = currentDate;
            }

            //如果时间发生时间比当前时间还大，则采用当前时间
            if (eventTime.after(currentDate)) {
                eventTime = currentDate;
            }

            //如果之前存在状态则先更新
            if (null != stockoutOrderCurrentState) {
                //如果收件时间在出库时间之前的话，需要修正出库时间
                if (StockoutOrderState.STOCKOUT_FINISH.getValue().equals(stockoutOrderCurrentState.getFromState())
                        && StockoutOrderState.COLLECTED.getValue().equals(toState)
                        && eventTime.before(stockoutOrderCurrentState.getEventTime())) {
                    stockoutOrderCurrentState.setFromTime(eventTime);
                    stockoutOrderCurrentState.setEventTime(eventTime);
                }
                Date fmTime = stockoutOrderCurrentState.getFromTime();
                stockoutOrderCurrentState.setToTime(eventTime);
                stockoutOrderCurrentState.setStateDuration((eventTime.getTime() - fmTime.getTime()) / 1000);
                stockoutOrderCurrentState.setToState(toState);
                this.update(stockoutOrderCurrentState);
            }
            //插入新增状态
            if (null == alarmTime) {
                String notAlermTimeStr = "2099-01-09 00:00:00";
                alarmTime = DateUtil.parseDate(notAlermTimeStr, DateUtil.DEF_PATTERN);
            }
            StockoutOrderStateLogDO newState = new StockoutOrderStateLogDO();
            newState.setStockoutOrderId(stockoutOrderDO.getId());
            newState.setBizId(stockoutOrderDO.getBizId());
            newState.setLineId(stockoutOrderDO.getLineId());
            newState.setEventTime(eventTime);
            newState.setAlarmDuration(100L);
            newState.setAlarmTime(alarmTime);
            newState.setStockoutOrderId(stockoutOrderDO.getId());
            newState.setFromState(toState);
            newState.setFromTime(eventTime);
            newState.setRemark(stateRemark);
            this.insert(newState);

        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("记录订单日志变更异常:" + e.getMessage())
                    .setException(e)
                    .setParms("订单", stockoutOrderDO.getBizId())
                    .log();
        }
    }


    /**
     * 订单状态日志变更,不进行状态预警
     *
     * @param stockoutOrderDO
     * @param toState
     * @param eventTime
     * @param userName
     */
    public void updateStockOutOrderStateLog(StockoutOrderDO stockoutOrderDO, String toState, Date eventTime, String userName) {
        updateStockOutOrderStateLog(stockoutOrderDO, toState, eventTime, userName, null);
    }

    /**
     * 订单状态日志变更,不进行状态预警
     *
     * @param stockoutOrderDO
     * @param toState
     * @param eventTime
     * @param userName
     * @param stateChangeRemark
     */
    public void updateStockOutOrderStateLog(StockoutOrderDO stockoutOrderDO, String toState, Date eventTime, String userName, String stateChangeRemark) {
        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.INFO)
                .setMsg("订单状态变更")
                .setParms("订单", stockoutOrderDO.getBizId())
                .setParms("从状态", stockoutOrderDO.getOrderState())
                .setParms("到状态", toState)
                .log();

        if (StockoutOrderType.SALES_STOCK_OUT.getValue() != stockoutOrderDO.getOrderType() &&
                StockoutOrderType.TRANSPORT_STOCK_OUT.getValue() != stockoutOrderDO.getOrderType()) {
            return;
        }

        if (stockoutOrderDO.getLineId() < 200) {
            //小于200为特殊路线，不进状态流转
            return;
        }

        StockoutOrderStateLogDO stockoutOrderState = queryLastState(stockoutOrderDO.getId(), stockoutOrderDO.getBizId());
        String notAlermTimeStr = "2099-01-09 00:00:00";
        Date notAlermTime = DateUtil.parseDate(notAlermTimeStr, DateUtil.DEF_PATTERN);
        try {
            if (null == eventTime) {
                eventTime = new Date();
            }
            //如果之前存在状态则先更新
            if (null != stockoutOrderState) {
                Date fmTime = stockoutOrderState.getFromTime();
                stockoutOrderState.setToTime(eventTime);
                stockoutOrderState.setStateDuration((eventTime.getTime() - fmTime.getTime()) / 1000);
                stockoutOrderState.setToState(toState);
                stockoutOrderState.setAlarmTime(notAlermTime);
                this.update(stockoutOrderState);
            }
            //插入新增状态
            StockoutOrderStateLogDO newState = new StockoutOrderStateLogDO();
            newState.setStockoutOrderId(stockoutOrderDO.getId());
            newState.setBizId(stockoutOrderDO.getBizId());
            newState.setLineId(stockoutOrderDO.getLineId());
            newState.setEventTime(eventTime);
            newState.setAlarmDuration(100L);
            newState.setAlarmTime(notAlermTime);
            newState.setStockoutOrderId(stockoutOrderDO.getId());
            newState.setFromState(toState);
            newState.setFromTime(eventTime);
            newState.setRemark(stateChangeRemark);
            this.insert(newState);

        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("记录订单日志变更异常:" + e.getMessage())
                    .setException(e)
                    .setParms("订单", stockoutOrderDO.getBizId())
                    .log();
        }
    }
}
