package com.sfebiz.supplychain.config.order;

import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>基于出库单的配置</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/5/27
 * Time: 下午6:14
 */
public class OrderConfig {

    private static final Logger logger = LoggerFactory.getLogger(OrderConfig.class);


    /**
     * 获取 指定的订单配置
     *
     * @param bizId 订单ID
     * @param key   key
     * @return
     */
    public static String getConfig(String bizId, String key) {
        if (StringUtils.isBlank(bizId) || StringUtils.isBlank(key)) {
            return null;
        }
        bizId = bizId.trim();
        key = key.trim();
        String orderConfig = LogisticsDynamicConfig.getOrder().getRule(bizId, key);
        if (StringUtils.isBlank(orderConfig)) {
            return null;
        } else {
            return orderConfig.trim();
        }
    }

    /**
     * 获取默认运费，单位为分
     *
     * @return
     */
    public static Integer getDefaultFreightAmount() {
        try {
            String defaultFreight = LogisticsDynamicConfig.getOrder().getRule("freightFeeTaxFeeSplit", "defaultFreight");
            if (StringUtils.isNotBlank(defaultFreight)) {
                return Integer.valueOf(defaultFreight.trim());
            }
        } catch (Exception e) {
            logger.error("获取默认运费异常,返回运费20元," + e.getMessage(), e);
        }
        return 2000;
    }

    /**
     * 获取默认运费，单位为分,根据不同渠道获取不同的运费,如果为空,取默认运费
     *
     * @param channel 渠道,如果想要获取默认运费,传空
     * @return
     */
    public static Integer getChannelFreightAmount(String channel) {
        try {
            if(StringUtils.isBlank(channel)){
                channel = "defaultFreight";
            }
            String defaultFreight = LogisticsDynamicConfig.getOrder().getRule("freightFeeTaxFeeSplit", channel);
            if (StringUtils.isNotBlank(defaultFreight)) {
                return Integer.valueOf(defaultFreight.trim());
            }
        } catch (Exception e) {
            logger.error("获取默认运费异常,返回运费20元," + e.getMessage(), e);
        }
        return 2000;
    }

    /**
     * 获取默认保留价格小数位数异常
     *
     * @return
     */
    public static Integer getDefaultPriceScale() {
        try {
            String defaultPriceScale = LogisticsDynamicConfig.getOrder().getRule("defaultPriceScale", "scale");
            if (StringUtils.isNotBlank(defaultPriceScale)) {
                return Integer.valueOf(defaultPriceScale.trim());
            }
        } catch (Exception e) {
            logger.error("获取默认保留价格小数位数异常,返回默认为2位," + e.getMessage(), e);
        }
        return 2;
    }

    /**
     * 获取默认进行拆分的申报总价格，单位为分
     *
     * @return
     */
    public static Integer getDefaultSplitAmount() {
        try {
            String defaultFreight = LogisticsDynamicConfig.getOrder().getRule("freightFeeTaxFeeSplit", "defaltSplitTotal");
            if (StringUtils.isNotBlank(defaultFreight)) {
                return Integer.valueOf(defaultFreight.trim());
            }
        } catch (Exception e) {
            logger.error("获取默认拆分总价异常,返回50元", e);
        }
        return 5000;
    }

