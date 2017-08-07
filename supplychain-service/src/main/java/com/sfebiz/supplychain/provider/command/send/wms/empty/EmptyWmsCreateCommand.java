package com.sfebiz.supplychain.provider.command.send.wms.empty;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;

/**
 * 广州保税局,海外通接口(下发订单)
 * 
 * @author li.hui
 *
 */
public class EmptyWmsCreateCommand extends WmsOrderCreateCommand {
	private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
	public boolean execute() {
//        logger.info("添加订单成功,orderId :" + stockoutOrderBO.getBizId());
//        StockoutOrderManager stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
//        StockoutOrderRecordManager stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
//        MessageProducer supplyChainMessageProducer = (MessageProducer) CommandConfig.getSpringBean("supplyChainMessageProducer");
//        stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(), LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
//        StockoutOrderRouteDO routeDO = new StockoutOrderRouteDO();
//        routeDO.setStockoutOrderId(stockoutOrderBO.getId());
//        routeDO.setBizId(stockoutOrderBO.getBizId());
//        routeDO.setEventTime(new Date());
//        routeDO.setMailNo(stockoutOrderBO.getMailNo());
//        routeDO.setCarrierCode("SF");
//        routeDO.setRouteMsg("仓物流下单成功");
//        routeDO.setRouteType(StockoutOrderRouteType.SFHT.getValue());
//        routeDO.setCode(StockoutOperation.ORDER_CREATE_COMMAND.getOpCode());
//        routeDO.setCodeDesc(StockoutOperation.ORDER_CREATE_COMMAND.getOpDesc());
//        routeDO.setState(stockoutOrderBO.getState());
//        routeDO.setSubMsg("仓库下单成功");
//        routeDO.setUserName(SystemUserName.SFHT.getValue());
//        writeLogisticsStockoutRoute(routeDO);
//        String message = "物流下单成功";
//
//        writeLogisticsStockoutRoute(routeDO);
//        traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO, "[供应链报文状态]下发出库单给仓库：物流下单成功"));
//
//        try {
//            Message msg = new Message();
//            msg.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_EVENT);
//            msg.setTag(MessageConstants.TAG_STOCKOUT_FINISH);
//            msg.setBody(" ".getBytes());
//            Properties properties = new Properties();
//            properties.put("stockoutOrderId", stockoutOrderBO.getId());
//            msg.setUserProperties(properties);
//            //延迟120s下发出库命令，用于在这段时间对订单进行个性化配置
//            if (LogisticsConfig.getInstance().getSendStockoutFinishDelayTime() == null) {
//                msg.setStartDeliverTime(System.currentTimeMillis() + 120000);
//            } else {
//                msg.setStartDeliverTime(System.currentTimeMillis() + LogisticsConfig.getInstance().getSendStockoutFinishDelayTime());
//            }
//            supplyChainMessageProducer.send(msg);
//            LogBetter.instance(logger).setLevel(LogLevel.INFO)
//                    .setMsg("接收订单出库消息成功")
//                    .addParm("出库单ID", stockoutOrderBO.getId())
//                    .log();
//        } catch (Exception e) {
//            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
//                    .setException(e)
//                    .setMsg("接收订单出库消息失败")
//                    .addParm("出库单ID", stockoutOrderBO.getId())
//                    .setException(e)
//                    .log();
//        }

        return true;
	}


}
