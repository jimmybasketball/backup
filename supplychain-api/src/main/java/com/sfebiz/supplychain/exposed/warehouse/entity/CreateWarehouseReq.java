package com.sfebiz.supplychain.exposed.warehouse.entity;

import net.sf.oval.constraint.NotNull;

import com.sfebiz.supplychain.exposed.common.entity.BaseRequest;

/**
 * 
 * <p>
 * 仓库创建请求
 * </p>
 * 
 * @author matt
 * @Date 2017年7月12日 下午6:06:27
 */
public class CreateWarehouseReq extends BaseRequest {

    /** 序号 */
    private static final long serialVersionUID = -474966966117119742L;

    /** 仓库相关信息 */
    @NotNull(message = "仓库信息不能为空")
    private WarehouseReqItem warehouseReqItem;

    /** 操作人 */
    @NotNull(message = "操作人不能为空")
    private String operator;

    public WarehouseReqItem getWarehouseReqItem() {
	return warehouseReqItem;
    }

    public void setWarehouseReqItem(WarehouseReqItem warehouseReqItem) {
	this.warehouseReqItem = warehouseReqItem;
    }

    public String getOperator() {
	return operator;
    }

    public void setOperator(String operator) {
	this.operator = operator;
    }

}
