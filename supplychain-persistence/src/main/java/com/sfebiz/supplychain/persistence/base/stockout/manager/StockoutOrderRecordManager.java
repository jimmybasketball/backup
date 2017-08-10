package com.sfebiz.supplychain.persistence.base.stockout.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.UpdateByQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.common.enums.PortState;
import com.sfebiz.supplychain.exposed.stockout.enums.TplIntlState;
import com.sfebiz.supplychain.exposed.stockout.enums.TplIntrState;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderDao;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderRecordDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDetailDO;
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

    @Resource
    private StockoutOrderDao stockoutOrderDao;

    @Override
    public BaseDao<StockoutOrderRecordDO> getDao() {
        return stockoutOrderRecordDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_record-sqlmap.xml",
                StockoutOrderRecordDao.class, StockoutOrderRecordDO.class, "sc_stockout_order_record");
    }
    
    public StockoutOrderRecordDO getByStockoutOrderId(Long stockoutOrderId){
        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = new BaseQuery<StockoutOrderRecordDO>(queryDO);
        List<StockoutOrderRecordDO> resultDOs = stockoutOrderRecordDao.query(query);
        if (CollectionUtils.isNotEmpty(resultDOs)) {
            return resultDOs.get(0);
        }
        return null;        
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

    /**
     * 修改国内物流下单状态
     *
     * @param stockoutOrderId 出库单ID
     * @param tplIntrState    状态值  -1, 未分配 0, 初始化 1, 下单成功 2, 确认订单成功 3, 确认订单失败 4, 取消成功 5, 取消失败 6, 下单失败
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/8/1 11:47
     */
    public int updateTplIntrState(long stockoutOrderId, int tplIntrState) {
        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = BaseQuery.getInstance(queryDO);
        StockoutOrderRecordDO updateDO = new StockoutOrderRecordDO();
        updateDO.setTplIntrState(tplIntrState);
        if (tplIntrState == TplIntrState.CREATE_SUCC.getValue()) {
            updateDO.setPortValidatePassTime(new Date());
        }
        UpdateByQuery<StockoutOrderRecordDO> update = new UpdateByQuery<StockoutOrderRecordDO>(query, updateDO);
        return stockoutOrderRecordDao.updateByQuery(update);
    }


    /**
     * 修改国内物流下单状态
     *
     * @param stockoutOrderId
     * @param intrMailNo 国内运单号
     * @param trackingNo 签单返还运单号
     * @param origincode 原寄地代码
     * @param destcode 目的地代码，绘制快递面单时需要（不同的快递公司，约定存储）
     * @param tplIntrState 状态值 -1, 未分配 0, 初始化 1, 下单成功 2, 确认订单成功 3, 确认订单失败 4, 取消成功 5, 取消失败 6, 下单失败
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/8/1 11:47
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateTplIntrState(long stockoutOrderId, String intrMailNo, String trackingNo ,String origincode, String destcode, int tplIntrState) {

        StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
        stockoutOrderDO.setId(stockoutOrderId);
        stockoutOrderDO.setIntrMailNo(intrMailNo);
        stockoutOrderDO.setDestcode(destcode);
        stockoutOrderDao.update(stockoutOrderDO);

        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = BaseQuery.getInstance(queryDO);
        StockoutOrderRecordDO updateDO = new StockoutOrderRecordDO();
        updateDO.setTplIntrState(tplIntrState);
        if (tplIntrState == TplIntrState.CREATE_SUCC.getValue()) {
            updateDO.setPortValidatePassTime(new Date());
        }
        UpdateByQuery<StockoutOrderRecordDO> update = new UpdateByQuery<StockoutOrderRecordDO>(query, updateDO);
        return stockoutOrderRecordDao.updateByQuery(update);
    }


    /**
     * 修改国际物流下单状态
     *
     * @param stockoutOrderId 出库单ID
     * @param tplIntlState    状态值 -1, 未分配 0, 初始化 1, 下单成功 2, 确认订单成功 3, 确认订单失败 4, 取消成功 5, 取消失败 6, 下单失败
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/8/1 11:47
     */
    public int updateTplIntlState(long stockoutOrderId, int tplIntlState) {
        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = BaseQuery.getInstance(queryDO);
        StockoutOrderRecordDO updateDO = new StockoutOrderRecordDO();
        updateDO.setTplIntlState(tplIntlState);
        if (tplIntlState == TplIntlState.CREATE_SUCC.getValue()) {
            updateDO.setTplIntrCmdSuccessSendTime(new Date());
        }
        UpdateByQuery<StockoutOrderRecordDO> update = new UpdateByQuery<StockoutOrderRecordDO>(query, updateDO);
        return stockoutOrderRecordDao.updateByQuery(update);
    }

    /**
     * 修改国际物流下单状态
     *
     * @param stockoutOrderId 出库单ID
     * @param intlMailNo 国际运单号
     * @param trackingNo 签单返还运单号
     * @param origincode 原寄地代码
     * @param destcode 目的地代码，绘制快递面单时需要（不同的快递公司，约定存储）
     * @param tplIntlState    状态值 -1, 未分配 0, 初始化 1, 下单成功 2, 确认订单成功 3, 确认订单失败 4, 取消成功 5, 取消失败 6, 下单失败
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/8/1 11:47
     */
    public int updateTplIntlState(long stockoutOrderId, String intlMailNo, String trackingNo ,String origincode, String destcode, int tplIntlState) {

        StockoutOrderDO stockoutOrderDO = new StockoutOrderDO();
        stockoutOrderDO.setId(stockoutOrderId);
        stockoutOrderDO.setIntlMailNo(intlMailNo);
        stockoutOrderDO.setDestcode(destcode);
        stockoutOrderDao.update(stockoutOrderDO);

        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = BaseQuery.getInstance(queryDO);
        StockoutOrderRecordDO updateDO = new StockoutOrderRecordDO();
        updateDO.setTplIntlState(tplIntlState);
        if (tplIntlState == TplIntlState.CREATE_SUCC.getValue()) {
            updateDO.setTplIntlCmdSuccessSendTime(new Date());
        }
        UpdateByQuery<StockoutOrderRecordDO> update = new UpdateByQuery<StockoutOrderRecordDO>(query, updateDO);
        return stockoutOrderRecordDao.updateByQuery(update);
    }


    /**
     * 更新包裹的物流状态标识，描述包裹的物流流转情况（参照LogisticsState枚举）
     *
     * @param stockoutOrderId 出库单ID
     * @param logisticsState  物流状态标识
     * @return
     *
     * @author tanzx [tanzongxi@ifunq.com]
     * @date 2017/8/1 11:47
     */
    public int updateLogisticsState(long stockoutOrderId, int logisticsState) {
        StockoutOrderRecordDO queryDO = new StockoutOrderRecordDO();
        queryDO.setStockoutOrderId(stockoutOrderId);
        BaseQuery<StockoutOrderRecordDO> query = BaseQuery.getInstance(queryDO);
        StockoutOrderRecordDO updateDO = new StockoutOrderRecordDO();
        updateDO.setLogisticsState(logisticsState);
        UpdateByQuery<StockoutOrderRecordDO> update = new UpdateByQuery<StockoutOrderRecordDO>(query, updateDO);
        return stockoutOrderRecordDao.updateByQuery(update);
    }



}
