package com.sfebiz.supplychain.config;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/6/1 下午3:26
 */
public class LogisticsAutoTestConfig extends AbstractRuleConfig<String> {

    private final static String LOGISTICS_DYNAMIC_CONFIG_DATAID = "com.sfebiz.logistics.common.dynamicConfig";
    private final static String LOGISTICS_DYNAMIC_CONFIG_GROUPID = "HAITAO";

    private static LogisticsAutoTestConfig logisticsAutoTestMockConfig = new LogisticsAutoTestConfig("mock");

    protected LogisticsAutoTestConfig(String subkeyPrefix) {
        super(PropertiesConfigFactory.RESOURCE_CONFIG, LOGISTICS_DYNAMIC_CONFIG_DATAID, LOGISTICS_DYNAMIC_CONFIG_GROUPID, subkeyPrefix, new PropertiesConfig.DynamicValueDecoder<String>() {
            @Override
            public String decode(String value) {
                return value;
            }
        });
    }

    /**
     * 获取mock相关配置信息
     *
     * @return
     */
    public static LogisticsAutoTestConfig getMock() {
        return logisticsAutoTestMockConfig;
    }
}
