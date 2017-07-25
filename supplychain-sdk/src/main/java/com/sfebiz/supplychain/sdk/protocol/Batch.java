package com.sfebiz.supplychain.sdk.protocol;

/**
 * Created by sam on 4/2/15.
 * Email: sambean@126.com
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 批次库存结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "batch", propOrder = {"batchNo", "skuCode", "batchQyCount", "batchFreezeCount", "batchWearCount"})
public class Batch {

    /**
     * batchNo
     */
    @XmlElement(nillable = false, required = true)
    private String batchNo;

    /**
     * skuCode
     */
    @XmlElement(nillable = false, required = false)
    private String skuCode;

    /**
     * batchQyCount
     */
    @XmlElement(nillable = false, required = false)
    private Integer batchQyCount;

    /**
     * batchFreezeCount
     */
    @XmlElement(nillable = false, required = false)
    private Integer batchFreezeCount;

    /**
     * batchWearCount
     */
    @XmlElement(nillable = false, required = false)
    private Integer batchWearCount;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Integer getBatchQyCount() {
        return batchQyCount;
    }

    public void setBatchQyCount(Integer batchQyCount) {
        this.batchQyCount = batchQyCount;
    }

    public Integer getBatchFreezeCount() {
        return batchFreezeCount;
    }

    public void setBatchFreezeCount(Integer batchFreezeCount) {
        this.batchFreezeCount = batchFreezeCount;
    }

    public Integer getBatchWearCount() {
        return batchWearCount;
    }

    public void setBatchWearCount(Integer batchWearCount) {
        this.batchWearCount = batchWearCount;
    }
}
