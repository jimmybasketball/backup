package com.sfebiz.supplychain.task.stock;

import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDO;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockBatchDailyDO;
import com.sfebiz.supplychain.persistence.base.stock.manager.StockBatchDailyManager;
import com.sfebiz.supplychain.persistence.base.stock.manager.StockBatchManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderDetailManager;
import com.sfebiz.supplychain.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 计算每日批次库存定时任务
 * @date 2017-07-19 11:08
 **/
public class CalcStockBatchDailyTask {
    public static final Logger LOGGER = LoggerFactory.getLogger(CalcStockBatchDailyTask.class);

    @Resource
    private StockBatchManager stockBatchManager;
    @Resource
    private StockBatchDailyManager stockBatchDailyManager;
    @Resource
    private StockinOrderDetailManager stockinOrderDetailManager;


    public void excute() {
        String localIp = HaitaoTraceLoggerFactory.getIp();
        //TODO 查看是否是配置定时任务IP执行
        System.out.println("开始执行");
        calcStockBatchDailyData();
    }

    /**
     * 计算昨日批次库存数据
     */
    private void calcStockBatchDailyData() {
        //取出所有批次库存信息。
        try {
            List<StockBatchDO> stockBatchDOList = stockBatchManager.getAllStockBatch();
            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("查询所有批次信息").addParm("stockBatchDOList", stockBatchDOList).log();
            if (stockBatchDOList != null && stockBatchDOList.size() > 0) {
                for (StockBatchDO stockBatchDO : stockBatchDOList) {
                    StockBatchDailyDO stockBatchDailyDO = new StockBatchDailyDO();
                    String DateString = DateUtil.formatDateStr(new Date(), "yyyy-MM-dd");
                    stockBatchDailyDO.setRecordDate(DateString);
                    //记录批次基础信息
                    stockBatchDailyDO.setSkuId(stockBatchDO.getSkuId());
                    stockBatchDailyDO.setBatchNo(stockBatchDO.getBatchNo());
                    stockBatchDailyDO.setWarehouseId(stockBatchDO.getWarehouseId());
                    stockBatchDailyDO.setMerchantId(stockBatchDO.getMerchantId());
                    stockBatchDailyDO.setMerchantProviderId(stockBatchDO.getMerchantProviderId());
                    //TODO 查询【采购入库正品】和【采购入库坏品】数量
//                    stockinOrderDetailManager.getStockinOrderDetailByStockinDateAndSkuBatch(stockBatchDO.getBatchNo(),new Date());

                    stockBatchDailyDO.setPurchaseStockinAvailableCount(0);
                    stockBatchDailyDO.setPurchaseStockoutDamagedCount(0);
                    //TODO 查询【调拨入库正品】 和【调拨入库坏品】数量
                    stockBatchDailyDO.setTransferStockinAvailableCount(0);
                    stockBatchDailyDO.setTransferStockinDamagedCount(0);
                    //TODO 查询【销售退货入库正品】和【销售退货入库坏品】数量
                    stockBatchDailyDO.setSaleReturnStockinAvailableCount(0);
                    stockBatchDailyDO.setSaleReturnStockinDamagedCount(0);
                    //TODO 查询【海关退货入库正品】和【海关退货入库坏品】数量
                    stockBatchDailyDO.setCustomReturnStockinAvailableCount(0);
                    stockBatchDailyDO.setCustomReturnStockinDamagedCount(0);
                    //TODO 查询【正品报损数量】和【坏品报损数量】数量***查询【正品报溢数量】和【坏品报溢数量】数量
                    stockBatchDailyDO.setReportGainsAvalibleCount(0);
                    stockBatchDailyDO.setReportGainsDamagedCount(0);
                    stockBatchDailyDO.setReportLossesAvalibleCount(0);
                    stockBatchDailyDO.setReportLossesDamagedCount(0);
                    //TODO 查询【销售出库正品】和【销售出库取消返回正品】 数量
                    stockBatchDailyDO.setSaleStockoutAvailableCount(0);
                    stockBatchDailyDO.setSaleCancelStockoutAvailableCount(0);
                    //TODO 查询【采退出库正品】和【采退出库坏品】数量
                    stockBatchDailyDO.setPurchaseStockoutAvailableCount(0);
                    stockBatchDailyDO.setPurchaseStockoutDamagedCount(0);
                    //TODO 查询【调整正品】和【调整坏品】数量
                    stockBatchDailyDO.setAdjustAvailableCount(0);
                    stockBatchDailyDO.setAdjustDamagedCount(0);
                    //查询昨日记录
                    String yesterdayStr = getYesterdayDateStr();
                    StockBatchDailyDO yeasterdayDaily = stockBatchDailyManager.getStockBatchDailyByDateStrAndSkuAndBatch(yesterdayStr, stockBatchDO.getSkuId(), stockBatchDO.getBatchNo());
                    //start_available_count 正品当日期处数量end_available_count正品当日期末数量 start_damaged_count坏品当日期初数量end_damaged_count坏品当日期末数量
                    if (yeasterdayDaily == null) {
                        //TODO 如果不存在昨日数据，则记录当前数据 （当日数据不准确,需重算）
                        stockBatchDailyDO.setStartAvailableCount(stockBatchDO.getAvailableCount());
                        stockBatchDailyDO.setStartDamagedCount(stockBatchDO.getDamagedCount());
                    } else {
                        stockBatchDailyDO.setStartAvailableCount(yeasterdayDaily.getEndAvailableCount());
                        stockBatchDailyDO.setStartDamagedCount(yeasterdayDaily.getEndDamagedCount());
                    }
                    stockBatchDailyDO.setEndAvailableCount(stockBatchDO.getAvailableCount());
                    stockBatchDailyDO.setEndDamagedCount(stockBatchDO.getDamagedCount());
                    stockBatchDailyManager.insert(stockBatchDailyDO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getYesterdayDateStr() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        return DateUtil.formatDateStr(yesterday, "yyyy-MM-dd");
    }

}
