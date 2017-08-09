package com.sfebiz.supplychain.service.stockout.convert;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import com.sfebiz.supplychain.open.exposed.wms.entity.request.OpenWmsTradeOrderCreateRequest;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderBuyerDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDeclarePriceDetailDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDetailDO;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderRecordDO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBuyerBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDeclarePriceDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderRecordBO;
import com.sfebiz.supplychain.service.stockout.enums.LogisticsState;

public class StockoutOrderConvert {

    private static ModelMapper vdModelMapper = null;

    /**
     * VO至DO对象
     * 
     * @return
     */
    private static ModelMapper getModelMapper() {
        if (vdModelMapper == null) {
            vdModelMapper = new ModelMapper();
            vdModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            vdModelMapper.validate();
        }
        return vdModelMapper;
    }

    /**
     * 出库单创建开放请求对象构建出库单业务实体
     * 
     * @param req 出库单创建开放请求
     * @return 出库单业务实体
     */
    public static StockoutOrderBO buildStockoutOrderBOByOpenOrderCreateReq(OpenWmsTradeOrderCreateRequest req) {
        StockoutOrderBO orderEntity = new StockoutOrderBO();
        // TODO
        return orderEntity;
    }

    public static StockoutOrderDO convertStockoutOrderBOToStockoutOrderDO(StockoutOrderBO bo) {
        return getModelMapper().map(bo, StockoutOrderDO.class);
    }

    public static StockoutOrderBO convertDOToBO(StockoutOrderDO stockoutOrderDO) {
        return getModelMapper().map(stockoutOrderDO, StockoutOrderBO.class);
    }

    public static StockoutOrderDO convertBOToDO(StockoutOrderBO bo) {
        return getModelMapper().map(bo, StockoutOrderDO.class);
    }

    public static List<StockoutOrderDetailDO> convertBOToDetailDOList(StockoutOrderBO stockoutOrderBO) {
        List<StockoutOrderDetailDO> detailDOList = new ArrayList<StockoutOrderDetailDO>();
        for (StockoutOrderDetailBO bo : stockoutOrderBO.getDetailBOs()) {
            detailDOList.add(convertDetailBOToDetailDO(bo));
        }
        return detailDOList;
    }

    public static StockoutOrderDetailDO convertDetailBOToDetailDO(StockoutOrderDetailBO bo) {
        return getModelMapper().map(bo, StockoutOrderDetailDO.class);
    }

    public static StockoutOrderDeclarePriceDO convertDeclarePriceBOToDeclarePriceDO(StockoutOrderDeclarePriceBO bo) {
        return getModelMapper().map(bo, StockoutOrderDeclarePriceDO.class);
    }

    public static StockoutOrderDeclarePriceBO convertDeclarePriceDOToDeclarePriceBO(StockoutOrderDeclarePriceDO bo) {
        return getModelMapper().map(bo, StockoutOrderDeclarePriceBO.class);
    }

    public static StockoutOrderDeclarePriceDetailDO convertDeclarePriceDetailBOToDeclarePriceDetailDO(StockoutOrderDeclarePriceDetailBO bo) {
        return getModelMapper().map(bo, StockoutOrderDeclarePriceDetailDO.class);
    }

    public static StockoutOrderBuyerBO convertBuyerDOToBO(StockoutOrderBuyerDO buyerDO) {
        return getModelMapper().map(buyerDO, StockoutOrderBuyerBO.class);
    }

    public static StockoutOrderBuyerDO convertBuyerBOToDO(StockoutOrderBuyerBO buyerBO) {
        return getModelMapper().map(buyerBO, StockoutOrderBuyerDO.class);
    }

    public static StockoutOrderRecordBO convertRecordDOToBO(StockoutOrderRecordDO recordDO) {
        return getModelMapper().map(recordDO, StockoutOrderRecordBO.class);
    }

    public static StockoutOrderRecordDO convertRecordBOToDO(StockoutOrderRecordBO recordBO) {
        return getModelMapper().map(recordBO, StockoutOrderRecordDO.class);
    }

    public static StockoutOrderRecordDO buildInitRecordDO(StockoutOrderBO stockoutOrderBO) {
        StockoutOrderRecordDO recordDO = new StockoutOrderRecordDO();
        recordDO.setStockoutOrderId(stockoutOrderBO.getId());
        recordDO.setBizId(stockoutOrderBO.getBizId());
        recordDO.setLogisticsState(LogisticsState.LOGISTICS_STATE_INIT.value);
        recordDO.setMerchantMailNo(stockoutOrderBO.getIntrMailNo());
        return recordDO;
    }
}
