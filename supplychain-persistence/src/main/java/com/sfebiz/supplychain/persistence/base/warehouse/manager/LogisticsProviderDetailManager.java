package com.sfebiz.supplychain.persistence.base.warehouse.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.exception.DataAccessException;
import com.sfebiz.common.dao.manager.BaseManager;
import com.sfebiz.supplychain.config.lp.LogisticsProviderConfig;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.LogisticsProviderDetailDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p></p>
 * User: 心远
 * Date: 14/12/15
 * Time: 下午4:41
 */
@Component("logisticsProviderDetailManager")
public class LogisticsProviderDetailManager extends BaseManager<LogisticsProviderDetailDO> {

//    @Resource
//    private LogisticsProviderDetailDao logisticsProviderDetailDao;

    @Resource
    private WarehouseManager warehouseManager;

    public static void main(String[] args) {
        //DaoHelper.genXMLWithFeature("../logistics-persistence/src/main/resources/sqlmap/logistics-provider-detail-sqlmap.xml", LogisticsProviderDetailDao.class, LogisticsProviderDetailDO.class, "sc_logistics_providers");
    }

    @Override
//    public BaseDao<LogisticsProviderDetailDO> getDao() {
//        return logisticsProviderDetailDao;
//    }
    public BaseDao<LogisticsProviderDetailDO> getDao() {
        return null;
    }

    @Override
    public LogisticsProviderDetailDO getById(Long id) throws DataAccessException {
        String lpNid = LogisticsProviderConfig.getLpNidById(String.valueOf(id));
        if (StringUtils.isBlank(lpNid)) {
            return null;
        }
        Map<String, String> properties = LogisticsProviderConfig.getLpPropertiesByLpNid(lpNid);
        if (properties == null || properties.size() == 0) {
            return null;
        }
        LogisticsProviderDetailDO logisticsProviderDetailDO = new LogisticsProviderDetailDO();
        logisticsProviderDetailDO.setId(properties.containsKey("id") ? Long.valueOf(properties.get("id")) : 0);
        logisticsProviderDetailDO.setLogisticsProviderNid(properties.get("logistics_provider_nid"));
        logisticsProviderDetailDO.setName(properties.get("name"));
        logisticsProviderDetailDO.setType(properties.containsKey("type") ? Integer.valueOf(properties.get("type")) : 0);
        logisticsProviderDetailDO.setInterfaceType(Integer.valueOf(properties.get("interface_type")));
        logisticsProviderDetailDO.setInterfaceUrl(properties.get("interface_url"));
        logisticsProviderDetailDO.setInterfaceKey(properties.get("interface_key"));
        logisticsProviderDetailDO.setFtpUser(properties.get("ftp_user"));
        logisticsProviderDetailDO.setFtpPassword(properties.get("ftp_password"));
        logisticsProviderDetailDO.setFilePath(properties.get("file_path"));
        logisticsProviderDetailDO.setProxyUrl(properties.get("proxy_url"));
        logisticsProviderDetailDO.setProxyPort(properties.get("proxy_port"));
        logisticsProviderDetailDO.setCustId(properties.get("cust_id"));
        logisticsProviderDetailDO.setPayMethod(properties.get("pay_method"));
        logisticsProviderDetailDO.setMeta(properties.get("meta"));
        logisticsProviderDetailDO.setCode(properties.get("code"));
        return logisticsProviderDetailDO;
    }
//
//    /**
//     * 根据物流供应商ID 获取物流供应商实体Entity
//     *
//     * @param providerId
//     * @return
//     */
//    public LogisticsProviderEntity getProviderEntityById(long providerId) {
//        String lpNid = LogisticsProviderConfig.getLpNidById(String.valueOf(providerId));
//        if (StringUtils.isBlank(lpNid)) {
//            return null;
//        }
//        Map<String, String> properties = LogisticsProviderConfig.getLpPropertiesByLpNid(lpNid);
//        if (properties == null || properties.size() == 0) {
//            return null;
//        }
//        LogisticsProviderEntity entity = new LogisticsProviderEntity();
//        entity.logisticsProviderId = properties.containsKey("id") ? Long.valueOf(properties.get("id")) : 0;
//        entity.logisticsProviderNid = properties.get("logistics_provider_nid");
//        entity.name = properties.get("name");
//        entity.type = properties.containsKey("type") ? Integer.valueOf(properties.get("type")) : 0;
//        entity.interfaceType = properties.get("interface_type");
//        entity.interfaceUrl = properties.get("interface_url");
//        entity.interfaceKey = properties.get("interface_key");
//        entity.custId = properties.get("cust_id");
//        entity.code = properties.get("code");
//        entity.payMethod = properties.get("pay_method");
//        entity.meta = properties.get("meta");
//        return entity;
//    }
//
//    /**
//     * 根据供应商NID 获取供应商实体Entity
//     *
//     * @param providerNId
//     * @return
//     */
//    public LogisticsProviderEntity getProviderEntityByproviderNId(String providerNId) {
//        Map<String, String> properties = LogisticsProviderConfig.getLpPropertiesByLpNid(providerNId);
//        if (properties == null || properties.size() == 0) {
//            return null;
//        }
//        LogisticsProviderEntity entity = new LogisticsProviderEntity();
//        entity.logisticsProviderId = properties.containsKey("id") ? Long.valueOf(properties.get("id")) : 0;
//        entity.logisticsProviderNid = properties.get("logistics_provider_nid");
//        entity.name = properties.get("name");
//        entity.type = properties.containsKey("type") ? Integer.valueOf(properties.get("type")) : 0;
//        entity.interfaceType = properties.get("interface_type");
//        entity.interfaceUrl = properties.get("interface_url");
//        entity.interfaceKey = properties.get("interface_key");
//        entity.custId = properties.get("cust_id");
//        entity.code = properties.get("code");
//        entity.payMethod = properties.get("pay_method");
//        entity.meta = properties.get("meta");
//        return entity;
//    }
//
//    /**
//     * 根据仓库ID 获取仓库的物流供应商信息
//     *
//     * @param warehouse
//     * @return
//     */
//    public LogisticsProviderEntity getProviderEntityByWarehouseId(long warehouse) {
//        WarehouseDO warehouseDO = warehouseManager.getById(warehouse);
//        if (warehouseDO == null || warehouseDO.getLogisticsProviderId() == null) {
//            return null;
//        }
//        return getProviderEntityById(warehouseDO.getLogisticsProviderId());
//    }
//
//    /**
//     * 根据仓库NID 获取仓库的物流供应商信息
//     *
//     * @param warehouseNid
//     * @return
//     */
//    public LogisticsProviderEntity getProviderEntityByWarehouseNId(String warehouseNid) {
//        WarehouseDO warehouseDO = warehouseManager.getByNid(warehouseNid);
//        if (warehouseDO == null || warehouseDO.getLogisticsProviderId() == null) {
//            return null;
//        }
//        return getProviderEntityById(warehouseDO.getLogisticsProviderId());
//    }
//
//    /**
//     * 从 LogisticsProviderEntity 转换为    LogisticsProviderDetailDO
//     *
//     * @param logisticsProviderEntity
//     * @return
//     */
//    public LogisticsProviderDetailDO convertLogisticsProviderEntity2DO(LogisticsProviderEntity logisticsProviderEntity) {
//        if (logisticsProviderEntity == null) {
//            return null;
//        }
//        LogisticsProviderDetailDO logisticsProviderDetailDO = new LogisticsProviderDetailDO();
//        logisticsProviderDetailDO.setId(logisticsProviderEntity.logisticsProviderId);
//        logisticsProviderDetailDO.setLogisticsProviderNid(logisticsProviderEntity.logisticsProviderNid);
//        logisticsProviderDetailDO.setName(logisticsProviderEntity.name);
//        logisticsProviderDetailDO.setType(logisticsProviderEntity.type);
//        logisticsProviderDetailDO.setInterfaceType(Integer.valueOf(logisticsProviderEntity.interfaceType));
//        logisticsProviderDetailDO.setInterfaceUrl(logisticsProviderEntity.interfaceUrl);
//        logisticsProviderDetailDO.setInterfaceKey(logisticsProviderEntity.interfaceKey);
//        logisticsProviderDetailDO.setCustId(logisticsProviderEntity.custId);
//        logisticsProviderDetailDO.setPayMethod(logisticsProviderEntity.payMethod);
//        logisticsProviderDetailDO.setMeta(logisticsProviderEntity.meta);
//        return logisticsProviderDetailDO;
//    }

