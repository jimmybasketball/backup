package com.sfebiz.supplychain.service.warehouse.convert;

import java.util.Date;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.warehouse.entity.WarehouseReqItem;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.util.DateUtil;

/**
 * 
 * <p>
 * 仓库实体转换类
 * </p>
 * 
 * @author matt
 * @Date 2017年7月12日 下午6:05:20
 */
public class WarehouseConvert {

    /** 日志 */
    private static final Logger logger        = LoggerFactory.getLogger(WarehouseConvert.class);

    private static ModelMapper  vdModelMapper = null;

    /**
     * VO至DO对象
     * 
     * @return
     */
    private static ModelMapper getVDModelMapper() {
        if (vdModelMapper == null) {
            vdModelMapper = new ModelMapper();
            vdModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            final Converter<String, Date> strToDateConverter = new AbstractConverter<String, Date>() {
                @Override
                protected Date convert(String source) {
                    return source == null ? null : DateUtil.parseDate(source, DateUtil.DEF_PATTERN);
                }
            };
            vdModelMapper.addConverter(strToDateConverter);

            PropertyMap<WarehouseReqItem, WarehouseDO> propertyMap = new PropertyMap<WarehouseReqItem, WarehouseDO>() {
                protected void configure() {
                    using(strToDateConverter).map(source.getContractPeriodStart())
                        .setContractPeriodStart(null);
                    using(strToDateConverter).map(source.getContractPeriodEnd())
                        .setContractPeriodEnd(null);
                }
            };
            vdModelMapper.addMappings(propertyMap);

            vdModelMapper.validate();
        }
        return vdModelMapper;
    }

    /**
     * 转换仓库信息VO对象为DO对象
     * 
     * @param item
     * @return
     */
    public static WarehouseDO convertWarehouseVOToWarehouseDO(WarehouseReqItem item) {
        WarehouseDO warehouseDO = getVDModelMapper().map(item, WarehouseDO.class);
        return warehouseDO;
    }

}
