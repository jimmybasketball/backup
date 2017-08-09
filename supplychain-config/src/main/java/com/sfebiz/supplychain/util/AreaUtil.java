package com.sfebiz.supplychain.util;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;

/**
 * 收货地址区域工具类
 * 
 * @author matt
 * @version $Id: AreaUtil.java, v 0.1 2017年4月26日 下午4:04:00 matt Exp $
 */
public class AreaUtil {

    public static String  CN_DESC     = "中国";

    /** 直辖市列表 */
    private static String zxsCityList = "北京市,重庆市,天津市,上海市";

    /**
     * 校验省、市、区各名称值，并整理成标准名称
     * 
     * @param provinceName
     * @param cityName
     * @param regionName
     * @return
     * @throws ServiceException
     */
    public static AreaCheckResult checkAreaInfo(String provinceName, String cityName,
                                                String regionName) {

        AreaCheckResult areaInfo = new AreaUtil().new AreaCheckResult();

        try {
            // 1. 基本参数校验
            if (StringUtils.isBlank(provinceName) || StringUtils.isBlank(cityName)
                || StringUtils.isBlank(regionName)) {
                areaInfo.setErrorMsg("省、市、区不能为空，请核对清楚");
                return areaInfo;
            }

            // 2. 参数规整
            provinceName = provinceName.trim();
            cityName = cityName.trim();
            regionName = regionName.trim();
            if (zxsCityList.contains(provinceName)) {
                provinceName = provinceName.replace("市", "");
            }

            // 3. 获取省市区配置
            String isOpen = LogisticsDynamicConfig.getLogisticsDynamicConfig().getRule(
                "areaConfig", "isOpen");
            String areaMapStr = LogisticsDynamicConfig.getLogisticsDynamicConfig().getRule(
                "areaConfig", "info");
            if (StringUtils.isBlank(isOpen) || !isOpen.equals("T")
                || StringUtils.isBlank(areaMapStr)) {
                areaInfo.setProvinceName(provinceName);
                areaInfo.setCityName(cityName);
                areaInfo.setRegionName(regionName);
                return areaInfo;
            }

            // 4. 校验省市区
            JSONObject provinceObj = JSON.parseObject(areaMapStr);
            JSONObject cityObj = null;
            JSONObject regionObj = null;
            for (String pName : provinceObj.keySet()) {
                if (pName.contains(provinceName)) {
                    areaInfo.setProvinceName(pName);
                    cityObj = provinceObj.getJSONObject(pName);
                    for (String cName : cityObj.keySet()) {
                        if (cName.contains(cityName)) {
                            areaInfo.setCityName(cName);
                            regionObj = cityObj.getJSONObject(cName);
                            for (String rName : regionObj.keySet()) {
                                if (rName.contains(regionName)) {
                                    areaInfo.setRegionName(rName);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }

            if (StringUtils.isBlank(areaInfo.getProvinceName())) {
                areaInfo.setErrorMsg("省，直辖市或自治区名称错误，请核对清楚");
                return areaInfo;
            }
            if (StringUtils.isBlank(areaInfo.getCityName())) {
                areaInfo.setErrorMsg("市名称错误，请核对清楚，请参考：" + cityObj.keySet());
                return areaInfo;
            }
            if (StringUtils.isBlank(areaInfo.getRegionName())) {
                areaInfo.setErrorMsg("区名称错误，请核对清楚，请参考：" + regionObj.keySet());
                return areaInfo;
            }
        } catch (Exception e) {
            areaInfo.setErrorMsg("校验省、市、区各名称值异常");
        }

        return areaInfo;
    }

    /**
     * 
     * <p>地址校验结果</p>
     * @author matt
     * @Date 2017年7月18日 下午1:58:50
     */
    public class AreaCheckResult {

        /** 省 */
        private String provinceName;
        /** 市 */
        private String cityName;
        /** 区 */
        private String regionName;
        /** 错误信息，不为空，表示校验失败 */
        private String errorMsg;

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        /** 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }
}
