package com.sfebiz.supplychain.autotest.open.wms;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.supplychain.autotest.BaseServiceTest;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineServiceType;
import com.sfebiz.supplychain.exposed.stockout.enums.IDType;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.open.exposed.wms.api.OpenWmsTradeService;
import com.sfebiz.supplychain.open.exposed.wms.entity.request.OpenWmsTradeOrderCreateRequest;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeConsigneeItem;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeGoodsItem;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeOrderItem;
import com.sfebiz.supplychain.open.exposed.wms.enums.OpenWmsTradeActionType;
import com.sfebiz.supplychain.queue.MessageConstants;
import com.sfebiz.supplychain.queue.consumer.process.stockout.ExecSendProcessEventProcesser;
import com.sfebiz.supplychain.queue.enums.StockoutOrderMsgTag;

public class OpenWmsTradeServiceTest extends BaseServiceTest{

    @Resource
    private OpenWmsTradeService openWmsTradeService;
    
    @Resource
    private ExecSendProcessEventProcesser execSendProcessEventProcesser;
    
    @Test
    public void test_createOrder(){
        
        OpenWmsTradeOrderCreateRequest request = new OpenWmsTradeOrderCreateRequest();
        
        OpenWmsTradeOrderItem order = new OpenWmsTradeOrderItem();
        // 商户订单基本信息
        order.merchantOrderId = "MAT_" + System.currentTimeMillis();
        order.tradesource = "FQHT";
        order.actionType = OpenWmsTradeActionType.CREATE.getCode();
        order.orderType = String.valueOf(StockoutOrderType.SALES_STOCK_OUT.getValue());
        order.serviceType = String.valueOf(LogisticsLineServiceType.FENG_JI_SF.getValue());
        order.warehouseCode = "DEV_MAT_003"; // 指定仓库
        order.routeCode = StringUtils.EMPTY; // 指定线路
        order.customerCode = "HZ000011";
        order.tradeAmount = "100.00";
        order.goodsTotalAmount = "100.00";
        order.discountAmount = "0.00";
        order.freight = "0.00";
        order.insuranceValue = "0.00";
        // 商品信息
        List<OpenWmsTradeGoodsItem> items = new ArrayList<OpenWmsTradeGoodsItem>();
        OpenWmsTradeGoodsItem goodsItem = new OpenWmsTradeGoodsItem();
        goodsItem.barCode = "B12038";
        goodsItem.skuUnitPrice = "100.00";
        goodsItem.quantity = "1";
        goodsItem.skuBillName = "开发测试商品";
        goodsItem.batchNo = "";
        goodsItem.merchantSkuId = "sku001";
        items.add(goodsItem);
        order.items = items;
        // 买家信息
        OpenWmsTradeConsigneeItem   consigneeInfo = new OpenWmsTradeConsigneeItem();
        consigneeInfo.consigneeName = "matt";
        consigneeInfo.consigneeMobile = "13802561147";
        consigneeInfo.addrCountry = "中国";
        consigneeInfo.addrProvince = "重庆";
        consigneeInfo.addrCity = "重庆市";
        consigneeInfo.addrDistrict = "南岸区";
        consigneeInfo.addrDetail = "南坪西路36号";
        consigneeInfo.zipCode = "400000";
        consigneeInfo.idType = String.valueOf(IDType.ID_CARD.getValue());
        consigneeInfo.idNumber = "410304198812130519";
        consigneeInfo.idFountImgUrl = "";
        consigneeInfo.idBackImgUrl = "";
        order.consigneeInfo = consigneeInfo;
        
        request.order = order;
        
        try {
            openWmsTradeService.createOrder(request);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 出库单下发流程测试
     */
    @Test
    public void test_StockoutOrderSendProcess(){
        Message message = new Message();
        message.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_EVENT);
        message.setTag(StockoutOrderMsgTag.TAG_STOCKOUTORDER_SEND_TO_PROVIDER.getTag());
        Properties properties = new Properties();
        properties.put("stockoutOrderId", String.valueOf("25"));
        message.setUserProperties(properties);
        message.setStartDeliverTime(System.currentTimeMillis());
        try {
            message.setBody(" ".getBytes("UTF-8")); //BODY不能为空
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        execSendProcessEventProcesser.process(message);
    }
}
