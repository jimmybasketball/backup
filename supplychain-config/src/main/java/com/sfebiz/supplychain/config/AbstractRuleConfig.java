package com.sfebiz.supplychain.config;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/3
 * Time: 上午10:21
 */
public abstract class AbstractRuleConfig<T> {

    private String type;
    private String dataId;
    private String dataIdGroup;
    private String subkeyPrefix;
    private PropertiesConfig.DynamicValueDecoder dynamicValueDecoder;
    private volatile PropertiesConfig<T> defaultpropertiesConfig;
    private volatile ConcurrentHashMap<String, PropertiesConfig<T>> appConfigs = new ConcurrentHashMap<String, PropertiesConfig<T>>();


    protected AbstractRuleConfig(String type, final String dataId, String dataIdGroup, String subkeyPrefix, PropertiesConfig.DynamicValueDecoder<T> dynamicValueDecoder) {
        this.type = type;
        this.dataId = dataId;
        this.dataIdGroup = dataIdGroup;
        this.subkeyPrefix = subkeyPrefix;
        this.dynamicValueDecoder = dynamicValueDecoder;
        this.defaultpropertiesConfig = PropertiesConfigFactory.generatePropertiesConfig(type, dataId, dataIdGroup, dynamicValueDecoder);
    }

    public T getRule(String ruleName, String key) {
        T ruleValue = null;
        PropertiesConfig<T> propertiesConfig = appConfigs.get(ruleName);
        if (null == propertiesConfig) {
            String dataId = this.dataId +
                    (StringUtils.isNotEmpty(subkeyPrefix) ? "." + subkeyPrefix : "") + "." + ruleName;
            final PropertiesConfig tmpPropertiesConfig = PropertiesConfigFactory.generatePropertiesConfig(type, dataId, dataIdGroup, dynamicValueDecoder);
            propertiesConfig = appConfigs.putIfAbsent(ruleName, tmpPropertiesConfig);
            if (null == propertiesConfig) {
                propertiesConfig = tmpPropertiesConfig;
            }
        }
        if (null != propertiesConfig) {
            ruleValue = propertiesConfig.getConfig(key);
        }
        if (null == ruleValue) {
            ruleValue = defaultpropertiesConfig.getConfig(ruleName);
        }
        return ruleValue;
    }

    public T getRule(String ruleName, String businessName, String key) {
        T ruleValue = null;
        PropertiesConfig<T> propertiesConfig = appConfigs.get(ruleName + businessName);
        if (null == propertiesConfig) {
            String dataId = this.dataId +
                    (StringUtils.isNotEmpty(subkeyPrefix) ? "." + subkeyPrefix : "")  + "." + ruleName + "." + businessName;
            final PropertiesConfig tmpPropertiesConfig = PropertiesConfigFactory.generatePropertiesConfig(type, dataId, dataIdGroup, dynamicValueDecoder);
            propertiesConfig = appConfigs.putIfAbsent(ruleName + businessName, tmpPropertiesConfig);
            if (null == propertiesConfig) {
                propertiesConfig = tmpPropertiesConfig;
            }
        }
        if (null != propertiesConfig) {
            ruleValue = propertiesConfig.getConfig(key);
        }
        if (null == ruleValue) {
            ruleValue = defaultpropertiesConfig.getConfig(ruleName + businessName);
        }
        return ruleValue;
    }

    /**
     * 获取指定规则下的DATAID里面的所有内容
     *
     * @param businessName
     * @return
     */
    public Map<String, T> getAllPropertiesOnDataId(String businessName) {
        Map<String, T> ruleValues = null;
        PropertiesConfig<T> propertiesConfig = appConfigs.get(businessName);
        if (null == propertiesConfig) {
            String dataId = this.dataId +
                    (StringUtils.isNotEmpty(subkeyPrefix) ? "." + subkeyPrefix : "") + "." + businessName;
            final PropertiesConfig tmpPropertiesConfig = PropertiesConfigFactory.generatePropertiesConfig(type, dataId, dataIdGroup, dynamicValueDecoder);
            propertiesConfig = appConfigs.putIfAbsent(businessName, tmpPropertiesConfig);
            if (null == propertiesConfig) {
                propertiesConfig = tmpPropertiesConfig;
            }
        }
        if (null != propertiesConfig) {
            ruleValues = propertiesConfig.getProperties();
        }

        return ruleValues;
    }

    /**
     * 获取指定规则下的DATAID里面的所有内容
     *
     * @param ruleName
     * @param businessName
     * @return
     */
    public Map<String, T> getAllPropertiesOnDataId(String ruleName, String businessName) {
        Map<String, T> ruleValues = null;
        PropertiesConfig<T> propertiesConfig = appConfigs.get(ruleName + businessName);
        if (null == propertiesConfig) {
            String dataId = this.dataId +
                    (StringUtils.isNotEmpty(subkeyPrefix) ? "." + subkeyPrefix : "") + "." + ruleName + "." + businessName;
            final PropertiesConfig tmpPropertiesConfig = PropertiesConfigFactory.generatePropertiesConfig(type, dataId, dataIdGroup, dynamicValueDecoder);
            propertiesConfig = appConfigs.putIfAbsent(ruleName + businessName, tmpPropertiesConfig);
            if (null == propertiesConfig) {
                propertiesConfig = tmpPropertiesConfig;
            }
        }
        if (null != propertiesConfig) {
            ruleValues = propertiesConfig.getProperties();
        }

        return ruleValues;
    }

}

