package com.sfebiz.supplychain.util;

import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 承运商编码转换
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/8/1 10:20
 */
public class TransCarrierCode {

    // key:丰趣海淘快递公司编码  value:快递100对应的编码
    // 获取快递100的快递公司编码
    public static String getKDCarrierCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        Map<String, Object> map = JSONUtil.parseJSONMessage(LogisticsProviderConfig.getKD100CarrierCodeMapping());
        if (MapUtils.isEmpty(map) || !map.containsKey(code)) {
            return null;
        }

        Object value = map.get(code);
        return null == value ? null : value.toString();
    }

    // 获取自己的快递公司编码
    public static String getCarrierCode(String code) {
        String result = null;
        Map<String, Object> map = JSONUtil.parseJSONMessage(LogisticsProviderConfig.getKD100CarrierCodeMapping());
        if (MapUtils.isEmpty(map)) {
            return result;
        }

        for (Map.Entry<String, Object> temp : map.entrySet()) {
            if (temp.getValue().equals(code)) {
                result = temp.getKey();
                break;
            }
        }
        return result;
    }
}
