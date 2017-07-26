package com.sfebiz.supplychain.config.lp;

import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * <p>物流供应商配置</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/9
 * Time: 下午6:48
 */
public class LogisticsProviderConfig {


    /**
     * 根据口岸ID 获取 口岸名称
     */
    public final static String LP_GET_PROVIDER_NID_BY_ID = "getLpNidById";

    /**
     * BSP 编码
     */
    private final static String BSP = "bsp";

    /**
     * ETK 编码
     */
    private final static String ETK = "etk";

    /**
     * 中通 编码
     */
    private final static String ZTO = "zto";
    
    /**
     * 韵达配置
     */
    private final static String YUNDA_CONFIG = "yunda-config";

    /**
     * 物流供应商名称
     */
    private final static String LP_NAME = "name";

    /**
     * 物流供应商 编码
     */
    private final static String LP_NID = "logistics_provider_nid";

    /**
     * 物流供应商 接入码
     */
    private final static String LP_CODE = "code";

    /**
     * 物流供应商 接口类型
     */
    private final static String LP_INTERFACE_TYPE = "interface_type";

    /**
     * 物流供应商 接口访问地址
     */
    private final static String LP_INTERFACE_URL = "interface_url";

    /**
     * 物流供应商 接口密钥
     */
    private final static String LP_INTERFACE_KEY = "interface_key";

    /**
     * 物流供应商 月结账号
     */
    private final static String LP_CUST_ID = "cust_id";

    /**
     * 物流供应商 支付方式
     */
    private final static String LP_PAY_METHOD = "pay_method";

    /**
     * 物流供应商 元信息
     */
    private final static String LP_META = "meta";

    /**
     * 快递100
     */
    private final static String KD = "kd";

    /**
     * 查询ETK国际段物流信息
     */
    private final static String ETK_ROUTES = "etkroutes";

    /**
     * 获取 BSP 的对接URL
     *
     * @return
     */
    public static String getBspInterfaceUrl() {
        return LogisticsDynamicConfig.getLP().getRule(BSP, LP_INTERFACE_URL);
    }

    /**
     * 获取 BSP 的接口交互密钥
     *
     * @return
     */
    public static String getBspInterfaceKey() {
        return LogisticsDynamicConfig.getLP().getRule(BSP, LP_INTERFACE_KEY);
    }

    /**
     * 获取BSP 对应的命令集合
     *
     * @return
     */
    public static String getBspInterfaceType() {
        return LogisticsDynamicConfig.getLP().getRule(BSP, LP_INTERFACE_TYPE);
    }

    /**
     * 获取BSP的接入码
     *
     * @return
     */
    public static String getBspCode() {
        return LogisticsDynamicConfig.getLP().getRule(BSP, LP_CODE);
    }

    /**
     * 获取BSP的元信息
     *
     * @return
     */
    public static String getBspMeta() {
        return LogisticsDynamicConfig.getLP().getRule(BSP, LP_META);
    }


