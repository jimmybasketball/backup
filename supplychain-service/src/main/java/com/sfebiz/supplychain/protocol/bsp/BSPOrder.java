package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "orderId", "origOrderId", "origMailNo", "expressType",
		"senderCompany", "senderContact", "senderTel", "senderMobile",
		"senderAddress", "receiverCompany", "receiverContact", "receiverTel",
		"receiverMobile", "receiverAddress", "parcelQuantity", "payMethod",
		"senderProvince", "senderCity", "receiverProvince", "receiverCity",
		"declaredValue", "declaredValueCurrency", "custId", "template",
		"senderCountry", "senderCounty", "senderShipperCode", "senderPostCode",
		"receiverCountry", "receiverCounty", "deliveryCode",
		"receiverPostCode", "cargoTotalWeight", "sendStartTime", "mailNo",
		"returnTracking", "remark", "needReturnTrackingNo", "isDoCall",
		"isGenBillNo", "isGenEletricPic", "waybillSize", "cargoLength",
		"cargoWidth", "cargoHeight", "cargo", "addedService",
		"senderPosttalCode", "receiverPosttalCode", "shopName", "taxPayType",
		"taxSetAccounts","orderName","orderCertType","orderCertNo","extra"})
@XmlRootElement(name = "Order")
@Description("BSP订单")
public class BSPOrder extends BSPBody implements Serializable {
	private static final long serialVersionUID = -2340280508621260043L;
	@XmlAttribute(name = "orderid")
	@Description("订单ID")
	public String orderId;
	@XmlAttribute(name = "orig_orderid")
	@Description("原订单ID")
	public String origOrderId;
	@XmlAttribute(name = "orig_mailno")
	@Description("原运单ID")
	public String origMailNo;
	@XmlAttribute(name = "express_type")
	@Description("快件产品类别(可根据需要定制扩展) 1 标准快递 2 顺丰特惠")
	public String expressType;
	@XmlAttribute(name = "j_company")
	@Description("寄件方公司名称")
	public String senderCompany;
	@XmlAttribute(name = "j_contact")
	@Description("寄件方联系人")
	public String senderContact;
	@XmlAttribute(name = "j_tel")
	@Description("寄件方联系电话")
	public String senderTel;
	@XmlAttribute(name = "j_mobile")
	@Description("寄件方手机")
	public String senderMobile;
	@XmlAttribute(name = "j_address")
	@Description("寄件方详细地址")
	public String senderAddress;
	@XmlAttribute(name = "d_company")
	@Description("到件方公司名称")
	public String receiverCompany;
	@XmlAttribute(name = "d_contact")
	@Description("到件方联系人")
	public String receiverContact;
	@XmlAttribute(name = "d_tel")
	@Description("到件方联系电话")
	public String receiverTel;
	@XmlAttribute(name = "d_mobile")
	@Description("到件方手机")
	public String receiverMobile;
	@XmlAttribute(name = "d_address")
	@Description("到件方详细地址")
	public String receiverAddress;
	@XmlAttribute(name = "parcel_quantity")
	@Description("包裹数")
	public Integer parcelQuantity;
	@XmlAttribute(name = "pay_method")
	@Description("付款方式:1:寄方付 2:收方付 3:第三 方付,默认为 1")
	public Integer payMethod;
	@XmlAttribute(name = "j_province")
	@Description("寄件方所在省份字段填写要求")
	public String senderProvince;
	@XmlAttribute(name = "j_city")
	@Description("寄件方所属城市名称")
	public String senderCity;
	@XmlAttribute(name = "d_province")
	@Description("到件方所在省份字段")
	public String receiverProvince;
	@XmlAttribute(name = "d_city")
	@Description("到件方所属城市名称")
	public String receiverCity;
	@XmlAttribute(name = "declared_value")
	@Description("托寄物声明价值")
	public String declaredValue;
	@XmlAttribute(name = "declared_value_currency")
	@Description("托寄物声明价值币别: CNY: 人民币 HKD: 港币 USD: 美元 NTD: 新台币 RUB: 卢布 EUR: 欧元 MOP: 澳门元 SGD: 新元 JPY: 日元 KRW: 韩元 MYR: 马币 VND: 越南盾 THB: 泰铢 AUD: 澳大利亚元 MNT: 图格里克")
	public String declaredValueCurrency;
	@XmlAttribute(name = "custid")
	@Description("月结卡号")
	public String custId;
	@XmlAttribute(name = "template")
	@Description("模板选择")
	public String template;
	@XmlAttribute(name = "j_country")
	@Description("寄方国家")
	public String senderCountry;
	@XmlAttribute(name = "j_county")
	@Description("寄件人所在县/区")
	public String senderCounty;
	@XmlAttribute(name = "j_shippercode")
	@Description("寄件方代码")
	public String senderShipperCode;
	@XmlAttribute(name = "j_post_code")
	@Description("寄方邮编")
	public String senderPostCode;
	@XmlAttribute(name = "j_postal_code")
	@Description("寄方邮编")
	public String senderPosttalCode;
	@Description("到方邮编")
	@XmlAttribute(name = "d_postal_code")
	public String receiverPosttalCode;
	@Description("店名")
	@XmlAttribute(name = "shop_name")
	public String shopName;

