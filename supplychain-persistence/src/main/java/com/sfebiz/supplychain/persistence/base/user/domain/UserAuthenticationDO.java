package com.sfebiz.supplychain.persistence.base.user.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * 
 * <p>实名认证信息实体</p>
 *
 * @author matt
 * @Date 2017年7月26日 下午1:55:50
 */
public class UserAuthenticationDO extends BaseDO {
    
    /** 序号 */
    private static final long serialVersionUID = -7801035644934212709L;

    /** 用户姓名 */
    private String name;

    /** 身份证号 */
    private String idNo;

    /** 身份证正面照片地址  */
    private String idCardFrontPhotoUrl;

    /** 身份证反面照片地址 */
    private String idCardBackPhotoUrl;
    
    /** 身份证照片快照时间戳 */
    private Long idCardPhotoTimestamp;
    
    /** 扩展信息 */
    private String features;
    
    /** 实名验证状态标识（1：成功，0：不成功） */
    private Integer verifyFlag;
    
    /** 实名认证的渠道 */
    private String verifyChannel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdCardFrontPhotoUrl() {
        return idCardFrontPhotoUrl;
    }

    public void setIdCardFrontPhotoUrl(String idCardFrontPhotoUrl) {
        this.idCardFrontPhotoUrl = idCardFrontPhotoUrl;
    }

    public String getIdCardBackPhotoUrl() {
        return idCardBackPhotoUrl;
    }

    public void setIdCardBackPhotoUrl(String idCardBackPhotoUrl) {
        this.idCardBackPhotoUrl = idCardBackPhotoUrl;
    }

    public Long getIdCardPhotoTimestamp() {
        return idCardPhotoTimestamp;
    }

    public void setIdCardPhotoTimestamp(Long idCardPhotoTimestamp) {
        this.idCardPhotoTimestamp = idCardPhotoTimestamp;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Integer getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(Integer verifyFlag) {
        this.verifyFlag = verifyFlag;
    }

    public String getVerifyChannel() {
        return verifyChannel;
    }

    public void setVerifyChannel(String verifyChannel) {
        this.verifyChannel = verifyChannel;
    }

    @Override
    public String toString() {
        return "UserAuthenticationDO{" +
                "name='" + name + '\'' +
                ", idNo='" + idNo + '\'' +
                ", idCardFrontPhotoUrl='" + idCardFrontPhotoUrl + '\'' +
                ", idCardBackPhotoUrl='" + idCardBackPhotoUrl + '\'' +
                ", idCardPhotoTimestamp=" + idCardPhotoTimestamp +
                ", features='" + features + '\'' +
                ", verifyFlag=" + verifyFlag +
                ", verifyChannel='" + verifyChannel + '\'' +
                '}';
    }
}
