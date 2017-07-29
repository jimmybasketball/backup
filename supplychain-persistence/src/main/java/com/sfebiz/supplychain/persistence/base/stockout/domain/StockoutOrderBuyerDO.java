package com.sfebiz.supplychain.persistence.base.stockout.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 
 * <p>出库单购买人信息实体</p>
 *
 * @author matt
 * @Date 2017年7月17日 下午11:53:33
 */
public class StockoutOrderBuyerDO extends BaseDO {

    /** 序号 */
    private static final long serialVersionUID = -6271746725911574315L;

    /** 出库单主键ID */
    private Long              stockoutOrderId;

    /** 业务订单号 */
    private String            bizId;

    /** 收货人姓名 */
    private String            buyerName;

    /** 电话号码 */
    private String            buyerTelephone;

    /** 购买人证件类型 */
    private Integer           buyerCertType;

    /** 收货人证件号码 */
    private String            buyerCertNo;

    /** 身份证正面照片地址 */
    private String            buyerIdCardFrontPhotoUrl;

    /** 身份证反面照片地址 */
    private String            buyerIdCardBackPhotoUrl;

    /** 身份证照片信息时间戳 */
    private Date              buyerIdCardPhotoTimestamp;

    /** 购买者收货地址，国家 */
    private String            buyerCountry;

    /** 购买者收货地址，省 */
    private String            buyerProvince;

    /** 购买者收货地址，市 */
    private String            buyerCity;

    /** 购买者收货地址，区 */
    private String            buyerRegion;

    /** 购买者收货地址，详细地址 */
    private String            buyerAddress;

    /** 购买者邮编 */
    private String            buyerZipcode;

    public Long getStockoutOrderId() {
        return stockoutOrderId;
    }

    public void setStockoutOrderId(Long stockoutOrderId) {
        this.stockoutOrderId = stockoutOrderId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTelephone() {
        return buyerTelephone;
    }

    public void setBuyerTelephone(String buyerTelephone) {
        this.buyerTelephone = buyerTelephone;
    }

    public Integer getBuyerCertType() {
        return buyerCertType;
    }

    public void setBuyerCertType(Integer buyerCertType) {
        this.buyerCertType = buyerCertType;
    }

    public String getBuyerCertNo() {
        return buyerCertNo;
    }

    public void setBuyerCertNo(String buyerCertNo) {
        this.buyerCertNo = buyerCertNo;
    }

    public String getBuyerIdCardFrontPhotoUrl() {
        return buyerIdCardFrontPhotoUrl;
    }

    public void setBuyerIdCardFrontPhotoUrl(String buyerIdCardFrontPhotoUrl) {
        this.buyerIdCardFrontPhotoUrl = buyerIdCardFrontPhotoUrl;
    }

    public String getBuyerIdCardBackPhotoUrl() {
        return buyerIdCardBackPhotoUrl;
    }

    public void setBuyerIdCardBackPhotoUrl(String buyerIdCardBackPhotoUrl) {
        this.buyerIdCardBackPhotoUrl = buyerIdCardBackPhotoUrl;
    }

    public Date getBuyerIdCardPhotoTimestamp() {
        return buyerIdCardPhotoTimestamp;
    }

    public void setBuyerIdCardPhotoTimestamp(Date buyerIdCardPhotoTimestamp) {
        this.buyerIdCardPhotoTimestamp = buyerIdCardPhotoTimestamp;
    }

    public String getBuyerCountry() {
        return buyerCountry;
    }

    public void setBuyerCountry(String buyerCountry) {
        this.buyerCountry = buyerCountry;
    }

    public String getBuyerProvince() {
        return buyerProvince;
    }

    public void setBuyerProvince(String buyerProvince) {
        this.buyerProvince = buyerProvince;
    }

    public String getBuyerCity() {
        return buyerCity;
    }

    public void setBuyerCity(String buyerCity) {
        this.buyerCity = buyerCity;
    }

    public String getBuyerRegion() {
        return buyerRegion;
    }

    public void setBuyerRegion(String buyerRegion) {
        this.buyerRegion = buyerRegion;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerZipcode() {
        return buyerZipcode;
    }

    public void setBuyerZipcode(String buyerZipcode) {
        this.buyerZipcode = buyerZipcode;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
