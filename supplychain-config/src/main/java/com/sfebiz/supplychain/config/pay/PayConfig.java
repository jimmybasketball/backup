package com.sfebiz.supplychain.config.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.exposed.line.enums.LineType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/3
 * Time: 下午1:30
 */
public class PayConfig {
    private static final Logger logger = LoggerFactory.getLogger(PayConfig.class);
    /**
     * 根据支付类型获取 支付企业 名称
     */
    public final static String PAY_GET_PROVIDER_NAME_BY_TYPE = "getProviderByPayType";

    /**
     * 根据支付类型获取 宁波鑫海通达仓 支付映射
     */
    public final static String GET_NBXHTD_PAYSOURCE_BY_PAY_TYPE = "getNbPaySource";

    /**
     * 根据支付类型 判断是否需要在支付的时候创建出库单
     */
    public final static String PAY_GET_IS_NEED_CREATE_ON_PAY_BY_TYPE = "isNeedCreatStockoutOrderOnPay";

    /**
     * 企业信息
     */
    public final static String PROVIDER = "provider";


    /**
     * 支付企业 对电商在特定口岸的 标识编码，用于电商支付单申报
     */
    private static String PAY_KEY = "pay_key";

    /**
     * 支付单申报企业代码
     */
    private static String PAY_DECLARE_PROVIDER_NID = "pay_declare_provider_nid";

    /**
     * 与 支付企业 交互的URL地址
     */
    private static String PAY_PROVIDER_INTERFACE_URL = "interface_url";

    /**
     * 与 支付企业 交互的接入码
     */
    private static String PAY_PROVIDER_INTERFACE_KEY = "interface_key";

    /**
     * 电商企业 在 支付企业 中的备案编码（简称ECP）
     */
    private static String E_CODE_ON_PAY_PROVIDER = "e_code_on_pay";

    /**
     * 支付企业 在 海关 中的备案编码（简称PCE）
     */
    private static String P_CODE_ON_PORT_PROVIDER = "pay_code_on_port";

    /**
     * 电商企业 在 支付企业 回调地址
     */
    private static String PAY_PROVIDER_CALLBACK_URL = "callback_url";

    /**
     * meta
     */
    private static String PAY_PROVIDER_META = "meta";

    /**
     * 根据订单ID获取支付单申报NID
     */
    private static String PAYBILL_DECLARE_PROVIDERNID_BY_BIZID = "getPayBillDeclareProviderNidByBizId";

    /**
     * 口岸在申报企业系统中的名称
     */
    private static String PAY_PROVIDER_PORT_NAME = "port_name";


