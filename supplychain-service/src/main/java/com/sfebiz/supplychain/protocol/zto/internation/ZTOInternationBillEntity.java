package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午4:36
 */
public class ZTOInternationBillEntity implements Serializable {

    private static final long serialVersionUID = -6179981600072383655L;

    /**
     * 电商平台编码
     */
    private String ecpcode;

    /**
     *
     */
    private String ecpname;


    /**
     * 电商平台编码（国检）
     */
    private String ecpCodeG;


    /**
     * 电商平台名称（国检）
     */
    private String ecpnameG;


    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 包装类型:1 木箱；2 纸箱；3 桶装；4 散装
     */
    private String wraptype;


    /**
     * 批次号
     */
    private String batchnumbers;


    public String getEcpcode() {
        return ecpcode;
    }

    public void setEcpcode(String ecpcode) {
        this.ecpcode = ecpcode;
    }

    public String getEcpname() {
        return ecpname;
    }

    public void setEcpname(String ecpname) {
        this.ecpname = ecpname;
    }

    public String getEcpCodeG() {
        return ecpCodeG;
    }

    public void setEcpCodeG(String ecpCodeG) {
        this.ecpCodeG = ecpCodeG;
    }

    public String getEcpnameG() {
        return ecpnameG;
    }

    public void setEcpnameG(String ecpnameG) {
        this.ecpnameG = ecpnameG;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBatchnumbers() {
        return batchnumbers;
    }

    public void setBatchnumbers(String batchnumbers) {
        this.batchnumbers = batchnumbers;
    }

    public String getWraptype() {
        return wraptype;
    }

    public void setWraptype(String wraptype) {
        this.wraptype = wraptype;
    }
}
