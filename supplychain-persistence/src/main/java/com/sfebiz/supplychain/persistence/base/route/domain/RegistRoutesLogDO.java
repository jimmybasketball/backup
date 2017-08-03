package com.sfebiz.supplychain.persistence.base.route.domain;

import com.sfebiz.common.dao.domain.BaseDO;

import java.util.Date;

/**
 * 快递100订阅记录实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-08-01 09:59
 **/
public class RegistRoutesLogDO extends BaseDO{

    private static final long serialVersionUID = 7042118829951388057L;


    private Long id;

    /**
     * 出库单ID
     */
    private Long stockoutOrderId;

    /**
     * 国内运单号
     */
    private String intrMailNo;

    /**
     * 国内承运商编码
     */
    private String intrCarrierCode;

    /**
     * 国际运单号
     */
    private String intlMailNo;

    /**
     * 国际承运商编码
     */
    private String intlCarrierCode;

    /**
     * 注册信息 0失败  1成功
     */
    private Integer isSuccess;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockoutOrderId() {
        return stockoutOrderId;
    }

    public void setStockoutOrderId(Long stockoutOrderId) {
        this.stockoutOrderId = stockoutOrderId;
    }

    public String getIntrMailNo() {
        return intrMailNo;
    }

    public void setIntrMailNo(String intrMailNo) {
        this.intrMailNo = intrMailNo;
    }

    public String getIntrCarrierCode() {
        return intrCarrierCode;
    }

    public void setIntrCarrierCode(String intrCarrierCode) {
        this.intrCarrierCode = intrCarrierCode;
    }

    public String getIntlMailNo() {
        return intlMailNo;
    }

    public void setIntlMailNo(String intlMailNo) {
        this.intlMailNo = intlMailNo;
    }

    public String getIntlCarrierCode() {
        return intlCarrierCode;
    }

    public void setIntlCarrierCode(String intlCarrierCode) {
        this.intlCarrierCode = intlCarrierCode;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "RegistRoutesLogDO{" +
                "id=" + id +
                ", stockoutOrderId=" + stockoutOrderId +
                ", intrMailNo='" + intrMailNo + '\'' +
                ", intrCarrierCode='" + intrCarrierCode + '\'' +
                ", intlMailNo='" + intlMailNo + '\'' +
                ", intlCarrierCode='" + intlCarrierCode + '\'' +
                ", isSuccess='" + isSuccess + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
