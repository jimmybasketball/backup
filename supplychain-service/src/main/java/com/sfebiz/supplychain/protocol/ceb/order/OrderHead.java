package com.sfebiz.supplychain.protocol.ceb.order;


import com.sfebiz.supplychain.protocol.ceb.util.AppandUtil;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:20
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"guid", "appType", "appTime", "appStatus", "orderType", "orderNo", "ebpCode", "ebpName",
        "ebcCode", "ebcName", "goodsValue", "freight", "discount", "taxTotal", "acturalPaid", "currency",
        "buyerRegNo", "buyerName", "buyerIdType", "buyerIdNumber", "payCode", "payName",
        "payTransactionId", "batchNumbers", "consignee", "consigneeTelephone", "consigneeAddress",
        "consigneeDitrict", "note"})
@XmlRootElement(name = "OrderHead")
public class OrderHead implements Serializable {

    private static final long serialVersionUID = 2592787473834754075L;

    /**
     * 系统唯一序号
     * 企业系统生成36位唯一序号（英文字母大写）
     */

    @XmlElement(name = "guid")
    private String guid;

    /**
     * 申报类型
     * 申报类型:1-新增 2-变更 3-删除,默认为1
     */
    @XmlElement(name = "appType")
    private String appType;

    /**
     * 申报时间
     * 申报时间以海关入库反馈时间为准,:格式:YYYYMMDDhhmmss
     */
    @XmlElement(name = "appTime")
    private String appTime;


    /**
     * 业务状态
     * 业务状态:1-暂存,2-申报,默认为2
     */
    @XmlElement(name = "appStatus")
    private String appStatus;

    /**
     * 订单类型
     * 电商平台的订单类型 I-进口商品订单；E-出口商品订单
     */
    @XmlElement(name = "orderType")
    private String orderType;


    /**
     * 订单编号
     * 原始交易平台的原始订单编号
     */
    @XmlElement(name = "orderNo")
    private String orderNo;

    /**
     * 电商平台代码
     * 电商平台的海关备案编码（18位）
     */
    @XmlElement(name = "ebpCode")
    private String ebpCode;

    /**
     * 电商平台名称
     * 电商平台的海关备案名称（电子口岸校验名称）
     */
    @XmlElement(name = "ebpName")
    private String ebpName;

    /**
     * 电商企业代码
     * 电商企业的海关备案编码(18位)
     */
    @XmlElement(name = "ebcCode")
    private String ebcCode;

    /**
     * 电商企业名称
     * 电商企业的海关备案名称（电子口岸校验名称）
     */
    @XmlElement(name = "ebcName")
    private String ebcName;

    /**
     * 货款金额
     * 商品实际成交价，不包括优惠减免
     */
    @XmlElement(name = "goodsValue")
    private String goodsValue;

    /**
     * 运杂费
     * 无则填写"0"
     */
    @XmlElement(name = "")
    private String freight;

    /**
     * 优惠减免金额
     * 无则填写"0"
     */
    @XmlElement(name = "discount")
    private String discount;

    /**
     * 订单商品税款
     * 按照货款金额计算的税款
     */
    @XmlElement(name = "taxTotal")
    private String taxTotal;

    /**
     * 实际支付金额
     * 货款+运费+税款-优惠金额，与支付保持一致（精确到元）
     */
    @XmlElement(name = "acturalPaid")
    private String acturalPaid;

    /**
     * 币制
     */
    @XmlElement(name = "currency")
    private String currency;

    /**
     * 订购人注册号
     * 订购人在交易平台唯一注册号，后续大数据分析使用，一个平台注册号对应一个身份证
     */
    @XmlElement(name = "buyerRegNo")
    private String buyerRegNo;

    /**
     * 订购人姓名
     * 海关监管的对象，需要对个人消费额度和实名认证进行管控
     */
    @XmlElement(name = "buyerName")
    private String buyerName;

    /**
     * 订购人证件类型
     * 1-身份证,2-其它
     */
    @XmlElement(name = "buyerIdType")
    private String buyerIdType;

    /**
     * 订购人证件号码
     * 默认为身份证号
     */
    @XmlElement(name = "buyerIdNumber")
    private String buyerIdNumber;

    /**
     * 支付企业代码
     * 支付企业需在JC2006注册备案
     */
    @XmlElement(name = "payCode")
    private String payCode;

    /**
     * 支付企业名称
     */
    @XmlElement(name = "payName")
    private String payName;

    /**
     * 支付交易编号
     */
    @XmlElement(name = "payTransactionId")
    private String payTransactionId;

    /**
     * 商品批次号
     */
    @XmlElement(name = "batchNumbers")
    private String batchNumbers;