	@XmlAttribute(name = "d_country")
	@Description("到方国家")
	public String receiverCountry;
	@XmlAttribute(name = "d_county")
	@Description("到方所在县/区")
	public String receiverCounty;
	@XmlAttribute(name = "d_deliverycode")
	@Description("到件方代码")
	public String deliveryCode;
	@XmlAttribute(name = "d_post_code")
	@Description("到件邮编")
	public String receiverPostCode;
	@XmlAttribute(name = "cargo_total_weight")
	@Description("订单货物总重量,单位 KG")
	public Double cargoTotalWeight;
	@XmlAttribute(name = "sendstarttime")
	@Description("要求上门取件开始时间")
	public String sendStartTime;
	@XmlAttribute(name = "mailno")
	@Description("运单号")
	public String mailNo;
	@XmlAttribute(name = "return_tracking")
	@Description("签回单单号")
	public String returnTracking;
	@XmlAttribute(name = "remark")
	@Description("备注")
	public String remark;
	@XmlAttribute(name = "need_return_tracking_no")
	@Description("是否需要签回单号,1:需要")
	public String needReturnTrackingNo;
	@XmlAttribute(name = "is_docall")
	@Description("是否下 call 1-下 call,其他否 SYSTEM 表示如果不提供,将从系统配置 获取")
	public Integer isDoCall;
	@XmlAttribute(name = "is_gen_bill_no")
	@Description("是否申请运单号 1-申请运单号,其他否 SYSTEM 表示如果不提供,将从系统配置 获取")
	public Integer isGenBillNo;
	@XmlAttribute(name = "is_gen_eletric_pic")
	@Description("是否生成电子运单图片 1-生成,其他否 SYSTEM 表示如果不提供,将从系统配置 获取")
	public Integer isGenEletricPic;
	@XmlAttribute(name = "waybill_size")
	@Description("图片格式 1 A4 2 A5")
	public Integer waybillSize;
	@XmlAttribute(name = "cargo_length")
	@Description("长")
	public Double cargoLength;
	@XmlAttribute(name = "cargo_width")
	@Description("宽")
	public Double cargoWidth;
	@XmlAttribute(name = "cargo_height")
	@Description("高")
	public Double cargoHeight;
	@XmlAttribute(name = "d_tax_no")
	@Description("收件人税号")
	public String receiverTaxNo;
	@XmlAttribute(name = "tax_pay_type")
	@Description("税金付款方式(寄付 1、到付 2)")
	public String taxPayType;
	@XmlAttribute(name = "tax_set_accounts")
	@Description("税金结算账号")
	public String taxSetAccounts;
	@XmlAttribute(name = "original_number")
	@Description("原单号")
	public String originalNumber;
	@XmlAttribute(name = "payment_tool")
	@Description("支付工具")
	public String paymentTool;
	@XmlAttribute(name = "payment_number")
	@Description("支付号码")
	public String paymentNumber;
	@XmlAttribute(name = "goods_code")
	@Description("商品编号")
	public String goodsCode;
	@XmlAttribute(name = "in_process_waybill_no")
	@Description("头程运单号")
	public String inProcessWaybillNo;
	@XmlAttribute(name = "brand")
	@Description("品牌")
	public String brand;
	@XmlAttribute(name = "specifications")
	@Description("规格型号")
	public String specifications;
	@XmlElements(@XmlElement(name = "Cargo", type = BSPCargo.class))
	@Description("货品列表")
	public List<BSPCargo> cargo;
	@XmlElements(@XmlElement(name = "AddedService", type = BSPAddedService.class))
	@Description("增值服务列表")
	public List<BSPAddedService> addedService;
    @XmlAttribute(name = "customs_batchs")
    @Description("报关批次")
    public String customsBatchs;

