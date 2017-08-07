package com.sfebiz.supplychain.service.merchant.model;

import com.sfebiz.supplychain.service.stockout.biz.model.BaseBO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 货主包材业务实体
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-25 16:45
 **/
public class MerchantPackageMaterialBO extends BaseBO{
    private static final long serialVersionUID = -5817321257827352619L;
    /**
     * 货主ID
     */
    private Long merchantId;

    /**
     * 订单来源
     */
    private String orderSource;

    /**
     * 寄件人名
     */
    private String senderName;

    /**
     * 寄件人电话
     */
    private String senderTelephone;

    /**
     * 包材类型
     */
    private String packageMaterialType;

    /**
     * 宁波店铺号（宁波跨境购使用）
     */
    private String nbShopNumber;

    /**
     * 品牌的Logo（头部）（图片路径）
     * 示例：http://img0.fengqucdn.com/logo/fq-logo.png
     */
    private String headerLogo;

    /**
     * 品牌的标题（头部）
     * 示例：丰趣海淘购物清单
     */
    private String headerTitle;

    /**
     * 品牌的广告（尾部）
     * 示例：http://img0.fengqucdn.com/logo/footer-logo.jpg
     */
    private String footerAdvert;

    /**
     * 品牌的说明（尾部）
     * 示例：非常感谢您在供销社海外购网站 www.coopoverseasbuy.com 购物，我们期待您的再次光临！
     */
    private String footerDesc;


    /**
     * 寄件人名PDF
     */
    private String pdfSenderName;

    /**
     * 管道代码，跟仓绑定，不同的仓使用不同的管道代码, 还要区分不同的渠道类型（斑马仓使用）
     * 示例：route_id={"43":"HKH-CN-FQEP","95":"SHA-CN-FQEP"}
     */
    private String routeId;

    /**
     * 渠道区分（费舍尔仓使用）
     */
    private String isMaster;



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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
