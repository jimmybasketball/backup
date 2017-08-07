package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"OrderFrom","OrderShop","OrderNo","GoodsAmount","PostFee","Amount",
		"BuyerAccount","TaxAmount","DisAmount","Promotions","Goods"})
@XmlRootElement(name="Order")
@Description("进口订单信息")
public class ImportOrderInfo extends NbbsBody implements Serializable{
	private static final long serialVersionUID = -7779278406451567909L;
	@Description("购物网站代码")
	@XmlElement(nillable = false, required = false)
	private String OrderFrom;
	
	@Description("备案店铺代码")
	@XmlElement(nillable = false, required = false)
	private String OrderShop;
	
	@Description("订单号")
	@XmlElement(nillable = false, required = false)
	private String OrderNo;
	
	@Description("订单总金额")
	@XmlElement(nillable = false, required = false)
	private double GoodsAmount;
	
	@Description("运费")
	@XmlElement(nillable = false, required = false)
	private double PostFee;
	
	@Description("买家实付金额")
	@XmlElement(nillable = false, required = false)
	private double Amount;
	
	@Description("买家账号")
	@XmlElement(nillable = false, required = false)
	private String BuyerAccount;
	
	@Description("税额")
	@XmlElement(nillable = false, required = false)
	private int TaxAmount;
	
	@Description("优惠金额")
	@XmlElement(nillable = false, required = false)
	private String DisAmount;
	
	@Description("优惠清单")
	@XmlElement(nillable = false, required = false)
	private PromotionsInfo Promotions;
	
	@Description("商品信息")
	@XmlElement(nillable = false, required = false)
	private GoodsInfo Goods;

	public String getOrderFrom() {
		return OrderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		OrderFrom = orderFrom;
	}

	public String getOrderShop() {
		return OrderShop;
	}

	public void setOrderShop(String orderShop) {
		OrderShop = orderShop;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}



	public double getGoodsAmount() {
		return GoodsAmount;
	}

	public void setGoodsAmount(double goodsAmount) {
		GoodsAmount = goodsAmount;
	}

	

	public double getPostFee() {
		return PostFee;
	}

	public void setPostFee(double postFee) {
		PostFee = postFee;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	public String getBuyerAccount() {
		return BuyerAccount;
	}

	public void setBuyerAccount(String buyerAccount) {
		BuyerAccount = buyerAccount;
	}

	public int getTaxAmount() {
		return TaxAmount;
	}

	public void setTaxAmount(int taxAmount) {
		TaxAmount = taxAmount;
	}

    public String getDisAmount() {
        return DisAmount;
    }

    public void setDisAmount(String disAmount) {
        DisAmount = disAmount;
    }

    public PromotionsInfo getPromotions() {
		return Promotions;
	}

	public void setPromotions(PromotionsInfo Promotions) {
		this.Promotions = Promotions;
	}

	public GoodsInfo getGoods() {
		return Goods;
	}

	public void setGoods(GoodsInfo Goods) {
		this.Goods = Goods;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("OrderFrom", OrderFrom)
				.append("OrderShop", OrderShop)
				.append("OrderNo", OrderNo)
				.append("GoodsAmount", GoodsAmount)
				.append("PostFee", PostFee)
				.append("Amount", Amount)
				.append("BuyerAccount", BuyerAccount)
				.append("TaxAmount", TaxAmount)
				.append("DisAmount", DisAmount)
				.append("Promotions", Promotions)
				.append("Goods", Goods)
				.toString();
	}
}
