package com.sfebiz.supplychain.service.port;

import com.sfebiz.supplychain.exposed.common.enums.PortNid;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 口岸相关辅助类
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-25 09:37
 **/
public class PortUtil {

    /**
     * 获取电商企业在海关的企业备案编码
     *
     * @return
     */
    public static String getCustomsCode(LogisticsLineBO lineBO) throws IllegalArgumentException {
        if (lineBO == null || lineBO.getPortBO() == null) {
            throw new IllegalArgumentException("参数不能为空");
        }


        Map<String,Object> portMeta = JSONUtil.parseJSONMessage(lineBO.getPortBO().getMeta());

        if(portMeta.containsKey("isConpanyJson") && (Boolean)portMeta.get("isConpanyJson")){
            Map<String,Object> portCompanyCode  = JSONUtil.parseJSONMessage(lineBO.getPortBO().getCompanyCode());
            if(portCompanyCode.containsKey(String.valueOf(lineBO.getWarehouseBO().getId()))){
                return (String)portCompanyCode.get(String.valueOf(lineBO.getWarehouseBO().getId()));
            }else{
                return  (String)portCompanyCode.get("DEFAULT");
            }
        }

        if (PortNid.GUANGZHOU.getNid().equalsIgnoreCase(lineBO.getPortBO().getPortNid())) {
                //广州口岸由于使用了丰趣主体&顺丰主体，所以进行了两次企业备案；
                if (lineBO.getClearanceLogisticsProviderBO() == null || StringUtils.isBlank(lineBO.getClearanceLogisticsProviderBO().getLogisticsProviderNid())) {
                    throw new IllegalArgumentException("广州口岸企业编码不符合规范");
                }

                String[] companyCodeArray = lineBO.getPortBO().getCompanyCode().split(",");
                if (companyCodeArray == null || companyCodeArray.length < 2) {
                    throw new IllegalArgumentException("广州口岸企业编码不符合规范");
                }

                //如果配置了多个企业编码，则默认第一个表示丰趣主体、第二个为顺丰主体
                if (lineBO.getClearanceLogisticsProviderBO().getLogisticsProviderNid().startsWith("bsp")) {
                    return companyCodeArray[1].trim();
                } else if (lineBO.getClearanceLogisticsProviderBO().getLogisticsProviderNid().startsWith("gaojie")) {
                    return companyCodeArray[0].trim();
                } else if (lineBO.getClearanceLogisticsProviderBO().getLogisticsProviderNid().startsWith("hwt")) {
                    return companyCodeArray[0].trim();
                } else {
                    throw new IllegalArgumentException("广州口岸企业编码不符合规范");
                }
            } else {
                return lineBO.getPortBO().getCompanyCode();
            }


    }


    /**
     * 获取电商企业在海关的企业备案名称
     *
     * @return
     */
    public static String getCustomsName(LogisticsLineBO lineBO) throws IllegalArgumentException {
        if (lineBO == null || lineBO.getPortBO() == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        Map<String,Object> portMeta = JSONUtil.parseJSONMessage(lineBO.getPortBO().getMeta());
        if(portMeta.containsKey("isConpanyJson") && (Boolean)portMeta.get("isConpanyJson")){
            Map<String,Object> portCompanyName  = JSONUtil.parseJSONMessage(lineBO.getPortBO().getCompanyName());
            if(portCompanyName.containsKey(String.valueOf(lineBO.getWarehouseBO().getId()))){
                return (String)portCompanyName.get(String.valueOf(lineBO.getWarehouseBO().getId()));
            }else{
                return  (String)portCompanyName.get("DEFAULT");
            }
        }

        if (PortNid.GUANGZHOU.getNid().equalsIgnoreCase(lineBO.getPortBO().getPortNid())) {
            String[] companyNameArray = lineBO.getPortBO().getCompanyName().split(",");
            if (companyNameArray == null || companyNameArray.length < 2) {
                throw new IllegalArgumentException("广州口岸企业名称不符合规范");
            }

            //如果配置了多个企业编码，则默认第一个表示丰趣主体、第二个为顺丰主体
            if (lineBO.getClearanceLogisticsProviderBO().getLogisticsProviderNid().startsWith("bsp")) {
                return companyNameArray[1].trim();
            } else if (lineBO.getClearanceLogisticsProviderBO().getLogisticsProviderNid().startsWith("gaojie")) {
                return companyNameArray[0].trim();
            } else if (lineBO.getClearanceLogisticsProviderBO().getLogisticsProviderNid().startsWith("hwt")) {
                return companyNameArray[0].trim();
            } else {
                throw new IllegalArgumentException("广州口岸企业名称不符合规范");
            }

        }else{
            return  lineBO.getPortBO().getCompanyName();
        }
    }

}
