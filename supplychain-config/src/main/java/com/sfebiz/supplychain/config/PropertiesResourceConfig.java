package com.sfebiz.supplychain.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/6/1 下午3:29
 */
public class PropertiesResourceConfig<T> extends PropertiesConfig<T> {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesDiamondConfig.class);

    public PropertiesResourceConfig(final String dataId, String dataIdGroup, DynamicValueDecoder<T> dynamicValueDecoder) {
        super(dataId, dataIdGroup, dynamicValueDecoder);
        //初始化数据
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(dataIdGroup + "/" + dataId);
        if (null != inputStream) {
            Properties prop = new Properties();
            try {
                Map<String, T> tmpProperties = new HashMap<String, T>();
                prop.load(new InputStreamReader(inputStream));
                for (Object key : prop.keySet()) {
                    tmpProperties.put((String) key, dynamicValueDecoder.decode(prop.getProperty((String) key)));
                }
                properties = tmpProperties;
            } catch (IOException e) {
                logger.error(dataId + " parse failed! ", e);
            }
        }
    }

}
