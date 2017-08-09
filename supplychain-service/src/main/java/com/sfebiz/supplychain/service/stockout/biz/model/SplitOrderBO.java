package com.sfebiz.supplychain.service.stockout.biz.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <p>拆分后的业务实体</p>
 * 
 * @author matt
 * @Date 2017年7月25日 上午6:45:56
 */
public class SplitOrderBO implements Serializable {

    /** 序号 */
    private static final long           serialVersionUID = 1326804358130250437L;

    // 路线信息
    private Long                        lineId;

    // 仓库id
    private Long                        warehouseId;

    // 仓库名称
    private String                      warehouseName;

    // 线路时效
    private String                      timeLimit;

    // 包含的商品信息
    private List<StockoutOrderDetailBO> skuStockOperaterEntities;

    // 包含的skuId信息
    private List<Long>                  skuIdList;

    // 包裹对应的出库单ID
    private Long                        stockoutOrderId;

    // 包裹对应的子订单ID
    private String                      bizId;

    // 口岸昵称
    private String                      portNid;

    // 承运商编码：SF 或 ETK
    private String                      carrierCode;

    // 口岸Id
    private Long                        portId;

    // 线路模式
    private String                      lineType;

    // 商品运费
    private int                         shippingFee;

    // 单包裹金额上限
    private int                         packageLimitAmount;

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public List<StockoutOrderDetailBO> getSkuStockOperaterEntities() {
        return skuStockOperaterEntities;
    }

    public void setSkuStockOperaterEntities(List<StockoutOrderDetailBO> skuStockOperaterEntities) {
        this.skuStockOperaterEntities = skuStockOperaterEntities;
    }

    public List<Long> getSkuIdList() {
        return skuIdList;
    }

    public void setSkuIdList(List<Long> skuIdList) {
        this.skuIdList = skuIdList;
    }

    public Long getStockoutOrderId() {
        return stockoutOrderId;
    }

    public void setStockoutOrderId(Long stockoutOrderId) {
        this.stockoutOrderId = stockoutOrderId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getPortNid() {
        return portNid;
    }

    public void setPortNid(String portNid) {
        this.portNid = portNid;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getPackageLimitAmount() {
        return packageLimitAmount;
    }

    public void setPackageLimitAmount(int packageLimitAmount) {
        this.packageLimitAmount = packageLimitAmount;
    }

}
