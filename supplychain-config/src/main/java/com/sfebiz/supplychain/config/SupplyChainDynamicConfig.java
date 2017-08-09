package com.sfebiz.supplychain.config;

/**
 * 物流动态配置
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/3/23 下午9:39
 */
public class SupplyChainDynamicConfig extends AbstractRuleConfig<String> {

    private final static String             SFBIZ_CONFIG_DATAID                  = "com.sfebiz.config";
    private final static String             SC_DYNAMIC_CONFIG_DATAID             = "com.sfebiz.sc.common.dynamicConfig";
    private final static String             LOGISTICS_DYNAMIC_CONFIG_GROUPID     = "HAITAO";

    /** 口岸相关动态配置 */
    private static SupplyChainDynamicConfig logisticsDynamicPortConfig           = new SupplyChainDynamicConfig(
                                                                                     "port");
    /** 支付相关 */
    private static SupplyChainDynamicConfig logisticsDynamicPayConfig            = new SupplyChainDynamicConfig(
                                                                                     "pay");
    /** 物流供应商相关 */
    private static SupplyChainDynamicConfig logisticsDynamicLPConfig             = new SupplyChainDynamicConfig(
                                                                                     "lp");
    /** 线路相关 */
    private static SupplyChainDynamicConfig logisticsDynamicLineConfig           = new SupplyChainDynamicConfig(
                                                                                     "line");
    /** 出库单相关 */
    private static SupplyChainDynamicConfig logisticsDynamicOrderConfig          = new SupplyChainDynamicConfig(
                                                                                     "order");
    /** 仓库相关 */
    private static SupplyChainDynamicConfig logisticsDynamicWmsConfig            = new SupplyChainDynamicConfig(
                                                                                     "wms");
    /** 路由相关 */
    private static SupplyChainDynamicConfig logisticsDynamicRouteConfig          = new SupplyChainDynamicConfig(
                                                                                     "route");
    /** 开放接口相关 */
    private static SupplyChainDynamicConfig logisticsDynamicOpenApiConfig        = new SupplyChainDynamicConfig(
                                                                                     "openApi");
    /** 风控相关 */
    private static SupplyChainDynamicConfig logisticsDynamicRiskConfig           = new SupplyChainDynamicConfig(
                                                                                     "risk");
    /** mock相关 */
    private static SupplyChainDynamicConfig logisticsDynamicMockConfig           = new SupplyChainDynamicConfig(
                                                                                     "mock");
    /** 缓存相关 */
    private static SupplyChainDynamicConfig logisticsDynamicCacheConfig          = new SupplyChainDynamicConfig(
                                                                                     "cache");

    private static SupplyChainDynamicConfig logisticsDynamicOrderSplitConfig     = new SupplyChainDynamicConfig(
                                                                                     "split");
    private static SupplyChainDynamicConfig logisticsDynamicSkuPurchaseConfig    = new SupplyChainDynamicConfig(
                                                                                     "skuPurchaseConfig");
    private static SupplyChainDynamicConfig orderDynamicConfig                   = new SupplyChainDynamicConfig(
                                                                                     SFBIZ_CONFIG_DATAID,
                                                                                     LOGISTICS_DYNAMIC_CONFIG_GROUPID,
                                                                                     "trade");
    private static SupplyChainDynamicConfig logisticsDynamicEffectiveAlarmConfig = new SupplyChainDynamicConfig(
                                                                                     "effectiveAlarm");

    private static SupplyChainDynamicConfig logisticsDynamicOssConfig            = new SupplyChainDynamicConfig(
                                                                                     "oss");
    private static SupplyChainDynamicConfig logisticsDynamicConfig               = new SupplyChainDynamicConfig(
                                                                                     null);

    protected SupplyChainDynamicConfig(String subkeyPrefix) {
        super(PropertiesConfigFactory.DIAMOND_CONFIG, SC_DYNAMIC_CONFIG_DATAID,
            LOGISTICS_DYNAMIC_CONFIG_GROUPID, subkeyPrefix,
            new PropertiesConfig.DynamicValueDecoder<String>() {
                @Override
                public String decode(String value) {
                    return value;
                }
            });
    }

    protected SupplyChainDynamicConfig(String dataId, String groupId, String subkeyPrefix) {
        super(PropertiesConfigFactory.DIAMOND_CONFIG, dataId, groupId, subkeyPrefix,
            new PropertiesConfig.DynamicValueDecoder<String>() {
                @Override
                public String decode(String value) {
                    return value;
                }
            });
    }

    /**
     * 获取时效告警相关配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getEffectiveAlarm() {
        return logisticsDynamicEffectiveAlarmConfig;
    }

    /**
     * 获取支付相关配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getPay() {
        return logisticsDynamicPayConfig;
    }

    /**
     * 获取口岸相关配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getPort() {
        return logisticsDynamicPortConfig;
    }

    /**
     * 获取mock相关配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getMock() {
        return logisticsDynamicMockConfig;
    }

    /**
     * 获取缓存相关配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getCache() {
        return logisticsDynamicCacheConfig;
    }

    /**
     * 获取物流供应商配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getLP() {
        return logisticsDynamicLPConfig;
    }

    /**
     * 获取仓库对应路线配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getSplit() {
        return logisticsDynamicOrderSplitConfig;
    }

    /*
     * 获取物流供应商配置信息
     * @return
     */
    public static SupplyChainDynamicConfig getLine() {
        return logisticsDynamicLineConfig;
    }

    /**
     * 获取物流供应商配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getOrder() {
        return logisticsDynamicOrderConfig;
    }

    /**
     * 获取物流配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getRoute() {
        return logisticsDynamicRouteConfig;
    }

    /**
     * 获取订单系统的配置信息
     */
    public static SupplyChainDynamicConfig getOrderConfig() {
        return orderDynamicConfig;
    }

    /**
     * 获取商品补货提醒配置信息
     *
     * @return
     */
    public static SupplyChainDynamicConfig getLogisticsDynamicSkuPurchaseConfig() {
        return logisticsDynamicSkuPurchaseConfig;
    }

    /**
     * 获取仓库的配置信息
     */
    public static SupplyChainDynamicConfig getWmsConfig() {
        return logisticsDynamicWmsConfig;
    }

    public static SupplyChainDynamicConfig getRiskConfig() {
        return logisticsDynamicRiskConfig;
    }

    public static SupplyChainDynamicConfig getOssConfig() {
        return logisticsDynamicOssConfig;
    }

    public static SupplyChainDynamicConfig getOpenApiConfig() {
        return logisticsDynamicOpenApiConfig;
    }

    public static SupplyChainDynamicConfig getLogisticsDynamicConfig() {
        return logisticsDynamicConfig;
    }

}
