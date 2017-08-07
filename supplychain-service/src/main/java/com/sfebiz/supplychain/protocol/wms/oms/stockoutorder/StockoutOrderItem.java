package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/21
 * Time: 下午9:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderItems")
@XmlType(propOrder = {"erpOrderLineNum","skuNo","itemName","itemUom","itemQuantity","itemPrice","itemDiscount","checkPrepardNo","customsPrepardNo","hsCode","itemBrand","itemSpecifications","bomAction","isPresent","isVirtualProduct","inventoryStatus","lot","note","lotAttr1","lotAttr2","lotAttr3","lotAttr4","lotAttr5","lotAttr6","lotAttr7","lotAttr8"})
public class StockoutOrderItem implements Serializable {


    private static final long serialVersionUID = 3351041363776455252L;
    @Description("行号")
    @XmlElement(name = "ErpOrderLineNum", required = false)
    private String erpOrderLineNum;

    @Description("商品编号")
    @XmlElement(name = "SkuNo", required = true)
    private String skuNo;

    @Description("商品名称")
    @XmlElement(name = "ItemName", required = true)
    private String itemName;

    @Description("商品单位")
    @XmlElement(name = "ItemUom", required = false)
    private String itemUom;

    @Description("商品数量")
    @XmlElement(name = "ItemQuantity", required = false)
    private String itemQuantity;

    @Description("商品单价")
    @XmlElement(name = "ItemPrice", required = false)
    private String itemPrice;

    @Description("商品数量优惠金额")
    @XmlElement(name = "ItemDiscount", required = false)
    private String itemDiscount;


    @Description("商品国检备案编号")
    @XmlElement(name = "CheckPrepardNo", required = false)
    private String checkPrepardNo;

    @Description("商品海关备案号")
    @XmlElement(name = "CustomsPrepardNo", required = false)
    private String customsPrepardNo;

    @Description("海关编号")
    @XmlElement(name = "HsCode", required = false)
    private String hsCode;

    @Description("商品品牌")
    @XmlElement(name = "ItemBrand", required = false)
    private String itemBrand;

    @Description("商品型号")
    @XmlElement(name = "ItemSpecifications", required = false)
    private String itemSpecifications;

    @Description("是否组合商品")
    @XmlElement(name = "BomAction", required = false)
    private String bomAction;

    @Description("是否赠品")
    @XmlElement(name = "IsPresent", required = false)
    private String isPresent;

    @Description("是否虚拟增值产品")
    @XmlElement(name = "IsVirtualProduct", required = false)
    private String isVirtualProduct;


    @Description("库存类型")
    @XmlElement(name = "InventoryStatus", required = false)
    private String inventoryStatus;

    @Description("批号")
    @XmlElement(name = "Lot", required = false)
    private String lot;

    @Description("明细备注")
    @XmlElement(name = "Note", required = false)
    private String note;

    @Description("批号属性1")
    @XmlElement(name = "LotAttr1",required = false)
    private String lotAttr1;

    @Description("批号属性2")
    @XmlElement(name = "LotAttr2",required = false)
    private String lotAttr2;

    @Description("批号属性3")
    @XmlElement(name = "LotAttr3",required = false)
    private String lotAttr3;

    @Description("批号属性4")
    @XmlElement(name = "LotAttr4",required = false)
    private String lotAttr4;

    @Description("批号属性5")
    @XmlElement(name = "LotAttr5",required = false)
    private String lotAttr5;

    @Description("批号属性6")
    @XmlElement(name = "LotAttr6",required = false)
    private String lotAttr6;

    @Description("批号属性7")
    @XmlElement(name = "LotAttr7",required = false)
    private String lotAttr7;

    @Description("批号属性8")
    @XmlElement(name = "LotAttr8",required = false)
    private String lotAttr8;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getErpOrderLineNum() {
        return erpOrderLineNum;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemUom() {
        return itemUom;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemDiscount() {
        return itemDiscount;
    }

    public String getCheckPrepardNo() {
        return checkPrepardNo;
    }

    public String getCustomsPrepardNo() {
        return customsPrepardNo;
    }

    public String getHsCode() {
        return hsCode;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public String getItemSpecifications() {
        return itemSpecifications;
    }

    public String getBomAction() {
        return bomAction;
    }

    public String getIsPresent() {
        return isPresent;
    }

    public String getIsVirtualProduct() {
        return isVirtualProduct;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public String getLot() {
        return lot;
    }

    public String getNote() {
        return note;
    }

    public String getLotAttr1() {
        return lotAttr1;
    }

    public String getLotAttr2() {
        return lotAttr2;
    }

    public String getLotAttr3() {
        return lotAttr3;
    }

    public String getLotAttr4() {
        return lotAttr4;
    }

    public String getLotAttr5() {
        return lotAttr5;
    }

    public String getLotAttr6() {
        return lotAttr6;
    }

    public String getLotAttr7() {
        return lotAttr7;
    }

    public String getLotAttr8() {
        return lotAttr8;
    }

    public void setErpOrderLineNum(String erpOrderLineNum) {
        this.erpOrderLineNum = erpOrderLineNum;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemUom(String itemUom) {
        this.itemUom = itemUom;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemDiscount(String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public void setCheckPrepardNo(String checkPrepardNo) {
        this.checkPrepardNo = checkPrepardNo;
    }

    public void setCustomsPrepardNo(String customsPrepardNo) {
        this.customsPrepardNo = customsPrepardNo;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public void setItemSpecifications(String itemSpecifications) {
        this.itemSpecifications = itemSpecifications;
    }

    public void setBomAction(String bomAction) {
        this.bomAction = bomAction;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }

    public void setIsVirtualProduct(String isVirtualProduct) {
        this.isVirtualProduct = isVirtualProduct;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setLotAttr1(String lotAttr1) {
        this.lotAttr1 = lotAttr1;
    }

    public void setLotAttr2(String lotAttr2) {
        this.lotAttr2 = lotAttr2;
    }

    public void setLotAttr3(String lotAttr3) {
        this.lotAttr3 = lotAttr3;
    }

    public void setLotAttr4(String lotAttr4) {
        this.lotAttr4 = lotAttr4;
    }

    public void setLotAttr5(String lotAttr5) {
        this.lotAttr5 = lotAttr5;
    }

    public void setLotAttr6(String lotAttr6) {
        this.lotAttr6 = lotAttr6;
    }

    public void setLotAttr7(String lotAttr7) {
        this.lotAttr7 = lotAttr7;
    }

    public void setLotAttr8(String lotAttr8) {
        this.lotAttr8 = lotAttr8;
    }
}