    /**
     * 是否跳过口岸验证限制
     *
     * @param bizId
     * @return
     */
    public static boolean getIsSkipPortValidate(String bizId) {
        try {
            String isSkip = LogisticsDynamicConfig.getOrder().getRule("skipPortValidate", bizId);
            if (StringUtils.isBlank(isSkip) || (!"true".equalsIgnoreCase(isSkip))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("获取是否跳过口岸验证异常,返回false", e);
        }
        return false;
    }

    /**
     * 是否跳过实名认证
     *
     * @param bizId
     * @return
     */
    public static boolean getIsSkipRealNameAuthentication(String bizId) {
        try {
            String isSkip = LogisticsDynamicConfig.getOrder().getRule("skipRealNameAuthentication", bizId);
            if (StringUtils.isBlank(isSkip) || (!"true".equalsIgnoreCase(isSkip))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("是否跳过实名认证异常,返回false", e);
        }
        return false;
    }


    /**
     * 获取订单运费
     *
     * @param bizId           订单ID
     * @param userShippingFee 原始出库单中的运费
     * @return
     */
    public static int getOrderFeeAmount(String bizId, int userShippingFee) {
        if (StringUtils.isNotBlank(OrderConfig.getConfig(bizId, "orderFeeAmount"))) {
            String orderFeeAmount = OrderConfig.getConfig(bizId, "orderFeeAmount");
            userShippingFee = Integer.valueOf(orderFeeAmount);
        }
        return userShippingFee;
    }

    public static int getOrderTaxAmount(String bizId, int tariff) {
        if (StringUtils.isNotBlank(OrderConfig.getConfig(bizId, "orderTaxAmount"))) {
            String orderTaxAmount = OrderConfig.getConfig(bizId, "orderTaxAmount");
            tariff = Integer.valueOf(orderTaxAmount);
        }
        return tariff;
    }

    /**
     * 获取订单货款
     *
     * @param bizId
     * @param userGoodsPrice
     * @return
     */
    public static int getOrderGoodsAmount(String bizId, int userGoodsPrice) {
        if (StringUtils.isNotBlank(OrderConfig.getConfig(bizId, "orderGoodsAmount"))) {
            String orderGoodsAmount = OrderConfig.getConfig(bizId, "orderGoodsAmount");
            userGoodsPrice = Integer.valueOf(orderGoodsAmount);
        }
        return userGoodsPrice;
    }

    /**
     * 获取订单总金额
     *
     * @param bizId
     * @param orderTotalPrice
     * @return
     */
    public static int getOrderTotalAmount(String bizId, int orderTotalPrice) {
        if (StringUtils.isNotBlank(OrderConfig.getConfig(bizId, "orderTotalAmount"))) {
            String orderTotalAmount = OrderConfig.getConfig(bizId, "orderTotalAmount");
            orderTotalPrice = Integer.valueOf(orderTotalAmount);
        }
        return orderTotalPrice;
    }

    /**
     * 获取订单中单个商品金额
     *
     * @param bizId
     * @param skuId    SKU ID
     * @param skuPrice
     * @return
     */
    public static int getOrderSkuAmount(String bizId, Long skuId, int skuPrice) {
        if (StringUtils.isNotBlank(OrderConfig.getConfig(bizId, skuId.toString()))) {
            String skuAmount = OrderConfig.getConfig(bizId, skuId.toString());
            skuPrice = Integer.valueOf(skuAmount);
        }
        return skuPrice;
    }

    /**
     * 获取订单成交总金额
     *
     * @param bizId
     * @param userOrderTotal
     * @return
     */
    public static int getOrderDealAmount(String bizId, int userOrderTotal) {
        if (StringUtils.isNotBlank(OrderConfig.getConfig(bizId, "orderDealAmount"))) {
            String orderDealAmount = OrderConfig.getConfig(bizId, "orderDealAmount");
            userOrderTotal = Integer.valueOf(orderDealAmount);
        }
        return userOrderTotal;
    }

    /**
     * 获取订单申报总金额
     *
     * @param bizId
     * @param userGoodsPrice
     * @return
     */
    public static int getOrderDeclareTotalAmount(String bizId, int userGoodsPrice, int userDiscountPrice) {
        int userOrderDeclareAmount = userGoodsPrice - userDiscountPrice;
        if (StringUtils.isNotBlank(OrderConfig.getConfig(bizId, "orderDeclareAmount"))) {
            String orderDeclareAmount = OrderConfig.getConfig(bizId, "orderDeclareAmount");
            userOrderDeclareAmount = Integer.valueOf(orderDeclareAmount);
        }
        return userOrderDeclareAmount;
    }

    /**
     * 获取订单中卖家地址信息的映射关系，用于一些老地址到新地址的转换
     *
     * @param oldAddress
     * @return
     */
    public static String getOrderBuyerAddressMapping(String oldAddress) {
        try {
            String newAddress = LogisticsDynamicConfig.getOrder().getRule("orderBuyerAddressMapping", oldAddress);
            if (StringUtils.isBlank(newAddress)) {
                return oldAddress;
            }
            return newAddress;
        } catch (Exception e) {
            logger.error("获取是否跳过口岸验证异常,返回false", e);
        }
        return oldAddress;
    }

    /**
     * 获取订单中卖家地址信息的映射关系，用于一些老地址到新地址的转换
     *
     * @param oldCity
     * @return
     */
    public static String getOrderBuyerCityOnZTOMapping(String oldCity) {
        try {
            String newAddress = LogisticsDynamicConfig.getOrder().getRule("orderBuyerCityOnZTOMapping", oldCity);
            if (StringUtils.isBlank(newAddress)) {
                return oldCity;
            }
            return newAddress;
        } catch (Exception e) {
            logger.error("获取中通地址隐射异常,返回false", e);
        }
        return oldCity;
    }


    /**
     * 获取订单路由轮训的起始出库单ID
     *
     * @return
     */
    public static Long getOrderRoutePollingStartId() {
        try {
            String startId = LogisticsDynamicConfig.getOrder().getRule("pollingRoutesConfig", "startId");
            if (StringUtils.isBlank(startId)) {
                return 193644L;//起始轮训是从购物车上线开始 5-28 号
            }
            return Long.valueOf(startId);
        } catch (Exception e) {
            logger.error("获取是否跳过口岸验证异常,返回false", e);
        }
        return 193644L;
    }



    /**
     * 根据路线判断，是否需要调用wms供应商的接口进行路由轮训
     */
    public static boolean isPollingWmsGetRoutesByLineId(Long lineId){
        if(lineId==null){
            return false;
        }
        String line = lineId.toString();

        try {
            String lines = LogisticsDynamicConfig.getOrder().getRule("pollingRoutesConfig", "line");
            String[] arrLine = lines.split(",");
            for(String s: arrLine){
                if(line.equals(s)){
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("获取线路是否需主动轮训物流信息异常,返回false", e);
        }
        return false;
    }


    public static void main(String[] args){
        int a=10;
        boolean b = isPollingWmsGetRoutesByLineId(300L);
        a++;
    }
}
