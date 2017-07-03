package com.sfebiz.supplychain.config;

import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/3/23 下午8:30
 */
public class PropertiesDiamondConfig<T> extends PropertiesConfig<T> implements ManagerListener {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesDiamondConfig.class);

    public PropertiesDiamondConfig(final String dataId, String dataIdGroup, DynamicValueDecoder<T> dynamicValueDecoder) {
        super(dataId, dataIdGroup, dynamicValueDecoder);
        //初始化数据
        DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager(dataIdGroup, dataId, this);
        defaultDiamondManager.getManagerListeners().get(0).receiveConfigInfo(defaultDiamondManager.getAvailableConfigureInfomation(5000));
    }


    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {
        logger.info(dataId + " receiveConfigInfo:" + configInfo);
        if (configInfo == null) {
            return;
        }
        Properties prop = new Properties();
        try {
            Map<String, T> tmpProperties = new HashMap<String, T>();
            prop.load(new StringReader(configInfo));
            for (Object key : prop.keySet()) {
                tmpProperties.put((String) key, dynamicValueDecoder.decode(prop.getProperty((String) key)));
            }
            properties = tmpProperties;
        } catch (IOException e) {
            logger.error(dataId + " parse failed, configInfo:" + configInfo, e);
        }
    }

}