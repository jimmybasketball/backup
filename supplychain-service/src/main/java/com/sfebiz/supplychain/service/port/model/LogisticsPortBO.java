package com.sfebiz.supplychain.service.port.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * <p>口岸业务信息实体</p>
 * 
 * @author matt
 * @Date 2017年7月20日 上午11:49:15
 */
public class LogisticsPortBO implements Serializable {

    /** 序号 */
    private static final long serialVersionUID = -3138294375297271201L;

    /**
     * 口岸ID
     */
    private Long              id;

    /**
     * 口岸逻辑ID
     */
    private String            portNid;

    /**
     * 口岸名称
     */
    private String            name;

    /**
     * 口岸编号
     */
    private String            code;

    /**
     * 企业在跨境电商通关服务的备案编号
     */
    private String            companyCode;

    /**
     * 企业在跨境电商通关服务平台的备案名称
     */
    private String            companyName;

    /**
     * 电商平台下的商家备案编号
     */
    private String            eCommerceCode;

    /**
     * 电商平台下的商家备案名称
     */
    private String            eCommerceName;

    /**
     * 物流企业编号
     */
    private String            logisCompanyCode;

    /**
     * 口岸物流企业名称
     */
    private String            logisCompanyName;

    /**
     * 与口岸交互的URL
     */
    private String            url;

    /**
     * 命令类型
     */
    private String            type;

    /**
     * 口岸支付对接编码
     */
    private String            payKey;

    /**
     * 限价幅度 null或者0表示不限价
     */
    private String            limitRange;

    /**
     * 口岸元信息
     */
    private String            meta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String geteCommerceCode() {
        return eCommerceCode;
    }

    public void seteCommerceCode(String eCommerceCode) {
        this.eCommerceCode = eCommerceCode;
    }

    public String geteCommerceName() {
        return eCommerceName;
    }

    public void seteCommerceName(String eCommerceName) {
        this.eCommerceName = eCommerceName;
    }

    public String getLogisCompanyCode() {
        return logisCompanyCode;
    }

    public void setLogisCompanyCode(String logisCompanyCode) {
        this.logisCompanyCode = logisCompanyCode;
    }

    public String getLogisCompanyName() {
        return logisCompanyName;
    }

    public void setLogisCompanyName(String logisCompanyName) {
        this.logisCompanyName = logisCompanyName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    public String getLimitRange() {
        return limitRange;
    }

    public void setLimitRange(String limitRange) {
        this.limitRange = limitRange;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
