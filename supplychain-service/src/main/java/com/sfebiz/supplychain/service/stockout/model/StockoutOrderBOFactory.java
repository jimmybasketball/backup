package com.sfebiz.supplychain.service.stockout.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.supplychain.exposed.common.enums.StockOutPlanType;
import com.sfebiz.supplychain.open.exposed.api.SCOpenReturnCode;
import com.sfebiz.supplychain.open.exposed.wms.entity.request.OpenWmsTradeOrderCreateRequest;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeConsigneeItem;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeGoodsItem;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeOrderItem;
import com.sfebiz.supplychain.persistence.base.line.domain.LogisticsLineDO;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantDO;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantManager;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuDO;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuMerchantDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuBarcodeManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuManager;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuMerchantManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderBuyerManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDeclarePriceDetailManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDeclarePriceManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderDetailManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderRecordManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderStateLogManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.line.LogisticsLineBOFactory;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBuyerBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderRecordBO;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.NumberUtil;

@Component("stockoutOrderBOFactory")
public class StockoutOrderBOFactory {

    /** 日志 */
    private static final Logger                    LOGGER = LoggerFactory
                                                              .getLogger(StockoutOrderBOFactory.class);

    @Resource
    private StockoutOrderManager                   stockoutOrderManager;

    @Resource
    private StockoutOrderBuyerManager              stockoutOrderBuyerManager;

    @Resource
    private StockoutOrderDetailManager             stockoutOrderDetailManager;

    @Resource
    private StockoutOrderDeclarePriceManager       stockoutOrderDeclarePriceManager;

    @Resource
    private StockoutOrderDeclarePriceDetailManager stockoutOrderDeclarePriceDetailManager;

    @Resource
    private StockoutOrderStateLogManager           stockoutOrderStateLogManager;

    @Resource
    private StockoutOrderRecordManager             stockoutOrderRecordManager;

    @Resource
    private StockoutOrderTaskManager               stockoutOrderTaskManager;

    @Resource
    private LogisticsLineBOFactory                 logisticsLineBOFactory;

    @Resource
    private WarehouseManager                       warehouseManager;

    @Resource
    private LogisticsLineManager                   logisticsLineManager;

    @Resource
    private MerchantManager                        merchantManager;

    @Resource
    private SkuBarcodeManager                      skuBarcodeManager;

    @Resource
    private SkuMerchantManager                     skuMerchantManager;

    @Resource
    private SkuManager                             skuManager;

