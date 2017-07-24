package com.sfebiz.supplychain.persistence.base.stockout.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.stockout.enums.SubTaskType;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.persistence.base.stockout.dao.StockoutOrderTaskDao;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;

/**
 * 
 * <p>出库单任务manager类</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:49:29
 */
@Component("stockoutOrderTaskManager")
public class StockoutOrderTaskManager extends BaseManager<StockoutOrderTaskDO> {

    @Resource
    private StockoutOrderTaskDao stockoutOrderTaskDao;

    @Override
    public BaseDao<StockoutOrderTaskDO> getDao() {
        return stockoutOrderTaskDao;
    }
    
    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_task-sqlmap.xml",
            StockoutOrderTaskDao.class, StockoutOrderTaskDO.class, "sc_stockout_order_task");
    }
    
    public List<StockoutOrderTaskDO> getAllFailDataByTaskType(String taskType) {
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setTaskType(taskType);
        BaseQuery<StockoutOrderTaskDO> qy = new BaseQuery<StockoutOrderTaskDO>(stockoutOrderTaskDO);
        List<String> notinList = new ArrayList<String>();
        notinList.add(TaskStatus.HANDLE_SUCCESS.getValue());
        qy.addNotIn("task_status", notinList);
        List<StockoutOrderTaskDO> stockoutOrderTaskDOs = stockoutOrderTaskDao.query(qy);
        if (stockoutOrderTaskDOs != null && stockoutOrderTaskDOs.size() > 0) {
            return stockoutOrderTaskDOs;
        }
        return null;
    }
    
    /**
     * 根据业务订单号、货主id、任务类型，获取任务实体
     * 
     * @param bizId
     * @param merchantId
     * @param taskType
     * @return
     */
    public StockoutOrderTaskDO getTaskByBizIdAndMerchantIdAndTaskType(String bizId,
                                                                      Long merchantId,
                                                                    String taskType) {
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setBizId(bizId);
        stockoutOrderTaskDO.setMerchantId(merchantId);
        stockoutOrderTaskDO.setTaskType(taskType);
        BaseQuery<StockoutOrderTaskDO> qy = new BaseQuery<StockoutOrderTaskDO>(stockoutOrderTaskDO);
        List<StockoutOrderTaskDO> stockoutOrderTaskDOs = stockoutOrderTaskDao.query(qy);
        if (CollectionUtils.isNotEmpty(stockoutOrderTaskDOs)) {
            return stockoutOrderTaskDOs.get(0);
        }
        return null;
    }

    /**
     * 根据出库单ID和任务类型查询所有任务，由于身份证收集项目增加了子任务类型，因此
     * 一个出库单在一个异常类型下可能有多个记录
     *
     * @param stockoutOrderId 出库单ID
     * @param taskType        任务类型 
     * @return
     */
    public List<StockoutOrderTaskDO> getListByStockoutOrderIdAndTaskType(Long stockoutOrderId,
                                                                         String taskType) {
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setStockoutOrderId(stockoutOrderId);
        stockoutOrderTaskDO.setTaskType(taskType);
        BaseQuery<StockoutOrderTaskDO> qy = new BaseQuery<StockoutOrderTaskDO>(stockoutOrderTaskDO);
        List<StockoutOrderTaskDO> stockoutOrderTaskDOs = stockoutOrderTaskDao.query(qy);
        if (stockoutOrderTaskDOs != null && stockoutOrderTaskDOs.size() > 0) {
            return stockoutOrderTaskDOs;
        }
        return null;
    }

    /**
     * 根据出库单ID和任务类型查询任务
     *
     * @param stockoutOrderId 出库单ID
     * @param taskType        任务类型 {@code StockoutTaskType}
     * @return
     */
    public StockoutOrderTaskDO getByStockoutOrderIdAndTaskType(Long stockoutOrderId, String taskType) {
        StockoutOrderTaskDO stockoutOrderTaskDO = new StockoutOrderTaskDO();
        stockoutOrderTaskDO.setStockoutOrderId(stockoutOrderId);
        stockoutOrderTaskDO.setTaskType(taskType);
        BaseQuery<StockoutOrderTaskDO> qy = new BaseQuery<StockoutOrderTaskDO>(stockoutOrderTaskDO);
        List<StockoutOrderTaskDO> stockoutOrderTaskDOs = stockoutOrderTaskDao.query(qy);
        if (stockoutOrderTaskDOs != null && stockoutOrderTaskDOs.size() > 0) {
            List<StockoutOrderTaskDO> taskDOList = new ArrayList<StockoutOrderTaskDO>();
            for (StockoutOrderTaskDO taskDO : stockoutOrderTaskDOs) {
                if (StringUtils.isNotBlank(taskDO.getSubTaskType())
                    && SubTaskType.allSubTaskType().contains(taskDO.getSubTaskType())) {
                    continue;
                } else {
                    taskDOList.add(taskDO);
                }
            }
            if (taskDOList.size() > 0) {
                return stockoutOrderTaskDOs.get(0);
            }
        }
        return null;
    }
}
