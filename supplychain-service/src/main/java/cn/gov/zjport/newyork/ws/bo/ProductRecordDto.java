package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * <p>商品记录实体</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/12
 * Time: 下午2:34
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"companyCode", "companyName", "postTaxNo", "goodsType", "goodsName", "barCode",
        "brand", "goodsModel", "mainElement", "purpose", "standards", "productionEnterprise", "productionCountry",
        "licenceKey", "categoryCode", "materialAddress", "declareTimeStr"})
@XmlRootElement(name = "productRecordDto")
public class ProductRecordDto {

    /**
     * 电商平台编号(必填)
     * (电商平台备案取得的唯一标示)
     */
    @XmlElement(name = "companyCode")
    private String companyCode;

    /**
     * 电商平台名称(必填)
     */
    @XmlElement(name = "companyName")
    private String companyName;

    /**
     * 行邮税号(必填)
     */
    @XmlElement(name = "postTaxNo")
    private String postTaxNo;

    /**
     * 商品类别(必填)
     */
    @XmlElement(name = "goodsType")
    private String goodsType;

    /**
     * 商品名称(必填)
     */
    @XmlElement(name = "goodsName")
    private String goodsName;

    /**
     * 条形码
     */
    @XmlElement(name = "barCode")
    private String barCode;

    /**
     * 品牌
     */
    @XmlElement(name = "brand")
    private String brand;

    /**
     * 规格型号
     */
    @XmlElement(name = "goodsModel")
    private String goodsModel;

    /**
     * 主要成份
     */
    @XmlElement(name = "mainElement")
    private String mainElement;

    /**
     * 用途
     */
    @XmlElement(name = "purpose")
    private String purpose;

    /**
     * 适用标准
     */
    @XmlElement(name = "standards")
    private String standards;

    /**
     * 生产企业
     */
    @XmlElement(name = "productionEnterprise")
    private String productionEnterprise;

    /**
     * 生产国
     */
    @XmlElement(name = "productionCountry")
    private String productionCountry;

    /**
     * 许可证号
     */
    @XmlElement(name = "licenceKey")
    private String licenceKey;

    /**
     * 类目编码(必填)
     */
    @XmlElement(name = "categoryCode")
    private String categoryCode;

    /**
     * 材料地址
     */
    @XmlElement(name = "materialAddress")
    private String materialAddress;

    /**
     * 申请时间(必填)
     */
    @XmlElement(name = "declareTimeStr")
    private String declareTimeStr;


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

    public String getPostTaxNo() {
        return postTaxNo;
    }

    public void setPostTaxNo(String postTaxNo) {
        this.postTaxNo = postTaxNo;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    public String getMainElement() {
        return mainElement;
    }

    public void setMainElement(String mainElement) {
        this.mainElement = mainElement;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStandards() {
        return standards;
    }

    public void setStandards(String standards) {
        this.standards = standards;
    }

    public String getProductionEnterprise() {
        return productionEnterprise;
    }

    public void setProductionEnterprise(String productionEnterprise) {
        this.productionEnterprise = productionEnterprise;
    }

    public String getProductionCountry() {
        return productionCountry;
    }

    public void setProductionCountry(String productionCountry) {
        this.productionCountry = productionCountry;
    }

    public String getLicenceKey() {
        return licenceKey;
    }

    public void setLicenceKey(String licenceKey) {
        this.licenceKey = licenceKey;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getMaterialAddress() {
        return materialAddress;
    }

    public void setMaterialAddress(String materialAddress) {
        this.materialAddress = materialAddress;
    }

    public String getDeclareTimeStr() {
        return declareTimeStr;
    }

    public void setDeclareTimeStr(String declareTimeStr) {
        this.declareTimeStr = declareTimeStr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("companyCode", companyCode)
                .append("companyName", companyName)
                .append("postTaxNo", postTaxNo)
                .append("goodsType", goodsType)
                .append("goodsName", goodsName)
                .append("barCode", barCode)
                .append("brand", brand)
                .append("goodsModel", goodsModel)
                .append("mainElement", mainElement)
                .append("purpose", purpose)
                .append("standards", standards)
                .append("productionEnterprise", productionEnterprise)
                .append("productionCountry", productionCountry)
                .append("licenceKey", licenceKey)
                .append("categoryCode", categoryCode)
                .append("materialAddress", materialAddress)
                .append("declareTimeStr", declareTimeStr)
                .toString();
    }
}
