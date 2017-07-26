package com.sfebiz.supplychain.sdk.protocol;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 物流订单类结构体
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logisticsOrder", propOrder = {"segmentCode", "poNo", "packageNo", "state",
        "routeId", "occurTime", "carrierCode", "mailNo", "internationalCarrierCode", "internationalMailNo", "needCheck",
        "needTakePhoto", "itemsIncluded", "senderDetail", "receiverDetail",
        "logisticsRemark", "skuDetail", "skuDetailEn", "logisticsCode", "logisticsWeight", "volume",
        "logisticsFreight", "logisticsUnit", "logisticsType", "skuStockInId",
        "electronicFromUrl", "electronicInvoiceUrl", "logisticsCustomsDutys",
        "isMorePackage", "confirmPaymethod", "fixedDeliveryCompany", "skuStockInCode", "skuStockInRemark", "supplierId",
        "customerCode", "skuStockOutId", "skuStockInTime", "receiptFormat", "stockinType", "shipOrderPdfLink", "packingMaterials", "receiptDetail","stockoutOrderType"})
public class LogisticsOrder implements Serializable {

    private static final long serialVersionUID = 462850407494243528L;

    /**
     * 仓库编号
     */
    @XmlElement(nillable = false, required = false)
    public String segmentCode;

    /**
     * 出库单类型
     */
    @XmlElement(nillable = false, required = false)
    public String stockoutOrderType;

    /**
     * 销售订单号
     */
    @XmlElement(nillable = false, required = true)
    public String poNo;

    /**
     * 包裹号
     */
    @XmlElement(nillable = false, required = true)
    public String packageNo;

    /**
     * 出库单状态
     */
    @XmlElement(nillable = false, required = true)
    public String state;

    /**
     * 管道编号
     */
    @XmlElement(nillable = false, required = false)
    public String routeId;

    /**
     * 业务发生时间
     */
    @XmlElement(nillable = false, required = false)
    public String occurTime;

    /**
     * 承运商编号
     */
    @XmlElement(nillable = false, required = false)
    public String carrierCode;

    /**
     * 承运商运单号
     */
    @XmlElement(nillable = false, required = false)
    public String mailNo;

    /**
     * 国际承运商编号
     */
    @XmlElement(nillable = false, required = false)
    public String internationalCarrierCode;

    /**
     * 国际承运商运单号
     */
    @XmlElement(nillable = false, required = false)
    public String internationalMailNo;

    /**
     * 需拆包检查物品（Y/N）
     */
    @XmlElement(nillable = false, required = false)
    public String needCheck;

    /**
     * 需所有包裹照片（Y/N）
     */
    @XmlElement(nillable = false, required = false)
    public String needTakePhoto;

    /**
     * 物流订单中包含的商品，id列表用,分割，或者是仓配物品
     */
    @XmlElement(nillable = false, required = false)
    public String itemsIncluded;

    /**
     * 发货人信息
     */
    @XmlElement(nillable = false, required = false)
    public ContactDetail senderDetail;

    /**
     * 仓库详情
     */
    @XmlElement(nillable = false, required = false)
    public ContactDetail receiverDetail;

    /**
     * 物流订单备注
     */
    @XmlElement(nillable = false, required = false)
    public String logisticsRemark;

    /**
     * 仓配物品列表，或者购买列表
     */
    @XmlElement(nillable = false, required = false)
    public SkuDetail skuDetail;

    /**
     * 仓配物品列表-英文，或者购买列表-英文
     */
    @XmlElement(nillable = false, required = false)
    public SkuDetail skuDetailEn;

    /**
     * 接收类型：SUCCESS：接单,SECURITY：包裹安全监测不通过,OTHER_REASON：其他异常
     */
    @XmlElement(nillable = false, required = false)
    public String logisticsCode;

    /**
     * 称重(单位:g)
     */
    @XmlElement(nillable = false, required = false)
    public Double logisticsWeight = 0.0;

