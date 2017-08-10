package com.sfebiz.supplychain.protocol.wms.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class Porder implements Serializable{
    private static final long serialVersionUID = 5766725548953807362L;

    /**
     * 入库单编号
     */
    public String code;

    /**
     * 货主编码
     */
    public String companyCode;

    /**
     * 供应商编码
     */
    public String supplierCode;

    /**
     * 供应商名称
     */
    public String supplierName;

    /**
     * 调入仓库
     */
    public String callInWarehouse;

    /**
     * 批次号
     */
    public String batch;

    /**
     * 总金额
     */
    public String totalAmount;

    /**
     * 入库类型
     */
    public String type;

    /**
     * 状态
     */
    public String status;

    /**
     * 入库时间
     */
    public String callInDate;

    /**
     * 起运国名称
     */
    public String originCountry;

    /**
     * 商品信息列表
     */
    public List<Item> Item;


    @Override
    public String toString() {
        return "Porder{" +
                "code='" + code + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", supplierCode='" + supplierCode + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", callInWarehouse='" + callInWarehouse + '\'' +
                ", batch='" + batch + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", callInDate='" + callInDate + '\'' +
                ", originCountry='" + originCountry + '\'' +
                ", Item=" + Item +
                '}';
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getCallInDate() {
        return callInDate;
    }

    public void setCallInDate(String callInDate) {
        this.callInDate = callInDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCallInWarehouse() {
        return callInWarehouse;
    }

    public void setCallInWarehouse(String callInWarehouse) {
        this.callInWarehouse = callInWarehouse;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItem() {
        return Item;
    }

    public void setItem(List<Item> item) {
        Item = item;
    }
}
