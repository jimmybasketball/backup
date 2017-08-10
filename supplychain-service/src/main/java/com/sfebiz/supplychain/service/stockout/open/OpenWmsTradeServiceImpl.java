package com.sfebiz.supplychain.service.stockout.open;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineServiceType;
import com.sfebiz.supplychain.exposed.stockout.api.StockoutService;
import com.sfebiz.supplychain.exposed.stockout.enums.IDType;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskType;
import com.sfebiz.supplychain.open.exposed.api.SCOpenReturnCode;
import com.sfebiz.supplychain.open.exposed.wms.api.OpenWmsTradeService;
import com.sfebiz.supplychain.open.exposed.wms.entity.WmsOrderRoutesResult;
import com.sfebiz.supplychain.open.exposed.wms.entity.request.OpenWmsTradeOrderCreateRequest;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeConsigneeItem;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeGoodsItem;
import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeOrderItem;
import com.sfebiz.supplychain.open.exposed.wms.enums.OpenWmsTradeActionType;
import com.sfebiz.supplychain.persistence.base.line.manager.LogisticsLineManager;
import com.sfebiz.supplychain.persistence.base.merchant.domain.MerchantDO;
import com.sfebiz.supplychain.persistence.base.merchant.manager.MerchantManager;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderBuyerDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderInvokeLogDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderBuyerManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderInvokeLogManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderBizService;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.model.StockoutOrderBOFactory;
import com.sfebiz.supplychain.util.AreaUtil;
import com.sfebiz.supplychain.util.NumberUtil;

@Service("openWmsTradeService")
public class OpenWmsTradeServiceImpl implements OpenWmsTradeService {

    private static final Logger           LOGGER = LoggerFactory
                                                     .getLogger(OpenWmsTradeServiceImpl.class);

    @Resource
    private StockoutOrderManager          stockoutOrderManager;

    @Resource
    private StockoutOrderTaskManager      stockoutOrderTaskManager;

    @Resource
    private StockoutOrderBuyerManager     stockoutOrderBuyerManager;

    @Resource
    private StockoutOrderInvokeLogManager stockoutOrderInvokeLogManager;

    @Resource
    private WarehouseManager              warehouseManager;

    @Resource
    private LogisticsLineManager          logisticsLineManager;

    @Resource
    private MerchantManager               merchantManager;

    @Resource
    private StockoutService               stockoutService;

    @Resource
    private StockoutOrderBizService       stockoutOrderBizService;

    @Resource
    private StockoutOrderBOFactory        stockoutOrderBOFactory;

