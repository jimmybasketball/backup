package com.sfebiz.supplychain.persistence.base.line.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineOperateStateType;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineStateType;
import com.sfebiz.supplychain.persistence.base.line.dao.LogisticsLineDao;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;

/**
 * 线路配置Manager
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-11 11:20
 **/
@Component("logisticsLineManager")
public class LogisticsLineManager extends BaseManager<LogisticsLineDO> {

    @Resource
    private LogisticsLineDao logisticsLineDao;

    @Override
    public BaseDao<LogisticsLineDO> getDao() {
        return logisticsLineDao;
    }

    public static void main(String[] args) {
        DaoHelper.genXMLWithFeature(
            "/Users/liujunchi/git_projects/" + "supplychain/supplychain-persistence/"
                    + "src/main/resources/base/sqlmap/line/sc_logistics_line_sqlmap.xml",
            LogisticsLineDao.class, LogisticsLineDO.class, "sc_logistics_line");
    }

    public int updateBySelected(LogisticsLineDO logisticsLineDO) {
        return logisticsLineDao.updateBySelected(logisticsLineDO);
    }

    /**
     * 根据线路Nid获取线路信息
     * 
     * @param logisticsLineNid
     * @return
     */
    public LogisticsLineDO getByLogisticsLineNid(String logisticsLineNid) {
        LogisticsLineDO queryDO = new LogisticsLineDO();
        queryDO.setLogisticsLineNid(logisticsLineNid);
        BaseQuery<LogisticsLineDO> query = BaseQuery.getInstance(queryDO);
        List<LogisticsLineDO> list = this.query(query);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 根据仓库id和服务类型，获取正在服务中的线路
     * （获取到的列表为，线路正在合作中，并且已经启用）
     * 
     * @param warehouseId
     * @param serviceType
     * @return
     */
    public List<LogisticsLineDO> getInServiceLineByWarehouseIdAndServiceType(Long warehouseId, Integer serviceType){
        LogisticsLineDO queryDO = new LogisticsLineDO();
        queryDO.setWarehouseId(warehouseId);
        queryDO.setServiceType(serviceType);
        queryDO.setOperateState(LogisticsLineOperateStateType.NORMAL.value);
        queryDO.setState(LogisticsLineStateType.ENABLE.value);
        BaseQuery<LogisticsLineDO> query = BaseQuery.getInstance(queryDO);
        query.addOrderBy("priority", 2);
        return  this.query(query);
    }
}
