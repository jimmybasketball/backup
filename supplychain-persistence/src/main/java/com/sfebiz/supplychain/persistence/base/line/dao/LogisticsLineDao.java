package com.sfebiz.supplychain.persistence.base.line.dao;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;

/**
 * 线路配置DAO
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-11 11:18
 **/
public interface LogisticsLineDao extends BaseDao<LogisticsLineDO>{

    public int updateBySelected(LogisticsLineDO logisticsLineDO);
}
