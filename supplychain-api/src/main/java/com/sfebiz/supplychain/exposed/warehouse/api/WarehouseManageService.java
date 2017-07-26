package com.sfebiz.supplychain.exposed.warehouse.api;

import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.warehouse.entity.CreateWarehouseReq;
import com.sfebiz.supplychain.exposed.warehouse.entity.EditWarehouseReq;

/**
 * 
 * <p>
 * 仓库管理服务
 * </p>
 * 
 * @author matt
 * @Date 2017年7月12日 下午6:03:48
 */
public interface WarehouseManageService {

    /**
     * 创建仓库
     * 
     * @param req
     *            创建请求
     * @return 创建成功的仓库Id
     */
    public CommonRet<Long> createWarehouse(CreateWarehouseReq req);

    /**
     * 修改仓库
     * 
     * @param req
     * @return
     */
    public CommonRet<Void> editWarehouse(EditWarehouseReq req);

    /**
     * 删除仓库
     * 
     * @param warehouseId
     * @return
     */
    public CommonRet<Void> deleteWarehouse(long warehouseId);

    /**
     * 更改仓库合作状态
     * 
     * @param warehouseId
     * @param cooperationState
     * @return
     */
    public CommonRet<Void> changeCooperationState(long warehouseId,
	    String cooperationState);

    /**
     * 更改仓库状态
     * 
     * @param warehouseId
     * @param warehouseState
     * @return
     */
    public CommonRet<Void> changeWarehouseState(long warehouseId,
	    String warehouseState);
}
