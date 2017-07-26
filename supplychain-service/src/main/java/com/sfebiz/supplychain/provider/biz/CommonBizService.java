package com.sfebiz.supplychain.provider.biz;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.exposed.sku.api.SkuService;
import com.sfebiz.supplychain.persistence.base.sku.manager.ProductDeclareManager;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.LogisticsProviderDetailManager;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 通用接口处理方法集合服务
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-26 10:12
 **/
@Component("commonBizService")
public class CommonBizService implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger("CommandLogger");

    private static ApplicationContext applicationContext;

    @Resource
    private ProductDeclareManager productDeclareManager;

//    @Resource
//    private MixedSkuManager mixedSkuManager;

    @Resource
    private LogisticsProviderDetailManager logisticsProviderDetailManager;

    @Resource
    private WarehouseManager warehouseManager;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private SkuService skuService;

//    @Resource
//    private MessageProducer supplyChainMessageProducer;

    public static CommonBizService getInstance() {
        return (CommonBizService) applicationContext.getBean("commonBizService");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 同步发网，通用发起post请求
     *
     * @param method  调用方法
     * @param xmlData 请求报文
     * @return
     */
    public String fineExSend(String method, String xmlData) {
        logger.info("同步FineEx发网请求参数：method：" + method + ";xmlData:" + xmlData);
        Map<String, String>  properties = getProviderEntityByproviderNId("fine-ex");
        if (null == properties) {
            logger.warn("同步fineEx发网,注入配置为空");
            throw new RuntimeException("同步fineEx发网,注入配置为空");
        }
        logger.info("fine-ex注入参数：" + properties);
        String url = properties.get("interface_url");
        String app_key = properties.get("interface_key");;
        String app_secret = "";
        String partner_id = "";
        if (StringUtils.isNotBlank(properties.get("meta"))) {
            Map<String, Object> map = JSONUtil.parseJSONMessage(properties.get("meta"));
            if (null != map) {
                app_secret = (null != map.get("app_secret")) ? (String) map.get("app_secret") : "";
                partner_id = (null != map.get("partner_id")) ? (String) map.get("partner_id") : "";
            }
        }
        //if (null==map){return null;}
        if (org.apache.commons.lang.StringUtils.isBlank(method)) {
            logger.warn("同步fineEx发网method为空");
            throw new RuntimeException("同步fineEx发网method为空");
        }
        if (org.apache.commons.lang.StringUtils.isBlank(xmlData)) {
            logger.warn("同步fineEx发网报文为空");
            throw new RuntimeException("同步fineEx发网报文为空");
        }
        StringBuilder signSb = new StringBuilder();
        Date date = new Date();//时间
        signSb.append("app_key" + app_key);
        signSb.append("method" + method);
        signSb.append("partner_id" + partner_id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        signSb.append("timestamp" + sdf.format(date));
        signSb.append("v2.0");
        signSb.append(xmlData);
        signSb.insert(0, app_secret);//首位追加secret
        signSb.append(app_secret);
        String sign = MD5Util.MD5In32(signSb.toString());
        //System.out.println("MD5sign签名："+sign);
        StringBuilder apiParam = new StringBuilder();
        String result = null;
        try {
            apiParam.append(url);
            apiParam.append("?method=" + URLEncoder.encode(method, "UTF-8"));
            apiParam.append("&app_key=" + URLEncoder.encode(app_key, "UTF-8"));
            apiParam.append("&partner_id=" + URLEncoder.encode(partner_id, "UTF-8"));
            String time = sdf.format(date);
            apiParam.append("&timestamp=" + URLEncoder.encode(time, "UTF-8"));
            apiParam.append("&v=2.0");
            apiParam.append("&sign=" + URLEncoder.encode(sign, "UTF-8"));
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向fineEx仓库下发指令]")
                    .addParm("url", apiParam.toString())
                    .addParm("内容", xmlData)
                    .log();

            result = HttpUtil.postFormByHttp(apiParam.toString(), xmlData);

            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向fineEx仓库下发指令完成]")
                    .addParm("回复报文", result)
                    .log();
        } catch (Exception e) {
            logger.error("同步fineEx post请求错误", e);
            throw new RuntimeException("同步fineEx post请求错误");
        }
        return result;
    }


    /**
     * 根据物流供应商ID 获取物流供应商diamond信息
     *
     * @param providerId
     * @return
     */
    public Map<String, String> getProviderEntityById(long providerId) {
        String lpNid = LogisticsProviderConfig.getLpNidById(String.valueOf(providerId));
        if (StringUtils.isBlank(lpNid)) {
            return null;
        }
        Map<String, String> properties = LogisticsProviderConfig.getLpPropertiesByLpNid(lpNid);
        if (properties == null || properties.size() == 0) {
            return null;
        }
        return properties;
    }
    /**
     * 根据供应商NID 获取物流供应商diamond信息
     *
     * @param providerNId
     * @return
     */
    public Map<String, String> getProviderEntityByproviderNId(String providerNId) {
        Map<String, String> properties = LogisticsProviderConfig.getLpPropertiesByLpNid(providerNId);
        if (properties == null || properties.size() == 0) {
            return null;
        }
        return properties;
    }
}