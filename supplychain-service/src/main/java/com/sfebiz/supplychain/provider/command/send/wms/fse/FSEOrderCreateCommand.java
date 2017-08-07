package com.sfebiz.supplychain.provider.command.send.wms.fse;

import com.alibaba.fastjson.JSON;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.exposed.common.enums.SystemUserName;
import com.sfebiz.supplychain.exposed.route.api.RouteService;
import com.sfebiz.supplychain.exposed.stockout.enums.LogisticsState;
import com.sfebiz.supplychain.persistence.base.common.manager.NationManager;
import com.sfebiz.supplychain.persistence.base.port.manager.PortParamManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDetailDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDeclarePriceDetailManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.protocol.wms.fse.*;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderCreateCommand;
import com.sfebiz.supplychain.service.lp.model.LogisticsProviderBO;
import com.sfebiz.supplychain.service.sku.model.SkuDeclareBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.NumberUtil;
import net.pocrd.entity.ServiceException;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 下发物流订单消息指令 logistics.event.wms.create SF -> LP
 * 待发货:待出库:出库单已生成，等待下发给仓库
 * <p/>
 * LOGISTICS_SKU_PAID
 */
public class FSEOrderCreateCommand extends WmsOrderCreateCommand {

    private static String methodName = "pushSaleOrder";

    private StockoutOrderManager stockoutOrderManager;
    private StockoutOrderRecordManager stockoutOrderRecordManager;
    private NationManager nationManager;
    private StockoutOrderDeclarePriceDetailManager skuDeclarePriceManager;
    private PortParamManager portParamManager;
    private RouteService routeService;
//    private ProviderSkuManager providerSkuManager;
    @Override
    public boolean execute() throws ServiceException {
        logger.info("费舍尔 下发物流订单消息指令: start");
        try{
            if (stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue()
                    || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_GOODS_WEIGHT.getValue()
                    || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_SEND_SUCCESS.getValue()
                    || stockoutOrderBO.getRecordBO().getLogisticsState() == LogisticsState.LOGISTICS_STATE_STOCKOUT.getValue()) {
                return true;
            }
            stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
            stockoutOrderRecordManager = (StockoutOrderRecordManager) CommandConfig.getSpringBean("stockoutOrderRecordManager");
//            providerSkuManager = (ProviderSkuManager) CommandConfig.getSpringBean("providerSkuManager");
            routeService = (RouteService) CommandConfig.getSpringBean("routeService");
            nationManager = (NationManager) CommandConfig.getSpringBean("nationManager");
            skuDeclarePriceManager = (StockoutOrderDeclarePriceDetailManager) CommandConfig.getSpringBean("stockoutOrderSkuDeclarePriceManager");
            portParamManager = (PortParamManager) CommandConfig.getSpringBean("portParamManager");
            boolean isMockAutoCreated = MockConfig.isMocked("fse", "createCommand");
            if (isMockAutoCreated) {
                //直接返回仓库已发货
                logger.info("MOCK：费舍尔仓库 物流下单 采用MOCK实现");
                return mockWarehouseStockoutCreateSuccess();
            }

            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("[海外物流商费舍尔下发物流下单]")
                    .addParm("线路信息", logisticsLineBO)
                    .addParm("wmsProviderEntity", logisticsLineBO.getWarehouseBO().getLogisticsProviderBO())
                    .addParm("出库单信息", stockoutOrderBO)
                    .log();

            LogisticsProviderBO logisticsProviderBO = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO();
            String meta = logisticsProviderBO.getInterfaceMeta().get("meta");

            Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
            String warehouseCode = "";
            String companyCode = "";
            String version = "";
            String ip = "";
            String sessionKey = "";
            if (metaData != null && metaData.containsKey("version") && metaData.containsKey("sessionKey") && metaData.containsKey("ip")
                    && metaData.containsKey("warehouseCode") && metaData.containsKey("companyCode")) {
                version = (String) metaData.get("version");
                ip = (String) metaData.get("ip");
                sessionKey = (String) metaData.get("sessionKey");
                warehouseCode = (String) metaData.get("warehouseCode");
                companyCode = (String) metaData.get("companyCode");
            } else {
                throw new Exception("费舍尔logisticsprovider meta信息不全" + meta);
            }

            FSEOrderItem orderItem = buildOrder(companyCode, warehouseCode);
            return send(orderItem, version, ip, sessionKey);
        }catch (Exception e){
            writeCreateCommandFailureLog(e.getMessage());
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                            TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .setMsg("海外物流商下发物流下单失败")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .log();
            this.setCreateFailureMessage("[供应链-海外物流商下发物流下单]" + e.getMessage());
        }
        return false;
    }

