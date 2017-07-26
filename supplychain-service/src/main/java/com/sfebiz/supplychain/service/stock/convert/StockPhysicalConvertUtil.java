package com.sfebiz.supplychain.service.stock.convert;

import com.sfebiz.supplychain.exposed.stock.entity.StockPhysicalEntity;
import com.sfebiz.supplychain.persistence.base.stock.domain.StockPhysicalDO;
import org.modelmapper.ModelMapper;

/**
 * @author yangh [yanghua@ifunq.com]
 * @description: 实物库存实体转换工具
 * @date 2017-07-24 15:24
 **/
public class StockPhysicalConvertUtil {
    private final static ModelMapper modelMapper = new ModelMapper();

    public static StockPhysicalEntity convertToStockPhysicalEntity(StockPhysicalDO stockPhysicalDO) {
        if (stockPhysicalDO == null) {
            return null;
        }
        StockPhysicalEntity stockPhysicalEntity = modelMapper.map(stockPhysicalDO, StockPhysicalEntity.class);
        return stockPhysicalEntity;
    }
}
