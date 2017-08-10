package com.sfebiz.supplychain.provider.command.send.wms.coe;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCancelCommand;
import com.sfebiz.supplychain.provider.entity.ResponseState;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseLogisticsProviderBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 下发取消发货指令 logistics.event.wms.cancel SF -> LP 卖家发货后由于一些原因取消发货了,需要通知LP那笔交易取消发货
 * 了,取消发货的时间必须是LP审单之前,如果仓库在取消订单后收到了货,则直接拒收。
 * 
 * @author xiaoxu
 */
public class COECancelCommand extends WmsOrderCancelCommand {

	private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory
			.getTraceLogger("order");
	private StockoutOrderManager stockoutOrderManager;
	private StockoutOrderRecordManager stockoutOrderRecordManager;
	private RouteService routeService;

	@Override
	public boolean execute() {
		try {

			// 1. 判断状态
			if (stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS
					.getValue()) {
				return true;
			}

			// 2. 获取相关信息
			WarehouseLogisticsProviderBO logisticsProviderBO = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO();

			stockoutOrderManager = (StockoutOrderManager) CommandConfig
					.getSpringBean("stockoutOrderManager");
			stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig
					.getSpringBean("stockoutOrderRecordManager");
			routeService = (RouteService) CommandConfig
					.getSpringBean("routeService");
			String meta = logisticsProviderBO.getInterfaceMeta().get("meta");
			Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
			String eventSource = StringUtils.EMPTY;
			if (metaData != null && metaData.containsKey("event_source")) {
				eventSource = (String) metaData.get("event_source");
			}
			String url = logisticsProviderBO.getInterfaceMeta().get("interfaceUrl");
			String key = logisticsProviderBO.getInterfaceMeta().get("interfaceKey");
			if (StringUtils.isBlank(url) || StringUtils.isBlank(key)) {
				LogBetter
						.instance(logger)
						.setLevel(LogLevel.INFO)
						.setTraceLogger(
								TraceLogEntity.instance(traceLogger,
										stockoutOrderBO.getBizId(),
										SystemConstants.TRACE_APP))
						.setMsg("[供应链-向COE下发取消发货指令] 异常，缺少指令相关配置信息（interfaceUrl or interfaceKey）")
						.addParm("订单号：", stockoutOrderBO.getBizId()).log();
				return false;
			}

			// 3. 下发指令
			LogisticsEventsRequest request = buildCancelDeliverRequest(
					eventSource, logisticsLineBO.getWarehouseBO());
			LogisticsEventsResponse responses = ProviderBizService.getInstance().send(
					request,
					WmsMessageType.CANCEL_DELIVER.getValue(),
					url,
					key,
					TraceLogEntity.instance(traceLogger,
					stockoutOrderBO.getBizId(),
					SystemConstants.TRACE_APP));

			LogBetter
					.instance(logger)
					.setLevel(LogLevel.INFO)
					.setTraceLogger(
							TraceLogEntity.instance(traceLogger,
									stockoutOrderBO.getBizId(),
									SystemConstants.TRACE_APP))
					.setMsg("[供应链-向COE下发取消发货指令] 请求发送成功: ")
					.addParm("订单号", stockoutOrderBO.getBizId())
					.addParm("request", request)
					.addParm("responses", responses).log();

			// 4. 处理返回信息
			if (responses == null
					|| CollectionUtils.isEmpty(responses.responseItems)) {
				logger.error(
						"[供应链-向COE下发取消发货指令] 请求返回信息为空: bizId={}, request={}, responses={}",
						stockoutOrderBO.getBizId(), request, responses);
				return false;
			}

			Response response = responses.getResponseItems().get(0);
			if (ResponseState.TRUE.getCode().equalsIgnoreCase(response.success)) {

				stockoutOrderBO.getRecordBO()
						.setLogisticsState(LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS
								.getValue());
				stockoutOrderRecordManager.updateLogisticsState(
						stockoutOrderBO.getId(),
						LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS
								.getValue());
				writeCancelCommandSuccessLog();
				LogBetter
						.instance(logger)
						.setLevel(LogLevel.INFO)
						.setTraceLogger(
								TraceLogEntity.instance(traceLogger,
										stockoutOrderBO.getBizId(),
										SystemConstants.TRACE_APP))
						.setMsg("[供应链-向COE下发取消发货指令] 仓库取消成功").log();
				return true;

			} else {

				writeCancelCommandFailureLog(responses.getResponseItems()
						.get(0).getReasonDesc());
				LogBetter
						.instance(logger)
						.setLevel(LogLevel.INFO)
						.setTraceLogger(
								TraceLogEntity.instance(traceLogger,
										stockoutOrderBO.getBizId(),
										SystemConstants.TRACE_APP))
						.setMsg("[供应链-向COE下发取消发货指令] 仓库取消失败").log();
				return false;

			}
		} catch (Exception e) {
			LogBetter.instance(logger).setLevel(LogLevel.ERROR)
					.setMsg("[供应链-向COE下发取消发货指令] 异常：")
					.setParms("bizId", stockoutOrderBO.getBizId())
					.setParms("e", e).log();
			return false;
		}
	}

