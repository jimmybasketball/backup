package com.sfebiz.supplychain.provider.command.send.wms.common;

import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 7.8.	下发退货信息指令  logistics.event.wms.refund	SF -> LP
 * 卖家申请退货是在海外仓库出库前申请退货,仓库收到指令后回传 true 表示同意退货,同意退货后,LP会与卖家线下协商如何退货
 *
 * @author xiaoxu
 */
public class WmsRefundCommand extends WmsOrderCreateCommand {

    private StockoutOrderRecordManager stockoutOrderRecordManager;
    private RouteService routeService;

	@Override
	public boolean execute() {
		
		try {
			
			String meta = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("meta");
			Map<String,Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
			String eventSource = "";
			if(metaData!=null&&metaData.containsKey("event_source")){
				eventSource = (String)metaData.get("event_source");
			}
			stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
            routeService = (RouteService) CommandConfig.getSpringBean("routeService");
			WarehouseBO warehouse = logisticsLineBO.getWarehouseBO();
			
			EventHeader eventHeader = new EventHeader();
			eventHeader.setEventSource(eventSource);
			eventHeader.setEventTarget(warehouse.getWarehouseNid());
			eventHeader.setEventTime(DateUtil.getCurrentDateTime());
			eventHeader.setEventType(EventType.LOGISTICS_BUYER_REFUND.value);
			
			TradeOrder tradeOrder = new TradeOrder();
			tradeOrder.setTradeOrderId(stockoutOrderBO.getId());
			
			
			List<TradeOrder> tradeOrders = new ArrayList<TradeOrder>();
			tradeOrders.add(tradeOrder);
			
			
			TradeDetail tradeDetail = new TradeDetail();
			tradeDetail.setTradeOrders(tradeOrders);

			List<LogisticsOrder> logisticsOrders = new ArrayList<LogisticsOrder>();
			
			for(StockoutOrderDetailBO logisticsEntity:stockoutOrderDetailBOs){
				
				LogisticsOrder logisticsOrder = new LogisticsOrder();
				logisticsOrder.setPoNo(stockoutOrderBO.getId()+"M"+logisticsOrder.getMailNo());
				logisticsOrder.setOccurTime(DateUtil.getCurrentDateTime());
				logisticsOrder.setLogisticsRemark("退货");
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
			
			LogisticsEventsRequest req = new LogisticsEventsRequest();
			req.setLogisticsEvent(logisticsEvent);
			
			String msgType = WmsMessageType.REFUND_GOODS.getValue();
			String url = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceUrl");
			String key = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO().getInterfaceMeta().get("interfaceKey");
			if(StringUtils.isEmpty(url)||StringUtils.isEmpty(key))return false;
			LogisticsEventsResponse responses = ProviderBizService.getInstance().send(req, msgType, url, key,
					TraceLogEntity.instance(traceLogger,stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP));
			Response response = responses.getResponseItems().get(0);
			if("true".equals(response.success)){
				stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS.getValue());
				stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),LogisticsState.LOGISTICS_STATE_CANCEL_SUCCESS.getValue());
                writeRefundCommandSuccessRoute();
				return true;
			}else{
                writeRefundCommandErrorRoute(responses.getResponseItems().get(0).getReasonDesc());
			}
		} catch (BeansException e) {
            writeRefundCommandErrorRoute(e.getMessage());
			logger.error("Exception", e);
			return false;
		} catch (ServiceException e) {
            writeRefundCommandErrorRoute(e.getMessage());
			logger.error("Exception", e);
			return false;
		}
		return false;
	}

    /**
     * 记录失败路由
     *
     * @param errMsg
     */
    private void writeRefundCommandErrorRoute(String errMsg) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "下发退货信息指令失败," + errMsg, SystemConstants.WARN_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 记录发送异议消息成功                   `
     *
     */
    private void writeRefundCommandSuccessRoute() {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "下发退货指令成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

}
