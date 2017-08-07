package com.sfebiz.supplychain.provider.command.send.wms.coe;

import com.sfebiz.supplychain.provider.command.send.wms.WmsGalOrderConfirmCommand;

/**
 * Created by wang_cl on 2015/4/13.
 */
public class COEGalOrderConfirmCommand extends WmsGalOrderConfirmCommand {

    @Override
    public boolean execute() {
//        LogBetter.instance(logger)
//                .setLevel(LogLevel.INFO)
//                .setMsg("COE 下发损溢确认消息指令: start")
//                .addParm("损溢单信息", this.getGalOrderDO())
//                .log();
//        try {
//            if (this.getGalOrderDO().getState().equals(GalOrderState.WAIT_PROCESS.getValue()) || this.getGalOrderDO().getState().equals(GalOrderState.FINISHED.getValue())) {
//                LogBetter.instance(logger)
//                        .setLevel(LogLevel.INFO)
//                        .setMsg("COE 下发损溢确认消息指令: 损溢单状态为已下发仓库确认损溢消息")
//                        .addParm("损溢单信息", this.getGalOrderDO())
//                        .log();
//                return true;
//            }
//
//            boolean isMockAutoCreated = MockConfig.isMocked("coe", "galOrderConfirmCommand");
//            if (isMockAutoCreated) {
//                //直接返回仓库已发货
//                logger.info("MOCK：COE 下发损溢确认消息指令 采用MOCK实现");
//                return true;
//            }
//
//            String msgType = WmsMessageType.GAL_ORDER_CONFIRM.getValue();
//            String url = getLogisticsProviderBO().getInterfaceUrl();
//            String key = getLogisticsProviderBO().getInterfaceKey();
//            if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
//                return false;
//            }
//            LogisticsEventsRequest request = buildGalOrderConfirmCommandRequest();
//
//            //下发指令
//            LogisticsEventsResponse responses = ProviderBO.getInstance().send(request, msgType, url, key, null);
//
//            if (responses.getResponseItems() == null || responses.getResponseItems().size() == 0) {
//                return false;
//            }
//
//            Response response = responses.getResponseItems().get(0);
//            if (ResponseState.TRUE.getCode().equalsIgnoreCase(response.success)) {
//                return true;
//            }
//        } catch (Exception e) {
//            LogBetter.instance(logger)
//                    .setLevel(LogLevel.ERROR)
//                    .setException(e)
//                    .setMsg("COE 下发损溢确认消息指令:失败")
//                    .addParm("损溢单信息", this.getGalOrderDO())
//                    .log();
//        } finally {
//            LogBetter.instance(logger)
//                    .setLevel(LogLevel.INFO)
//                    .setMsg("COE 下发损溢确认消息指令: end")
//                    .addParm("损溢单信息", this.getGalOrderDO())
//                    .log();
//        }
        return false;
    }

//    /**
//     * 构建
//     *
//     * @return
//     */
//    public LogisticsEventsRequest buildGalOrderConfirmCommandRequest() {
//        String meta = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta");
//        Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
//        String eventSource = "";
//        if (metaData != null && metaData.containsKey("event_source")) {
//            eventSource = (String) metaData.get("event_source");
//        }
//        WarehouseBO warehouse = this.getWarehouseBO();
//
//        EventHeader eventHeader = new EventHeader();
//        eventHeader.setEventSource(eventSource);
//        eventHeader.setEventTarget(warehouse.getWarehouseNid());
//        eventHeader.setEventTime(DateUtil.getCurrentDateTime());
//        eventHeader.setEventType(EventType.WMS_GALORDER_CONFIRM.value);
//
//        EventBody eventBody = new EventBody();
//        eventBody.setReferenceNo(this.getGalOrderDO().getId() + "");
//        eventBody.setOrderType(this.getGalOrderDO().getBizType());
//        eventBody.setOrderNo(this.getGalOrderDO().getBizNo());
//
//        LogisticsEvent logisticsEvent = new LogisticsEvent();
//        logisticsEvent.setEventHeader(eventHeader);
//        logisticsEvent.setEventBody(eventBody);
//
//        LogisticsEventsRequest request = new LogisticsEventsRequest();
//        request.setLogisticsEvent(logisticsEvent);
//        return request;
//    }
}
