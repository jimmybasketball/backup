package com.sfebiz.supplychain.protocol.fineex.skuSyn;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 同步商品子母码参数实体
 * Created by wuyun on 2017/3/20.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class FineExSkuSubSynRequest {
	@Description("操作类型：ADD-新增 MODIFY-修改 DELETE-删除")
    @XmlElement(name = "actionType",required = true)
    private String actionType;
	
	@Description("商家条码 [唯一确定该商品,可作为分拣依据]")
    @XmlElement(name = "barCode",required = true)
	private String barCode;
	
	@Description("子条码")
    @XmlElement(name = "subCode",required = true)
	private String subCode;
	
	@Description("原始子条码（actionType=MODIFY 必填）")
    @XmlElement(name = "oldSubCode",required = true)
	private String oldSubCode;
	
	/**
     * skuId
     */
    private Long skuId;
    /**
     * 仓库的关联ID
     */
    private Long storehouseId;
    /**
     * 是否是组合商品
     */
    private Integer combination;

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getOldSubCode() {
		return oldSubCode;
	}

	public void setOldSubCode(String oldSubCode) {
		this.oldSubCode = oldSubCode;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getStorehouseId() {
		return storehouseId;
	}

	public void setStorehouseId(Long storehouseId) {
		this.storehouseId = storehouseId;
	}

	public Integer getCombination() {
		return combination;
	}

	public void setCombination(Integer combination) {
		this.combination = combination;
	}
	
}
