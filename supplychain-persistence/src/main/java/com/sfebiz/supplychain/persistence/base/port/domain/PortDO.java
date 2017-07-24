package com.sfebiz.supplychain.persistence.base.port.domain;

import com.sfebiz.common.dao.domain.BaseDO;

public class PortDO extends BaseDO {

    private static final long serialVersionUID = -7812543477136566611L;
    private String portNid;
    private String name;
    private String code;
    private String companyCode;
    private String companyName;
    private String eCommerceCode;
    private String eCommerceName;
    private Integer type;
    private String url;
    private String uKey;
    private String payKey;
    private String logisCompanyName;
    private String logisCompanyCode;
    private String meta;

    public String getPortNid() {
        return portNid;
    }

    public void setPortNid(String portNid) {
        this.portNid = portNid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    public String getLogisCompanyName() {
        return logisCompanyName;
    }

    public void setLogisCompanyName(String logisCompanyName) {
        this.logisCompanyName = logisCompanyName;
    }

    public String getLogisCompanyCode() {
        return logisCompanyCode;
    }

    public void setLogisCompanyCode(String logisCompanyCode) {
        this.logisCompanyCode = logisCompanyCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUKey() {
        return uKey;
    }

    public void setUKey(String uKey) {
        this.uKey = uKey;
    }

    public String getECommerceCode() {
        return eCommerceCode;
    }

    public void setECommerceCode(String eCommerceCode) {
        this.eCommerceCode = eCommerceCode;
    }

    public String getECommerceName() {
        return eCommerceName;
    }

    public void setECommerceName(String eCommerceName) {
        this.eCommerceName = eCommerceName;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getuKey() {
        return uKey;
    }

    public void setuKey(String uKey) {
        this.uKey = uKey;
    }

    public String geteCommerceName() {
        return eCommerceName;
    }

    public void seteCommerceName(String eCommerceName) {
        this.eCommerceName = eCommerceName;
    }

    public String geteCommerceCode() {
        return eCommerceCode;
    }

    public void seteCommerceCode(String eCommerceCode) {
        this.eCommerceCode = eCommerceCode;
    }
}