	/**
	 * 构建取消发货指令请求 COE区分 自营发货&转运发货
	 * 
	 * @param eventSource
	 * @param warehouse
	 * @return
	 */
	private LogisticsEventsRequest buildCancelDeliverRequest(String eventSource, WarehouseBO warehouse) {
		EventHeader eventHeader = new EventHeader();
		eventHeader.setEventSource(eventSource);
		eventHeader.setEventTarget(warehouse.getWarehouseNid());
		eventHeader.setEventTime(DateUtil.getCurrentDateTime());
		if (stockoutOrderBO.getOrderType() == StockoutOrderType.SALES_STOCK_OUT.value) {
			eventHeader.setEventType(EventType.LOGISTICS_CANCEL.value);
		} else {
			eventHeader.setEventType(EventType.LOGISTICS_STOCKIN_CANCEL.value);
		}

		TradeOrder tradeOrder = new TradeOrder();
		tradeOrder.setTradeOrderId(stockoutOrderBO.getId());
		List<TradeOrder> tradeOrders = new ArrayList<TradeOrder>();
		tradeOrders.add(tradeOrder);
		TradeDetail tradeDetail = new TradeDetail();
		tradeDetail.setTradeOrders(tradeOrders);

		List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
		for (StockoutOrderDetailBO item : stockoutOrderDetailBOList) {
			LogisticsOrder logisticsOrder = new LogisticsOrder();
			logisticsOrder.setPoNo(stockoutOrderBO.getId() + "M"+ stockoutOrderBO.getIntrMailNo());
			logisticsOrder.setOccurTime(DateUtil.getCurrentDateTime());
			logisticsOrders.add(logisticsOrder);
		}

		LogisticsDetail logisticsDetail = new LogisticsDetail();
		logisticsDetail.setLogisticsOrders(logisticsOrders);

		EventBody eventBody = new EventBody();
		eventBody.setTradeDetail(tradeDetail);
		eventBody.setLogisticsDetail(logisticsDetail);

		LogisticsEvent logisticsEvent = new LogisticsEvent();
		logisticsEvent.setEventHeader(eventHeader);
		logisticsEvent.setEventBody(eventBody);

		LogisticsEventsRequest request = new LogisticsEventsRequest();
		request.setLogisticsEvent(logisticsEvent);
		return request;
	}

	/**
	 * 记录取消发货失败日志
	 * 
	 * @param errMsg
	 */
	private void writeCancelCommandFailureLog(String errMsg) {
		routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "仓库订单取消失败,"
				+ errMsg, SystemConstants.WARN_LEVEL, new Date(),
				SystemUserName.OPSC.getValue());
	}

	/**
	 * 记录下发取消发货指令成功日志 `
	 * 
	 */
	private void writeCancelCommandSuccessLog() {
		routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "仓库订单取消成功",
				SystemConstants.INFO_LEVEL, new Date(),
				SystemUserName.OPSC.getValue());
	}
}
