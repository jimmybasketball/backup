package com.sfebiz.supplychain.provider.command.send.wms.coe;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.send.wms.WmsTransferOutOrderCreateCommand;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsRequest;
import com.sfebiz.supplychain.sdk.protocol.LogisticsEventsResponse;
import com.sfebiz.supplychain.sdk.protocol.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * COE下发调拨出库单到仓库
 */
public class COETransOutOrderCreateCommand extends WmsTransferOutOrderCreateCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    @Override
    public boolean execute() {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("COE 下发调拨出库单消息指令: start")
                .addParm("调拨出库单信息", stockoutOrderBO).log();

        // 是否使用mock方式
        boolean isMockAutoCreated = MockConfig.isMocked("coe", "createCommand");
        if (isMockAutoCreated) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("MOCK：COE仓库 下发调拨出库单指令 采用MOCK实现").log();
            return mockWmsTransferOutOrderCreateSuccess();
        }

        // 判断物流状态(0 INIT 1 物流下单成功 2 物流拒单 3 海外仓接受订单 4 海外仓包裹收齐 5 海外包裹异常 6 海外仓称重回传 7回传仓内异常信息 8 海外仓运费回传 9 下发发货指令成功 10 海外仓已发货 11 取消订单 12 取消成功 13 物流取消失败 14 物流下单失败 15 物流发货失败)
        if (stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue()
                || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_GOODS_WEIGHT.getValue()
                || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue()
                || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_STOCKOUT.getValue()) {
            return true;
        }

        boolean result = false;
        try {
            String msgType = WmsMessageType.TRANSFER_STOCK_OUT.getValue();
            String url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
            String key = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
                throw new Exception("路线配置错误" + logisticsLineBO.getLogisticsLineNid());
            }
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("[COE下发调拨出库单]")
                    .addParm("线路信息", logisticsLineBO).addParm("wmsProviderEntity", logisticsLineBO.getWarehouseBO().getLogisticsProviderBO())
                    .addParm("url信息", url).addParm("interfaceKey", key).log();

            // 构建请求
            LogisticsEventsRequest request = buildCreateTransferOutOrderCommandRequest();
            // 下发指令
            LogisticsEventsResponse responses = ProviderBizService.getInstance().send(request, msgType, url, key,
                    TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP));

            // 处理接口响应
            List<Response> responseList = responses.getResponseItems();
            if (CollectionUtils.isEmpty(responseList)) {
                throw new Exception("COE下发调拨出库单反馈报文异常");
            }
            result = processResponse(responseList.get(0));
        } catch (Exception e) {
            writeCreateCommandFailureLog(e.getMessage());
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getId(), "supplychain"))
                    .setException(e)
                    .setMsg("[供应链报文-向COE仓库下发调拨出库单指令异常]: \"\n"
                            + "                            + \"[订单ID:\" + stockoutOrderBO.getBizId()\n"
                            + "                            + \", 异常信息: \" + e.getMessage()\n"
                            + "                            + \"]")
                    .addParm("调拨单号", stockoutOrderBO.getBizId())
                    .addParm("调拨出库单ID:", stockoutOrderBO.getId()).log();
        } finally {
            LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("COE 下发调拨出库单消息指令: end")
                    .addParm("调拨出库单信息", stockoutOrderBO).log();
        }
        return result;
    }
}
