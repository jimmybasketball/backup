package com.sfebiz.supplychain.persistence.base.sku.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 基础商品条码DO
 *
 * @author tanzx
 * @create 2017-07-12 18:22
 **/
public class SkuBarcodeDO extends BaseDO {

    private static final long serialVersionUID = -4498478048292642391L;

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
    private String barCode;

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

    /**
     * 删除标识
     */
    private int isDelete;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "SkuBarcodeDO{" +
                "id=" + id +
                ", skuId='" + skuId + '\'' +
                ", barCode='" + barCode + '\'' +
                '}';
    }
}