    /**
     * 体积
     */
    @XmlElement(nillable = false, required = false)
    public Volume volume;

    /**
     * 出库运费
     */
    @XmlElement(nillable = false, required = false)
    public Double logisticsFreight = 0.0;

    /**
     * 运费单位
     */
    @XmlElement(nillable = false, required = false)
    public String logisticsUnit;

    /**
     * 物流类型
     */
    @XmlElement(nillable = false, required = false)
    public Integer logisticsType = 0;

    /**
     * sku入库唯一单号
     */
    @XmlElement(nillable = false, required = false)
    public String skuStockInId;

    /**
     * sku入库类型
     */
    @XmlElement(nillable = false, required = false)
    public Integer stockinType;

    /**
     * 电子面单URL
     */
    @XmlElement(nillable = false, required = false)
    public String electronicFromUrl;

    /**
     * 发票URL
     */
    @XmlElement(nillable = false, required = false)
    public String electronicInvoiceUrl;

    /**
     * 关税,人民币,单位分
     */
    @XmlElement(nillable = false, required = false)
    public Double logisticsCustomsDutys = 0.0;

    /**
     * 是否多包裹,Y-是;N-否 若 mailNo 存在多个,必为 Y,否则 N,或不 填。
     */
    @XmlElement(nillable = false, required = false)
    public String isMorePackage;

    /**
     * 确认关税支付方式,二选一:A 或 C A-表示确认并从我的账户扣款; C-请电话与我确认后,再扣款;
     */
    @XmlElement(nillable = false, required = false)
    public String confirmPaymethod;

    /**
     * 指定国内派送公司,比如 EMS,SF
     */
    @XmlElement(nillable = false, required = false)
    public String fixedDeliveryCompany;

    /**
     * 完成 or 取消
     */
    @XmlElement(nillable = false, required = false)
    public String skuStockInCode;

    /**
     * 入库完成备注
     */
    @XmlElement(nillable = false, required = false)
    public String skuStockInRemark;

    /**
     * 供货商id,对于sc_provider表主键
     */
    @XmlElement(nillable = false, required = false)
    public Integer supplierId;

    /**
     * 货主
     */
    @XmlElement(nillable = false, required = false)
    public String customerCode;

    /**
     * sku出库唯一单号
     */
    @XmlElement(nillable = false, required = false)
    public String skuStockOutId;

    /**
     * 入库时间
     */
    @XmlElement(nillable = false, required = false)
    public String skuStockInTime;

    /**
     * 小票格式
     */
    @XmlElement(nillable = false, required = false)
    public Integer receiptFormat;

    /**
     * 面单链接
     */
    @XmlElement(nillable = false, required = false)
    public String shipOrderPdfLink;


    /**
     * 包材类型： FQ、GXS、EMPTY
     */
    @XmlElement(nillable = false, required = false)
    public String packingMaterials;

    /**
     * 小票格式详情
     */
    @XmlElement(nillable = false, required = false)
    public ReceiptDetail receiptDetail;

    public String getSkuStockOutId() {
        return skuStockOutId;
    }

