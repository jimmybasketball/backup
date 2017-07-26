package com.sfebiz.supplychain.exposed.sku.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础商品条码实体
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-12 17:57
 **/
public class SkuBarcodeEntity implements Serializable {

    private static final long serialVersionUID = -9146526440252602786L;
    /**
     * id
     */
    private Long id;

    /**
     * 商品id
     */
    private Long skuId;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 修改人
     */
    private String modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString() {
        return "SkuBarcodeEntity{" +
                "id=" + id +
                ", skuId='" + skuId + '\'' +
                ", barcode='" + barcode + '\'' +
                '}';
    }
}