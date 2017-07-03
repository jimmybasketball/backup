package com.sfebiz.supplychain.config;

import org.apache.commons.lang.StringUtils;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/6/1 下午4:04
 */
public class PropertiesConfigFactory {

    public final static String RESOURCE_CONFIG = "RESOURCE_CONFIG";

    public final static String DIAMOND_CONFIG = "DIAMOND_CONFIG";

    public static PropertiesConfig generatePropertiesConfig(String type, String dataId, String dataIdGroup, PropertiesConfig.DynamicValueDecoder dynamicValueDecoder) {
        if (StringUtils.equalsIgnoreCase(RESOURCE_CONFIG, type)) {
            return new PropertiesResourceConfig(dataId, dataIdGroup, dynamicValueDecoder);
        } else if (StringUtils.equalsIgnoreCase(DIAMOND_CONFIG, type)) {
            return new PropertiesDiamondConfig(dataId, dataIdGroup, dynamicValueDecoder);
        }
        return null;
    }
}
