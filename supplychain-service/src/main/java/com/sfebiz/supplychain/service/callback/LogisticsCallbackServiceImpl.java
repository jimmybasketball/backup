package com.sfebiz.supplychain.service.callback;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.exposed.route.enums.RouteType;
import com.sfebiz.supplychain.open.exposed.callback.api.LogisticsCallbackService;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.kd100.KD100CallbackItem;
import com.sfebiz.supplychain.protocol.kd100.KD100CallbackRequest;
import com.sfebiz.supplychain.protocol.kd100.KD100CallbackResponse;
import com.sfebiz.supplychain.queue.MessageConstants;
import com.sfebiz.supplychain.queue.MessageProducer;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.TransCarrierCode;
import net.pocrd.entity.ServiceException;
import net.pocrd.responseEntity.RawString;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 外部系统回传实现
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-01 15:22
 **/
public class LogisticsCallbackServiceImpl implements LogisticsCallbackService {


    private static final Logger logger = LoggerFactory.getLogger(LogisticsCallbackServiceImpl.class);

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    private static final String ABORT = "abort";

    @Resource
    private RouteService routeService;

    @Resource
    private MessageProducer supplyChainRouteMessageProducer;

    @Resource
    private StockoutOrderManager stockoutOrderManager;

    /**
     * 快递100回传
     *
     * @param param
     * @return
     * @throws ServiceException
     */
    @Override
    public RawString kd100Callback(String param) throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("[开放物流平台报文-快递100推送信息开始]")
                .addParm("回传报文", param)
                .log();

        // 判断是否处于维护中
//        if (LogisticsConfig.getInstance().getIsPauseAudit() != null && LogisticsConfig.getInstance().getIsPauseAudit()) {
//            return CallbackResponseUtils.setResponse(CallbackResponseUtils.RESPONSE_FALSE, "S12", "系统维护中，请稍后再试。。。");
//        }

        // 响应结果
        KD100CallbackResponse response = new KD100CallbackResponse();
        if (StringUtils.isBlank(param)) {
            return assembleRawString(response, "回传报文为空", false, "500");
        }

        KD100CallbackRequest callbackRequest = JSON.parseObject(param, KD100CallbackRequest.class);
        if (callbackRequest == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[开放物流平台报文-快递100回传信息异常]: 回传报文为空")
                    .addParm("回传报文", param)
                    .log();
            return assembleRawString(response, "回传报文为空", false, "500");
        }