    /**
     * 是否需要在支付的时候创建出库单
     *
     * @param payType
     * @return
     */
    public static boolean isNeedCreatStockoutOrderOnPay(String payType) {
        try {
            if (StringUtils.isBlank(payType)) {
                return false;
            }
            payType = payType.trim();
            logger.info("pay:" + LogisticsDynamicConfig.getPay());
            logger.info("rule:" + LogisticsDynamicConfig.getPay().getRule(PAY_GET_IS_NEED_CREATE_ON_PAY_BY_TYPE, payType));
            if ("true".equalsIgnoreCase(LogisticsDynamicConfig.getPay().getRule(PAY_GET_IS_NEED_CREATE_ON_PAY_BY_TYPE, payType).trim())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("根据PayType获取是否需要在支付时创建出库单发生异常:" + e + ",PayType:" + payType);
            return true;
        }
    }

    /**
     * 根据支付类型 获取支付提供商企业的编码
     *
     * @param payType
     * @return
     */
    public static String getPayProviderNidByPayType(String payType) {
        if (StringUtils.isBlank(payType)) {
            return null;
        }
        payType = payType.trim();
        String payProviderNid = LogisticsDynamicConfig.getPay().getRule(PAY_GET_PROVIDER_NAME_BY_TYPE, payType);
        if (StringUtils.isBlank(payProviderNid)) {
            return null;
        } else {
            return payProviderNid.trim();
        }
    }

    /**
     * 根据支付类型 获取支付提供商企业的编码
     *
     * @param payType
     * @return
     */
    public static String getNBXHTDPayProviderNidByPayType(String payType) {
        if (StringUtils.isBlank(payType)) {
            return null;
        }
        payType = payType.trim();
        String payProviderNid = LogisticsDynamicConfig.getPay().getRule(GET_NBXHTD_PAYSOURCE_BY_PAY_TYPE, payType);
        if (StringUtils.isBlank(payProviderNid)) {
            return null;
        } else {
            return payProviderNid.trim();
        }
    }

    /**
     * 获取 特定口岸下，支付类型在口岸 编码Key
     *
     * @param portNid 口岸编码，比如 HZPORT，全大写
     * @param payType 支付类型，比如，alipay，全小写
     * @return
     */
    public static String getPayKey(String portNid, String payType) {
        if (StringUtils.isBlank(portNid) || StringUtils.isBlank(payType)) {
            return null;
        }
        payType = payType.trim();
        portNid = portNid.trim();
        String payProviderNid = LogisticsDynamicConfig.getPay().getRule(payType.toLowerCase(), portNid.toUpperCase(), PAY_KEY);
        if (StringUtils.isBlank(payProviderNid)) {
            return null;
        } else {
            return payProviderNid.trim();
        }
    }

    /**
     * 获取 支付单申报 的企业编码，如果不存在，则返回null
     *
     * @param portNid
     * @param payType
     * @param lineType
     * @return
     */
    private static String getPayBillDeclareProviderNidByPayType(String portNid, String payType, String lineType) {
        if (StringUtils.isBlank(portNid) || StringUtils.isBlank(payType) || StringUtils.isBlank(lineType)) {
            return null;
        }
        payType = payType.trim();
        portNid = portNid.trim();
        lineType = lineType.trim();
        String payProviderNid = LogisticsDynamicConfig.getPay().getRule(payType.toLowerCase(), portNid.toUpperCase(), lineType.toLowerCase());
        if (StringUtils.isBlank(payProviderNid)) {
            return null;
        } else {
            return payProviderNid.trim();
        }
    }

    /**
     * 获取 支付单申报 的企业编码，如果不存在，则返回null
     *
     * @param portNid
     * @param payType
     * @param lineType
     * @return
     */
    private static String getPayBillDeclareProviderNidByPayType(String portNid, String payType, String lineType, String bizType) {
        if (StringUtils.isBlank(portNid) || StringUtils.isBlank(payType) || StringUtils.isBlank(lineType) || StringUtils.isBlank(bizType)) {
            return null;
        }
        payType = payType.trim();
        portNid = portNid.trim();
        lineType = lineType.trim();
        bizType = bizType.trim();
        String payProviderNid = LogisticsDynamicConfig.getPay().getRule(payType.toLowerCase(), portNid.toUpperCase(), bizType.toLowerCase() + "-" + lineType.toLowerCase());
        if (StringUtils.isBlank(payProviderNid)) {
            //如果为空，则取默认的申报方式
            payProviderNid = LogisticsDynamicConfig.getPay().getRule(payType.toLowerCase(), portNid.toUpperCase(), lineType.toLowerCase());
            if (StringUtils.isNotBlank(payProviderNid)) {
                return payProviderNid.trim();
            }
            return null;
        } else {
            return payProviderNid.trim();
        }
    }

    /**
     * 获取 订单指定的 支付单申报 的企业编码，如果不存在，则返回null
     *
     * @param bizId
     * @return
     */
    private static String getPayBillDeclareProviderNidByBizId(String bizId) {
        if (StringUtils.isBlank(bizId)) {
            return null;
        }
        bizId = bizId.trim();
        String payProviderNid = LogisticsDynamicConfig.getPay().getRule(PAYBILL_DECLARE_PROVIDERNID_BY_BIZID, bizId);
        if (StringUtils.isBlank(payProviderNid)) {
            return null;
        } else {
            return payProviderNid.trim();
        }
    }

    /**
     * 获取支付申报的providerNid
     * 
     * @param bizId
     * @param portNid
     * @param payType
     * @param lineType
     * @param bizType
     * @return
     */
    public static String getPayBillDeclareProviderNid(String bizId, String portNid, String payType, String lineType, String bizType) {
        
    	//1.首先查询是否有根据订单ID指定的支付单申报企业NID
        String payDeclareProviderNid = PayConfig.getPayBillDeclareProviderNidByBizId(bizId);
        
        //2.查询是否有根据 口岸 & 支付类型 指定的支付单申报企业NID
        if (StringUtils.isBlank(payDeclareProviderNid)) {
            payDeclareProviderNid = PayConfig.getPayBillDeclareProviderNidByPayType(portNid, payType, lineType, bizType);
        }
        logger.info("订单：" + bizId + ",支付类型：" + payType + ",口岸：" + portNid + ",路线模式：" + lineType + " 在配置中的支付单申报企业为：" + payDeclareProviderNid);

        return payDeclareProviderNid;
    }


    /**
     * 获取 支付企业 口岸名称
     *
     * @param payProviderNid
     * @return
     */
    public static String getPayProviderPortName(String payProviderNid, String portNid) {
        try {
            if (StringUtils.isBlank(payProviderNid) || StringUtils.isBlank(payProviderNid)) {
                return null;
            }
            payProviderNid = payProviderNid.trim();
            String ports = LogisticsDynamicConfig.getPay().getRule(PROVIDER, payProviderNid, PAY_PROVIDER_PORT_NAME);

            if (StringUtils.isBlank(ports)) {
                return null;
            } else {

                JSONObject codes = JSON.parseObject(ports);
                if (codes.containsKey(portNid.trim())) {
                    return codes.getString(portNid);
                }
            }
        } catch (Exception e) {
            logger.error("Diamond中支付企业不支持此口岸", e);
        }
        return null;
    }

    /**
     * 获取 支付企业 系统交互URL地址
     *
     * @param payProviderNid
     * @return
     */
    public static String getPayProviderInterfaceUrl(String payProviderNid) {
        if (StringUtils.isBlank(payProviderNid) || StringUtils.isBlank(payProviderNid)) {
            return null;
        }
        payProviderNid = payProviderNid.trim();
        String interfaceUrl = LogisticsDynamicConfig.getPay().getRule(PROVIDER, payProviderNid, PAY_PROVIDER_INTERFACE_URL);
        if (StringUtils.isBlank(interfaceUrl)) {
            return null;
        } else {
            return interfaceUrl.trim();
        }
    }


    /**
     * 获取 支付企业 系统交互的接入码
     *
     * @param payProviderNid
     * @return
     */
    public static String getPayProviderInterfaceKey(String payProviderNid) {
        if (StringUtils.isBlank(payProviderNid) || StringUtils.isBlank(payProviderNid)) {
            return null;
        }
        payProviderNid = payProviderNid.trim();
        String interfaceKey = LogisticsDynamicConfig.getPay().getRule(PROVIDER, payProviderNid, PAY_PROVIDER_INTERFACE_KEY);
        if (StringUtils.isBlank(interfaceKey)) {
            return null;
        } else {
            return interfaceKey.trim();
        }
    }

    /**
     * 获取 电商企业 在 支付企业 中的备案编码
     *
     * @param payProviderNid
     * @return
     */
    public static String getECodeOnPayProvider(String payProviderNid) {
        if (StringUtils.isBlank(payProviderNid) || StringUtils.isBlank(payProviderNid)) {
            return null;
        }
        payProviderNid = payProviderNid.trim();
        String ecode = LogisticsDynamicConfig.getPay().getRule(PROVIDER, payProviderNid, E_CODE_ON_PAY_PROVIDER);
        if (StringUtils.isBlank(ecode)) {
            return null;
        } else {
            return ecode.trim();
        }
    }


    /**
     * 获取 支付企业 在 海关口岸 中的备案编码
     *
     * @param payProviderNid
     * @return
     */
    public static String getPayCodeOnPort(String payProviderNid, String portNid) {
        if (StringUtils.isBlank(payProviderNid) || StringUtils.isBlank(portNid)) {
            return null;
        }
        payProviderNid = payProviderNid.trim();
        String pcodes = LogisticsDynamicConfig.getPay().getRule(PROVIDER, payProviderNid, P_CODE_ON_PORT_PROVIDER);
        if (StringUtils.isBlank(pcodes)) {
            return null;
        } else {
            try {
                JSONObject codes = JSON.parseObject(pcodes);
                if (codes.containsKey(portNid.trim())) {
                    return codes.getString(portNid);
                }
            } catch (Exception e) {
                logger.error("Diamond中支付企业PCODE配置JSON格式不合法", e);
            }
            return null;
        }
    }


    /**
     * 获取 支付企业 在 海关 中的备案名称
     *
     * @param payProviderNid
     * @return
     */
    public static String getPayCompanyName(String payProviderNid) {
        if (StringUtils.isBlank(payProviderNid)) {
            return null;
        }
        payProviderNid = payProviderNid.trim();
        String payCompanyName = LogisticsDynamicConfig.getPay().getRule(PROVIDER, payProviderNid, "name");
        return payCompanyName;
    }

    /**
     * 获取 电商企业 在 支付企业 回调地址
     *
     * @param payProviderNid
     * @return
     */
    public static String getPayProviderCallbackUrl(String payProviderNid) {
        if (StringUtils.isBlank(payProviderNid) || StringUtils.isBlank(payProviderNid)) {
            return null;
        }
        payProviderNid = payProviderNid.trim();
        String callbackUrl = LogisticsDynamicConfig.getPay().getRule(PROVIDER, payProviderNid, PAY_PROVIDER_CALLBACK_URL);
        if (StringUtils.isBlank(callbackUrl)) {
            return null;
        } else {
            return callbackUrl.trim();
        }
    }

    /**
     * 获取meta信息
     *
     * @param payProviderNid
     * @return
     */
    public static Map<String, Object> getPayProviderMeta(String payProviderNid) {
        if (StringUtils.isBlank(payProviderNid)) {
            return null;
        }
        payProviderNid = payProviderNid.trim();
        String meta = LogisticsDynamicConfig.getPay().getRule(PROVIDER, payProviderNid, PAY_PROVIDER_META);
        if (StringUtils.isBlank(meta)) {
            return null;
        } else {
            try {
                return JSON.parseObject(meta);
            } catch (Exception e) {
                logger.error("Diamond中配置Meta JSON格式不合法", e);
            }
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(PayConfig.getPayBillDeclareProviderNid(null, "GZPORT", "alipay", LineType.DIRECTMAIL.getValue(), "1"));
        System.out.println(PayConfig.getPayBillDeclareProviderNid(null, "GZPORT", "alipay", LineType.BONDED.getValue(), "1"));
        //String payCodeOnPort = PayConfig.getPayCodeOnPort("ALIPAY", "GZPORT");
        String s = PayConfig.getPayProviderPortName("YIHUIJINPAY", "HZPORT");
        System.out.println(s);
    }
}
