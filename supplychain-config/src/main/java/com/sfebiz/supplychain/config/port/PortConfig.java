package com.sfebiz.supplychain.config.port;

import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.util.JSONUtil;
import net.pocrd.entity.ServiceException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/3
 * Time: 下午2:48
 */
public class PortConfig {

    /**
     * 企业信息
     */
    public final static String PROVIDER = "provider";

    /**
     * 根据口岸ID 获取 口岸名称
     */
    public final static String PORT_GET_PROVIDER_NAME_BY_ID = "getProviderById";

    /**
     * 口岸类型
     */
    public final static String TYPE = "type";

    /**
     * 口岸金额无限制配置
     */
    public static final String BILL_MONEY_LIMIT_BY_CNY = "billMoneyLimitByCNY";

    /**
     * 口岸第一单商品数限制
     */
    public static final String FIRST_BILL_SKU_LIMT = "firstBillSkuLimt";

    /**
     * 口岸订单限制
     */
    public static final String BILL_COUNTLIMT = "billCountLimt";

    /**
     * 口岸累计税费限制
     */
    public static final String TAX_FEE_LIMIT_BY_CNY = "taxFeeLimitByCNY";

    /**
     * 开始申报时间
     */
    public static final String DECLARE_TIME_FM = "declareTimeFm";

    /**
     * 口岸订单数 金额无限制配置
     */
    public static final String NO_LIMIT = "NO_LIMIT";


    /**
     * 根据口岸ID 获取口岸Nid
     *
     * @param portId
     * @return
     */
    public static String getPortNidByPortId(String portId) {
        if (StringUtils.isBlank(portId)) {
            return null;
        }
        portId = portId.trim();
        String portNid = LogisticsDynamicConfig.getPort().getRule(PORT_GET_PROVIDER_NAME_BY_ID, portId);
        if (StringUtils.isBlank(portNid)) {
            return null;
        } else {
            return portNid.trim();
        }
    }

    /**
     * 获取口岸的所有属性
     *
     * @param portNid
     * @return
     */
    public static Map<String, String> getPortPropertiesByPortNid(String portNid) {
        if (StringUtils.isBlank(portNid)) {
            return null;
        }
        portNid = portNid.trim();
        Map<String, String> rules = LogisticsDynamicConfig.getPort().getAllPropertiesOnDataId(PROVIDER, portNid.toUpperCase());
        return rules;
    }

    /**
     * 获取口岸申报金额限制
     * NO_LIMIT 为不限制
     *
     * @param portMeta
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
//    public static Integer getPortBillMoneyLimit(Map<String, Object> portMeta) throws ServiceException {
//        if (null == portMeta || portMeta.size() <= 0) {
//            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION, "口岸[meta]未配置");
//        }
//        String portLimitBillMoneyStr = (String) portMeta.get(BILL_MONEY_LIMIT_BY_CNY);
//        if (StringUtils.isBlank(portLimitBillMoneyStr)) {
//            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION,
//                    "口岸申报订单金额限额[" + BILL_MONEY_LIMIT_BY_CNY + "]未配置");
//        }
//        return Integer.valueOf(portLimitBillMoneyStr);
//    }

    /**
     * 获取口岸第一单申报商品数限制
     * 在大于金额时候再校验
     *
     * @param portMeta
     * @return
     * @throws ServiceException
     */
//    public static Integer getPortFirstBillSkuLimt(Map<String, Object> portMeta) throws ServiceException {
//        if (null == portMeta || portMeta.size() <= 0) {
//            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION, "口岸[meta]未配置");
//        }
//        String portFirstBillSkuLimtCfg = (String) portMeta.get(FIRST_BILL_SKU_LIMT);
//        if (StringUtils.isBlank(portFirstBillSkuLimtCfg)) {
//            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION,
//                    "口岸申报订单金额限额[" + FIRST_BILL_SKU_LIMT + "]未配置");
//        }
//        Integer portFirstBillSkuLimt = 0;
//        if (NO_LIMIT.equals(portFirstBillSkuLimtCfg)) {
//            portFirstBillSkuLimt = Integer.MAX_VALUE;
//        } else {
//            portFirstBillSkuLimt = Integer.valueOf(portFirstBillSkuLimtCfg);
//        }
//        return portFirstBillSkuLimt;
//    }

