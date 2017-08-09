package com.sfebiz.supplychain.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sfebiz.supplychain.config.LogisticsDynamicConfig;

/**
 * 开放API工具类
 * 
 * @author matt
 * @version $Id: OpenAPIUtil.java, v 0.1 2017年3月28日 下午1:46:53 matt Exp $
 */
public class OpenAPIUtil {

	/**
	 * 判断是否是通过开放云仓API接入的商户
	 * 
	 * @param bizType
	 * @return
	 */
	public static boolean isOpenWmsBizType(String bizType){
		
		if(StringUtils.isBlank(bizType)){
			return false;
		}
		
    	List<String> openStockoutBizTypeList = new ArrayList<String>();
    	String openStockoutBizTypeListStr = LogisticsDynamicConfig.getLogisticsDynamicConfig().getRule("openApi", "openStockoutBizTypeList");
    	if(StringUtils.isNotBlank(openStockoutBizTypeListStr)){
    		openStockoutBizTypeList = Arrays.asList(openStockoutBizTypeListStr.split(","));
    	}
    	if(openStockoutBizTypeList.contains(bizType)){
    		return true;
    	}else{
    		return false;
    	}
	}
}
