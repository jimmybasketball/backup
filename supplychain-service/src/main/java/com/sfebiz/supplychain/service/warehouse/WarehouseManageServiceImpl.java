package com.sfebiz.supplychain.service.warehouse;

import com.sfebiz.supplychain.aop.annotation.MethodParamValidate;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.exposed.common.entity.CommonRet;
import com.sfebiz.supplychain.exposed.warehouse.api.WarehouseManageService;
import com.sfebiz.supplychain.exposed.warehouse.entity.CreateWarehouseReq;
import com.sfebiz.supplychain.exposed.warehouse.entity.EditWarehouseReq;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.service.warehouse.convert.WarehouseConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("warehouseManageService")
public class WarehouseManageServiceImpl implements WarehouseManageService {

    @Resource
    private WarehouseManager warehouseManager;

    @Override
    @MethodParamValidate
    public CommonRet<Long> createWarehouse(
            @ParamNotBlank("请求不能为空") CreateWarehouseReq req) {

        CommonRet<Long> result = new CommonRet<Long>();
        try {
            // 1. 业务校验
            WarehouseDO existWarehouse = warehouseManager.getByNid(req
                    .getWarehouseReqItem().getWarehouseNid());
            if (existWarehouse != null) {
                result.setRetMsg("仓库创建失败，此自定义仓库编码已经存在，请更换！");
                return result;
            }

            // 2. 转换为DO对象
            WarehouseDO warehouseDO = WarehouseConvert
                    .convertWarehouseVOToWarehouseDO(req.getWarehouseReqItem());

            // 3. 保存仓库数据
            warehouseManager.insert(warehouseDO);

            // 4. 设置返回结果
            result.setResult(warehouseDO.getId());

        } catch (Exception e) {
            result.setRetMsg("创建仓库异常！");
        }

        return result;
    }

    @Override
    public CommonRet<Void> editWarehouse(EditWarehouseReq req) {
        CommonRet<Void> result = new CommonRet<Void>();
        try {
            // 1. 业务校验
            WarehouseDO existWarehouse = warehouseManager.getByIdForUpdate(req
                    .getWarehouseReqItem().getId());
            if (existWarehouse == null) {
                result.setRetMsg("仓库修改失败，仓库不存在！");
                return result;
            }

            // 2. 转换为DO对象
            WarehouseDO warehouseDO = WarehouseConvert
                    .convertWarehouseVOToWarehouseDO(req.getWarehouseReqItem());

            // 3. 保存仓库数据
            warehouseManager.update(warehouseDO);

            // 4. 设置返回结果
            result.setRetMsg("修改仓库成功！");

        } catch (Exception e) {
            result.setRetMsg("修改仓库异常！");
        }

        return result;
    }

    @Override
    public CommonRet<Void> deleteWarehouse(long warehouseId) {
        CommonRet<Void> result = new CommonRet<Void>();
        try {
            // 1. 业务校验
            WarehouseDO existWarehouse = warehouseManager
                    .getByIdForUpdate(warehouseId);
            if (existWarehouse == null) {
                result.setRetMsg("仓库删除失败，仓库不存在！");
                return result;
            }

            // 2. 保存仓库数据
            warehouseManager.deleteById(warehouseId);

            // 3. 设置返回结果
            result.setRetMsg("删除仓库成功！");

        } catch (Exception e) {
            result.setRetMsg("删除仓库异常！");
        }

        return result;
    }

    @Override
    public CommonRet<Void> changeCooperationState(long warehouseId,
                                                  String cooperationState) {
        CommonRet<Void> result = new CommonRet<Void>();
        try {
            // 1. 业务校验
            WarehouseDO existWarehouse = warehouseManager
                    .getByIdForUpdate(warehouseId);
            if (existWarehouse == null) {
                result.setRetMsg("更改失败，仓库不存在！");
                return result;
            }

            // 2. 保存仓库数据
            warehouseManager.updateCooperationState(warehouseId,
                    cooperationState);

            // 3. 设置返回结果
            result.setRetMsg("更改仓库合作状态成功！");

        } catch (Exception e) {
            result.setRetMsg("更改仓库合作状态异常！");
        }

        return result;
    }

    @Override
    public CommonRet<Void> changeWarehouseState(long warehouseId,
                                                String warehouseState) {
        CommonRet<Void> result = new CommonRet<Void>();
        try {
            // 1. 业务校验
            WarehouseDO existWarehouse = warehouseManager
                    .getByIdForUpdate(warehouseId);
            if (existWarehouse == null) {
                result.setRetMsg("更改失败，仓库不存在！");
                return result;
            }

            // 2. 保存仓库数据
            warehouseManager.updateWarehouseState(warehouseId, warehouseState);

            // 3. 设置返回结果
            result.setRetMsg("更改仓库状态成功！");

        } catch (Exception e) {
            result.setRetMsg("更改仓库状态异常！");
        }

        return result;
    }

}
