package com.sfebiz.supplychain.msg.processer.impl;

import com.aliyun.openservices.ServiceException;
import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.common.entity.Void;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.msg.processer.MessageProcesser;
import com.sfebiz.supplychain.queue.MessageConstants;
import com.sfebiz.supplychain.service.route.handler.impl.InternationalRouteFetchHandler;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 路由获取更新消息处理器， 里面嵌有消息重复发送逻辑
 * 如果出库单在运输中状态，正常情况会一直保持一个延时轮询
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-02 11:36
 **/
@Component("routeFetchEventMessageProcesser")
public class RouteFetchEventMessageProcesser implements MessageProcesser{

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteFetchEventMessageProcesser.class);

    @Resource
    private InternationalRouteFetchHandler internationalRouteFetchHandler;

    @Resource
    private RouteService routeService;

    @Override
    public Boolean process(Message message) {
        //获取订单ID
        String orderId = message.getUserProperties("orderId");
        if (StringUtils.isBlank(orderId)) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[物流平台路由-查询路由信息消息处理] orderId为空")
                    .log();
            return true;
        }

        LogBetter.instance(LOGGER)
                .setLevel(LogLevel.WARN)
                .setMsg("[物流平台路由-查询路由信息消息处理] 开始处理")
                .addParm("orderId", orderId)
                .log();

        try {
            //TODO 获取出库单BO对象
            StockoutOrderBO stockoutOrderBO = new StockoutOrderBO();

            //执行路由获取更新操作
            boolean isPolling = internationalRouteFetchHandler.fetchRouteByStockOrder(stockoutOrderBO);
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[物流平台路由-查询路由信息消息处理] 处理结束")
                    .addParm("orderId", orderId)
                    .addParm("isPolling", isPolling)
                    .log();
            //是否需要继续轮询，在这里就是重新发送一条消息
            if (isPolling) {
                //再次发送路由获取更新消息， 这里的时间具体有没有一个策略，后续再说
                CommonRet<Void> commonRet = routeService.sendRouteFetchMessage(orderId, 1200L);
                if (commonRet.getRetCode() != SCReturnCode.COMMON_SUCCESS.getCode()) {
                    return false;
                }
            }
            return true;
        } catch (ServiceException e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setErrorMsg("[物流平台路由-查询路由信息消息处理] 业务异常")
                    .addParm("orderId", orderId)
                    .log();
            return true;
        } catch (Exception e) {
            LogBetter.instance(LOGGER)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setErrorMsg("[物流平台路由-查询路由信息消息处理] 未知异常")
                    .addParm("orderId", orderId)
                    .log();
            return false;
        }
    }

    @Override
    public String getTag() {
        return MessageConstants.TAG_ROUTE_FETCH;
    }
}