    // ================================================================
    public LogisticsProviderDetailDO getByWarehouseNidFromDiamond(String warehouseNid) throws DataAccessException {
        String lpNid = LogisticsProviderConfig.getLpNidById(warehouseNid);
        if (StringUtils.isBlank(lpNid)) {
            return null;
        }
        Map<String, String> properties = LogisticsProviderConfig.getLpPropertiesByLpNid(lpNid);
        if (properties == null || properties.size() == 0) {
            return null;
        }
        LogisticsProviderDetailDO logisticsProviderDetailDO = new LogisticsProviderDetailDO();
        logisticsProviderDetailDO.setId(properties.containsKey("id") ? Long.valueOf(properties.get("id")) : 0);
        logisticsProviderDetailDO.setLogisticsProviderNid(properties.get("logistics_provider_nid"));
        logisticsProviderDetailDO.setName(properties.get("name"));
        logisticsProviderDetailDO.setType(properties.containsKey("type") ? Integer.valueOf(properties.get("type")) : 0);
        logisticsProviderDetailDO.setInterfaceType(Integer.valueOf(properties.get("interface_type")));
        logisticsProviderDetailDO.setInterfaceUrl(properties.get("interface_url"));
        logisticsProviderDetailDO.setInterfaceKey(properties.get("interface_key"));
        logisticsProviderDetailDO.setFtpUser(properties.get("ftp_user"));
        logisticsProviderDetailDO.setFtpPassword(properties.get("ftp_password"));
        logisticsProviderDetailDO.setFilePath(properties.get("file_path"));
        logisticsProviderDetailDO.setProxyUrl(properties.get("proxy_url"));
        logisticsProviderDetailDO.setProxyPort(properties.get("proxy_port"));
        logisticsProviderDetailDO.setCustId(properties.get("cust_id"));
        logisticsProviderDetailDO.setPayMethod(properties.get("pay_method"));
        logisticsProviderDetailDO.setMeta(properties.get("meta"));
        logisticsProviderDetailDO.setCode(properties.get("code"));
        return logisticsProviderDetailDO;
    }
}