    public void setSkuStockOutId(String skuStockOutId) {
        this.skuStockOutId = skuStockOutId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getLogisticsType() {
        return logisticsType == null ? 0 : logisticsType;
    }

    public void setLogisticsType(Integer logisticsType) {
        this.logisticsType = logisticsType;
    }

    public Double getLogisticsFreight() {
        return logisticsFreight;
    }

    public void setLogisticsFreight(Double logisticsFreight) {
        this.logisticsFreight = logisticsFreight;
    }

    public String getLogisticsUnit() {
        return logisticsUnit;
    }

    public void setLogisticsUnit(String logisticsUnit) {
        this.logisticsUnit = logisticsUnit;
    }

    public Double getLogisticsWeight() {
        return logisticsWeight;
    }

    public void setLogisticsWeight(Double logisticsWeight) {
        this.logisticsWeight = logisticsWeight;
    }

    public Volume getVolume() {
        if (volume == null) {
            volume = new Volume();
        }
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getInternationalMailNo() {
        return internationalMailNo;
    }

    public void setInternationalMailNo(String internationalMailNo) {
        this.internationalMailNo = internationalMailNo;
    }

    public String getInternationalCarrierCode() {
        return internationalCarrierCode;
    }

    public void setInternationalCarrierCode(String internationalCarrierCode) {
        this.internationalCarrierCode = internationalCarrierCode;
    }

    public String getNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(String needCheck) {
        this.needCheck = needCheck;
    }

    public String getNeedTakePhoto() {
        return needTakePhoto;
    }

    public void setNeedTakePhoto(String needTakePhoto) {
        this.needTakePhoto = needTakePhoto;
    }

    public String getItemsIncluded() {
        return itemsIncluded;
    }

    public void setItemsIncluded(String itemsIncluded) {
        this.itemsIncluded = itemsIncluded;
    }

    public ContactDetail getSenderDetail() {
        return senderDetail;
    }

    public void setSenderDetail(ContactDetail senderDetail) {
        this.senderDetail = senderDetail;
    }

    public ContactDetail getReceiverDetail() {
        return receiverDetail;
    }

    public void setReceiverDetail(ContactDetail receiverDetail) {
        this.receiverDetail = receiverDetail;
    }

    public String getLogisticsRemark() {
        return logisticsRemark;
    }

    public void setLogisticsRemark(String logisticsRemark) {
        this.logisticsRemark = logisticsRemark;
    }

    public SkuDetail getSkuDetail() {
        if (skuDetail == null) {
            skuDetail = new SkuDetail();
        }
        return skuDetail;
    }

    public void setSkuDetail(SkuDetail skuDetail) {
        this.skuDetail = skuDetail;
    }

    public SkuDetail getSkuDetailEn() {
        return skuDetailEn;
    }

    public void setSkuDetailEn(SkuDetail skuDetailEn) {
        this.skuDetailEn = skuDetailEn;
    }

    public String getSkuStockInId() {
        return skuStockInId;
    }

    public void setSkuStockInId(String skuStockInId) {
        this.skuStockInId = skuStockInId;
    }

    public String getElectronicFromUrl() {
        return electronicFromUrl;
    }

    public void setElectronicFromUrl(String electronicFromUrl) {
        this.electronicFromUrl = electronicFromUrl;
    }

    public String getElectronicInvoiceUrl() {
        return electronicInvoiceUrl;
    }

    public void setElectronicInvoiceUrl(String electronicInvoiceUrl) {
        this.electronicInvoiceUrl = electronicInvoiceUrl;
    }

    public Double getLogisticsCustomsDutys() {
        return logisticsCustomsDutys;
    }

    public void setLogisticsCustomsDutys(Double logisticsCustomsDutys) {
        this.logisticsCustomsDutys = logisticsCustomsDutys;
    }

    public String getIsMorePackage() {
        return isMorePackage;
    }

    public void setIsMorePackage(String isMorePackage) {
        this.isMorePackage = isMorePackage;
    }

    public String getConfirmPaymethod() {
        return confirmPaymethod;
    }

    public void setConfirmPaymethod(String confirmPaymethod) {
        this.confirmPaymethod = confirmPaymethod;
    }

    public String getFixedDeliveryCompany() {
        return fixedDeliveryCompany;
    }

    public void setFixedDeliveryCompany(String fixedDeliveryCompany) {
        this.fixedDeliveryCompany = fixedDeliveryCompany;
    }

    public String getSkuStockInCode() {
        return skuStockInCode;
    }

    public void setSkuStockInCode(String skuStockInCode) {
        this.skuStockInCode = skuStockInCode;
    }

    public String getSkuStockInRemark() {
        return skuStockInRemark;
    }

    public void setSkuStockInRemark(String skuStockInRemark) {
        this.skuStockInRemark = skuStockInRemark;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getReceiptFormat() {
        return receiptFormat;
    }

    public void setReceiptFormat(Integer receiptFormat) {
        this.receiptFormat = receiptFormat;
    }

    public String getSkuStockInTime() {
        return skuStockInTime;
    }

    public void setSkuStockInTime(String skuStockInTime) {
        this.skuStockInTime = skuStockInTime;
    }

    public Integer getStockinType() {
        return stockinType;
    }

    public void setStockinType(Integer stockinType) {
        this.stockinType = stockinType;
    }

    public String getShipOrderPdfLink() {
        return shipOrderPdfLink;
    }

    public void setShipOrderPdfLink(String shipOrderPdfLink) {
        this.shipOrderPdfLink = shipOrderPdfLink;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ReceiptDetail getReceiptDetail() {
        return receiptDetail;
    }

    public void setReceiptDetail(ReceiptDetail receiptDetail) {
        this.receiptDetail = receiptDetail;
    }

    public String getPackingMaterials() {
        return packingMaterials;
    }

    public void setPackingMaterials(String packingMaterials) {
        this.packingMaterials = packingMaterials;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getStockoutOrderType() {
        return stockoutOrderType;
    }

    public void setStockoutOrderType(String stockoutOrderType) {
        this.stockoutOrderType = stockoutOrderType;
    }

    @Override
    public String toString() {
        return "LogisticsOrder{" +
                "segmentCode='" + segmentCode + '\'' +
                ", poNo='" + poNo + '\'' +
                ", packageNo='" + packageNo + '\'' +
                ", state='" + state + '\'' +
                ", routeId='" + routeId + '\'' +
                ", occurTime='" + occurTime + '\'' +
                ", carrierCode='" + carrierCode + '\'' +
                ", mailNo='" + mailNo + '\'' +
                ", internationalCarrierCode='" + internationalCarrierCode + '\'' +
                ", internationalMailNo='" + internationalMailNo + '\'' +
                ", needCheck='" + needCheck + '\'' +
                ", needTakePhoto='" + needTakePhoto + '\'' +
                ", itemsIncluded='" + itemsIncluded + '\'' +
                ", senderDetail=" + senderDetail +
                ", receiverDetail=" + receiverDetail +
                ", logisticsRemark='" + logisticsRemark + '\'' +
                ", skuDetail=" + skuDetail +
                ", skuDetailEn=" + skuDetailEn +
                ", logisticsCode='" + logisticsCode + '\'' +
                ", logisticsWeight=" + logisticsWeight +
                ", volume=" + volume +
                ", logisticsFreight=" + logisticsFreight +
                ", logisticsUnit='" + logisticsUnit + '\'' +
                ", logisticsType=" + logisticsType +
                ", skuStockInId='" + skuStockInId + '\'' +
                ", stockinType=" + stockinType +
                ", electronicFromUrl='" + electronicFromUrl + '\'' +
                ", electronicInvoiceUrl='" + electronicInvoiceUrl + '\'' +
                ", logisticsCustomsDutys=" + logisticsCustomsDutys +
                ", isMorePackage='" + isMorePackage + '\'' +
                ", confirmPaymethod='" + confirmPaymethod + '\'' +
                ", fixedDeliveryCompany='" + fixedDeliveryCompany + '\'' +
                ", skuStockInCode='" + skuStockInCode + '\'' +
                ", skuStockInRemark='" + skuStockInRemark + '\'' +
                ", supplierId=" + supplierId +
                ", customerCode='" + customerCode + '\'' +
                ", skuStockOutId='" + skuStockOutId + '\'' +
                ", skuStockInTime='" + skuStockInTime + '\'' +
                ", receiptFormat=" + receiptFormat +
                ", shipOrderPdfLink='" + shipOrderPdfLink + '\'' +
                ", packingMaterials='" + packingMaterials + '\'' +
                ", receiptDetail=" + receiptDetail +
                '}';
    }
}
