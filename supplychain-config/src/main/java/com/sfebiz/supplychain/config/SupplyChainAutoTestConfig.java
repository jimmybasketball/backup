package com.sfebiz.supplychain.config;

/**
 * @author liujc
 * @create 2017-06-30 14:13
 **/
public class SupplyChainAutoTestConfig extends AbstractRuleConfig<String>{
    private final static String SUPPLYCHAIN_DYNAMIC_CONFIG_DATAID = "com.sfebiz.supplychain.common.dynamicConfig";
    private final static String SUPPLYCHAIN_DYNAMIC_CONFIG_GROUPID = "HAITAO";

    private static SupplyChainAutoTestConfig supplyChainAutoTestConfig = new SupplyChainAutoTestConfig("test");

    protected SupplyChainAutoTestConfig(String subkeyPrefix) {
        super(PropertiesConfigFactory.DIAMOND_CONFIG, SUPPLYCHAIN_DYNAMIC_CONFIG_DATAID, SUPPLYCHAIN_DYNAMIC_CONFIG_GROUPID, subkeyPrefix, new PropertiesConfig.DynamicValueDecoder<String>() {
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
    public static SupplyChainAutoTestConfig getTest() {
        return supplyChainAutoTestConfig;
    }
}
