package com.sfebiz.supplychain.open.exposed.wms.entity.request;

import java.io.Serializable;
import java.util.List;

import net.pocrd.annotation.Description;

import com.sfebiz.supplychain.open.exposed.wms.entity.trade.WmsTradeOrderSkuItem;

/**
 * 商品信息同步请求实体对象
 * 
 * @author wuyun
 * @version $Id: WmsTradeOrderSkuSyncRequest.java, v 0.1 2017年6月7日  wuyun Exp $
 */
@Description("商品信息同步请求")
public class WmsTradeOrderSkuSyncRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5090065176316732844L;
	
	/**货主编码*/
	@Description("货主编码，必填")
	public String customerCode;
	
	/**操作类型，CREATE 新增 ，EDIT修改*/
	@Description("操作类型，CREATE 新增 ，EDIT修改，必填")
	public String actionType;
	
	/**操作时间*/
	@Description("操作时间，必填")
	public String operateTime;
	
	/**商品信息list*/
	@Description("商品信息list，必填")
	public List<WmsTradeOrderSkuItem> skuList;

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

	public List<WmsTradeOrderSkuItem> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<WmsTradeOrderSkuItem> skuList) {
		this.skuList = skuList;
	}

	@Override
	public String toString() {
		return "WmsTradeOrderSkuSyncEntity [customerCode=" + customerCode + ", actionType=" + actionType
				+ ", operateTime=" + operateTime + ", skuList=" + skuList + "]";
	}
}