    /**
     * 获取口岸第一单申报商品数限制
     * 在大于金额时候再校验
     *
     * @param portMeta
     * @return
     * @throws ServiceException
     */
    public static String getPortBillCountLimt(Map<String, Object> portMeta) throws ServiceException {
        if (null == portMeta || portMeta.size() <= 0) {
            //throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION, "口岸[meta]未配置");
        }
        String billCountLimtCfg = (String) portMeta.get(BILL_COUNTLIMT);
        if (StringUtils.isBlank(billCountLimtCfg)) {
//            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION,
//                    "口岸申报订单数限额[" + BILL_COUNTLIMT + "]未配置");
        }
        return billCountLimtCfg;
    }

    public static String getPortTaxLimt(Map<String, Object> portMeta) throws ServiceException {
        if (null == portMeta || portMeta.size() <= 0) {
//            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION, "口岸[meta]未配置");
        }
        String portTaxLimt = (String) portMeta.get(TAX_FEE_LIMIT_BY_CNY);
        if (StringUtils.isBlank(portTaxLimt)) {
//            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION,
//                    "口岸申报订税额限制[" + TAX_FEE_LIMIT_BY_CNY + "]未配置");
        }
        return portTaxLimt;
    }

    /**
     * 获取口岸开始申报时间
     *
     * @param portMeta
     * @return
     * @throws ServiceException
     */
    public static String getPortDecalredTimeFm(Map<String, Object> portMeta) throws ServiceException {
        String timeFm = (String) portMeta.get(DECLARE_TIME_FM);
        if (StringUtils.isBlank(timeFm)) {
//            throw new ServiceException(LogisticsReturnCode.STOCKOUT_ORDER_CONFIG_EXCEPTION,
//                    "口岸开始申报时间[" + DECLARE_TIME_FM + "]未配置");
        }
        return timeFm;
    }

    /**
     * 是否不限制
     *
     * @param cfg
     * @return
     */
    public static boolean isNoLimit(String cfg) {
        if (StringUtils.isBlank(cfg)) {
            return false;
        }
        return NO_LIMIT.equals(cfg);
    }


    /**
     * 获取电商企业在海关的企业备案编码
     *
     * @return
     */
//    public static String getCustomsCode(LineEntity lineEntity) throws IllegalArgumentException {
//        if (lineEntity == null || lineEntity.portEntity == null) {
//            throw new IllegalArgumentException("参数不能为空");
//        }
//
//
//        Map<String,Object> portMeta = JSONUtil.parseJSONMessage(lineEntity.portEntity.meta);
//
//        if(portMeta.containsKey("isConpanyJson") && (Boolean)portMeta.get("isConpanyJson")){
//            Map<String,Object> portCompanyCode  = JSONUtil.parseJSONMessage(lineEntity.portEntity.companyCode);
//            if(portCompanyCode.containsKey(String.valueOf(lineEntity.warehouseEntity.warehouseId))){
//                return (String)portCompanyCode.get(String.valueOf(lineEntity.warehouseEntity.warehouseId));
//            }else{
//                return  (String)portCompanyCode.get("DEFAULT");
//            }
//        }
//
//        if (PortNid.GUANGZHOU.getNid().equalsIgnoreCase(lineEntity.portEntity.portNid)) {
//            //广州口岸由于使用了丰趣主体&顺丰主体，所以进行了两次企业备案；
//                if (lineEntity.clearanceProviderEntity == null || StringUtils.isBlank(lineEntity.clearanceProviderEntity.logisticsProviderNid)) {
//                    throw new IllegalArgumentException("广州口岸企业编码不符合规范");
//                }
//
//                String[] companyCodeArray = lineEntity.portEntity.companyCode.split(",");
//                if (companyCodeArray == null || companyCodeArray.length < 2) {
//                    throw new IllegalArgumentException("广州口岸企业编码不符合规范");
//                }
//
//                //如果配置了多个企业编码，则默认第一个表示丰趣主体、第二个为顺丰主体
//                if (lineEntity.clearanceProviderEntity.logisticsProviderNid.startsWith("bsp")) {
//                    return companyCodeArray[1].trim();
//                } else if (lineEntity.clearanceProviderEntity.logisticsProviderNid.startsWith("gaojie")) {
//                    return companyCodeArray[0].trim();
//                } else if (lineEntity.clearanceProviderEntity.logisticsProviderNid.startsWith("hwt")) {
//                    return companyCodeArray[0].trim();
//                } else {
//                    throw new IllegalArgumentException("广州口岸企业编码不符合规范");
//                }
//            } else {
//                return lineEntity.portEntity.companyCode;
//            }
//
//
//    }


