package com.sfebiz.supplychain.exposed.merchant.entity;

import com.sfebiz.supplychain.exposed.common.enums.RegularExpressionConstant;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 货主包材实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-20 12:43
 **/
public class MerchantPackageMaterialEntity implements Serializable{

    private static final long serialVersionUID = 5983393304719133878L;


    public Long id;

    /**
     * 货主ID
     */
    public Long merchantId;

    /**
     * 订单来源
     */
    @NotNull(message = "订单来源不能为空")
    public String orderSource;

    /**
     * 寄件人名
     */
    @NotNull(message = "寄件人姓名不能为空")
    public String senderName;

    /**
     * 寄件人手机号
     */
    @NotNull(message = "寄件人电话不能为空")
    @MatchPattern(pattern = {RegularExpressionConstant.PHONE_CALL_PATTERN}, message = "寄件人电话不合法")
    public String senderTelephone;

    /**
     * 包材类型
     */
    @NotNull(message = "包材类型不能为空")
    public String packageMaterialType;

    /**
     * 宁波店铺号（宁波跨境购使用）
     */
    public String nbShopNumber;

    /**
     * 品牌的Logo（头部）（图片路径）
     * 示例：http://img0.fengqucdn.com/logo/fq-logo.png
     */
    public String headerLogo;

    /**
     * 品牌的标题（头部）
     * 示例：丰趣海淘购物清单
     */
    public String headerTitle;

    /**
     * 品牌的广告（尾部）
     * 示例：http://img0.fengqucdn.com/logo/footer-logo.jpg
     */
    public String footerAdvert;

    /**
     * 品牌的说明（尾部）
     * 示例：非常感谢您在供销社海外购网站 www.coopoverseasbuy.com 购物，我们期待您的再次光临！
     */
    public String footerDesc;


    /**
     * 寄件人名PDF
     */
    public String pdfSenderName;

    /**
     * 管道代码，跟仓绑定，不同的仓使用不同的管道代码, 还要区分不同的渠道类型（斑马仓使用）
     * 示例：route_id={"43":"HKH-CN-FQEP","95":"SHA-CN-FQEP"}
     */
    public String routeId;

    /**
     * 渠道区分（费舍尔仓使用）
     */
    public String isMaster;

    /**
     * 创建时间
     */
    public Date gmtCreate;

    /**
     * 修改时间
     */
    public Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderTelephone() {
        return senderTelephone;
    }

    public void setSenderTelephone(String senderTelephone) {
        this.senderTelephone = senderTelephone;
    }

    public String getPackageMaterialType() {
        return packageMaterialType;
    }

    public void setPackageMaterialType(String packageMaterialType) {
        this.packageMaterialType = packageMaterialType;
    }

    public String getNbShopNumber() {
        return nbShopNumber;
    }

    public void setNbShopNumber(String nbShopNumber) {
        this.nbShopNumber = nbShopNumber;
    }

    public String getHeaderLogo() {
        return headerLogo;
    }

    public void setHeaderLogo(String headerLogo) {
        this.headerLogo = headerLogo;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getFooterAdvert() {
        return footerAdvert;
    }

    public void setFooterAdvert(String footerAdvert) {
        this.footerAdvert = footerAdvert;
    }

    public String getFooterDesc() {
        return footerDesc;
    }

    public void setFooterDesc(String footerDesc) {
        this.footerDesc = footerDesc;
    }

    public String getPdfSenderName() {
        return pdfSenderName;
    }

    public void setPdfSenderName(String pdfSenderName) {
        this.pdfSenderName = pdfSenderName;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "MerchantPackageMaterialEntity{" +
                "id=" + id +
                ", merchantId=" + merchantId +
                ", orderSource='" + orderSource + '\'' +
                ", senderName='" + senderName + '\'' +
                ", senderTelephone='" + senderTelephone + '\'' +
                ", packageMaterialType='" + packageMaterialType + '\'' +
                ", nbShopNumber='" + nbShopNumber + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}

