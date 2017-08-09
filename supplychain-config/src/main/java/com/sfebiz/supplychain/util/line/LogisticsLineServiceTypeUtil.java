package com.sfebiz.supplychain.util.line;

import com.sfebiz.supplychain.exposed.line.enums.LogisticsLineServiceType;
import com.sfebiz.supplychain.exposed.warehouse.enums.TransitWarehouseType;

public class LogisticsLineServiceTypeUtil {

    /**
     * 根据服务类型获取集货仓的使用类型
     * 
     * @param serviceType
     * @return
     */
    public static TransitWarehouseType getTransitWarehouseTypeByServiceType(LogisticsLineServiceType serviceType) {
        if (serviceType == LogisticsLineServiceType.FENG_JI_USPS_B
            || serviceType == LogisticsLineServiceType.FENG_JI_USPS_C) {
            return TransitWarehouseType.TRANSPORT;
        } else {
            return TransitWarehouseType.STOREGOODS;
        }
    }
}
