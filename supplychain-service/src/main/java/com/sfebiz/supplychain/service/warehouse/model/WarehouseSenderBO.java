package com.sfebiz.supplychain.service.warehouse.model;

/**
 * 
 * <p>仓库发货信息字段</p>
 * 
 * @author matt
 * @Date 2017年7月24日 下午5:38:42
 */
public class WarehouseSenderBO {

    /** 仓库对外显示的发货商名称 */
    private String senderName;

    /** 仓库对外显示的发货商联系方式 */
    private String senderTelephone;

    /** 仓库对外显示的发货国家 */
    private String senderCountry;

    /** 仓库对外显示的发货商省 */
    private String senderProvince;

    /** 仓库对外显示的发货商城市 */
    private String senderCity;

    /** 仓库对外显示的发货商区 */
    private String senderDistrict;

    /** 仓库对外显示的发货商详细地址 */
    private String senderAddress;

    /** 仓库对外显示的发货商邮编 */
    private String senderZipcode;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderTelephone() {
        return senderTelephone;
    }

    public void setSenderTelephone(String senderTelephone) {
        this.senderTelephone = senderTelephone;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
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

    public String getSenderDistrict() {
        return senderDistrict;
    }

    public void setSenderDistrict(String senderDistrict) {
        this.senderDistrict = senderDistrict;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderZipcode() {
        return senderZipcode;
    }

    public void setSenderZipcode(String senderZipcode) {
        this.senderZipcode = senderZipcode;
    }

}
