package com.sfebiz.supplychain.service.lp.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.supplychain.service.stockout.biz.model.BaseBO;

/**
 * 
 * <p>物流供应商/提供者业务对象</p>
 * 
 * @author matt
 * @Date 2017年7月20日 下午1:56:59
 */
public class LogisticsProviderBO extends BaseBO {

    /** 序号 */
    private static final long   serialVersionUID = 13555807321934224L;

    /** 服务商公司名称 */
    private String              companyName;

    /** 所对接外部系统的系统描述、对接描述，方便日后查看 */
    private String              integratingSystemDesc;

    /** 服务提供商业务id */
    private String              logisticsProviderNid;

    /** 接口类型，对应xml中配置的key */
    private String              interfaceType;

    /** 是否存仓库管理系统对接（0：不是,1：是） */
    private Integer             isWms;

    /** 是否存清关管理系统对接（0：不是,1：是） */
    private Integer             isCcms;

    /** 是否是支付申报系统对接（0：不是,1：是） */
    private Integer             isPds;

    /** 是否是国内运输系统对接（0：不是,1：是） */
    private Integer             isIntrTms;

    /** 是否是国际运输系统对接（0：不是,1：是） */
    private Integer             isIntlTms;

    /** 是否是国内路由查询系统对接（0：不是,1：是） */
    private Integer             isIntrLqs;

    /** 是否是国际路由系统系统对接（0：不是,1：是） */
    private Integer             isIntlLqs;

    /** 业务相关扩展信息 */
    private Map<String, String> bizMeta;

    /** 接口相关扩展信息 */
    private Map<String, String> interfaceMeta;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIntegratingSystemDesc() {
        return integratingSystemDesc;
    }

    public void setIntegratingSystemDesc(String integratingSystemDesc) {
        this.integratingSystemDesc = integratingSystemDesc;
    }

    public String getLogisticsProviderNid() {
        return logisticsProviderNid;
    }

    public void setLogisticsProviderNid(String logisticsProviderNid) {
        this.logisticsProviderNid = logisticsProviderNid;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public Integer getIsWms() {
        return isWms;
    }

    public void setIsWms(Integer isWms) {
        this.isWms = isWms;
    }

    public Integer getIsCcms() {
        return isCcms;
    }

    public void setIsCcms(Integer isCcms) {
        this.isCcms = isCcms;
    }

    public Integer getIsPds() {
        return isPds;
    }

    public void setIsPds(Integer isPds) {
        this.isPds = isPds;
    }

    public Integer getIsIntrTms() {
        return isIntrTms;
    }

    public void setIsIntrTms(Integer isIntrTms) {
        this.isIntrTms = isIntrTms;
    }

    public Integer getIsIntlTms() {
        return isIntlTms;
    }

    public void setIsIntlTms(Integer isIntlTms) {
        this.isIntlTms = isIntlTms;
    }

    public Integer getIsIntrLqs() {
        return isIntrLqs;
    }

    public void setIsIntrLqs(Integer isIntrLqs) {
        this.isIntrLqs = isIntrLqs;
    }

    public Integer getIsIntlLqs() {
        return isIntlLqs;
    }

    public void setIsIntlLqs(Integer isIntlLqs) {
        this.isIntlLqs = isIntlLqs;
    }

    public Map<String, String> getBizMeta() {
        return bizMeta;
    }

    public void setBizMeta(Map<String, String> bizMeta) {
        this.bizMeta = bizMeta;
    }

    public Map<String, String> getInterfaceMeta() {
        return interfaceMeta;
    }

    public void setInterfaceMeta(Map<String, String> interfaceMeta) {
        this.interfaceMeta = interfaceMeta;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