    @Description("订单收货人姓名")
    @XmlAttribute(name = "order_name")
    public String orderName;

    @Description("订单收货人姓名")
    @XmlAttribute(name = "order_cert_type")
    public String orderCertType;

    @Description("订单人证件号")
    @XmlAttribute(name = "order_cert_no")
    public String orderCertNo;

    @Description("订单人证件号")
    @XmlElements(@XmlElement(name = "Extra", type = BSPExtra.class))
    public BSPExtra extra;


    public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrigOrderId() {
		return origOrderId;
	}

	public void setOrigOrderId(String origOrderId) {
		this.origOrderId = origOrderId;
	}

	public String getOrigMailNo() {
		return origMailNo;
	}

	public void setOrigMailNo(String origMailNo) {
		this.origMailNo = origMailNo;
	}

	public String getExpressType() {
		return expressType;
	}

	public void setExpressType(String expressType) {
		this.expressType = expressType;
	}

	public String getSenderCompany() {
		return senderCompany;
	}

	public void setSenderCompany(String senderCompany) {
		this.senderCompany = senderCompany;
	}

	public String getSenderContact() {
		return senderContact;
	}

	public void setSenderContact(String senderContact) {
		this.senderContact = senderContact;
	}

	public String getSenderTel() {
		return senderTel;
	}

	public void setSenderTel(String senderTel) {
		this.senderTel = senderTel;
	}

	public String getSenderMobile() {
		return senderMobile;
	}

	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getReceiverCompany() {
		return receiverCompany;
	}

	public void setReceiverCompany(String receiverCompany) {
		this.receiverCompany = receiverCompany;
	}

	public String getReceiverContact() {
		return receiverContact;
	}

	public void setReceiverContact(String receiverContact) {
		this.receiverContact = receiverContact;
	}

	public String getReceiverTel() {
		return receiverTel;
	}

	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public Integer getParcelQuantity() {
		return parcelQuantity;
	}

