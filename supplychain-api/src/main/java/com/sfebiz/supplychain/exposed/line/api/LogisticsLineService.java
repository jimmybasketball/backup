package com.sfebiz.supplychain.exposed.line.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.line.entity.LogisticsLineEntity;

/**
 * 物流线路服务
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-10 19:41
 **/
public interface LogisticsLineService {

    /**
     * 创建物流线路
     *
     * @param logisticsLineEntity 线路实体
     * @return ID
     */
    public CommonRet<Long> createLogisticsLine(LogisticsLineEntity logisticsLineEntity);

    /**
     * 修改物流线路
     *
     * @param id                  主键ID
     * @param logisticsLineEntity 线路实体
     * @return void
     */
    public CommonRet<Void> updateLogisticsLine(Long id, LogisticsLineEntity logisticsLineEntity);


    /**
     * 修改物流线路状态
     *
     * @param id    主键ID
     * @param state 状态值
     * @return void
     */
    public CommonRet<Void> changeLogisticsLineState(Long id, String state);


    /**
     * 修改物流线路的运营状态
     *
     * @param id           主键ID
     * @param operateState 状态值
     * @return void
     */
    public CommonRet<Void> changechangeLogisticsLineOpState(Long id, String operateState);
}
