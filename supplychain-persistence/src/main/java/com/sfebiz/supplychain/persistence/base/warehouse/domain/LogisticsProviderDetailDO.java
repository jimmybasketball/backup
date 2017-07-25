package com.sfebiz.supplychain.persistence.base.warehouse.domain;

import com.sfebiz.common.dao.domain.BaseDO;

public class LogisticsProviderDetailDO extends BaseDO {
    private static final long serialVersionUID = -2990376938705910136L;
    private Integer type;
    private String logisticsProviderNid;
    private String name;
    private Integer firstWeight;
    private Integer firstPrice;
    private Integer stepWeight;
    private Integer stepPrice;
    /**
     * 对应 application-context 里 lpcMapping 里的 key
     */
    private Integer interfaceType;
    private String interfaceUrl;
    private String interfaceKey;
    private String code;
    private String proxyUrl;
    private String proxyPort;
    private String ftpUser;
    private String ftpPassword;
    private String filePath;
    private String custId;
    private String payMethod;
    private String meta;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLogisticsProviderNid() {
        return logisticsProviderNid;
    }

    public void setLogisticsProviderNid(String logisticsProviderNid) {
        this.logisticsProviderNid = logisticsProviderNid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Integer getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Integer firstPrice) {
        this.firstPrice = firstPrice;
    }

    public Integer getStepWeight() {
        return stepWeight;
    }

    public void setStepWeight(Integer stepWeight) {
        this.stepWeight = stepWeight;
    }

    public Integer getStepPrice() {
        return stepPrice;
    }

    public void setStepPrice(Integer stepPrice) {
        this.stepPrice = stepPrice;
    }

    public Integer getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Integer interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getInterfaceUrl() {
        return interfaceUrl;
    }

    public void setInterfaceUrl(String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    public String getInterfaceKey() {
        return interfaceKey;
    }

    public void setInterfaceKey(String interfaceKey) {
        this.interfaceKey = interfaceKey;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "LogisticsProviderDetailDO{" +
                "type=" + type +
                ", logisticsProviderNid='" + logisticsProviderNid + '\'' +
                ", name='" + name + '\'' +
                ", firstWeight=" + firstWeight +
                ", firstPrice=" + firstPrice +
                ", stepWeight=" + stepWeight +
                ", stepPrice=" + stepPrice +
                ", interfaceType=" + interfaceType +
                ", interfaceUrl='" + interfaceUrl + '\'' +
                ", interfaceKey='" + interfaceKey + '\'' +
                ", code='" + code + '\'' +
                ", proxyUrl='" + proxyUrl + '\'' +
                ", proxyPort='" + proxyPort + '\'' +
                ", ftpUser='" + ftpUser + '\'' +
                ", ftpPassword='" + ftpPassword + '\'' +
                ", filePath='" + filePath + '\'' +
                ", custId='" + custId + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", meta='" + meta + '\'' +
                '}';
    }

}