    public FSEOrderItem buildOrder(String companyCode, String warehouseCode) throws Exception {

        FSEOrderItem orderItem = new FSEOrderItem();
        Order order = new Order();
        String originCode = "";
        List<FSEOrderDtl> orderDtls = new ArrayList<FSEOrderDtl>();

        Integer tariffAmount = 0;
        Integer addedValueTax = 0;
        Integer consumptionDutyAmount = 0;
        for(StockoutOrderDetailBO stockoutOrderDetailBO: stockoutOrderDetailBOs){
            StockoutOrderDeclarePriceDetailDO skuDeclarePriceDO = skuDeclarePriceManager.getSkuDeclareDOByStockoutIdAndSkuId(stockoutOrderDetailBO.getStockoutOrderId(), stockoutOrderDetailBO.getSkuId());
            tariffAmount += skuDeclarePriceDO.getTariffTax();
            addedValueTax += skuDeclarePriceDO.getAddedValueTax();
            consumptionDutyAmount += skuDeclarePriceDO.getConsumptionDutyTax();
            FSEOrderDtl orderDtl = new FSEOrderDtl();
            orderDtl.commodityCode = stockoutOrderDetailBO.getSkuId()+"";
            orderDtl.batch = stockoutOrderDetailBO.getSkuBatch();

            // TODO
//            logger.info("联集provider："+logisticsLineBO.providerId);
//            //如果是供应商的商品，则取供应商对应的sku
//            if(lineEntity.providerId >0){
//                ProviderSkuDO providerSkuDO = providerSkuManager.queryBySkuIdAndProviderId(stockoutOrderDetailBO.getSkuId(), lineEntity.providerId);
//                logger.info("联集providerSkuDO："+providerSkuDO);
//                if(providerSkuDO !=null && StringUtils.isNotBlank(providerSkuDO.getThirdSkuId())){
//                    orderDtl.commodityCode = providerSkuDO.getThirdSkuId();
//                }
//            }

            orderDtl.commodityName = stockoutOrderDetailBO.getSkuName();
            SkuDeclareBO skuDeclareBO = skuDeclareBOMap.get(stockoutOrderDetailBO.getSkuId());
            orderDtl.commodityBarcode = skuDeclareBO.getSingleBarCode();
            orderDtl.qty = stockoutOrderDetailBO.getQuantity();
            if(null == skuDeclareBO.getGrossWeight() || null == skuDeclareBO.getNetWeight()){
                throw new ServiceException(
                        LogisticsReturnCode.PRODUCT_ATTRIBUTE_VALUE_NOT_EXIST);
            }
            if(Double.parseDouble(skuDeclareBO.getNetWeight()) > Double.parseDouble(skuDeclareBO.getGrossWeight())) {
                throw new ServiceException(LogisticsReturnCode.PRODUCT_GROSSWEIGHT_ERROR);
            }
            Double grossWeight = NumberUtil.roundDown(Double.parseDouble(skuDeclareBO.getGrossWeight()) * stockoutOrderDetailBO.getQuantity());
            orderDtl.weight = grossWeight + "";
            orderDtl.tradePrice = NumberUtil.defaultParsePriceFeng2Yuan(skuDeclarePriceDO.getDeclarePrice(), 2);
            orderDtl.tradeTotal = NumberUtil.roundDown(Double.parseDouble(NumberUtil.defaultParsePriceFeng2Yuan(skuDeclarePriceDO.getDeclarePrice(), 2)) * stockoutOrderDetailBO.getQuantity()) + "";
            orderDtl.declPrice = NumberUtil.defaultParsePriceFeng2Yuan(skuDeclarePriceDO.getDeclarePrice(), 2);
            orderDtl.declTotalPrice = NumberUtil.roundDown(Double.parseDouble(NumberUtil.defaultParsePriceFeng2Yuan(skuDeclarePriceDO.getDeclarePrice(), 2)) * stockoutOrderDetailBO.getQuantity()) + "";
            orderDtl.codeTs = skuDeclareBO.getHsCode();
            String unit = skuDeclareBO.getSecondMeasuringUnit();
            if(StringUtils.isBlank(unit)){
                orderDtl.secondUnit  = "无";
            } else {
                orderDtl.secondUnit = portParamManager.getPortParamCode(Long.valueOf(PortNid.CHONGQING.getValue()), 0, skuDeclareBO.getSecondMeasuringUnit(), false);
            }
            orderDtl.taxDtl = NumberUtil.defaultParsePriceFeng2Yuan(skuDeclarePriceDO.getTotalTax(), 2);
            originCode = nationManager.getNationCaCode(stockoutOrderDetailBO.getSkuMerchantBO().getOriginLand());
            orderDtls.add(orderDtl);
        }
        order.setOrderDtls(orderDtls);
        order.orderCode = stockoutOrderBO.getBizId();
        order.companyCode = companyCode;
        order.orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stockoutOrderBO.getGmtCreate());
        order.outType = "SHB";
        order.senderName = stockoutOrderBO.getMerchantPackageMaterialBO().getSenderName();
        order.senderTel = stockoutOrderBO.getMerchantPackageMaterialBO().getSenderTelephone();
        order.senderAddr = logisticsLineBO.getWarehouseBO().getSenderBO().getSenderAddress();
        order.receiverName = stockoutOrderBO.getBuyerBO().getBuyerName();
        order.mobile = stockoutOrderBO.getBuyerBO().getBuyerTelephone();
        order.warehouseCode = warehouseCode;
        order.originCountry = originCode;
        order.province = stockoutOrderBO.getBuyerBO().getBuyerProvince();
        order.city = stockoutOrderBO.getBuyerBO().getBuyerCity();
        order.district = stockoutOrderBO.getBuyerBO().getBuyerRegion();
        order.receiverAddress = stockoutOrderBO.getBuyerBO().getBuyerAddress();
        order.holdCode = "本人承诺所购买商品系个人合理自用，现委托商家代理申报、代缴税款等通关事宜，本人保证遵守《海关法》和国家相关法律法规，保证所提供的身份信息和收货信息真实完整，无侵犯他人权益的行为，以上委托关系系如实填写，本人愿意接受海关、检验检疫机构及其他监管部门的监管，并承担相应法律责任。";
        order.payType = "01";
        String payCodeOnPort = PayConfig.getPayCodeOnPort(stockoutOrderBO.getDeclarePayType(), logisticsLineBO.getPortBO().getPortNid());
        order.payCompanyCode = payCodeOnPort;
        order.payNumber = stockoutOrderBO.getDeclarePayNo();

