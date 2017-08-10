package com.sfebiz.supplychain.protocol.zto.internation;

import com.sfebiz.supplychain.protocol.zto.ZTORequest;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午6:41
 */
public class ZTOInternationDistMarkRequest2 implements ZTORequest {


    private static final long serialVersionUID = -3351335909060374206L;
    /**
     * 发件省
     */
    private String SenderProvince;

    /**
     * 发件市
     */
    private String SenderCity;

    /**
     * 发件区(县/镇)
     */
    private String  SenderDistrict;

    /**
     * 发件详细地址
     */
    private String SenderAddress;

    /**
     * 收件省
     */
    private String BuyerProvince;

    /**
     * 收件市
     */
    private String BuyerCity;

    /**
     * 收件区(县/镇)
     */
    private String BuyerDistrict;


    /**
     * 收件详细地址
     */
    private String BuyerAddress;


    public String getSenderProvince() {
        return SenderProvince;
    }

    public void setSenderProvince(String senderProvince) {
        SenderProvince = senderProvince;
    }

    public String getSenderCity() {
        return SenderCity;
    }

    public void setSenderCity(String senderCity) {
        SenderCity = senderCity;
    }

    public String getSenderDistrict() {
        return SenderDistrict;
    }

    public void setSenderDistrict(String senderDistrict) {
        SenderDistrict = senderDistrict;
    }

    public String getSenderAddress() {
        return SenderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        SenderAddress = senderAddress;
    }

    public String getBuyerProvince() {
        return BuyerProvince;
    }

    public void setBuyerProvince(String buyerProvince) {
        BuyerProvince = buyerProvince;
    }

    public String getBuyerCity() {
        return BuyerCity;
    }

    public void setBuyerCity(String buyerCity) {
        BuyerCity = buyerCity;
    }

    public String getBuyerDistrict() {
        return BuyerDistrict;
    }

    public void setBuyerDistrict(String buyerDistrict) {
        BuyerDistrict = buyerDistrict;
    }

    public String getBuyerAddress() {
        return BuyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        BuyerAddress = buyerAddress;
    }

    @Override
    public String toString() {
        return "ZTOInternationDistMarkRequest2{" +
                "SenderProvince='" + SenderProvince + '\'' +
                ", SenderCity='" + SenderCity + '\'' +
                ", SenderDistrict='" + SenderDistrict + '\'' +
                ", SenderAddress='" + SenderAddress + '\'' +
                ", BuyerProvince='" + BuyerProvince + '\'' +
                ", BuyerCity='" + BuyerCity + '\'' +
                ", BuyerDistrict='" + BuyerDistrict + '\'' +
                '}';
    }

}

