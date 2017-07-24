package com.sfebiz.supplychain.persistence.base.lp.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 
 * <p>物流提供者信息实体</p>
 *
 * @author matt
 * @Date 2017年7月20日 下午2:07:44
 */
public class LogisticsProviderDO extends BaseDO {

    /** 序号 */
    private static final long serialVersionUID = -694828515345740921L;

    /** 服务商公司名称 */
    private String            companyName;

    /** 所对接外部系统的系统描述、对接描述，方便日后查看 */
    private String            integratingSystemDesc;

    /** 服务提供商业务id */
    private String            logisticsProviderNid;

    /** 接口类型 */
    private Integer           interfaceType;

    /** 是否存仓库管理系统对接（0：不是,1：是） */
    private Integer           isWms;

    /** 是否存清关管理系统对接（0：不是,1：是） */
    private Integer           isCcms;

    /** 是否是支付申报系统对接（0：不是,1：是） */
    private Integer           isPds;

    /** 是否是国内运输系统对接（0：不是,1：是） */
    private Integer           isIntrTms;

    /** 是否是国际运输系统对接（0：不是,1：是） */
    private Integer           isIntlTms;

    /** 是否是国内路由查询系统对接（0：不是,1：是） */
    private Integer           isIntrLqs;

    /** 是否是国际路由系统系统对接（0：不是,1：是） */
    private Integer           isIntlLqs;

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

    public Integer getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Integer interfaceType) {
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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
