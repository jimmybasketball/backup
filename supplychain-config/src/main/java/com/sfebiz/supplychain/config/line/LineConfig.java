package com.sfebiz.supplychain.config.line;

import com.alibaba.fastjson.JSONObject;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>路线配置</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/3
 * Time: 下午1:30
 */
public class LineConfig {

    private static final Logger logger = LoggerFactory.getLogger(LineConfig.class);

    /**
     * 根据路线ID获取路线NID
     */
    public final static String PAY_GET_PROVIDER_NAME_BY_TYPE = "getLineNidById";

    /**
     * 获取路线是否被暂时关闭
     *
     * @param lineId
     * @return
     */
    public static Boolean getLineIsTemporaryClose(Long lineId) {
        try {
            if (lineId == null) {
                return false;
            }
            String closeLines = LogisticsDynamicConfig.getLine().getRule("lineTemporaryCloseConfig", "lines");
            if (StringUtils.isBlank(closeLines)) {
                return false;
            } else {
                String[] closeLineArray = closeLines.split(",");
                for (String closeLine : closeLineArray) {
                    if (closeLine.trim().equals(lineId.toString())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("获取路线是否暂时关闭异常", e);
        }
        return false;
    }

    /**
     * 特殊订单跳过关闭的路线
     *
     * @param bizId
     * @return
     */
    public static Boolean getLineIsTemporaryCloseSpecialOrderIsSkip(String bizId) {
        try {
            if (StringUtils.isBlank(bizId)) {
                return false;
            }
            String bizIdIsSkip = LogisticsDynamicConfig.getLine().getRule("lineTemporaryCloseSpecialOrderIsSkip", bizId);
            if (StringUtils.isBlank(bizIdIsSkip)) {
                return false;
            } else {
                if ("true".equalsIgnoreCase(bizIdIsSkip)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("获取路线暂时关闭特殊订单是否跳过异常", e);
        }
        return false;
    }

    /**
     * 获取路线关闭后消息延迟时间
     *
     * @return
     */
    public static Long getLineCloseDelayMill() {
        try {
            String delayMill = LogisticsDynamicConfig.getLine().getRule("lineTemporaryCloseConfig", "delayMill");
            if (org.apache.commons.lang.StringUtils.isBlank(delayMill)) {
                return 0l;
            } else {
                return Long.valueOf(delayMill);
            }
        } catch (Exception e) {
            logger.error("获取路线暂时关闭延迟时间异常", e);
        }
        return 0l;
    }


    /**
     * 根据商户业务类型，获取海淘无支付流的三方渠道所支持的路线信息
     *
     * @param bizType
     * @return 返回空，表示没有配置
     */
    public static List<Long> getLinesOnNotPayMerchant(String bizType) {
        List<Long> lineList = new ArrayList<Long>();
        try {
            if (StringUtils.isBlank(bizType)) {
                return lineList;
            }
            String lines = LogisticsDynamicConfig.getLine().getRule("getLinesOnNotPayMerchant", bizType);
            if (StringUtils.isBlank(lines)) {
                return null;
            } else {
                String[] lineArray = lines.split(",");
                for (String line : lineArray) {
                    lineList.add(Long.valueOf(line));
                }
            }
        } catch (Exception e) {
            logger.error("获取无支付渠道供应商支持的路线异常，bizType：" + bizType, e);
        }
        return lineList;
    }

//    /**
//     * 根据路线ID获取路线的物流时效信息
//     *
//     * @param lineId
//     * @return 获取失败时返回null
//     */
//    public static String getLineTimeLimitByLineId(Long lineId) {
//        try {
//            if (lineId == null) {
//                return null;
//            }
//            Long warehouseId = OrderSplit.getWarehouseIdByLineId(lineId);
//            List<Map<String, Object>> lines = OrderSplit.getWarehouseSupportLines(warehouseId);
//            if (CollectionUtils.isEmpty(lines)) {
//                return null;
//            }
//            for (Map<String, Object> lineConfig : lines) {
//                if (lineConfig.containsKey("id")
//                        && lineId.equals(Long.valueOf(String.valueOf(lineConfig.get("id"))))
//                        && lineConfig.containsKey("timeLimit")) {
//                    return String.valueOf(lineConfig.get("timeLimit"));
//                }
//            }
//        } catch (Exception e) {
//            logger.error("[供应链]根据路线ID获取物流时效信息异常", e);
//        }
//
//        return null;
//    }

    /**
     * 根据承运商Code获取承运商名称
     *
     * @param carrierCode
     * @return
     */
    public static String getCarrierNameByCode(String carrierCode) throws Exception {
        if (StringUtils.isBlank(carrierCode)) {
            throw new IllegalArgumentException("承运商编码不能为空");
        }
        String carrierDetail = LogisticsDynamicConfig.getLine().getRule("getCarrierNameByCode", carrierCode);
        if (StringUtils.isBlank(carrierDetail)) {
            throw new IllegalArgumentException("承运商信息未配置，编码：" + carrierCode);
        } else {
            try {
                JSONObject detail = JSONObject.parseObject(carrierDetail);
                if (detail.containsKey("name")) {
                    return detail.getString("name");
                } else {
                    throw new IllegalArgumentException("承运商" + carrierCode + "未配置Name属性");
                }
            } catch (Exception e) {
                logger.error("[供应链]获取承运商信息异常", e);
                throw e;
            }
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println(LineConfig.getLinesOnNotPayMerchant("MENGDIAN"));
    }

}
