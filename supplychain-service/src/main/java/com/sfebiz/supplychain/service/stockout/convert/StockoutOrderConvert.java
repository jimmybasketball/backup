package com.sfebiz.supplychain.service.stockout.convert;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;

import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
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

    public static StockoutOrderDO convertStockoutOrderBOToStockoutOrderDO(StockoutOrderBO orderBO) {
        StockoutOrderDO orderDO = new StockoutOrderDO();
        BeanUtils.copyProperties(orderBO, orderDO);
        // TODO matt 运单号生成规则
        orderDO.setBizId(orderBO.getMerchantOrderNo() + "_" + orderBO.getMerchantId() + "S001");
        orderDO.setOrderType(Integer.valueOf(orderBO.getOrderType()));
        orderDO.setOrderState(StockoutOrderState.WAIT_AUDITING.value);
        return orderDO;
    }

    public static StockoutOrderBO convertDOToBO(StockoutOrderDO stockoutOrderDO) {
        StockoutOrderBO stockoutOrderBO = new StockoutOrderBO();
        BeanUtils.copyProperties(stockoutOrderDO, stockoutOrderBO);
        return stockoutOrderBO;
    }

    public static StockoutOrderDO convertBOToDO(StockoutOrderBO bo) {
        return getModelMapper().map(bo, StockoutOrderDO.class);
    }

    public static List<StockoutOrderDetailDO> convertBOToDetailDOList(StockoutOrderBO stockoutOrderBO) {
        List<StockoutOrderDetailDO> detailDOList = new ArrayList<StockoutOrderDetailDO>();
        for (StockoutOrderDetailBO detailBO : stockoutOrderBO.getDetailBOs()) {
            detailDOList.add(convertDetailBOToDetailDO(detailBO));
        }
        return detailDOList;
    }

    public static StockoutOrderDetailDO convertDetailBOToDetailDO(StockoutOrderDetailBO detailBO) {
        StockoutOrderDetailDO detailDO = new StockoutOrderDetailDO();
        BeanUtils.copyProperties(detailBO, detailDO);
        return detailDO;
    }

    public static List<StockoutOrderDetailBO> convertDetailDOsToDetailBOs(List<StockoutOrderDetailDO> detailDOs) {
        List<StockoutOrderDetailBO> detailBOs = new ArrayList<StockoutOrderDetailBO>();
        for (StockoutOrderDetailDO detailDO : detailDOs) {
            detailBOs.add(convertDetailDOToDetailBO(detailDO));
        }
        return detailBOs;
    }

    public static StockoutOrderDetailBO convertDetailDOToDetailBO(StockoutOrderDetailDO detailDO) {
        StockoutOrderDetailBO detailBO = new StockoutOrderDetailBO();
        BeanUtils.copyProperties(detailDO, detailBO);
        return detailBO;
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
        StockoutOrderBuyerBO buyerBO = new StockoutOrderBuyerBO();
        BeanUtils.copyProperties(buyerDO, buyerBO);
        return buyerBO;
    }

    public static StockoutOrderBuyerDO convertBuyerBOToDO(StockoutOrderBuyerBO buyerBO) {
        StockoutOrderBuyerDO buyerDO = new StockoutOrderBuyerDO();
        BeanUtils.copyProperties(buyerBO, buyerDO);
        return buyerDO;
    }

    public static StockoutOrderRecordBO convertRecordDOToBO(StockoutOrderRecordDO recordDO) {
        StockoutOrderRecordBO recordBO = new StockoutOrderRecordBO();
        BeanUtils.copyProperties(recordDO, recordBO);
        return recordBO;
    }

    public static StockoutOrderRecordDO convertRecordBOToDO(StockoutOrderRecordBO recordBO) {
        return getModelMapper().map(recordBO, StockoutOrderRecordDO.class);
    }

    public static StockoutOrderRecordDO buildInitRecordDO(StockoutOrderBO stockoutOrderBO) {
        StockoutOrderRecordDO recordDO = new StockoutOrderRecordDO();
        recordDO.setStockoutOrderId(stockoutOrderBO.getId());
        recordDO.setBizId(stockoutOrderBO.getBizId());

        recordDO.setPayState(0);
        recordDO.setPortState(0);
        recordDO.setTplIntlState(0);
        recordDO.setTplIntrState(0);
        recordDO.setCcbState(0);
        recordDO.setTwsState(0);
        recordDO.setWmsState(0);

        recordDO.setLogisticsState(LogisticsState.LOGISTICS_STATE_INIT.value);

        recordDO.setMerchantMailNo(stockoutOrderBO.getIntrMailNo());
        return recordDO;
    }
}