    @Override
    public List<WmsOrderRoutesResult> orderRouteSearch(String customerCode, List<String> orderNoList)
                                                                                                     throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean createOrder(OpenWmsTradeOrderCreateRequest request) throws ServiceException {

        LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[云仓API-出库单创建]: ")
            .addParm("request", request).log();

        try {
            // 1. 参数校验
            checkCreateOrderParam(request);

            MerchantDO merchantDO = merchantManager
                .getMerchantByMerchantAccountId(request.order.customerCode);

            // 2. 根据操作类型，选择相应的操作
            if (StringUtils.equals(OpenWmsTradeActionType.CREATE.getCode(),
                request.order.actionType)) {

                // 校验是否存在异常单
                StockoutOrderTaskDO taskDO = stockoutOrderTaskManager
                    .getTaskByBizIdAndMerchantIdAndTaskType(request.order.merchantOrderId,
                        merchantDO.getId(), TaskType.CREATE_STOCKOUT_EXCEPTION.value);
                if (null != taskDO) {
                    throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                        "此商户订单号系统已存在，不用重复创建");
                }

                // 2.1. 创建出库单
                StockoutOrderBO stockoutOrderBO = stockoutOrderBOFactory
                    .buildByOpenWmsTradeOrderCreateRequest(request);
                stockoutOrderBizService.createOrder(stockoutOrderBO);

            } else if (StringUtils.equals(OpenWmsTradeActionType.EDIT.getCode(),
                request.order.actionType)) {

                StockoutOrderDO stockoutOrderDO = stockoutOrderManager
                    .queryByMerchantOrderNoAndMerchantId(request.order.merchantOrderId,
                        merchantDO.getId());
                if (null == stockoutOrderDO) {
                    throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL, "此订单还未创建");
                }
                // 2.2. 修改出库单
                StockoutOrderBuyerDO stockoutOrderBuyerDO = stockoutOrderBuyerManager
                    .getByStockoutOrderId(stockoutOrderDO.getId());

                // 修改出库单购买人信息
                stockoutOrderDO.setDeclarePayerName(request.order.consigneeInfo.consigneeName);
                stockoutOrderDO.setDeclarePayerCertNo(request.order.consigneeInfo.idNumber);
                stockoutOrderManager.update(stockoutOrderDO);
                stockoutOrderBuyerDO.setBuyerName(request.order.consigneeInfo.consigneeName);
                stockoutOrderBuyerDO.setBuyerCertNo(request.order.consigneeInfo.idNumber);
                stockoutOrderBuyerManager.update(stockoutOrderBuyerDO);

                StockoutOrderTaskDO stockoutOrderTaskDO = stockoutOrderTaskManager
                    .getByStockoutOrderIdAndTaskType(stockoutOrderDO.getId(),
                        TaskType.CREATE_STOCKOUT_EXCEPTION.value);
                if (null != stockoutOrderTaskDO) {
                    stockoutService.executeStockoutExceptionTaskByHandle(
                        stockoutOrderTaskDO.getId(), null, "SYSTEM");
                } else {
                    LOGGER.info("[云仓API-出库单创建] 修改时未找到指定异常：stockoutOrderId={}",
                        stockoutOrderDO.getId());
                }

            }

        } catch (Exception e) {

            // 保存调用日志
            saveCreateExceptionInvokeLog(request, e);

            if (e instanceof ServiceException) {
                LOGGER.info("[云仓API-出库单创建]: 创建出库单业务异常", e);
                throw (ServiceException) e;
            } else {
                LOGGER.error("[云仓API-出库单创建]: 创建出库单未知异常", e);
                throw new ServiceException(SCOpenReturnCode.COMMON_FAIL, "系统异常");
            }
        }

        return true;
    }

    private void saveCreateExceptionInvokeLog(OpenWmsTradeOrderCreateRequest request,
                                              Exception createException) {
        if (null == request) {
            return;
        }
        try {
            StockoutOrderInvokeLogDO invokeLogDO = new StockoutOrderInvokeLogDO();
            if (null != request.order) {
                invokeLogDO.setMerchantOrderNo(request.order.merchantOrderId);
                invokeLogDO.setMerchantAccountId(request.order.customerCode);
            }
            invokeLogDO.setActionType("STOCKOUT_OPEN_CREATE");
            invokeLogDO.setActionDesc("开放出库单创建");
            invokeLogDO.setReqInfo(JSON.toJSONString(request));
            if (createException instanceof ServiceException) {
                invokeLogDO.setErrCode(String.valueOf(((ServiceException) createException)
                    .getCode()));
                invokeLogDO.setErrDesc(((ServiceException) createException).getMsg());
            } else {
                invokeLogDO.setErrCode(StringUtils.EMPTY);
                invokeLogDO.setErrDesc(createException.getMessage());
            }

            invokeLogDO.setGmtInvoke(new Date());
            stockoutOrderInvokeLogManager.insert(invokeLogDO);
        } catch (Exception e) {
            LOGGER.info("[出库单创建-保存调用日志] 异常");
        }
    }

    /**
     * 参数校验
     *
     * @param request
     */
    private void checkCreateOrderParam(OpenWmsTradeOrderCreateRequest request)
                                                                              throws ServiceException {

        // 请求实体
        if (null == request || null == request.order) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL, "order信息不能为空");
        }
        OpenWmsTradeOrderItem orderItem = request.order;

        // 货主
        if (StringUtils.isBlank(orderItem.customerCode)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "customerCode必填字段不能为空");
        }

        // 商户订单号
        if (StringUtils.isBlank(orderItem.merchantOrderId)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "merchantOrderId商户订单号不能为空");
        }

        // 操作类型
        if (StringUtils.isBlank(orderItem.actionType)
            || null == OpenWmsTradeActionType.getEnumByCode(orderItem.actionType)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "actionType不为有效的值，请参考" + OpenWmsTradeActionType.getCodeListStr());
        }

        // 订单类型
        if (StringUtils.isBlank(orderItem.orderType)
            || !StockoutOrderType.containValue(Integer.valueOf(orderItem.orderType))) {
            throw new ServiceException(SCReturnCode.PARAM_ILLEGAL_ERR,
                "orderType不为有效的值，请参考" + StockoutOrderType.getCodeListStr());
        }
        if (!StringUtils.equals(orderItem.orderType,
            String.valueOf(StockoutOrderType.SALES_STOCK_OUT.getValue()))
            && !StringUtils.equals(orderItem.orderType,
                String.valueOf(StockoutOrderType.TRANSPORT_STOCK_OUT.getValue()))) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "orderType不为有效的值，当前仅支持" + StockoutOrderType.SALES_STOCK_OUT.getValue() + "（"
                        + StockoutOrderType.SALES_STOCK_OUT.getDescription() + "）、"
                        + StockoutOrderType.TRANSPORT_STOCK_OUT.getValue() + "（"
                        + StockoutOrderType.TRANSPORT_STOCK_OUT.getDescription());
        }

        // 服务类型
        if (StringUtils.isBlank(orderItem.serviceType)
            || !LogisticsLineServiceType.containValue(Integer.valueOf(orderItem.serviceType))) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "serviceType不为有效的值，请参考" + LogisticsLineServiceType.getCodeListStr());
        }
        if (Integer.valueOf(orderItem.serviceType) == LogisticsLineServiceType.FENG_YUN_VMI
            .getValue()) {
            if (StringUtils.isEmpty(orderItem.mailNo) || StringUtils.isEmpty(orderItem.carrierCode)) {
                throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                    "使用" + LogisticsLineServiceType.FENG_YUN_VMI.getDescription()
                            + "类型的产品时，mailNo与carrierCode为必填字段，请填写");
            }
            if (orderItem.orderType != String.valueOf(StockoutOrderType.TRANSPORT_STOCK_OUT
                .getValue())) {
                throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                    "使用" + LogisticsLineServiceType.FENG_YUN_VMI.getDescription()
                            + "类型的产品时，orderType必须为"
                            + StockoutOrderType.TRANSPORT_STOCK_OUT.getValue() + "（"
                            + StockoutOrderType.TRANSPORT_STOCK_OUT.getDescription() + "）" + "，请填写");
            }
        }

        // 仓库
        if (StringUtils.isBlank(orderItem.warehouseCode)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "warehouseCode必填字段不能为空");
        }

        // 金额相关计算
        if (StringUtils.isBlank(orderItem.tradeAmount)
            || StringUtils.isBlank(orderItem.goodsTotalAmount)
            || StringUtils.isBlank(orderItem.discountAmount)
            || StringUtils.isBlank(orderItem.freight)
            || StringUtils.isBlank(orderItem.insuranceValue)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "tradeAmount、goodsTotalAmount、discountAmount、freight、insuranceValue必填字段不能为空，如果为0请填写0");
        }
        if (NumberUtil.parsePriceYuan2Feng(orderItem.insuranceValue) > 0) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "暂不支持insuranceValue字段，请填写0");
        }
        int calculateTradeAmount = NumberUtil.parsePriceYuan2Feng(orderItem.goodsTotalAmount)
                                   - NumberUtil.parsePriceYuan2Feng(orderItem.discountAmount)
                                   + NumberUtil.parsePriceYuan2Feng(orderItem.freight)
                                   + NumberUtil.parsePriceYuan2Feng(orderItem.insuranceValue);
        if (NumberUtil.parsePriceYuan2Feng(orderItem.tradeAmount) != calculateTradeAmount) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "金额错误（tradeAmount = goodsTotalAmount - discountAmount + freight + insuranceValue），请修改");
        }

        // 出库单商品信息校验
        if (CollectionUtils.isEmpty(orderItem.items)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL, "items必填字段不能为空");
        }

        for (OpenWmsTradeGoodsItem goodsItem : orderItem.items) {
            if (StringUtils.isBlank(goodsItem.barCode)) {
                throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                    "barCode商品条码必填字段不能为空");
            }
            if (StringUtils.isBlank(goodsItem.skuUnitPrice)) {
                throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                    "skuUnitPrice商品单价必填字段不能为空");
            } else {
                if (NumberUtil.parsePriceYuan2Feng(goodsItem.skuUnitPrice) < 0) {
                    throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                        "skuUnitPrice商品单价必须大于0");
                }
            }
            if (StringUtils.isBlank(goodsItem.quantity) || Integer.valueOf(goodsItem.quantity) <= 0) {
                throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                    "quantity商品数量必填大于0");
            }
        }

        // 收货人相关信息校验
        if (null == orderItem.consigneeInfo) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "consigneeInfo收货人信息必填字段不能为空");
        }
        OpenWmsTradeConsigneeItem consigneeItem = orderItem.consigneeInfo;
        if (StringUtils.isBlank(consigneeItem.consigneeName)
            || StringUtils.isBlank(consigneeItem.consigneeMobile)
            || StringUtils.isBlank(consigneeItem.idType)
            || StringUtils.isBlank(consigneeItem.idNumber)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "consigneeName,consigneeMobile,idType,idNumber必填字段不能为空");
        }

        if (!StringUtils.equals(String.valueOf(IDType.ID_CARD.getValue()), consigneeItem.idType)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "证件类型错误，当前只支持身份证，请填写1");
        }

        consigneeItem.idNumber = StringUtils.upperCase(consigneeItem.idNumber);
        if (!NumberUtil.checkIDNumFormat(consigneeItem.idNumber)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL, "身份证号码不符合规则");
        }

        // 收货地址信息相关校验
        if (StringUtils.isEmpty(consigneeItem.addrCountry)
            || StringUtils.isEmpty(consigneeItem.addrProvince)
            || StringUtils.isEmpty(consigneeItem.addrCity)
            || StringUtils.isEmpty(consigneeItem.addrDistrict)
            || StringUtils.isEmpty(consigneeItem.addrDetail)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "收货地址中国家、省、市、区、详细地址信息均为必填字段不能为空");
        }
        if (!StringUtils.equals(consigneeItem.addrCountry, AreaUtil.CN_DESC)) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                "当前只支持收货地址国家在国内，addrCountry请填写中国");
        }

        AreaUtil.AreaCheckResult areaCheckResult = AreaUtil.checkAreaInfo(
            consigneeItem.addrProvince, consigneeItem.addrCity, consigneeItem.addrDistrict);
        if (StringUtils.isNotBlank(areaCheckResult.getErrorMsg())) {
            throw new ServiceException(SCOpenReturnCode.COMMON_PARAMS_ILLEGAL,
                areaCheckResult.getErrorMsg());
        }
        consigneeItem.addrProvince = areaCheckResult.getProvinceName();
        consigneeItem.addrCity = areaCheckResult.getCityName();
        consigneeItem.addrDistrict = areaCheckResult.getRegionName();
    }
}