    /**
     * 收货人姓名
     */
    @XmlElement(name = "consignee")
    private String consignee;

    /**
     * 收货人电话
     */
    @XmlElement(name = "consigneeTelephone")
    private String consigneeTelephone;

    /**
     * 收货人地址
     */
    @XmlElement(name = "consigneeAddress")
    private String consigneeAddress;

    /**
     * 收货人行政区划代码, 非必填
     */
    @XmlElement(name = "consigneeDitrict")
    private String consigneeDitrict;

    /**
     * 备注
     */
    @XmlElement(name = "note")
    private String note;

    /**
     * 所有属性的值的拼接
     */
    @XmlTransient
    private String allValues;

    /**
     * 根据属性项生成属性值
     *
     * @return
     */
    public String generateValueString() {
        final StringBuilder sb = new StringBuilder();
        AppandUtil.appendStringNotNull(sb, guid);
        AppandUtil.appendStringNotNull(sb, appType);
        AppandUtil.appendStringNotNull(sb, appTime);
        AppandUtil.appendStringNotNull(sb, appStatus);
        AppandUtil.appendStringNotNull(sb, orderType);
        AppandUtil.appendStringNotNull(sb, orderNo);
        AppandUtil.appendStringNotNull(sb, ebpCode);
        AppandUtil.appendStringNotNull(sb, ebpName);
        AppandUtil.appendStringNotNull(sb, ebcCode);
        AppandUtil.appendStringNotNull(sb, ebcName);
        AppandUtil.appendStringNotNull(sb, goodsValue);
        AppandUtil.appendStringNotNull(sb, freight);
        AppandUtil.appendStringNotNull(sb, discount);
        AppandUtil.appendStringNotNull(sb, taxTotal);
        AppandUtil.appendStringNotNull(sb, acturalPaid);
        AppandUtil.appendStringNotNull(sb, currency);
        AppandUtil.appendStringNotNull(sb, buyerRegNo);
        AppandUtil.appendStringNotNull(sb, buyerName);
        AppandUtil.appendStringNotNull(sb, buyerIdType);
        AppandUtil.appendStringNotNull(sb, buyerIdNumber);
        AppandUtil.appendStringNotNull(sb, payCode);
        AppandUtil.appendStringNotNull(sb, payName);
        AppandUtil.appendStringNotNull(sb, payTransactionId);
        AppandUtil.appendStringNotNull(sb, batchNumbers);
        AppandUtil.appendStringNotNull(sb, consignee);
        AppandUtil.appendStringNotNull(sb, consigneeTelephone);
        AppandUtil.appendStringNotNull(sb, consigneeAddress);
        AppandUtil.appendStringNotNull(sb, consigneeDitrict);
        AppandUtil.appendStringNotNull(sb, note);
        return sb.toString();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getEbpCode() {
        return ebpCode;
    }

    public void setEbpCode(String ebpCode) {
        this.ebpCode = ebpCode;
    }

    public String getEbpName() {
        return ebpName;
    }

    public void setEbpName(String ebpName) {
        this.ebpName = ebpName;
    }

    public String getEbcCode() {
        return ebcCode;
    }

    public void setEbcCode(String ebcCode) {
        this.ebcCode = ebcCode;
    }

    public String getEbcName() {
        return ebcName;
    }

    public void setEbcName(String ebcName) {
        this.ebcName = ebcName;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(String taxTotal) {
        this.taxTotal = taxTotal;
    }

    public String getActuralPaid() {
        return acturalPaid;
    }

    public void setActuralPaid(String acturalPaid) {
        this.acturalPaid = acturalPaid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBuyerRegNo() {
        return buyerRegNo;
    }

    public void setBuyerRegNo(String buyerRegNo) {
        this.buyerRegNo = buyerRegNo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerIdType() {
        return buyerIdType;
    }

    public void setBuyerIdType(String buyerIdType) {
        this.buyerIdType = buyerIdType;
    }

    public String getBuyerIdNumber() {
        return buyerIdNumber;
    }

    public void setBuyerIdNumber(String buyerIdNumber) {
        this.buyerIdNumber = buyerIdNumber;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId;
    }

    public String getBatchNumbers() {
        return batchNumbers;
    }

    public void setBatchNumbers(String batchNumbers) {
        this.batchNumbers = batchNumbers;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTelephone() {
        return consigneeTelephone;
    }

    public void setConsigneeTelephone(String consigneeTelephone) {
        this.consigneeTelephone = consigneeTelephone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeDitrict() {
        return consigneeDitrict;
    }

    public void setConsigneeDitrict(String consigneeDitrict) {
        this.consigneeDitrict = consigneeDitrict;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
