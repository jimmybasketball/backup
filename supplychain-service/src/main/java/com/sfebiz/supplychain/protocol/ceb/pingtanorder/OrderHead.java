package com.sfebiz.supplychain.protocol.ceb.pingtanorder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:20
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {})
@XmlRootElement(name = "OrderHead")
public class OrderHead implements Serializable {

    private static final long serialVersionUID = -6639552021898792844L;

    /**
     *电商代码
     */
    @XmlElement(name = "cbeCode")
    private String cbeCode;
    /**
     *电商名称
     */
    @XmlElement(name = "cbeName")
    private String cbeName;
    /**
     *电商平台代码
     */
    @XmlElement(name = "ecpCode")
    private String ecpCode;
    /**
     *电商平台名称
     */
    @XmlElement(name = "ecpName")
    private String ecpName;
    /**
     *订单编号
     */
    @XmlElement(name = "orderNo")
    private String orderNo;
    /**
     *总费用
     */
    @XmlElement(name = "charge")
    private String charge;
    /**
     *货值
     */
    @XmlElement(name = "goodsValue")
    private String goodsValue;
    /**
     *运费
     */
    @XmlElement(name = "freight")
    private String freight;
    /**
     *其他费用
     */
    @XmlElement(name = "other")
    private String other;
    /**
     *进口行邮税
     */
    @XmlElement(name = "tax")
    private String tax;
    /**
     *客户姓名
     */
    @XmlElement(name = "customer")
    private String customer;
    /**
     *发货人名称
     */
    @XmlElement(name = "shipper")
    private String shipper;
    /**
     *发货人地址
     */
    @XmlElement(name = "shipperAddress")
    private String shipperAddress;
    /**
     *发货人电话
     */
    @XmlElement(name = "shipperTelephone")
    private String shipperTelephone;
    /**
     *发货人所在国
     */
    @XmlElement(name = "shipperCountry")
    private String shipperCountry;
    /**
     *订购人名称
     */
    @XmlElement(name = "consignee")
    private String consignee;
    /**
     *收货人地址
     */
    @XmlElement(name = "consigneeAddress")
    private String consigneeAddress;
    /**
     *收货人电话
     */
    @XmlElement(name = "consigneeTelephone")
    private String consigneeTelephone;
    /**
     *收货人所在国
     */
    @XmlElement(name = "consigneeCountry")
    private String consigneeCountry;
    /**
     *订购人证件类型
     */
    @XmlElement(name = "idType")
    private String idType;
    /**
     *订购人证件号码
     */
    @XmlElement(name = "customerId")
    private String customerId;
    /**
     *进出口标志
     */
    @XmlElement(name = "ieType")
    private String ieType;
    /**
     *批次号
     */
    @XmlElement(name = "batchNumbers")
    private String batchNumbers;
    /**
     *总运单号
     */
    @XmlElement(name = "totalLogisticsNo")
    private String totalLogisticsNo;
    /**
     *贸易国别
     */
    @XmlElement(name = "tradeCountry")
    private String tradeCountry;
    /**
     *代理企业
     */
    @XmlElement(name = "agentCode")
    private String agentCode;
    /**
     *代理企业名称
     */
    @XmlElement(name = "agentName")
    private String agentName;
    /**
     *包装种类
     */
    @XmlElement(name = "wrapType")
    private String wrapType;
    /**
     *操作类型
     */
    @XmlElement(name = "modifyMark")
    private String modifyMark;
    /**
     *备注
     */
    @XmlElement(name = "Note")
    private String note;

    public String getCbeCode() {
        return cbeCode;
    }

    public void setCbeCode(String cbeCode) {
        this.cbeCode = cbeCode;
    }

    public String getCbeName() {
        return cbeName;
    }

    public void setCbeName(String cbeName) {
        this.cbeName = cbeName;
    }

    public String getEcpCode() {
        return ecpCode;
    }

    public void setEcpCode(String ecpCode) {
        this.ecpCode = ecpCode;
    }

    public String getEcpName() {
        return ecpName;
    }

    public void setEcpName(String ecpName) {
        this.ecpName = ecpName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(String goodsValue) {
        this.goodsValue = goodsValue;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    public String getShipperTelephone() {
        return shipperTelephone;
    }

    public void setShipperTelephone(String shipperTelephone) {
        this.shipperTelephone = shipperTelephone;
    }

    public String getShipperCountry() {
        return shipperCountry;
    }

    public void setShipperCountry(String shipperCountry) {
        this.shipperCountry = shipperCountry;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeTelephone() {
        return consigneeTelephone;
    }

    public void setConsigneeTelephone(String consigneeTelephone) {
        this.consigneeTelephone = consigneeTelephone;
    }

    public String getConsigneeCountry() {
        return consigneeCountry;
    }

    public void setConsigneeCountry(String consigneeCountry) {
        this.consigneeCountry = consigneeCountry;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getIeType() {
        return ieType;
    }

    public void setIeType(String ieType) {
        this.ieType = ieType;
    }

    public String getBatchNumbers() {
        return batchNumbers;
    }

    public void setBatchNumbers(String batchNumbers) {
        this.batchNumbers = batchNumbers;
    }

    public String getTotalLogisticsNo() {
        return totalLogisticsNo;
    }

    public void setTotalLogisticsNo(String totalLogisticsNo) {
        this.totalLogisticsNo = totalLogisticsNo;
    }

    public String getTradeCountry() {
        return tradeCountry;
    }

    public void setTradeCountry(String tradeCountry) {
        this.tradeCountry = tradeCountry;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getWrapType() {
        return wrapType;
    }

    public void setWrapType(String wrapType) {
        this.wrapType = wrapType;
    }

    public String getModifyMark() {
        return modifyMark;
    }

    public void setModifyMark(String modifyMark) {
        this.modifyMark = modifyMark;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