    /**
     * 根据开放出库单对象，构建出库单业务实体
     * 
     * @param req
     * @return
     */
    public StockoutOrderBO buildByOpenWmsTradeOrderCreateRequest(OpenWmsTradeOrderCreateRequest req)
                                                                                                    throws ServiceException {
        OpenWmsTradeOrderItem orderItem = req.order;

        // 1. 构建出库单主要信息
        StockoutOrderBO stockoutOrderBO = new StockoutOrderBO();
        stockoutOrderBO.setMerchantOrderNo(orderItem.merchantOrderId);
        stockoutOrderBO.setOrderSource(orderItem.tradesource);
        stockoutOrderBO.setOrderType(Integer.valueOf(orderItem.orderType));
        stockoutOrderBO.setServiceType(Integer.valueOf(orderItem.serviceType));

        WarehouseDO warehouseDO = warehouseManager.getByNid(orderItem.warehouseCode);
        if (null == warehouseDO) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "warehouseCode编码不存在，请核对");
        }
        stockoutOrderBO.setWarehouseNid(orderItem.warehouseCode);
        stockoutOrderBO.setWarehouseId(warehouseDO.getId());

        if (StringUtils.isNotBlank(orderItem.routeCode)) {
            LogisticsLineDO lineDO = logisticsLineManager
                .getByLogisticsLineNid(orderItem.routeCode);
            if (null == lineDO) {
                throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                    "routeCode编码不存在，请核对");
            }
            stockoutOrderBO.setLineId(lineDO.getId());
        }

        MerchantDO merchantDO = merchantManager
            .getMerchantByMerchantAccountId(orderItem.customerCode);
        if (null == merchantDO) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "customerCode编码不存在，请核对");
        }
        stockoutOrderBO.setMerchantAccountId(orderItem.customerCode);
        stockoutOrderBO.setMerchantId(merchantDO.getId());

        stockoutOrderBO.setUserGoodsPrice(NumberUtil
            .parsePriceYuan2Feng(orderItem.goodsTotalAmount));
        stockoutOrderBO.setUserDiscountPrice(NumberUtil
            .parsePriceYuan2Feng(orderItem.discountAmount));
        stockoutOrderBO.setUserFreightFee(NumberUtil.parsePriceYuan2Feng(orderItem.freight));

        if (StringUtils.isNotBlank(orderItem.expectedShipmentTime)) {
            stockoutOrderBO.setEstimatedShippingTime(DateUtil.parseDate(
                orderItem.expectedShipmentTime, DateUtil.DEF_PATTERN));
        }
        if (StringUtils.isNotBlank(orderItem.tradeTime)) {
            // TODO matt
        }
        stockoutOrderBO.setDestcode(orderItem.destCode);
        stockoutOrderBO.setIntlMailNo(orderItem.mailNo);
        stockoutOrderBO.setIntlCarrierCode(orderItem.carrierCode);
        stockoutOrderBO.setIntrMailNo(orderItem.mailNo);
        stockoutOrderBO.setIntrCarrierCode(orderItem.carrierCode);

        // 2. 设置商品信息
        List<String> barcodeList = new ArrayList<String>();
        for (OpenWmsTradeGoodsItem goodsItem : orderItem.items) {
            if (!barcodeList.contains(goodsItem.barCode)) {
                barcodeList.add(goodsItem.barCode);
            }
        }
        Map<String, Long> barcode4SkuIdMap = skuBarcodeManager
            .getBarcode4SkuIdMapByBarcodeList(barcodeList);
        if (barcode4SkuIdMap.isEmpty() || barcode4SkuIdMap.size() != barcodeList.size()) {
            List<String> errorList = barcodeList;
            if (!barcode4SkuIdMap.isEmpty()) {
                errorList.removeAll(barcode4SkuIdMap.keySet());
            }
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "barCode商品编码不存在，barCode列表中" + errorList + "不存在，请核对");
        }
        Map<Long, SkuMerchantDO> skuId4SkuMerchantDOMap = skuMerchantManager
            .getSkuMerchantDOMapBySkuIdsAndMerchantId(stockoutOrderBO.getMerchantId(),
                new ArrayList<Long>(barcode4SkuIdMap.values()));
        if (skuId4SkuMerchantDOMap.isEmpty() || skuId4SkuMerchantDOMap.size() != barcodeList.size()) {

            List<String> errorList = new ArrayList<String>();
            for (String barcode : barcode4SkuIdMap.keySet()) {
                if (!skuId4SkuMerchantDOMap.containsKey(barcode4SkuIdMap.get(barcode))) {
                    errorList.add(barcode);
                }
            }
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "barCode在货主下不存在对应商品信息，barCode列表中" + errorList + "不存在，请核对");
        }
        Map<Long, SkuDO> skuId4SkuDOMap = skuManager.getSkuDOMapBySkuIds(new ArrayList<Long>(
            barcode4SkuIdMap.values()));
        if (skuId4SkuDOMap.isEmpty() || skuId4SkuDOMap.size() != barcodeList.size()) {
            List<String> errorList = new ArrayList<String>();
            for (String barcode : barcode4SkuIdMap.keySet()) {
                if (!skuId4SkuDOMap.containsKey(barcode4SkuIdMap.get(barcode))) {
                    errorList.add(barcode);
                }
            }
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "barCode所对应的商品基础信息不存在，barCode列表中" + errorList + "不存在，请核对");
        }

        List<StockoutOrderDetailBO> detailBOs = new ArrayList<StockoutOrderDetailBO>();
        for (OpenWmsTradeGoodsItem goodsItem : orderItem.items) {

            String skuBarcode = goodsItem.barCode;
            Long skuId = barcode4SkuIdMap.get(skuBarcode);
            SkuMerchantDO skuMerchantDO = skuId4SkuMerchantDOMap.get(skuId);
            SkuDO skuDO = skuId4SkuDOMap.get(skuId);

            StockoutOrderDetailBO detailBO = new StockoutOrderDetailBO();
            detailBO.setSkuId(skuId);
            detailBO.setMerchantSkuId(goodsItem.merchantSkuId);
            detailBO.setSkuBarcode(skuBarcode);
            detailBO.setQuantity(Integer.valueOf(goodsItem.quantity));
            detailBO.setSkuBatch(goodsItem.batchNo);
            detailBO.setMerchantPrice(NumberUtil.parsePriceYuan2Feng(goodsItem.skuUnitPrice));
            detailBO.setMerchantDiscountPrice(0);
            detailBO.setFreightFee(0);
            detailBO.setCurrency("");
            detailBO.setSkuName(skuDO.getName());
            if (StringUtils.isNotBlank(goodsItem.skuBillName)) {
                detailBO.setSkuBillName(goodsItem.skuBillName);
            } else {
                detailBO.setSkuBillName(skuMerchantDO.getBillName());
            }
            detailBO.setSkuForeignName(skuDO.getForeignName());
            detailBO.setBrandName(skuDO.getBrandName());
            detailBO.setWeight(Long.valueOf(skuDO.getNetWeight()));
            detailBO.setStockOutPlan(StockOutPlanType.valueOf(skuDO.getStockoutPlan()));

            detailBOs.add(detailBO);
        }
        stockoutOrderBO.setDetailBOs(detailBOs);

        // 3. 设置收货人信息
        StockoutOrderBuyerBO buyerBO = bulidBuyerBOByOpenWmsTradeOrderCreateRequest(req);
        stockoutOrderBO.setBuyerBO(buyerBO);
        stockoutOrderBO.setDeclarePayerName(buyerBO.getBuyerName());
        stockoutOrderBO.setDeclarePayerCertType(String.valueOf(buyerBO.getBuyerCertType()));
        stockoutOrderBO.setDeclarePayerCertNo(buyerBO.getBuyerCertNo());
        stockoutOrderBO.setMerchantPayNo(orderItem.merchantOrderId);
        stockoutOrderBO.setMerchantPayType("merchant_pay");

        // 4. 设置出库单记录信息
        StockoutOrderRecordBO recordBO = null;
        stockoutOrderBO.setRecordBO(recordBO);

        return stockoutOrderBO;
    }

    public StockoutOrderBO loadStockoutBOById(Long stockoutOrderId) {
        StockoutOrderBO stockoutOrderBO = new StockoutOrderBO();
        // TODO matt
        return stockoutOrderBO;
    }

    /**
     * 构建购买人信息实体
     * 
     * @param req
     * @return
     */
    private StockoutOrderBuyerBO bulidBuyerBOByOpenWmsTradeOrderCreateRequest(OpenWmsTradeOrderCreateRequest req) {

        OpenWmsTradeConsigneeItem consigneeInfo = req.order.consigneeInfo;

        StockoutOrderBuyerBO buyerBO = new StockoutOrderBuyerBO();

        buyerBO.setBuyerName(consigneeInfo.consigneeName);
        buyerBO.setBuyerTelephone(consigneeInfo.consigneeMobile);
        buyerBO.setBuyerCertType(Integer.valueOf(consigneeInfo.idType));
        buyerBO.setBuyerCertNo(consigneeInfo.idNumber);
        buyerBO.setBuyerIdCardFrontPhotoUrl(consigneeInfo.idFountImgUrl);
        buyerBO.setBuyerIdCardBackPhotoUrl(consigneeInfo.idBackImgUrl);
        buyerBO.setBuyerCountry(consigneeInfo.addrCountry);
        buyerBO.setBuyerProvince(consigneeInfo.addrProvince);
        buyerBO.setBuyerCity(consigneeInfo.addrCity);
        buyerBO.setBuyerRegion(consigneeInfo.addrDistrict);
        buyerBO.setBuyerAddress(consigneeInfo.addrDetail);
        buyerBO.setBuyerZipcode(consigneeInfo.zipCode);

        return buyerBO;
    }

}
