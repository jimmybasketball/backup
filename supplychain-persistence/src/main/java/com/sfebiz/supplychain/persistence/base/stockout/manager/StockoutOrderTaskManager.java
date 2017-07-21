package com.sfebiz.supplychain.persistence.base.stockout.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
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

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature("C:/sc_stockout_order_task-sqlmap.xml",
            StockoutOrderTaskDao.class, StockoutOrderTaskDO.class, "sc_stockout_order_task");
    }
}
