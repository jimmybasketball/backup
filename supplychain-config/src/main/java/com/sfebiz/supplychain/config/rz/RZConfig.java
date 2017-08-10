package com.sfebiz.supplychain.config.rz;

import com.sfebiz.common.utils.diamond.DynamicConfig;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 实名认证三方服务配置
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-26 18:39
 **/
public class RZConfig {


    public static YiJiFuRz yiJiFuRz;

    private final static Logger logger = LoggerFactory.getLogger(DynamicConfig.class);

    /**
     * 监听
     */
    private static ManagerListener dynamicConfigListener = new ManagerListener() {

        public void receiveConfigInfo(String configInfo) {
            configInfo = StringUtils.strip(configInfo);
            if (StringUtils.isNotBlank(configInfo)) {
                logger.warn("Change alipush-ps config: " + configInfo);
                try {
                    Properties prop = new Properties();
                    prop.load(new StringReader(configInfo));

                    //易极付配置信息
                    String yjfUrl = prop.get("risk.yijifupay.real.name.authentication.url").toString();
                    String yjfPartnerid = prop.get("risk.yijifupay.real.name.authentication.partnerid").toString();
                    String yjfPartnerkey = prop.get("risk.yijifupay.real.name.authentication.partnerkey").toString();

                    yiJiFuRz = new YiJiFuRz(yjfUrl, yjfPartnerid, yjfPartnerkey);
                } catch (Exception e) {
                    logger.error("Parse alipush-ps config failed:" + configInfo, e);
                }
            }
        }

        public Executor getExecutor() {
            return null;
        }
    };

    static {
        try {
            String groupId = "HAITAO";
            String dataId = "com.sfebiz.config.haitao-b2b-supplychain.RZConfig";
            DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager(groupId,
                    dataId, dynamicConfigListener);
            String configInfo = defaultDiamondManager.getAvailableConfigureInfomation(3000);
            dynamicConfigListener.receiveConfigInfo(configInfo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("初始化DynamicConfig失败,失败信息如下：" + e);
        }
    }




    public static void main(String[] args) {
        System.out.println(RZConfig.yiJiFuRz);
    }
}
