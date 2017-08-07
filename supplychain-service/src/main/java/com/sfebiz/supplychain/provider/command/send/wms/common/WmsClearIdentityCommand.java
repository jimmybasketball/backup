package com.sfebiz.supplychain.provider.command.send.wms.common;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.provider.biz.ProviderBizService;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderClearIdentityCommand;
import com.sfebiz.supplychain.sdk.protocol.*;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import net.pocrd.entity.ServiceException;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WmsClearIdentityCommand extends WmsOrderClearIdentityCommand {

	private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
	
	/**
	 *  7.12.	向CMSP下发身份证信息指令
	 * 	服务名称	logistics.event.wms.tm.clearidentity
	 *	消息类型	TMS_CLEAR_IDENTITY_INFO
	 *	消息流向	SF -> LP
	 *	服务调用方	SF
	 *
	 */
	@Override
	public boolean execute() {
		try {
			
			String meta = getLpDetail().getInterfaceMeta().get("meta");
			Map<String,Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
			String eventSource = "";
			if(metaData!=null&&metaData.containsKey("event_source")){
				eventSource = (String)metaData.get("event_source");
			}
			WarehouseBO warehouse = stockoutOrderBO.getLineBO().getWarehouseBO();
			
			EventHeader eventHeader = new EventHeader();
			eventHeader.setEventType(EventType.TMS_CLEAR_IDENTITY_INFO.value);
			eventHeader.setEventTime(DateUtil.getCurrentDateTime());
			eventHeader.setEventSource(eventSource);
			eventHeader.setEventTarget(warehouse.getWarehouseNid());
			
			TradeOrder tradeOrder = new TradeOrder();
			tradeOrder.setTradeOrderId(stockoutOrderBO.getId());
			
			Buyer buyer = new Buyer();
			//buyer.setName(name);
			tradeOrder.setBuyer(buyer);
			
			List<TradeOrder> tradeOrders = new ArrayList<TradeOrder>();
			tradeOrders.add(tradeOrder);
			
			TradeDetail tradeDetail = new TradeDetail();
			tradeDetail.setTradeOrders(tradeOrders);

			EventBody eventBody = new EventBody();
			eventBody.setTradeDetail(tradeDetail);
			
			LogisticsEvent logisticsEvent = new LogisticsEvent();
			logisticsEvent.setEventHeader(eventHeader);
			logisticsEvent.setEventBody(eventBody);
			
			LogisticsEventsRequest req = new LogisticsEventsRequest();
			req.setLogisticsEvent(logisticsEvent);
			
			String msgType = "zebra.logistics.event.tms.clearidentity";
			String url = getLpDetail().getInterfaceMeta().get("interfaceUrl");
			String key = getLpDetail().getInterfaceMeta().get("interfaceKey");
			if(StringUtils.isEmpty(url)|| StringUtils.isEmpty(key))return false;
			LogisticsEventsResponse responses = ProviderBizService.getInstance().send(req,msgType,url,key,
					TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP));
			Response response = responses.getResponseItems().get(0);
			if("true".equals(response.success)){
				return true;
			}else{
//				StockoutOrderErrorDO dto = new StockoutOrderErrorDO();
//				dto.setTradeOrderId(this.getOrderDetail().getStockoutOrder().getId());
//				dto.setOrderId(this.getOrderDetail().getStockoutOrder().getBizId());
//				dto.setMessage("下发身份证指令失败");
//				dto.setCode(response.reason);
//				dto.setRemark(response.reasonDesc);
			}
		} catch (BeansException e) {
			logger.error("Exception", e);
			return false;
		} catch (ServiceException e) {
			logger.error("Exception", e);
			return false;
		}
		return false;
	}
}
