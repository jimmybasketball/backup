package com.sfebiz.supplychain.service.warehouse.model;

/**
 * 
 * <p>仓库地址信息业务对象</p>
 * @author matt
 * @Date 2017年7月24日 下午5:38:24
 */
public class WarehouseAddressBO {

    /** 仓库所在国家 */
    private String country;

    /** 仓库所在省 */
    private String province;

    /** 仓库所在城市 */
    private String city;

    /** 仓库所在区 */
    private String district;

    /** 仓库所在详细地址 */
    private String address;

    /** 仓库所在邮编 */
    private String zipcode;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
