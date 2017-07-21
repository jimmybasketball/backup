package com.sfebiz.supplychain.config;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/1/9 下午12:11
 */
public class SupplychainConfig {

    private final static Logger logger = LoggerFactory.getLogger(SupplychainConfig.class);
    private static final SupplychainConfig dynamicConfig = new SupplychainConfig();
    private volatile Integer processInterval;
    private volatile List<String> processMachineList = new ArrayList<String>();
    private volatile Boolean enableProcess;


    private ManagerListener dynamicConfigListener = new ManagerListener() {
        public void receiveConfigInfo(String configInfo) {
            configInfo = StringUtils.strip(configInfo);
            if (StringUtils.isNotBlank(configInfo)) {
                logger.warn("Change logistics config: " + configInfo);
                Properties prop = new Properties();
                try {
                    prop.load(new StringReader(configInfo));
                    updateConfig(prop);
                } catch (Exception e) {
                    logger.error("Parse logistics config failed:" + configInfo, e);
                }
            }
        }

        public Executor getExecutor() {
            return null;
        }
    };
    private volatile Boolean enableGenerateDailyReport = true;

    private SupplychainConfig() {
        String groupId = "HAITAO";
        String dataId = "com.sfebiz.supplychain.dynamic.config";
        DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager(groupId, dataId, dynamicConfigListener);
        String configInfo = defaultDiamondManager.getAvailableConfigureInfomation(1000);
        dynamicConfigListener.receiveConfigInfo(configInfo);
    }

    public static SupplychainConfig getInstance() {
        return dynamicConfig;
    }

    public static void main(String args[]) {
        System.out.println(SupplychainConfig.getInstance().getProcessInterval());
        System.out.println(SupplychainConfig.getInstance().getProcessMachineList().size());
        System.out.println( SupplychainConfig.getInstance().getEnableProcess());

    }

    private void updateConfig(Properties properties) throws Exception {
        BeanUtils.copyProperties(this, properties);
    }

    public Integer getProcessInterval() {
        return processInterval;
    }

    public void setProcessInterval(Integer processInterval) {
        this.processInterval = processInterval;
    }

    public Boolean getEnableProcess() {
        return enableProcess;
    }

    public void setEnableProcess(Boolean enableProcess) {
        this.enableProcess = enableProcess;
    }

    public List<String> getProcessMachineList() {
        return processMachineList;
    }

    public void setProcessMachineList(List<String> processMachineList) {
        this.processMachineList = processMachineList;
    }
    
    public boolean getEnableGenerateDailyReport() {
        return enableGenerateDailyReport;
    }

    public boolean isEnableGenerateDailyReport() {
        return enableGenerateDailyReport;
    }

    public void enableGenerateDailyReport(boolean enableGenerateDailyReport) {
        this.enableGenerateDailyReport = enableGenerateDailyReport;
    }

}
