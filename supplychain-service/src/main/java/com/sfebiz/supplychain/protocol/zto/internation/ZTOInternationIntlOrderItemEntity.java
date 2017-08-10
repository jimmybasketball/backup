package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午4:39
 */
public class ZTOInternationIntlOrderItemEntity implements Serializable {

    private static final long serialVersionUID = -2328439149569903394L;

    /**
     * 商品 ID
     */
    private String itemId;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品价格
     */
    private BigDecimal itemUnitPrice;

    /**
     * 商品购买数量(内容不可为 0)
     */
    private Integer itemQuantity;

    /**
     * 货币类型(CNY)
     */
    private String currencyType;

    /**
     * 计量单位(如:个,件,台,双)
     */
    private String itemUnit;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(BigDecimal itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }
}
