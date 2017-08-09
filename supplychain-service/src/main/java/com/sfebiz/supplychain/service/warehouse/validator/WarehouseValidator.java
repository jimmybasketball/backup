package com.sfebiz.supplychain.service.warehouse.validator;

import java.util.List;

import javax.annotation.Resource;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import org.springframework.stereotype.Component;

import com.sfebiz.supplychain.exposed.warehouse.entity.CreateWarehouseReq;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;

@Component("warehouseValidator")
public class WarehouseValidator {

    @Resource
    private WarehouseManager warehouseManager;

    public void verifyWarehouseCreate(CreateWarehouseReq req) {

	try {
	    // 1. 基础参数校验
	    Validator validator = new Validator();
	    List<ConstraintViolation> ret = validator.validate(req);
	    if (ret.size() > 0) {

	    }

	    // 2. 业务校验
	    if (null != warehouseManager.getByNid(req.getWarehouseReqItem()
		    .getWarehouseNid())) {

	    }

	} catch (Exception e) {
	    // TODO: handle exception
	}

    }

    public static void main(String[] args) {

    }
}
