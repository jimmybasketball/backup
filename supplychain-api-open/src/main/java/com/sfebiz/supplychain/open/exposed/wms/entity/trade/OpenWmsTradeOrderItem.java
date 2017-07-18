package com.sfebiz.supplychain.open.exposed.wms.entity.trade;

import java.io.Serializable;
import java.util.List;

import net.pocrd.annotation.Description;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 云仓交易出库单订单信息
 * 
 * @author matt
 * @version $Id: OpenWmsTradeOrderItem.java, v 0.1 2017年3月23日 下午3:21:47 matt Exp
 * 
 */
@Description("订单信息")
public class OpenWmsTradeOrderItem implements Serializable {

    /** 序列号 */
    private static final long serialVersionUID = 1658693729844975315L;

    /** 订单编号 */
    @Description("商户订单号（必填）")
    public String merchantOrderId;
    /** 订单来源 */
    @Description("商家订单来源，预留跟踪订单使用")
    public String tradesource;

    /** 操作类型（必填）：1 新增，2 修改 */
    @Description("操作类型（必填）")
    public String actionType;
    /** 订单类型：1 正常销售，2 预售 （必填） */
    @Description("订单类型（必填）")
    public String orderType;
    /** 业务服务类型：提供的订单服务类型 （必填） */
    @Description("业务服务类型（必填）")
    public String serviceType;

    /** 仓库标识，仓库Nid */
    @Description("仓库标识（必填）")
    public String warehouseCode;
    /** 指定线路标识 */
    @Description("线路标识")
    public String routeCode;
    /** 货主标识 */
    @Description("货主标识（必填）")
    public String customerCode;

    /** 订单总金额 */
    @Description("订单总金额，单位元（必填）（注：tradeAmount = goodsTotalAmount - discountAmount + freight + insuranceValue）")
    public String tradeAmount;
    /** 商品总金额 */
    @Description("商品总金额，单位元 （必填）")
    public String goodsTotalAmount;
    /** 折扣金额 */
    @Description("折扣金额，单位元（必填）")
    public String discountAmount;
    /** 运费 */
    @Description("运费，单位元（必填）")
    public String freight;
    /** 保险费用 */
    @Description("保险费用，单位元")
    public String insuranceValue;

    /** 预计发货时间 */
    @Description("预计发货时间")
    public String expectedShipmentTime;
    /** 订单下单时间 */
    @Description("订单下单时间")
    public String tradeTime;

    /** 物流大头笔 */
    @Description("物流大头笔")
    public String destCode;
    /** 运单号 */
    @Description("运单号")
    public String mailNo;
    /** 承运商编码 */
    @Description("承运商编码 ")
    public String carrierCode;

    /** 商品条目信息 */
    @Description("商品条目信息（必填）")
    public List<OpenWmsTradeGoodsItem> items;

    /** 收货人信息 */
    @Description("收货人信息（必填）")
    public OpenWmsTradeConsigneeItem consigneeInfo;

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this,
		ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
