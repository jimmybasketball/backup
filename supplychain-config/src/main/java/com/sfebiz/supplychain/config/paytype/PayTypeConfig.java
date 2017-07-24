package com.sfebiz.supplychain.config.paytype;

import com.alibaba.fastjson.JSON;
import com.sfebiz.common.utils.diamond.DynamicConfig;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 支付方式diamond配置 --- 里面是枚举类型
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-19 13:44
 **/
public class PayTypeConfig {

    /**
     * 日志
     */
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
                    payTypes = JSON.parseArray(configInfo, PayType.class);
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
            String dataId = "com.sfebiz.config.haitao-b2b-supplychain.payTypeConfig";
            DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager(groupId,
                    dataId, dynamicConfigListener);
            String configInfo = defaultDiamondManager.getAvailableConfigureInfomation(3000);
            dynamicConfigListener.receiveConfigInfo(configInfo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("初始化DynamicConfig失败,失败信息如下：" + e);
        }
    }

    private static volatile List<PayType> payTypes;

    public static List<PayType> getPayTypes() {
        return new ArrayList<PayType>(payTypes);
    }

    public static void main(String[] args) {
        System.out.println(getPayTypes());
    }
}
