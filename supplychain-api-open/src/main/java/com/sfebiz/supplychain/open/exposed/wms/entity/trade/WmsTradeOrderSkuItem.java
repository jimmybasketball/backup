package com.sfebiz.supplychain.open.exposed.wms.entity.trade;

import java.io.Serializable;

import net.pocrd.annotation.Description;
/**
 * Created by wuyun on 2017/6/6.
 */
@Description("商品同步信息")
public class WmsTradeOrderSkuItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167109158899344180L;
	
	/**商品ID*/
	@Description("商品ID，必填")
	public String skuId;
	
	/**商品条码*/
	@Description("商品条码，必填")
	public String skuBarCode;
	
	/**商品名称*/
	@Description("商品名称，必填")
	public String skuName;
	
	/**商品中文名称*/
	@Description("商品中文名称，必填")
	public String skuforeignname;
	
	/**商品规格*/
	@Description("商品规格，必填")
	public String specification;
	
	/**商品类目*/
	@Description("商品类目，必填")
	public String categoryName;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSkuBarCode() {
		return skuBarCode;
	}

	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuforeignname() {
		return skuforeignname;
	}

	public void setSkuforeignname(String skuforeignname) {
		this.skuforeignname = skuforeignname;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "WmsTradeOrderSkuEntity [skuId=" + skuId + ", skuBarCode=" + skuBarCode + ", skuName=" + skuName
				+ ", skuforeignname=" + skuforeignname + ", specification=" + specification + ", categoryName="
				+ categoryName + "]";
	}
	
}
