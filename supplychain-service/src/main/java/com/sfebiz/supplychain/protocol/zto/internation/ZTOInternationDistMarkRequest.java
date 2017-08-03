package com.sfebiz.supplychain.protocol.zto.internation;

import com.sfebiz.supplychain.protocol.zto.ZTORequest;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午6:41
 */
public class ZTOInternationDistMarkRequest implements ZTORequest {

    private static final long serialVersionUID = -4576187451717759278L;

    /**
     * 发件省
     */
    private String send_province;

    /**
     * 发件市
     */
    private String send_city;

    /**
     * 发件区(县/镇)
     */
    private String  send_district;

    /**
     * 发件详细地址
     */
    private String send_address;

    /**
     * 收件省
     */
    private String receive_province;

    /**
     * 收件市
     */
    private String receive_city;

    /**
     * 收件区(县/镇)
     */
    private String receive_district;


    /**
     * 收件详细地址
     */
    private String receive_address;

    public String getSend_province() {
        return send_province;
    }

    public void setSend_province(String send_province) {
        this.send_province = send_province;
    }

    public String getSend_city() {
        return send_city;
    }

    public void setSend_city(String send_city) {
        this.send_city = send_city;
    }

    public String getSend_district() {
        return send_district;
    }

    public void setSend_district(String send_district) {
        this.send_district = send_district;
    }

    public String getSend_address() {
        return send_address;
    }

    public void setSend_address(String send_address) {
        this.send_address = send_address;
    }

    public String getReceive_province() {
        return receive_province;
    }

    public void setReceive_province(String receive_province) {
        this.receive_province = receive_province;
    }

    public String getReceive_city() {
        return receive_city;
    }

    public void setReceive_city(String receive_city) {
        this.receive_city = receive_city;
    }

    public String getReceive_district() {
        return receive_district;
    }

    public void setReceive_district(String receive_district) {
        this.receive_district = receive_district;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }
}