    /**
     * 获取 ETK 的对接URL
     *
     * @return
     */
    public static String getEtkInterfaceUrl() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, LP_INTERFACE_URL);
    }

    /**
     * 获取 ETK 的接口交互密钥
     *
     * @return
     */
    public static String getEtkInterfaceKey() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, LP_INTERFACE_KEY);
    }

    /**
     * 获取 ETK 对应的命令集合
     *
     * @return
     */
    public static String getEtkInterfaceType() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, LP_INTERFACE_TYPE);
    }

    public static String getEtkSource() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, "etk_event_source");
    }

    public static String getEtkTarget() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, "etk_event_target");
    }

    public static String getEtkNoType() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, "etk_no_type");
    }

    public static String getEtkCustomerNo() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, "etk_customer_no");
    }

    public static String getEtkToken() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, "etk_token");
    }

    public static String getEtkTokenKey() {
        return getEtkInterfaceKey();
    }

    public static String getEtkEventAppType() {
        return LogisticsDynamicConfig.getLP().getRule(ETK, "etk_app_type");
    }


    /**
     * 获取 ZTO 对应的命令集合
     *
     * @return
     */
    public static String getZtoInterfaceUrl() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, LP_INTERFACE_URL);
    }

    public static String getZtoInterfaceKey() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, LP_INTERFACE_KEY);
    }

    public static String getZtoInterfaceType() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, LP_INTERFACE_TYPE);
    }

    public static String getZtoSource() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, "zto_event_source");
    }

    public static String getZtoTarget() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, "zto_event_target");
    }

    public static String getZtoNoType() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, "zto_no_type");
    }

    public static String getZtoCustomerNo() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, "zto_customer_no");
    }

    public static String getZtoToken() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, "zto_token");
    }

    public static String getZtoTokenKey() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, LP_INTERFACE_KEY);
    }

    public static String getZtoEventAppType() {
        return LogisticsDynamicConfig.getLP().getRule(ZTO, "zto_app_type");
    }


    /**
     * 根据LP ID 获取 LP 的NID
     *
     * @param lpId
     * @return
     */
    public static String getLpNidById(String lpId) {
        if (StringUtils.isBlank(lpId)) {
            return null;
        }
        lpId = lpId.trim();
        String lpNid = LogisticsDynamicConfig.getLP().getRule(LP_GET_PROVIDER_NID_BY_ID, lpId);
        if (StringUtils.isBlank(lpNid)) {
            return null;
        } else {
            return lpNid.trim();
        }
    }

    /**
     * 获取物流供应商的所有属性
     *
     * @param lpNid 物流供应商Nid
     * @return
     */
    public static Map<String, String> getLpPropertiesByLpNid(String lpNid) {
        if (StringUtils.isBlank(lpNid)) {
            return null;
        }
        lpNid = lpNid.trim();
        Map<String, String> rules = LogisticsDynamicConfig.getLP().getAllPropertiesOnDataId(lpNid.toLowerCase());
        return rules;
    }

    /**
     * 获取LSCM注册时的国家信息
     *
     * @return
     */
    public static String getLscmVendorRegistCountry() {
        String province = LogisticsDynamicConfig.getLP().getRule("lscmVendorInfo", "country");
        if (StringUtils.isNotBlank(province)) {
            return province.trim();
        } else {
            return "中国";
        }
    }

    /**
     * 获取LSCM注册时的省份信息
     *
     * @return
     */
    public static String getLscmVendorRegistProvince() {
        String province = LogisticsDynamicConfig.getLP().getRule("lscmVendorInfo", "province");
        if (StringUtils.isNotBlank(province)) {
            return province.trim();
        } else {
            return "上海";
        }
    }

    /**
     * 获取LSCM注册时的城市信息
     *
     * @return
     */
    public static String getLscmVendorRegistCity() {
        String city = LogisticsDynamicConfig.getLP().getRule("lscmVendorInfo", "city");
        if (StringUtils.isNotBlank(city)) {
            return city.trim();
        } else {
            return "上海市";
        }
    }

    /**
     * 获取LSCM注册时的名称信息
     *
     * @return
     */
    public static String getLscmVendorRegistName() {
        String name = LogisticsDynamicConfig.getLP().getRule("lscmVendorInfo", "name");
        if (StringUtils.isNotBlank(name)) {
            return name.trim();
        } else {
            return "顺丰海淘签约供应商";
        }
    }
    
    /**
     * 获取快递100公司请求URL
     */
    public static String getKD100RequestUrl() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "registUrl");
    }
    
    /**
     * 获取快递100回调URL
     */
    public static String getKD100ResponseUrl() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "callbackUrl");
    }

    /**
     * 获取快递100实时查询URL
     */
    public static String getKD100QueryUrl() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "queryUrl");
    }

    /**
     * 获取快递100公司授权的key
     */
    public static String getKD100RequestKey() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "key");
    }

    /**
     * 获取快递100公司授权的查询key
     */
    public static String getKD100RequestQueryKey() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "queryKey");
    }

    /**
     * 获取快递100公司授权的查询customer
     */
    public static String getKD100RequestCustomer() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "customer");
    }

    /**
     * 获取丰趣海淘、快递100快递公司编码的映射
     */
    public static String getKD100CarrierCodeMapping() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "carrierCode");
    }

    /**
     * 获取快递100回调command版本号
     */
    public static String getKD100CommandVersion() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "type");
    }

    /**
     * 获取需要快递100注册的承运商
     */
    public static String getNeedtoRegist() {
        return LogisticsDynamicConfig.getLP().getRule(KD, "needtoRegist");
    }

    /**
     * 获取查询ETK国际段物流信息:command版本号
     */
    public static String getETKRoutesVersion() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "type");
    }

    /**
     * 获取查询ETK国际段物流信息:source
     */
    public static String getETKRoutesSource() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "source");
    }

    /**
     * 获取查询ETK国际段物流信息:target
     */
    public static String getETKRoutesTarget() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "target");
    }

    /**
     * 获取查询ETK国际段物流信息:url
     */
    public static String getETKRoutesUrl() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "interface_url");
    }

    /**
     * 获取查询ETK国际段物流信息:etk_no_type
     */
    public static String getEtkRoutesNoType() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "etk_no_type");
    }

    public static String getEtkRoutesCustomerNo() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "etk_customer_no");
    }

    public static String getEtkRoutesToken() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "etk_token");
    }

    public static String getEtkRoutesTokenKey() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "etk_token_key");
    }

    public static String getEtkRoutesAppType() {
        return LogisticsDynamicConfig.getLP().getRule(ETK_ROUTES, "etk_app_type");
    }
    
    /**
     * 获取韵达创建订单命令中的城市数据
     * 
     * @return 区域json数据
     */
    public static String getYDAreaConfig(){
    	return LogisticsDynamicConfig.getLP().getRule(YUNDA_CONFIG, "areas");
    }

    public static void main(String[] args) {
        System.out.println(LogisticsProviderConfig.getLpPropertiesByLpNid("bsp"));
        System.out.println(LogisticsProviderConfig.getLpPropertiesByLpNid("bsp"));
    }
}