        if (!ABORT.equals(callbackRequest.getStatus()) && CollectionUtils.isEmpty(callbackRequest.getLastResult().getData())) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[开放物流平台报文-快递100回传信息异常]: 回传报文(lastResult.data)为空")
                    .addParm("回传报文", param)
                    .log();
            return assembleRawString(response, "回传报文(lastResult.data)明细为空", false, "500");
        }


        //获取运单号
        String mailNo = callbackRequest.getLastResult().getNu().trim();
        StockoutOrderDO stockOutOrder = null;
        try {
            //查询执行中的出库单
            stockOutOrder  = stockoutOrderManager.getByMailNo(mailNo);
        } catch (ServiceException e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .log();
            throw e;
        }
        if (stockOutOrder == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[开放物流平台-快递100保存路由] 根据运单号找不到对应的出库单")
                    .addParm("mailNo", callbackRequest.getLastResult().getNu())
                    .log();
            return assembleRawString(response, "找不到对应的出库单:" + callbackRequest.getLastResult().getNu(), false, "500");
        }

        LogBetter.instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(TraceLogEntity.instance(traceLogger, stockOutOrder.getBizId(), SystemConstants.TRACE_APP))
                .setMsg("[开放物流平台报文-快递100回传]")
                .addParm("回传报文", param)
                .log();
        //路由类型
        RouteType routeType = null;
        if (mailNo.equals(stockOutOrder.getIntrMailNo())) {
            //只要国内运单号匹配则就确认是国内路由类型，无论国际运单号是否也匹配
            routeType = RouteType.INTERNAL;
        } else {
            routeType = RouteType.INTERNATIONAL;
        }

        // 推送message为“3天查询无记录”或“60天无变化时”、status= abort的提醒，即监控中止。
        try {
            if (ABORT.equals(callbackRequest.getStatus())  &&
                    (callbackRequest.getMessage().contains("3天"))) {
                if (StringUtils.isNotEmpty(callbackRequest.getComNew())) {
                    //有新的承运商编码就不需要再订阅，修改承运商编码
                    String carrierCode = TransCarrierCode.getCarrierCode(callbackRequest.getComNew());
                    stockoutOrderManager.updateCarrierCodeById(stockOutOrder.getId(), routeType, carrierCode);
                } else {
                    //重新订阅KD100
                    sendRegistKD100(stockOutOrder.getBizId(), routeType.getType());
                }
            }
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN)
                    .setMsg("[开放物流平台-快递100处理status=abort异常]")
                    .setParms("[异常信息]", e.getMessage())
                    .setException(e).log();
        }

        // 保存快递信息
        List<LogisticsUserRouteEntity> routeList = new ArrayList<LogisticsUserRouteEntity>();
        for (KD100CallbackItem item : callbackRequest.getLastResult().getData()) {
            LogisticsUserRouteEntity entity = new LogisticsUserRouteEntity();
            entity.orderId = stockOutOrder.getBizId();
            entity.mailNo = callbackRequest.getLastResult().getNu();
            entity.carrierCode = TransCarrierCode.getCarrierCode(callbackRequest.getLastResult().getCom());
            try {
                entity.eventTime = DateUtil.getDatetime(DateUtil.DEF_PATTERN, item.getFtime());
            } catch (Exception e) {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                        .setMsg("[开放物流平台-路由信息时间转换失败]")
                        .setParms("[异常信息]", e.getMessage())
                        .setException(e).log();
            }
            entity.content = item.getContext();
            routeList.add(entity);
        }
        routeService.overrideUserRoute(stockOutOrder.getBizId(), routeType.getType(), routeList);
        return assembleRawString(response, "ok", true, "200");
    }


    /**
     * 快递100响应报文
     *
     * @param response
     * @param message
     * @param res
     * @param returnCode
     * @return
     */
    private RawString assembleRawString(KD100CallbackResponse response, String message, boolean res, String returnCode) {
        response.setMessage(message);
        response.setResult(res);
        response.setReturnCode(returnCode);
        RawString result = new RawString();
        try {
            result.value = JSON.toJSONString(response);
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[开放物流平台快递100回传-对象转换异常]")
                    .setParms("[异常信息]", e.getMessage())
                    .setException(e)
                    .log();
        }
        return result;
    }


    private void sendRegistKD100(String bizId, String routeType) {
        try {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链-注册快递100]:发送出库单信息")
                    .addParm("子单号", bizId)
                    .addParm("routeType", routeType)
                    .log();
            StockoutOrderDO stockoutOrderDO = stockoutOrderManager.getByBizId(bizId);
            if(null == stockoutOrderDO) {
                return;
            }

            Message msg = new Message();
            msg.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_ROUTE_EVENT);
            msg.setTag(MessageConstants.TAG_REGIST_KUAIDI100);
            //消息体没有内容
            msg.setBody(" ".getBytes("UTF-8"));//BODY不能为空
            msg.setStartDeliverTime(System.currentTimeMillis() + 1000 * 30 * 60);

            //向用户属性中赋值
            Properties properties = new Properties();
            properties.put("bizId", bizId);
            properties.put("routeType", routeType);
            msg.setUserProperties(properties);
            supplyChainRouteMessageProducer.send(msg);
        } catch (UnsupportedEncodingException e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.WARN)
                    .setMsg("[供应链-注册快递100]:发送出库单信息异常：" + e.getMessage())
                    .addParm("子单号", bizId)
                    .addParm("routeType", routeType)
                    .log();
        }

    }

}
