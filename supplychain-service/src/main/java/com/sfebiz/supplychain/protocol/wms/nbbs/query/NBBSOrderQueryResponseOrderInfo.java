package com.sfebiz.supplychain.protocol.wms.nbbs.query;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;

/**
 * Description: 宁波保税订单查询数据节点构造体
 * Created by yanghua on 2017/3/21.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "orderInfo")
@XmlType(propOrder = {"warehouseName", "busOrderNo", "orderNo", "mftNo", "mftStatus", "orderStatus", "taxAmount", "disAmount", "postFee", "grossWeight", "paySource", "waybillNo", "packageWeight"})
public class NBBSOrderQueryResponseOrderInfo {
    @Description("仓库名称")
    @XmlElement(name = "warehouseName", required = false)
    private String warehouseName;
    @Description("线路订单号")
    @XmlElement(name = "busOrderNo", required = false)
    private String busOrderNo;
    @Description("能容订单号")
    @XmlElement(name = "orderNo", required = false)
    private String orderNo;
    @Description("商家订单号")
    @XmlElement(name = "mftNo", required = false)
    private String mftNo;
    @Description("订单状态")
    @XmlElement(name = "mftStatus", required = false)
    private String mftStatus;
    @Description("税金")
    @XmlElement(name = "taxAmount", required = false)
    private String taxAmount;
    @Description("折扣金额")
    @XmlElement(name = "disAmount", required = false)
    private String disAmount;
    @Description("邮费")
    @XmlElement(name = "postFee", required = false)
    private String postFee;
    @Description("重量")
    @XmlElement(name = "grossWeight", required = false)
    private String grossWeight;
    @Description("支付单号")
    @XmlElement(name = "paySource", required = false)
    private String paySource;
    @Description("运单号")
    @XmlElement(name = "waybillNo", required = false)
    private String waybillNo;
    @Description("订单状态")
    @XmlElement(name = "orderStatus", required = false)
    private String orderStatus;

    @Description("包裹重量")
    @XmlElement(name = "packageWeight", required = false)
    private String packageWeight;

    public String getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getBusOrderNo() {
        return busOrderNo;
    }

    public void setBusOrderNo(String busOrderNo) {
        this.busOrderNo = busOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMftNo() {
        return mftNo;
    }

    public void setMftNo(String mftNo) {
        this.mftNo = mftNo;
    }

    public String getMftStatus() {
        return mftStatus;
    }

    public void setMftStatus(String mftStatus) {
        this.mftStatus = mftStatus;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getDisAmount() {
        return disAmount;
    }

    public void setDisAmount(String disAmount) {
        this.disAmount = disAmount;
    }

    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }
}
