package com.sfebiz.supplychain.open.exposed.wms.entity.request;

import java.io.Serializable;
import java.util.List;

import net.pocrd.annotation.Description;

@Description("商品信息查询请求")
public class WmsTradeOrderSkuSearchRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7707919739166348596L;
	
	/**货主编码*/
	@Description("货主编码，必填")
	public String customerCode;
	
	/**操作类型，SEARCH*/
	@Description("操作类型:SEARCH，必填")
	public String actionType;
	
	/**操作时间*/
	@Description("操作时间，必填")
	public String operateTime;
	
	/**skuIdList*/
	@Description("skuIdList，必填")
	public List<String> skuList;

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public List<String> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}

	@Override
	public String toString() {
		return "WmsTradeOrderSkuSearchEntity [customerCode=" + customerCode + ", actionType=" + actionType
				+ ", operateTime=" + operateTime + ", skuList=" + skuList + "]";
	}
	
}
