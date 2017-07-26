package com.sfebiz.supplychain.config.mock;

import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试Mock配置项
 * Created by zhaojingyang on 2015/4/9.
 */
public class MockConfig {
    private static final Logger logger = LoggerFactory.getLogger(MockConfig.class);
    private static String DEF_OPEN = "true";

    public final static boolean isMocked(String serviceName, String mockKey) {
        try {
            if (StringUtils.isBlank(mockKey)) {
                return false;
            }

            String configValue;

            String isAutotest = System.getProperty("autotest");
            if (StringUtils.isNotEmpty(isAutotest)) {
                // configValue = LogisticsAutoTestConfig.getMock().getRule(serviceName, mockKey);
                configValue = LogisticsDynamicConfig.getMock().getRule(serviceName, mockKey);
            } else {
                configValue = LogisticsDynamicConfig.getMock().getRule(serviceName, mockKey);
            }

            logger.info("mock:service:[" + serviceName + "]key:[" + mockKey + "]value:[" + configValue + "]");
            if (StringUtils.isEmpty(configValue)) {
                return false;
            }

            configValue = configValue.trim();

            if (DEF_OPEN.equalsIgnoreCase(configValue)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.warn("根据service:[" + serviceName + "]key:[" + mockKey + "]获取是否开启Mock发生异常:" + e.getMessage());
            return false;
        }
    }

    public static void main(String args[]) {
        System.out.println(MockConfig.isMocked("platform", "createCommand"));
    }
}
