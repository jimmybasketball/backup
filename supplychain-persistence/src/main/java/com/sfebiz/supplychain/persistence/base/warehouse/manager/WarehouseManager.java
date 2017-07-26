package com.sfebiz.supplychain.persistence.base.warehouse.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.UpdateByQuery;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.persistence.base.warehouse.dao.WarehouseDao;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;

/**
 * 
 * <p>
 * 仓库管理类
 * </p>
 * 
 * @author matt
 * @Date 2017年7月7日 上午10:44:58
 */
@Component("warehouseManager")
public class WarehouseManager extends BaseManager<WarehouseDO> {

    @Resource
    private WarehouseDao warehouseDao;

    @Override
    public BaseDao<WarehouseDO> getDao() {
	return warehouseDao;
    }

    /**
     * 根据仓库的Nid查询仓库对象
     * 
     * @param nid
     * @return
     */
    public WarehouseDO getByNid(String nid) {
	WarehouseDO d = new WarehouseDO();
	d.setWarehouseNid(nid);
	BaseQuery<WarehouseDO> q = BaseQuery.getInstance(d);
	List<WarehouseDO> list = this.query(q);
	if (list != null && list.size() > 0) {
	    return list.get(0);
	}
	return null;
    }

    public Map<Long, WarehouseDO> getAllWarehouseMap() {
	Map<Long, WarehouseDO> map = new HashMap<Long, WarehouseDO>();
	WarehouseDO lineDO = new WarehouseDO();
	BaseQuery<WarehouseDO> ql = BaseQuery.getInstance(lineDO);
	List<WarehouseDO> lines = warehouseDao.query(ql);
	if (CollectionUtils.isNotEmpty(lines)) {
	    for (WarehouseDO WarehouseDO : lines) {
		map.put(WarehouseDO.getId(), WarehouseDO);
	    }
	}
	return map;
    }

    public int updateCooperationState(Long warehouseId, String cooperationState) {
	WarehouseDO queryDO = new WarehouseDO();
	queryDO.setId(warehouseId);
	WarehouseDO updateDO = new WarehouseDO();
	updateDO.setCooperationState(cooperationState);
	BaseQuery<WarehouseDO> baseQuery = BaseQuery.getInstance(queryDO);
	UpdateByQuery<WarehouseDO> updateByQuery = new UpdateByQuery<WarehouseDO>(
		baseQuery, updateDO);
	return this.updateByQuery(updateByQuery);
    }

    public int updateWarehouseState(Long warehouseId, String warehouseState) {
	WarehouseDO queryDO = new WarehouseDO();
	queryDO.setId(warehouseId);
	WarehouseDO updateDO = new WarehouseDO();
	updateDO.setWarehouseState(warehouseState);
	BaseQuery<WarehouseDO> baseQuery = BaseQuery.getInstance(queryDO);
	UpdateByQuery<WarehouseDO> updateByQuery = new UpdateByQuery<WarehouseDO>(
		baseQuery, updateDO);
	return this.updateByQuery(updateByQuery);
    }

    public static void main(String[] args) {
	DaoHelper.genXMLWithFeature("C:/sc_warehouse-sqlmap.xml",
		WarehouseDao.class, WarehouseDO.class, "sc_warehouse");
    }
}
