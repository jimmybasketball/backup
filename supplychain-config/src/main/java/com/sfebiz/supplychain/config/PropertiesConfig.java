package com.sfebiz.supplychain.config;

import java.util.HashMap;
import java.util.Map;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/6/1 下午3:43
 */
public abstract class PropertiesConfig<T> {

    protected String dataId;
    protected String dataIdGroup;
    protected DynamicValueDecoder<T> dynamicValueDecoder;
    protected volatile Map<String, T> properties = new HashMap<String, T>();

    public PropertiesConfig(final String dataId, String dataIdGroup, DynamicValueDecoder<T> dynamicValueDecoder) {
        this.dataId = dataId;
        this.dataIdGroup = dataIdGroup;
        this.dynamicValueDecoder = dynamicValueDecoder;
    }

    public Map<String, T> getProperties() {
        return properties;
    }

    public T getConfig(String configItemName) {
        return properties.get(configItemName);
    }

    public interface DynamicValueDecoder<T> {

        /**
         * 解析动态参数值
         *
         * @param value
         * @return
         */
        T decode(String value);
    }
}
