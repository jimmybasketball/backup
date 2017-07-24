package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/4
 * Time: 下午5:29
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"goodsOrder", "codeTs", "goodsItemNo", "goodsName", "goodsModel", "originCountry", "tradeCurr"
        , "tradeTotal", "declPrice", "declTotalPrice", "useTo", "declareCount", "goodsUnit", "goodsGrossWeight",
        "firstUnit", "firstCount", "secondUnit", "secondCount", "productRecordNo", "webSite"})
@XmlRootElement(name = "goodsDeclare")
public class GoodsDeclareDetail {

    /**
     * 商品序号
     * 只能有数字，外网自动生成大于0小于50
     */
    @XmlElement(name = "goodsOrder", required = true)
    private int goodsOrder;

    /**
     * 行邮税号
     */
    @XmlElement(name = "codeTs", required = true)
    private String codeTs;

    /**
     * 商品货号
     * 保税进口业务中，货号即指料号，与仓储企业备案的电子账册中料号数据一致
     */
    @XmlElement(name = "goodsItemNo", required = true)
    private String goodsItemNo;

    /**
     * 物品名称
     */
    @XmlElement(name = "goodsName", required = true)
    private String goodsName;

    /**
     * 商品规格、型号
     */
    @XmlElement(name = "goodsModel", required = true)
    private String goodsModel;

    /**
     * 产销国
     */
    @XmlElement(name = "originCountry", required = true)
    private String originCountry;

    /**
     * 成交币制
     */
    @XmlElement(name = "tradeCurr", required = true)
    private String tradeCurr;

    /**
     * 成交总价
     * 包裹实际成交的金额
     */
    @XmlElement(name = "tradeTotal", required = true)
    private String tradeTotal;

    /**
     * 申报单价
     */
    @XmlElement(name = "declPrice", required = true)
    private String declPrice;

    /**
     * 申报总价
     */
    @XmlElement(name = "declTotalPrice", required = true)
    private String declTotalPrice;

    /**
     * 用途
     */
    @XmlElement(name = "useTo")
    private String useTo;

    /**
     * 申报数量
     */
    @XmlElement(name = "declareCount")
    private int declareCount;

    /**
     * 申报计量单位
     */
    @XmlElement(name = "goodsUnit")
    private String goodsUnit;

    /**
     * 商品毛重
     */
    @XmlElement(name = "goodsGrossWeight")
    private String goodsGrossWeight;

    /**
     * 第一单位
     * 保税进口模式下必填
     */
    @XmlElement(name = "firstUnit")
    private String firstUnit;

    /**
     * 第一数量
     * 保税进口模式下必填
     */
    @XmlElement(name = "firstCount")
    private int firstCount;

    /**
     * 第二单位
     */
    @XmlElement(name = "secondUnit")
    private String secondUnit;

    /**
     * 第二数量
     */
    @XmlElement(name = "secondCount")
    private String secondCount;

    /**
     * 产品国检备案编号
     */
    @XmlElement(name = "productRecordNo", required = true)
    private String productRecordNo;

    /**
     * 商品网址
     */
    @XmlElement(name = "webSite")
    private String webSite;

    public int getGoodsOrder() {
        return goodsOrder;
    }

    public void setGoodsOrder(int goodsOrder) {
        this.goodsOrder = goodsOrder;
    }

    public String getCodeTs() {
        return codeTs;
    }

    public void setCodeTs(String codeTs) {
        this.codeTs = codeTs;
    }

    public String getGoodsItemNo() {
        return goodsItemNo;
    }

    public void setGoodsItemNo(String goodsItemNo) {
        this.goodsItemNo = goodsItemNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getTradeCurr() {
        return tradeCurr;
    }

    public void setTradeCurr(String tradeCurr) {
        this.tradeCurr = tradeCurr;
    }

    public String getTradeTotal() {
        return tradeTotal;
    }

    public void setTradeTotal(String tradeTotal) {
        this.tradeTotal = tradeTotal;
    }

    public String getDeclPrice() {
        return declPrice;
    }

    public void setDeclPrice(String declPrice) {
        this.declPrice = declPrice;
    }

    public String getDeclTotalPrice() {
        return declTotalPrice;
    }

    public void setDeclTotalPrice(String declTotalPrice) {
        this.declTotalPrice = declTotalPrice;
    }

    public String getUseTo() {
        return useTo;
    }

    public void setUseTo(String useTo) {
        this.useTo = useTo;
    }

    public int getDeclareCount() {
        return declareCount;
    }

    public void setDeclareCount(int declareCount) {
        this.declareCount = declareCount;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getGoodsGrossWeight() {
        return goodsGrossWeight;
    }

    public void setGoodsGrossWeight(String goodsGrossWeight) {
        this.goodsGrossWeight = goodsGrossWeight;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(String firstUnit) {
        this.firstUnit = firstUnit;
    }

    public int getFirstCount() {
        return firstCount;
    }

    public void setFirstCount(int firstCount) {
        this.firstCount = firstCount;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public void setSecondUnit(String secondUnit) {
        this.secondUnit = secondUnit;
    }

    public String getSecondCount() {
        return secondCount;
    }

    public void setSecondCount(String secondCount) {
        this.secondCount = secondCount;
    }

    public String getProductRecordNo() {
        return productRecordNo;
    }

    public void setProductRecordNo(String productRecordNo) {
        this.productRecordNo = productRecordNo;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}
