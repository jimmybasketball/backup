package com.sfebiz.supplychain.config.line.validate;

import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaojingyang on 2015/6/11.
 * com.sfebiz.logistics.common.dynamicConfig.line.getLineValidateById
 * 1={"billMoneyLimitByCNY":"1000",
 * "firstBillSkuLimt":"1",
 * "billCountLimt":"6",
 * "taxFeeLimitByCNY":"50",
 * "declareTimeFm":"16:00:00"}
 */
public class LineValidateConfig {

    /**
     * 根据口岸ID 获取 口岸名称
     */
    public final static String LINE_GET_VALIDATE_BY_ID = "getLineValidateById";

    /**
     * 是否需要口岸验证
     */
    public final static String NEED_PORT_VALIDATE = "needPortValidate";
    /**
     * 线路分组
     */
    public final static String LINE_GROUP = "lineGroup";

    /**
     * 线路分组分隔符
     */
    public final static String LINE_GROUP_SEQ = "-";

    /**
     * 根据口岸ID 获取口岸Nid
     *
     * @param lineId
     * @return
     */
    public static String getPortNidByLineId(String lineId) {
        if (StringUtils.isBlank(lineId)) {
            return null;
        }
        lineId = lineId.trim();
        String portNid = LogisticsDynamicConfig.getLine().getRule(LINE_GET_VALIDATE_BY_ID, lineId);
        if (StringUtils.isBlank(portNid)) {
            return null;
        } else {
            return portNid.trim();
        }
    }

    public static void main(String args[]) {
        String ss = getPortNidByLineId("1");
        Map<String, Object> cfgMap = JSONUtil.parseJSONMessage(ss);
        Object obj = cfgMap.get("lineGroup");
        List d = JSONUtil.parseJSONMessage(obj.toString(), List.class);
        System.out.println(d);
        String[] dd = (String[]) obj;
        System.out.println(dd);

    }
}
