package com.sfebiz.supplychain.config.route;

import com.alibaba.fastjson.JSONObject;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>供应链路由服务</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/6/18
 * Time: 下午6:28
 */
public class LogisticsRoutes {

    private static final Logger logger = LoggerFactory.getLogger(LogisticsRoutes.class);



    /**
     * 根据丰趣的物流承运商编码，获取快递100的承运商编码
     *
     * @param fengQuCarrierCode
     * @return
     */
    public static String getKuaiDi100CodeByFengQuCarrierCode(String fengQuCarrierCode) throws Exception {

        if (org.apache.commons.lang3.StringUtils.isEmpty(fengQuCarrierCode)) {
            throw new IllegalArgumentException("承运商编码不能为空");
        }

        Map<String, Object> map = null;
        try {
            map = JSONUtil.parseJSONMessage(LogisticsProviderConfig.getKD100CarrierCodeMapping());
        } catch (Exception e) {
            throw new IllegalArgumentException("获取供应链快递100承运商编码异常");
        }
        if (MapUtils.isEmpty(map) || !map.containsKey(fengQuCarrierCode)) {
            throw new IllegalArgumentException("承运商编码在供应链快递100配置列表不不存在");
        }

        Object value = map.get(fengQuCarrierCode);
        if (value == null) {
            throw new IllegalArgumentException("供应链快递100配置列表异常");
        }
        return value.toString();

    }

    public static List<String> getkeyWordsByCodeAndStatus(String carrierCode, String status) {
        if(org.apache.commons.lang3.StringUtils.isBlank(carrierCode)){
            throw new IllegalArgumentException("承运商编码不能为空");
        }

        String keyWordsList = LogisticsDynamicConfig.getRoute().getRule("getKeyWordsByCode", carrierCode);
        if(org.apache.commons.lang3.StringUtils.isBlank(keyWordsList)){
            logger.warn("承运商编码在getKeyWordsByCode配置列表中不存在");
        }else{
            JSONObject detail = JSONObject.parseObject(keyWordsList);
            if(detail.containsKey(status)){
                String[] keyWordes = detail.getString(status).split(",");
                return Arrays.asList(keyWordes);
            }else {
                logger.warn("承运商" + carrierCode + "未配置"+status+"属性");
            }
        }
        return null;
    }

}