	public void setParcelQuantity(Integer parcelQuantity) {
		this.parcelQuantity = parcelQuantity;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public String getSenderProvince() {
		return senderProvince;
	}

	public void setSenderProvince(String senderProvince) {
		this.senderProvince = senderProvince;
	}

	public String getSenderCity() {
		return senderCity;
	}

	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

    public String getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(String declaredValue) {
        this.declaredValue = declaredValue;
    }

    public String getDeclaredValueCurrency() {
		return declaredValueCurrency;
	}

	public void setDeclaredValueCurrency(String declaredValueCurrency) {
		this.declaredValueCurrency = declaredValueCurrency;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getSenderCountry() {
		return senderCountry;
	}

	public void setSenderCountry(String senderCountry) {
		this.senderCountry = senderCountry;
	}

	public String getSenderCounty() {
		return senderCounty;
	}

	public void setSenderCounty(String senderCounty) {
		this.senderCounty = senderCounty;
	}

	public String getSenderShipperCode() {
		return senderShipperCode;
	}

	public void setSenderShipperCode(String senderShipperCode) {
		this.senderShipperCode = senderShipperCode;
	}

	public String getSenderPostCode() {
		return senderPostCode;
	}

	public void setSenderPostCode(String senderPostCode) {
		this.senderPostCode = senderPostCode;
	}

	public String getSenderPosttalCode() {
		return senderPosttalCode;
	}

	public void setSenderPosttalCode(String senderPosttalCode) {
		this.senderPosttalCode = senderPosttalCode;
	}

	public String getReceiverPosttalCode() {
		return receiverPosttalCode;
	}

	public void setReceiverPosttalCode(String receiverPosttalCode) {
		this.receiverPosttalCode = receiverPosttalCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getReceiverCountry() {
		return receiverCountry;
	}

	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}

	public String getReceiverCounty() {
		return receiverCounty;
	}

	public void setReceiverCounty(String receiverCounty) {
		this.receiverCounty = receiverCounty;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getReceiverPostCode() {
		return receiverPostCode;
	}

	public void setReceiverPostCode(String receiverPostCode) {
		this.receiverPostCode = receiverPostCode;
	}

	public Double getCargoTotalWeight() {
		return cargoTotalWeight;
	}

	public void setCargoTotalWeight(Double cargoTotalWeight) {
		this.cargoTotalWeight = cargoTotalWeight;
	}

	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getReturnTracking() {
		return returnTracking;
	}

	public void setReturnTracking(String returnTracking) {
		this.returnTracking = returnTracking;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNeedReturnTrackingNo() {
		return needReturnTrackingNo;
	}

	public void setNeedReturnTrackingNo(String needReturnTrackingNo) {
		this.needReturnTrackingNo = needReturnTrackingNo;
	}

	public Integer getIsDoCall() {
		return isDoCall;
	}

	public void setIsDoCall(Integer isDoCall) {
		this.isDoCall = isDoCall;
	}

	public Integer getIsGenBillNo() {
		return isGenBillNo;
	}

	public void setIsGenBillNo(Integer isGenBillNo) {
		this.isGenBillNo = isGenBillNo;
	}

	public Integer getIsGenEletricPic() {
		return isGenEletricPic;
	}

	public void setIsGenEletricPic(Integer isGenEletricPic) {
		this.isGenEletricPic = isGenEletricPic;
	}

	public Integer getWaybillSize() {
		return waybillSize;
	}

	public void setWaybillSize(Integer waybillSize) {
		this.waybillSize = waybillSize;
	}

	public Double getCargoLength() {
		return cargoLength;
	}

	public void setCargoLength(Double cargoLength) {
		this.cargoLength = cargoLength;
	}

	public Double getCargoWidth() {
		return cargoWidth;
	}

	public void setCargoWidth(Double cargoWidth) {
		this.cargoWidth = cargoWidth;
	}

	public Double getCargoHeight() {
		return cargoHeight;
	}

	public void setCargoHeight(Double cargoHeight) {
		this.cargoHeight = cargoHeight;
	}

	public String getReceiverTaxNo() {
		return receiverTaxNo;
	}

	public void setReceiverTaxNo(String receiverTaxNo) {
		this.receiverTaxNo = receiverTaxNo;
	}

	public String getTaxPayType() {
		return taxPayType;
	}

	public void setTaxPayType(String taxPayType) {
		this.taxPayType = taxPayType;
	}

	public String getTaxSetAccounts() {
		return taxSetAccounts;
	}

	public void setTaxSetAccounts(String taxSetAccounts) {
		this.taxSetAccounts = taxSetAccounts;
	}

	public String getOriginalNumber() {
		return originalNumber;
	}

	public void setOriginalNumber(String originalNumber) {
		this.originalNumber = originalNumber;
	}

	public String getPaymentTool() {
		return paymentTool;
	}

	public void setPaymentTool(String paymentTool) {
		this.paymentTool = paymentTool;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getInProcessWaybillNo() {
		return inProcessWaybillNo;
	}

	public void setInProcessWaybillNo(String inProcessWaybillNo) {
		this.inProcessWaybillNo = inProcessWaybillNo;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public List<BSPCargo> getCargo() {
		if (cargo == null) {
			cargo = new ArrayList<BSPCargo>();
		}
		return cargo;
	}

	public void setCargo(List<BSPCargo> cargo) {
		this.cargo = cargo;
	}

	public List<BSPAddedService> getAddedService() {
		if (addedService == null) {
			addedService = new ArrayList<BSPAddedService>();
		}
		return addedService;
	}

	public void setAddedService(List<BSPAddedService> addedService) {
		this.addedService = addedService;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

    public String getCustomsBatchs() {
        return customsBatchs;
    }

    public void setCustomsBatchs(String customsBatchs) {
        this.customsBatchs = customsBatchs;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderCertType() {
        return orderCertType;
    }

    public void setOrderCertType(String orderCertType) {
        this.orderCertType = orderCertType;
    }

    public String getOrderCertNo() {
        return orderCertNo;
    }

    public void setOrderCertNo(String orderCertNo) {
        this.orderCertNo = orderCertNo;
    }

    public BSPExtra getExtra() {
        return extra;
    }

    public void setExtra(BSPExtra extra) {
        this.extra = extra;
    }



    @Override
    public String toString() {
        return "BSPOrder{" +
                "orderId='" + orderId + '\'' +
                ", origOrderId='" + origOrderId + '\'' +
                ", origMailNo='" + origMailNo + '\'' +
                ", expressType='" + expressType + '\'' +
                ", senderCompany='" + senderCompany + '\'' +
                ", senderContact='" + senderContact + '\'' +
                ", senderTel='" + senderTel + '\'' +
                ", senderMobile='" + senderMobile + '\'' +
                ", senderAddress='" + senderAddress + '\'' +
                ", receiverCompany='" + receiverCompany + '\'' +
                ", receiverContact='" + receiverContact + '\'' +
                ", receiverTel='" + receiverTel + '\'' +
                ", receiverMobile='" + receiverMobile + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", parcelQuantity=" + parcelQuantity +
                ", payMethod=" + payMethod +
                ", senderProvince='" + senderProvince + '\'' +
                ", senderCity='" + senderCity + '\'' +
                ", receiverProvince='" + receiverProvince + '\'' +
                ", receiverCity='" + receiverCity + '\'' +
                ", declaredValue=" + declaredValue +
                ", declaredValueCurrency='" + declaredValueCurrency + '\'' +
                ", custId='" + custId + '\'' +
                ", template='" + template + '\'' +
                ", senderCountry='" + senderCountry + '\'' +
                ", senderCounty='" + senderCounty + '\'' +
                ", senderShipperCode='" + senderShipperCode + '\'' +
                ", senderPostCode='" + senderPostCode + '\'' +
                ", senderPosttalCode='" + senderPosttalCode + '\'' +
                ", receiverPosttalCode='" + receiverPosttalCode + '\'' +
                ", shopName='" + shopName + '\'' +
                ", receiverCountry='" + receiverCountry + '\'' +
                ", receiverCounty='" + receiverCounty + '\'' +
                ", deliveryCode='" + deliveryCode + '\'' +
                ", receiverPostCode='" + receiverPostCode + '\'' +
                ", cargoTotalWeight=" + cargoTotalWeight +
                ", sendStartTime='" + sendStartTime + '\'' +
                ", mailNo='" + mailNo + '\'' +
                ", returnTracking='" + returnTracking + '\'' +
                ", remark='" + remark + '\'' +
                ", needReturnTrackingNo='" + needReturnTrackingNo + '\'' +
                ", isDoCall=" + isDoCall +
                ", isGenBillNo=" + isGenBillNo +
                ", isGenEletricPic=" + isGenEletricPic +
                ", waybillSize=" + waybillSize +
                ", cargoLength=" + cargoLength +
                ", cargoWidth=" + cargoWidth +
                ", cargoHeight=" + cargoHeight +
                ", receiverTaxNo='" + receiverTaxNo + '\'' +
                ", taxPayType='" + taxPayType + '\'' +
                ", taxSetAccounts='" + taxSetAccounts + '\'' +
                ", originalNumber='" + originalNumber + '\'' +
                ", paymentTool='" + paymentTool + '\'' +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", goodsCode='" + goodsCode + '\'' +
                ", inProcessWaybillNo='" + inProcessWaybillNo + '\'' +
                ", brand='" + brand + '\'' +
                ", specifications='" + specifications + '\'' +
                ", cargo=" + cargo +
                ", addedService=" + addedService +
                ", customsBatchs='" + customsBatchs + '\'' +
                ", orderName='" + orderName + '\'' +
                ", orderCertType='" + orderCertType + '\'' +
                ", orderCertNo='" + orderCertNo + '\'' +
                "} " + super.toString();
    }
}
