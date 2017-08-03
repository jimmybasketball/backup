package com.sfebiz.supplychain.provider.command.send.tpl.kd100;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.route.entity.LogisticsUserRouteEntity;
import com.sfebiz.supplychain.protocol.kd100.KD100CallbackItem;
import com.sfebiz.supplychain.protocol.kd100.KD100HttpUtil;
import com.sfebiz.supplychain.protocol.kd100.KD100QueryResponse;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderGetRoutesCommand;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.MD5;
import com.sfebiz.supplychain.util.TransCarrierCode;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 实时查询快递100路由信息
 * User: <a href="mailto:zhang.yajing@ifunq.com">张雅静</a>
 * Version: 1.0.0
 * Since: 2017/2/8  15:23
 */
public class KD100GetRouteCommand extends TplOrderGetRoutesCommand{

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    @Override
    public boolean execute() throws ServiceException {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("[kd100]路由信息查询")
                .addParm("订单ID", orderId)
                .log();
        if (!checkParam()) {
            return false;
        }

        if (MockConfig.isMocked("kd", "tplOrderGetRoutesCommand")) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("MOCK获取快递100路由")
                    .setParms(orderId)
                    .log();
            return true;
        }

        return queryRoute();
    }

    /**
     * 检查参数信息是否存在；
     *
     * @return
     */
    private boolean checkParam() {
        if (StringUtils.isBlank(orderId)) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("快递100信息查询失败-订单为null").log();
            return false;
        }
        if (StringUtils.isEmpty(mailNo) || StringUtils.isEmpty(carrierCode)) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("快递100信息查询-运单号或承运商编码为空")
                    .addParm("订单:", orderId)
                    .log();
            return false;
        } else if (StringUtils.isNotEmpty(mailNo) && mailNo.length() > 32) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("快递100信息查询-运单号长度超过32位")
                    .addParm("订单:", orderId)
                    .log();
            return false;
        }
        return true;
    }

    private boolean queryRoute() {
        String kdCarrierCode = TransCarrierCode.getKDCarrierCode(carrierCode);
        if (StringUtils.isEmpty(kdCarrierCode)) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("快递100信息订阅-找不到对应的承运商编码,请检查是否配置")
                    .addParm("订单:", orderId)
                    .addParm("承运商编码", carrierCode)
                    .log();
            return false;
        }

        String param = "{\"num\":\"" + mailNo +"\",\"com\":\""+ kdCarrierCode +"\"}";
        String sign = MD5.encode(param
                + LogisticsProviderConfig.getKD100RequestQueryKey()
                + LogisticsProviderConfig.getKD100RequestCustomer());

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("param",param);
        params.put("sign",sign);
        params.put("customer",LogisticsProviderConfig.getKD100RequestCustomer());

        try {
            String resp = KD100HttpUtil.postData(LogisticsProviderConfig.getKD100QueryUrl(), params, "UTF-8").toString();
            if (StringUtils.isEmpty(resp)) {
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, orderId, SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-快递100实时查询失败]")
                        .addParm("请求报文", JSONUtil.toJson(params))
                        .addParm("回复报文", resp)
                        .log();
                return false;
            } else {
                KD100QueryResponse response = JSONUtil.parseJSONMessage(resp, KD100QueryResponse.class);
                //200: 提交成功
                //400: 提交的数据不完整，或者贵公司没授权
                //500: 表示查询失败，或没有POST提交
                //501: 服务器错误，快递100服务器压力过大或需要升级，暂停服务
                //502: 服务器繁忙，详细说明见2.2《查询接口并发协议》
                //503: 验证签名失败

                List<LogisticsUserRouteEntity> routeList = new ArrayList<LogisticsUserRouteEntity>();
                if(response.getState() != null && response.getData() != null ){
                    for(KD100CallbackItem item : response.getData()) {
                        LogisticsUserRouteEntity entity = new LogisticsUserRouteEntity();
                        entity.orderId = orderId;
                        entity.mailNo = mailNo;
                        entity.carrierCode = TransCarrierCode.getCarrierCode(kdCarrierCode);
                        try {
                            entity.eventTime = DateUtil.getDatetime(DateUtil.DEF_PATTERN, item.getFtime());
                        } catch (Exception e) {
                            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                                    .setMsg("[供应链-路由信息时间转换失败]")
                                    .setParms("[异常信息]", e.getMessage())
                                    .setException(e).log();
                        }
                        entity.content = item.getContext();
                        routeList.add(entity);
                    }
                }

                setRoutes(routeList);

                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, orderId, SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-快递100实时查询]")
                        .addParm("请求报文", JSONUtil.toJson(params))
                        .addParm("回复报文", resp)
                        .log();
            }
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-快递100实时查询异常]: " + e.getMessage())
                    .addParm("订单", orderId)
                    .log();
            return false;
        }
        return true;
    }
}
