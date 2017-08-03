package com.sfebiz.supplychain.open.exposed.callback.api;

import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ServiceException;
import net.pocrd.responseEntity.RawString;

/**
 * 外部系统回调接口
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/8/1 15:19
 */
public interface LogisticsCallbackService {

    @HttpApi(name = "logistics.kd100Callback", desc = "快递100回调", detail = "快递100回调", security = SecurityType.Integrated, owner = "刘峻池")
    RawString kd100Callback(@ApiParameter(required = true, name = "param", desc = "快递100路由") String param) throws ServiceException;

}
