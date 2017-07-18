package com.sfebiz.supplychain.open.exposed.wms.entity.trade;

import java.io.Serializable;

import net.pocrd.annotation.Description;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 云仓交易出库单收货人信息
 * 
 * @author matt
 * @version $Id: OpenWmsTradeConsigneeItem.java, v 0.1 2017年3月23日 下午2:49:11 matt Exp $
 */
@Description("收货人信息")
public class OpenWmsTradeConsigneeItem implements Serializable {

	/** 序列号 */
	private static final long serialVersionUID = -507399369370188379L;

	/** 收货人姓名 */
	@Description("收货人姓名（必填）")
	public String consigneeName;
	
	/** 收货人手机号 */
	@Description("收货人手机号（必填）")
	public String consigneeMobile;
	
	/** 收货地址国家 */
	@Description("收货地址国家（必填）")
	public String addrCountry;
	
	/** 收货地址省 */
	@Description("收货地址省（必填）")
	public String addrProvince;
	
	/** 收货地址市 */
	@Description("收货地址市（必填）")
	public String addrCity;
	
	/** 收货地址区 */
	@Description("收货地址区（必填）")
	public String addrDistrict;
	
	/** 收货地址详细信息 */
	@Description("收货地址详细信息（必填）")
	public String addrDetail;
	
	/** 收件地址邮编 */
	@Description("收货地址邮编（必填）")
	public String zipCode;

	/** 证件类型 1：身份证 */
	@Description("证件类型（必填）")
	public String idType;
	
	/** 身份证号 */
	@Description("证件号（必填）")
	public String idNumber;
	
	/** 身份证正面图片链接 */
	@Description("身份证正面图片链接")
	public String idFountImgUrl;
	
	/** 身份证反面图片链接 */
	@Description("身份证反面图片链接")
	public String idBackImgUrl;
	
    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
