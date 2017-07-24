package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * <p>运单记录实体</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/03
 * Time: 下午2:34
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"wayBill", "totalWayBill", "packNo", "grossWeight", "netWeight", "goodsName",
        "sendArea", "consignee", "consigneeArea", "consigneeAddress", "consigneeTel", "zipCode", "customsCode",
        "worth", "currCode", "importDateStr"})
@XmlRootElement(name = "WayBillImportDto")
public class WayBillImportDto {

    /**
     * 分运单号(必填)
     */
    @XmlElement(name = "wayBill", required = true)
    private String wayBill;

    /**
     * 总运单号
     * 直邮模式下，总运单号必填（暂不实现）
     */
    @XmlElement(name = "totalWayBill")
    private String totalWayBill;

    /**
     * 件数
     */
    @XmlElement(name = "packNo", required = true)
    private int packNo;

    /**
     * 毛重
     */
    @XmlElement(name = "grossWeight", required = true)
    private String grossWeight;

    /**
     * 净重
     */
    @XmlElement(name = "netWeight")
    private String netWeight;

    /**
     * 主要货物名称
     */
    @XmlElement(name = "goodsName", required = true)
    private String goodsName;

    /**
     * 发件地区
     */
    @XmlElement(name = "sendArea", required = true)
    private String sendArea;

    /**
     * 收件人名称
     */
    @XmlElement(name = "consignee", required = true)
    private String consignee;

    /**
     * 收件地区
     */
    @XmlElement(name = "consigneeArea", required = true)
    private String consigneeArea;

    /**
     * 收件人地址
     */
    @XmlElement(name = "consigneeAddress", required = true)
    private String consigneeAddress;

    /**
     * 收件人联系方式
     */
    @XmlElement(name = "consigneeTel", required = true)
    private String consigneeTel;

    /**
     * 邮编
     */
    @XmlElement(name = "zipCode")
    private String zipCode;

    /**
     * 关区代码
     */
    @XmlElement(name = "customsCode", required = true)
    private String customsCode;

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
     * 进口日期
     * 格式要求：2012-04-23
     */
    @XmlElement(name = "importDateStr", required = true)
    private String importDateStr;

    public String getWayBill() {
        return wayBill;
    }

    public void setWayBill(String wayBill) {
        this.wayBill = wayBill;
    }

    public String getTotalWayBill() {
        return totalWayBill;
    }

    public void setTotalWayBill(String totalWayBill) {
        this.totalWayBill = totalWayBill;
    }

    public int getPackNo() {
        return packNo;
    }

    public void setPackNo(int packNo) {
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSendArea() {
        return sendArea;
    }

    public void setSendArea(String sendArea) {
        this.sendArea = sendArea;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeArea() {
        return consigneeArea;
    }

    public void setConsigneeArea(String consigneeArea) {
        this.consigneeArea = consigneeArea;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCustomsCode() {
        return customsCode;
    }

    public void setCustomsCode(String customsCode) {
        this.customsCode = customsCode;
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

    public String getImportDateStr() {
        return importDateStr;
    }

    public void setImportDateStr(String importDateStr) {
        this.importDateStr = importDateStr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("wayBill", wayBill)
                .append("totalWayBill", totalWayBill)
                .append("packNo", packNo)
                .append("grossWeight", grossWeight)
                .append("netWeight", netWeight)
                .append("goodsName", goodsName)
                .append("sendArea", sendArea)
                .append("consignee", consignee)
                .append("consigneeArea", consigneeArea)
                .append("consigneeAddress", consigneeAddress)
                .append("consigneeTel", consigneeTel)
                .append("zipCode", zipCode)
                .append("customsCode", customsCode)
                .append("worth", worth)
                .append("currCode", currCode)
                .append("importDateStr", importDateStr)
                .toString();
    }
}
