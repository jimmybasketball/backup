package com.sfebiz.supplychain.config;

/**
 * 物流动态配置
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/3/23 下午9:39
 */
public class LogisticsDynamicConfig extends AbstractRuleConfig<String> {

    private final static String SFBIZ_CONFIG_DATAID = "com.sfebiz.config";
    private final static String LOGISTICS_DYNAMIC_CONFIG_DATAID = "com.sfebiz.logistics.common.dynamicConfig";
    private final static String LOGISTICS_DYNAMIC_CONFIG_GROUPID = "HAITAO";


    private static LogisticsDynamicConfig logisticsDynamicPortConfig = new LogisticsDynamicConfig("port");
    private static LogisticsDynamicConfig logisticsDynamicPayConfig = new LogisticsDynamicConfig("pay");
    private static LogisticsDynamicConfig logisticsDynamicLPConfig = new LogisticsDynamicConfig("lp");
    private static LogisticsDynamicConfig logisticsDynamicMockConfig = new LogisticsDynamicConfig("mock");
    private static LogisticsDynamicConfig logisticsDynamicOrderSplitConfig = new LogisticsDynamicConfig("split");
    private static LogisticsDynamicConfig logisticsDynamicLineConfig = new LogisticsDynamicConfig("line");
    private static LogisticsDynamicConfig logisticsDynamicOrderConfig = new LogisticsDynamicConfig("order");
    private static LogisticsDynamicConfig logisticsDynamicWmsConfig = new LogisticsDynamicConfig("wms");
    private static LogisticsDynamicConfig logisticsDynamicSkuPurchaseConfig = new LogisticsDynamicConfig("skuPurchaseConfig");
    private static LogisticsDynamicConfig orderDynamicConfig = new LogisticsDynamicConfig(SFBIZ_CONFIG_DATAID, LOGISTICS_DYNAMIC_CONFIG_GROUPID, "trade");
    private static LogisticsDynamicConfig logisticsDynamicEffectiveAlarmConfig = new LogisticsDynamicConfig("effectiveAlarm");
    private static LogisticsDynamicConfig logisticsDynamicRouteConfig = new LogisticsDynamicConfig("route");
    private static LogisticsDynamicConfig logisticsDynamicRiskConfig = new LogisticsDynamicConfig("risk");
    private static LogisticsDynamicConfig logisticsDynamicOssConfig = new LogisticsDynamicConfig("oss");
    private static LogisticsDynamicConfig logisticsDynamicOpenApiConfig = new LogisticsDynamicConfig("openApi");
    private static LogisticsDynamicConfig logisticsDynamicChannelConfig = new LogisticsDynamicConfig("channels.customize");
    private static LogisticsDynamicConfig logisticsDynamicConfig = new LogisticsDynamicConfig(null);
    private static LogisticsDynamicConfig logisticsDynamicTicketConfig = new LogisticsDynamicConfig("ticket");

    protected LogisticsDynamicConfig(String subkeyPrefix) {
        super(PropertiesConfigFactory.DIAMOND_CONFIG, LOGISTICS_DYNAMIC_CONFIG_DATAID, LOGISTICS_DYNAMIC_CONFIG_GROUPID, subkeyPrefix, new PropertiesConfig.DynamicValueDecoder<String>() {
            @Override
            public String decode(String value) {
                return value;
            }
        });
    }

    protected LogisticsDynamicConfig(String dataId, String groupId, String subkeyPrefix) {
        super(PropertiesConfigFactory.DIAMOND_CONFIG, dataId, groupId, subkeyPrefix, new PropertiesConfig.DynamicValueDecoder<String>() {
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
    public static LogisticsDynamicConfig getEffectiveAlarm() {
        return logisticsDynamicEffectiveAlarmConfig;
    }

    /**
     * 获取支付相关配置信息
     *
     * @return
     */
    public static LogisticsDynamicConfig getPay() {
        return logisticsDynamicPayConfig;
    }

    /**
     * 获取口岸相关配置信息
     *
     * @return
     */
    public static LogisticsDynamicConfig getPort() {
        return logisticsDynamicPortConfig;
    }

    /**
     * 获取mock相关配置信息
     *
     * @return
     */
    public static LogisticsDynamicConfig getMock() {
        return logisticsDynamicMockConfig;
    }

    /**
     * 获取物流供应商配置信息
     *
     * @return
     */
    public static LogisticsDynamicConfig getLP() {
        return logisticsDynamicLPConfig;
    }

    /**
     * 获取仓库对应路线配置信息
     *
     * @return
     */
    public static LogisticsDynamicConfig getSplit() {
        return logisticsDynamicOrderSplitConfig;
    }

    /*
     * 获取物流供应商配置信息
     * @return
     */
    public static LogisticsDynamicConfig getLine() {
        return logisticsDynamicLineConfig;
    }

    /**
     * 获取物流供应商配置信息
     *
     * @return
     */
    public static LogisticsDynamicConfig getOrder() {
        return logisticsDynamicOrderConfig;
    }

    /**
     * 获取物流配置信息
     *
     * @return
     */
    public static LogisticsDynamicConfig getRoute() {
        return logisticsDynamicRouteConfig;
    }

    /**
     * 获取订单系统的配置信息
     */
    public static LogisticsDynamicConfig getOrderConfig() {
        return orderDynamicConfig;
    }

    /**
     * 获取商品补货提醒配置信息
     *
     * @return
     */
    public static LogisticsDynamicConfig getLogisticsDynamicSkuPurchaseConfig() {
        return logisticsDynamicSkuPurchaseConfig;
    }

    /**
     * 获取仓库的配置信息
     */
    public static LogisticsDynamicConfig getWmsConfig() {
        return logisticsDynamicWmsConfig;
    }

    public static LogisticsDynamicConfig getRiskConfig() {
        return logisticsDynamicRiskConfig;
    }

    public static LogisticsDynamicConfig getOssConfig() {
        return logisticsDynamicOssConfig;
    }

    public static LogisticsDynamicConfig getOpenApiConfig(){
        return logisticsDynamicOpenApiConfig;
    }

    public static LogisticsDynamicConfig getChannelConfig() {
        return logisticsDynamicChannelConfig;
    }

    public static LogisticsDynamicConfig getLogisticsDynamicConfig() {
        return logisticsDynamicConfig;
    }

    public static LogisticsDynamicConfig getLogisticsDynamicTicketConfig() {
        return logisticsDynamicTicketConfig;
    }
}