        order.orderTotalAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getOrderActualPrice() + stockoutOrderDeclarePriceBO.getDiscountTotalPrice());
        order.orderGoodsAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getGoodsTotalPrice());
        order.orderTaxAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getTaxFee());
        order.tariffAmount = NumberUtil.defaultParsePriceFeng2Yuan(tariffAmount);
        order.consumptionDutyAmount = NumberUtil.defaultParsePriceFeng2Yuan(consumptionDutyAmount);
        order.addedValueTax = NumberUtil.defaultParsePriceFeng2Yuan(addedValueTax);
        order.feeAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getFreightFee());
        order.tradeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stockoutOrderBO.getGmtCreate());
        order.currCode = "142";
        order.discount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getDiscountTotalPrice());
        order.totalAmount = NumberUtil.defaultParsePriceFeng2Yuan(stockoutOrderDeclarePriceBO.getOrderActualPrice());
        order.purchaserId = stockoutOrderBO.getUserId()+"";
        order.id = stockoutOrderBO.getUserId()+"";
        order.name = CommonUtil.getBuyerName(stockoutOrderBO);
        order.telNumber = stockoutOrderBO.getBuyerBO().getBuyerTelephone();
        order.paperType = "01";
        order.paperNumber = CommonUtil.getBuyerIdNo(stockoutOrderBO);
        order.address = stockoutOrderBO.getBuyerBO().getBuyerAddress();
        if(StringUtils.isNotBlank(stockoutOrderBO.getIntrCarrierCode())){
            order.expressName = FSECarrierEnum.getValueByKey(stockoutOrderBO.getIntrCarrierCode());
        }

        order.isMaster = stockoutOrderBO.getMerchantPackageMaterialBO().getIsMaster();
        List<Order> orders = new ArrayList<Order>();
        orders.add(order);
        orderItem.Orders = orders;
        return orderItem;
    }

    public boolean send(FSEOrderItem orderItem, String version, String ip, String sessionKey){
        LogisticsProviderBO logisticsProviderBO = logisticsLineBO.getWarehouseBO().getLogisticsProviderBO();
        String meta = logisticsProviderBO.getInterfaceMeta().get("meta");
        String json = JSONUtil.toJson(orderItem);
        try{
            Response response = FSEUtil.send(version, ip, sessionKey,json,logisticsProviderBO.getInterfaceMeta().get("interfaceUrl"),logisticsProviderBO.getInterfaceMeta().get("interfaceKey"),methodName);
            String jsonResponse = response.body().string();
            LogBetter.instance(logger)
            .setLevel(LogLevel.INFO)
            .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
            .setMsg("[供应链报文-向仓库下发指令完成]")
            .addParm("消息来源", "SF")
            .addParm("版本", "1")
            .addParm("请求报文", json)
            .addParm("回复报文", jsonResponse)
            .log();
            FSEResponse fseResponse = JSON.parseObject(jsonResponse, FSEResponse.class);
            if (fseResponse.ROWSET.resultCode.equals("1000")) {
                stockoutOrderBO.getRecordBO().setLogisticsState(LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                stockoutOrderRecordManager.updateLogisticsState(stockoutOrderBO.getId(),
                        LogisticsState.LOGISTICS_STATE_CREATE_SUCCESS.getValue());
                writeCreateCommandSuccessLog();
//                LogBetter.instance(logger)
//                        .setLevel(LogLevel.INFO)
//                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
//                        .setMsg("[供应链报文-向仓库下发指令完成]")
//                        .addParm("消息来源", "SF")
//                        .addParm("版本", "1")
//                        .addParm("请求报文", json)
//                        .addParm("回复报文", JSONUtil.toJson(fseResponse))
//                        .log();
                return true;
            }else{
                writeCreateCommandFailureLog(fseResponse.ROWSET.ERROR.get(0).errorMsg);
                LogBetter.instance(logger)
                        .setLevel(LogLevel.ERROR)
                        .setTraceLogger(
                                TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("海外物流商下发物流下单失败")
                        .addParm("订单ID", stockoutOrderBO.getBizId())
                        .addParm("回复报文", JSONUtil.toJson(fseResponse))
                        .log();
                this.setCreateFailureMessage("[供应链-海外物流商下发物流下单]" + fseResponse.ROWSET.resultMsg);
                return false;
            }
        }catch (Exception e){
            writeCreateCommandFailureLog(e.getMessage());
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(
                            TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setException(e)
                    .setMsg("海外物流商下发物流下单失败")
                    .addParm("订单ID", stockoutOrderBO.getBizId())
                    .log();
            this.setCreateFailureMessage("[供应链-海外物流商下发物流下单]" + e.getMessage());
            return false;
        }
    }

    /**
     * 记录下发出库失败日志
     *
     * @param errMsg
     */
    private void writeCreateCommandFailureLog(String errMsg) {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "重庆费舍尔仓下物流订单失败," + errMsg, SystemConstants.WARN_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }

    /**
     * 记录下发出库指令成功日志                   `
     */
    private void writeCreateCommandSuccessLog() {
        routeService.appandSystemRoute(stockoutOrderBO.getBizId(), "重庆费舍尔仓物流下单成功", SystemConstants.INFO_LEVEL, new Date(), SystemUserName.OPSC.getValue());
    }
}
