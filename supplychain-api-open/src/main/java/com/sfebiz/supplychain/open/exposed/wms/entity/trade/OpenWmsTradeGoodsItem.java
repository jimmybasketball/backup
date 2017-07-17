package com.sfebiz.supplychain.open.exposed.wms.entity.trade;

import java.io.Serializable;

import net.pocrd.annotation.Description;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 云仓交易出库单商品信息
 * 
 * @author matt
 * @version $Id: OpenWmsTradeGoodsItem.java, v 0.1 2017年3月23日 下午3:17:21 matt Exp $
 */
@Description("商品条目信息")
public class OpenWmsTradeGoodsItem implements Serializable {

	/** 序列号 */
	private static final long serialVersionUID = 840854831218337501L;

	/** 商品条码（必填） */
	@Description("商品条码（必填）")
	public String barCode;
	
	/** 商品单价（必填） */
	@Description("商品单价（必填）")
	public String skuUnitPrice;
	
	/** 数量（必填） */
	@Description("商品数量（必填）")
	public String quantity;
	
	/** 商品面单打印名称 */
	@Description("商品面单打印名称")
	public String skuBillName;
	
	/** 商品批次号 */
	@Description("商品批次号")
	public String batchNo;
	
    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