    /**
     * 获取电商企业在海关的企业备案名称
     *
     * @return
     */
//    public static String getCustomsName(LineEntity lineEntity) throws IllegalArgumentException {
//        if (lineEntity == null || lineEntity.portEntity == null) {
//            throw new IllegalArgumentException("参数不能为空");
//        }
//
//        Map<String,Object> portMeta = JSONUtil.parseJSONMessage(lineEntity.portEntity.meta);
//        if(portMeta.containsKey("isConpanyJson") && (Boolean)portMeta.get("isConpanyJson")){
//            Map<String,Object> portCompanyName  = JSONUtil.parseJSONMessage(lineEntity.portEntity.companyName);
//            if(portCompanyName.containsKey(String.valueOf(lineEntity.warehouseEntity.warehouseId))){
//                return (String)portCompanyName.get(String.valueOf(lineEntity.warehouseEntity.warehouseId));
//            }else{
//                return  (String)portCompanyName.get("DEFAULT");
//            }
//        }
//
//        if (PortNid.GUANGZHOU.getNid().equalsIgnoreCase(lineEntity.portEntity.portNid)) {
//            String[] companyNameArray = lineEntity.portEntity.companyName.split(",");
//            if (companyNameArray == null || companyNameArray.length < 2) {
//                throw new IllegalArgumentException("广州口岸企业名称不符合规范");
//            }
//
//            //如果配置了多个企业编码，则默认第一个表示丰趣主体、第二个为顺丰主体
//            if (lineEntity.clearanceProviderEntity.logisticsProviderNid.startsWith("bsp")) {
//                return companyNameArray[1].trim();
//            } else if (lineEntity.clearanceProviderEntity.logisticsProviderNid.startsWith("gaojie")) {
//                return companyNameArray[0].trim();
//            } else if (lineEntity.clearanceProviderEntity.logisticsProviderNid.startsWith("hwt")) {
//                return companyNameArray[0].trim();
//            } else {
//                throw new IllegalArgumentException("广州口岸企业名称不符合规范");
//            }
//
//        }else{
//            return  lineEntity.portEntity.companyName;
//        }
//    }


    /**
     * 根据口岸、承运商编码获取在海关的备案编号
     *
     * @param portNid
     * @param carrierCode
     * @return
     * @throws IllegalArgumentException
     */
    public static String getLogisCompanyCode(String portNid, String carrierCode) throws IllegalArgumentException {
        if (StringUtils.isBlank(portNid) || StringUtils.isBlank(carrierCode)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        String logisticsCompanyCode = LogisticsDynamicConfig.getPort().getRule(carrierCode.toUpperCase(), portNid.toUpperCase(), "logis_company_code");
        return logisticsCompanyCode;
    }

    public static String getCountryCompanyCode(String portNid) {
        if(org.apache.commons.lang3.StringUtils.isBlank(portNid)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        Map<String,String> properties = getPortPropertiesByPortNid(portNid);
        if (properties == null || properties.size() == 0) {
            return null;
        }
        String meta = properties.get("meta");
        String countryCompanyCode = "";
        Map<String, Object> metaData = JSONUtil.parseJSONMessage(meta, Map.class);
        if(null != metaData && metaData.containsKey("country_company_code")){
            countryCompanyCode = (String) metaData.get("country_company_code");
        }

        return countryCompanyCode;
    }

    /**
     * 根据口岸、承运商编码获取在海关的备案名称
     *
     * @param portNid
     * @param carrierCode
     * @return
     * @throws IllegalArgumentException
     */
    public static String getLogisCompanyName(String portNid, String carrierCode) throws IllegalArgumentException {
        if (StringUtils.isBlank(portNid) || StringUtils.isBlank(carrierCode)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        String logisticsCompanyName = LogisticsDynamicConfig.getPort().getRule(carrierCode.toUpperCase(), portNid.toUpperCase(), "logis_company_name");
        return logisticsCompanyName;
    }


    public static void main(String[] args) {
        //System.out.println(PortConfig.getLogisCompanyCode("HZPORT", "ZTO"));
        //System.out.println(PortConfig.getLogisCompanyName("HZPORT", "ZTO"));
        System.out.print(PortConfig.getPortNidByPortId("99"));
    }


}
