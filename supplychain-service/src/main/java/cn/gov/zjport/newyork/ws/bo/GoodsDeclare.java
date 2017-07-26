package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;

/**
 * <p>个人商品备案</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/4
 * Time: 下午5:27
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"accountBookNo", "ieFlag", "preEntryNumber", "importType", "inOutDateStr", "iePort", "destinationPort"
        , "trafName", "voyageNo", "trafMode", "declareCompanyType", "declareCompanyCode", "declareCompanyName", "eCommerceCode",
        "eCommerceName", "orderNo", "wayBill", "tradeCountry", "packNo", "grossWeight", "netWeight", "warpType", "remark",
        "declPort", "enteringPerson", "enteringCompanyName", "declarantNo", "customsField", "senderName", "consignee",
        "senderCountry", "senderCity", "paperType", "paperNumber", "worth", "currCode", "mainGName",
        "internalAreaCompanyNo", "internalAreaCompanyName", "applicationFormNo", "isAuthorize"})
@XmlRootElement(name = "goodsDeclare")
public class GoodsDeclare {

    /**
     * 账册编号
     * 可以数字和字母（转出的手册、转入、转出的报关单）
     */
    @XmlElement(name = "accountBookNo")
    private String accountBookNo;


    /**
     * 进出口标志
     * 默认为I
     */
    @XmlElement(name = "ieFlag", required = true)
    private String ieFlag;

    /**
     * 预录入号码
     * 4位电商编号+14位企业流水，电商平台/物流企业生成后发送服务平台，与运单号一一对应，同个运单重新申报时，保持不变
     */
    @XmlElement(name = "preEntryNumber", required = true)
    private String preEntryNumber;

    /**
     * 进口类型
     * 0：一般进口
     * 1：保税进口
     */
    @XmlElement(name = "importType", required = true)
    private String importType;

    /**
     * 进出口日期
     */
    @XmlElement(name = "inOutDateStr")
    private String inOutDateStr;

    /**
     * 进出口岸代码
     */
    @XmlElement(name = "iePort", required = true)
    private String iePort;


    /**
     * 指运港(抵运港)
     */
    @XmlElement(name = "destinationPort", required = true)
    private String destinationPort;

    /**
     * 运输工具名称
     */
    @XmlElement(name = "trafName")
    private String trafName;


    /**
     * 运输工具航次(班)号
     */
    @XmlElement(name = "voyageNo")
    private String voyageNo;

    /**
     * 运输方式代码
     */
    @XmlElement(name = "trafMode", required = true)
    private String trafMode;

    /**
     * 申报单位类别:
     * 个人委托电商企业申报
     * 个人委托物流企业申报
     * 个人委托第三方申报
     */
    @XmlElement(name = "declareCompanyType", required = true)
    private String declareCompanyType;

    /**
     * 申报单位代码
     */
    @XmlElement(name = "declareCompanyCode", required = true)
    private String declareCompanyCode;

    /**
     * 申报单位名称
     */
    @XmlElement(name = "declareCompanyName", required = true)
    private String declareCompanyName;

    /**
     * 电商企业代码
     */
    @XmlElement(name = "eCommerceCode", required = true)
    private String eCommerceCode;

    /**
     * 电商企业名称
     */
    @XmlElement(name = "eCommerceName", required = true)
    private String eCommerceName;

    /**
     * 订单编号
     */
    @XmlElement(name = "orderNo", required = true)
    private String orderNo;

    /**
     * 分运单号
     */
    @XmlElement(name = "wayBill", required = true)
    private String wayBill;

    /**
     * 贸易国别（起运地）
     */
    @XmlElement(name = "tradeCountry", required = true)
    private String tradeCountry;

    /**
     * 件数
     */
    @XmlElement(name = "packNo", required = true)
    private String packNo;

    /**
     * 毛重（公斤）
     */
    @XmlElement(name = "grossWeight", required = true)
    private String grossWeight;

    /**
     * 净重（公斤）
     */
    @XmlElement(name = "netWeight")
    private String netWeight;

    /**
     * 包装种类
     */
    @XmlElement(name = "warpType", required = true)
    private String warpType;

    /**
     * 备注
     */
    @XmlElement(name = "remark")
    private String remark;

    /**
     * 申报口岸代码
     */
    @XmlElement(name = "declPort", required = true)
    private String declPort;

    /**
     * 录入人
     */
    @XmlElement(name = "enteringPerson", required = true)
    private String enteringPerson;


    /**
     * 录入单位名称
     */
    @XmlElement(name = "enteringCompanyName", required = true)
    private String enteringCompanyName;


    /**
     * 报关员代码
     */
    @XmlElement(name = "declarantNo")
    private String declarantNo;

    /**
     * 码头/货场代码
     */
    @XmlElement(name = "customsField", required = true)
    private String customsField;

    /**
     * 发件人
     */
    @XmlElement(name = "senderName", required = true)
    private String senderName;

    /**
     * 收件人
     */
    @XmlElement(name = "consignee", required = true)
    private String consignee;

    /**
     * 发件人国别
     */
    @XmlElement(name = "senderCountry", required = true)
    private String senderCountry;

    /**
     * 发件人城市
     */
    @XmlElement(name = "senderCity")
    private String senderCity;

    /**
     * 支付人证件类型
     */
    @XmlElement(name = "paperType")
    private String paperType;

    /**
     * 支付人证件号
     */
    @XmlElement(name = "paperNumber")
    private String paperNumber;

    /**
     * 价值
     */
    @XmlElement(name = "worth", required = true)
    private String worth;

    /**
     * 币制
     */
    @XmlElement(name = "currCode", required = true)
    private String currCode;

    /**
     * 主要货物名称
     */
    @XmlElement(name = "mainGName", required = true)
    private String mainGName;

    /**
     * 区内企业编码
     * 保税进口必填，填报仓储企业编码
     */
    @XmlElement(name = "internalAreaCompanyNo")
    private String internalAreaCompanyNo;

    /**
     * 区内企业名称
     * 保税进口必填，填报仓储企业名称
     */
    @XmlElement(name = "internalAreaCompanyName")
    private String internalAreaCompanyName;

    /**
     * 申请单编号
     * 保税进口必填，指仓储企业事先在辅助系统申请的分送集报申请单编号
     */
    @XmlElement(name = "applicationFormNo")
    private String applicationFormNo;

    /**
     * 是否授权
     * 代表是否个人买家授权电商申报数据，填写0或1，0代表否，1代表是
     */
    @XmlElement(name = "isAuthorize", required = true)
    private String isAuthorize;

    public String getAccountBookNo() {
        return accountBookNo;
    }

    public void setAccountBookNo(String accountBookNo) {
        this.accountBookNo = accountBookNo;
    }

    public String getIeFlag() {
        return ieFlag;
    }

    public void setIeFlag(String ieFlag) {
        this.ieFlag = ieFlag;
    }

    public String getPreEntryNumber() {
        return preEntryNumber;
    }

    public void setPreEntryNumber(String preEntryNumber) {
        this.preEntryNumber = preEntryNumber;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getInOutDateStr() {
        return inOutDateStr;
    }

    public void setInOutDateStr(String inOutDateStr) {
        this.inOutDateStr = inOutDateStr;
    }

    public String getIePort() {
        return iePort;
    }

    public void setIePort(String iePort) {
        this.iePort = iePort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getTrafName() {
        return trafName;
    }

    public void setTrafName(String trafName) {
        this.trafName = trafName;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public String getTrafMode() {
        return trafMode;
    }

    public void setTrafMode(String trafMode) {
        this.trafMode = trafMode;
    }

    public String getDeclareCompanyType() {
        return declareCompanyType;
    }

    public void setDeclareCompanyType(String declareCompanyType) {
        this.declareCompanyType = declareCompanyType;
    }

    public String getDeclareCompanyCode() {
        return declareCompanyCode;
    }

    public void setDeclareCompanyCode(String declareCompanyCode) {
        this.declareCompanyCode = declareCompanyCode;
    }

    public String getDeclareCompanyName() {
        return declareCompanyName;
    }

    public void setDeclareCompanyName(String declareCompanyName) {
        this.declareCompanyName = declareCompanyName;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWayBill() {
        return wayBill;
    }

    public void setWayBill(String wayBill) {
        this.wayBill = wayBill;
    }

    public String getTradeCountry() {
        return tradeCountry;
    }

    public void setTradeCountry(String tradeCountry) {
        this.tradeCountry = tradeCountry;
    }

    public String getPackNo() {
        return packNo;
    }

    public void setPackNo(String packNo) {
        this.packNo = packNo;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getWarpType() {
        return warpType;
    }

    public void setWarpType(String warpType) {
        this.warpType = warpType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeclPort() {
        return declPort;
    }

    public void setDeclPort(String declPort) {
        this.declPort = declPort;
    }

    public String getEnteringPerson() {
        return enteringPerson;
    }

    public void setEnteringPerson(String enteringPerson) {
        this.enteringPerson = enteringPerson;
    }

    public String getEnteringCompanyName() {
        return enteringCompanyName;
    }

    public void setEnteringCompanyName(String enteringCompanyName) {
        this.enteringCompanyName = enteringCompanyName;
    }

    public String getDeclarantNo() {
        return declarantNo;
    }

    public void setDeclarantNo(String declarantNo) {
        this.declarantNo = declarantNo;
    }

    public String getCustomsField() {
        return customsField;
    }

    public void setCustomsField(String customsField) {
        this.customsField = customsField;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getPaperNumber() {
        return paperNumber;
    }

    public void setPaperNumber(String paperNumber) {
        this.paperNumber = paperNumber;
    }

    public String getWorth() {
        return worth;
    }

    public void setWorth(String worth) {
        this.worth = worth;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getMainGName() {
        return mainGName;
    }

    public void setMainGName(String mainGName) {
        this.mainGName = mainGName;
    }

    public String getInternalAreaCompanyNo() {
        return internalAreaCompanyNo;
    }

    public void setInternalAreaCompanyNo(String internalAreaCompanyNo) {
        this.internalAreaCompanyNo = internalAreaCompanyNo;
    }

    public String getInternalAreaCompanyName() {
        return internalAreaCompanyName;
    }

    public void setInternalAreaCompanyName(String internalAreaCompanyName) {
        this.internalAreaCompanyName = internalAreaCompanyName;
    }

    public String getApplicationFormNo() {
        return applicationFormNo;
    }

    public void setApplicationFormNo(String applicationFormNo) {
        this.applicationFormNo = applicationFormNo;
    }

    public String getIsAuthorize() {
        return isAuthorize;
    }

    public void setIsAuthorize(String isAuthorize) {
        this.isAuthorize = isAuthorize;
    }
}
