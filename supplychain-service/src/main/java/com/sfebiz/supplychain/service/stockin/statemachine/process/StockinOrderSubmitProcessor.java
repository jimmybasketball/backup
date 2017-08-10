package com.sfebiz.supplychain.service.stockin.statemachine.process;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.code.StockInReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.sku.enums.SkuWarehouseSyncStateType;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsOperaterType;
import com.sfebiz.supplychain.persistence.base.sku.domain.SkuWarehouseSyncDO;
import com.sfebiz.supplychain.persistence.base.sku.manager.SkuWarehouseSyncManager;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderDetailManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderStateLogManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.provider.biz.SkuSyncBizService;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuStockInCommand;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderBO;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderDetailBO;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequest;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseBO;
import com.sfebiz.supplychain.service.warehouse.model.WarehouseLogisticsProviderBO;
import net.pocrd.entity.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyajing on 2017/7/19.
 */
@Component("stockinOrderSubmitProcessor")
public class StockinOrderSubmitProcessor extends StockinAbstractProcessor{
    private final static ModelMapper modelMapper = new ModelMapper();
    private static Logger logger = LoggerFactory.getLogger(StockinOrderSubmitProcessor.class);

    @Resource
    WarehouseManager warehouseManager;
    @Resource
    StockinOrderDetailManager stockinOrderDetailManager;
    @Resource
    StockinOrderManager stockinOrderManager;
    @Resource
    StockinOrderStateLogManager stockinOrderStateLogManager;
    @Resource
    SkuWarehouseSyncManager skuWarehouseSyncManager;
    @Resource
    SkuSyncBizService skuSyncBizService;

    @Override
    public BaseResult process(StockinOrderRequest request) throws ServiceException {
        //1. 验证请求参数
        validateRequest(request);

        //2. 检测仓库是否支持入库命令
        StockinOrderDO stockinOrderDO = request.getStockinOrderDO();
        WarehouseDO warehouseDO = warehouseManager.getById(stockinOrderDO.getWarehouseId());
//        WarehouseLogisticsProviderBO logisticsProvider = new WarehouseLogisticsProviderBO();// TODO: 2017/8/9
//        if (logisticsProvider.getIntegrationBO().getIsIntegrationStockin() == 1) {
            submitStockinOrderToWarehouse(request, warehouseDO);
//        }
        //更新入库单状态日志记录
        stockinOrderStateLogManager.insertOrUpdate(stockinOrderDO.getId(), request.getOperator().getId(), request.getOperator().getName(), StockinOrderState.WAREHOUSING.getValue());


        return BaseResult.SUCCESS_RESULT;
    }

    protected void submitStockinOrderToWarehouse(StockinOrderRequest request, WarehouseDO warehouseDO) throws ServiceException {
        LogBetter.instance(logger)
                .setTraceLogger(TraceLogEntity.instance(request.getTraceLogger(), request.getTraceId(), SystemConstants.TRACE_APP))
                .setLevel(LogLevel.INFO)
                .setMsg("[物流平台-提交入库单]: 仓库的入库方式是接口入库,需要下发报文")
                .addParm("入库单ID", request.getId())
                .log();

        StockinOrderDO stockinOrderDO = request.getStockinOrderDO();
        StockinOrderDetailDO stockinOrderDetailDO = new StockinOrderDetailDO();
        stockinOrderDetailDO.setStockinOrderId(stockinOrderDO.getId());
        List<StockinOrderDetailDO> stockinOrderDetailDOs = stockinOrderDetailManager.query(BaseQuery.getInstance(stockinOrderDetailDO));
        if (null == stockinOrderDetailDOs || 0 == stockinOrderDetailDOs.size()) {
            throw new ServiceException(
                    SCReturnCode.PARAM_ILLEGAL_ERR,
                    "[物流平台-提交入库单失败]: " + SCReturnCode.PARAM_ILLEGAL_ERR.getDesc() + " "
                            + "[入库单ID: " + request.getId()
                            + "]");
        }

        //不允许商品数量为空和0
        for (StockinOrderDetailDO detailDO : stockinOrderDetailDOs) {
            if (null == detailDO.getCount() || 0 == detailDO.getCount()) {
                throw new ServiceException(
                        SCReturnCode.PARAM_ILLEGAL_ERR,
                        "[物流平台-提交入库单失败]: " + SCReturnCode.PARAM_ILLEGAL_ERR.getDesc() + " "
                                + "[入库单ID: " + request.getId()
                                + "]");
            }
        }

        ProviderCommand stockinCmd = null;
        //检查商品是否已经同步到仓库
        checkIsSkuSynToWarehouse(stockinOrderDO, warehouseDO,stockinOrderDetailDOs);
        //创建入库命令
        stockinCmd = generateWarehouseStockinCommad(stockinOrderDO, stockinOrderDetailDOs, warehouseDO);

        //执行命令
        if (stockinCmd != null) {
            stockinCmd.execute();
        }

        String stockinId = request.getStockinOrderDO().getStockinId();
        StockinOrderDO stockInorderDo = stockinOrderManager.getByStockinId(stockinId);
        if(stockInorderDo == null) {
            throw new ServiceException(StockInReturnCode.LOGISTICS_STOCKINID_ERR,"[物流平台-提交入库单] 入库单异常：" + stockinId);
        }
        stockInorderDo.setAsnSuccessTime(new Date());
        stockinOrderManager.update(stockInorderDo);
    }

    protected ProviderCommand generateWarehouseStockinCommad(StockinOrderDO stockinOrderDO, List<StockinOrderDetailDO> stockinOrderDetailDOs, WarehouseDO warehouseDO) throws ServiceException {
        ProviderCommand cmd;
        String version = "0";
        String key = "supplychain.event.wms.skustockin";// TODO: 2017/8/9  
        cmd = CommandFactory.createCommand(version, key);

        StockinOrderBO stockinOrderBO = new StockinOrderBO();
        BeanCopier beanCopier = BeanCopier.create(StockinOrderDO.class, StockinOrderBO.class, false);
        beanCopier.copy(stockinOrderBO, stockinOrderDO, null);

        WarehouseBO warehouseBO = new WarehouseBO();
        beanCopier = BeanCopier.create(WarehouseDO.class, WarehouseBO.class, false);
        beanCopier.copy(warehouseBO, warehouseDO, null);

        List<StockinOrderDetailBO> stockinOrderDetailBOs = new ArrayList<StockinOrderDetailBO>();
        for (StockinOrderDetailDO stockinOrderDetailDO : stockinOrderDetailDOs) {
            StockinOrderDetailBO stockinOrderDetailBO = new StockinOrderDetailBO();
            beanCopier = BeanCopier.create(StockinOrderDetailDO.class, StockinOrderDetailBO.class, false);
            beanCopier.copy(stockinOrderDetailBO, stockinOrderDetailDO, null);
            stockinOrderDetailBOs.add(stockinOrderDetailBO);
        }

        WmsOrderSkuStockInCommand wmsOrderSkuStockInCommand = (WmsOrderSkuStockInCommand) cmd;
        wmsOrderSkuStockInCommand.setStockinOrderDetailBOs(stockinOrderDetailBOs);
        wmsOrderSkuStockInCommand.setStockinOrderBO(stockinOrderBO);
        wmsOrderSkuStockInCommand.setWarehouseBO(warehouseBO);

        return wmsOrderSkuStockInCommand;
    }

    protected void checkIsSkuSynToWarehouse(StockinOrderDO stockinOrderDO, WarehouseDO warehouseDO, List<StockinOrderDetailDO> stockinOrderDetailDOList) throws ServiceException {
        Long warehouseId = stockinOrderDO.getWarehouseId();
        WarehouseLogisticsProviderBO logisticsProviderBO = new WarehouseLogisticsProviderBO();
        if (logisticsProviderBO.getIntegrationBO().getIsIntegrationSkuSync() == 1) {
            for (StockinOrderDetailDO detailDO : stockinOrderDetailDOList) {
                SkuWarehouseSyncDO syncDO = new SkuWarehouseSyncDO();
                syncDO.setSkuId(detailDO.getSkuId());
                syncDO.setWarehouseId(warehouseId);
                List<SkuWarehouseSyncDO> syncDOs = skuWarehouseSyncManager.query(BaseQuery.getInstance(syncDO));
                if (CollectionUtils.isEmpty(syncDOs)) {
                    List<Long> skuIds = new ArrayList<Long>();
                    skuIds.add(detailDO.getSkuId());
                    BaseResult result = skuSyncBizService.sendProductBasicInfo2WmsNotSync(skuIds, warehouseId, WmsOperaterType.ADD);
                    if (!result.isSuccess()){
                        throw new ServiceException(StockInReturnCode.LOGISTICS_STOCKINID_ERR,"[物流平台-提交入库单] 入库单异常,商品同步不成功：仓库id：" + warehouseId + ",skuId:" + detailDO.getSkuId());
                    }
                }else{
                    if (syncDOs.get(0).getSyncState().equals(SkuWarehouseSyncStateType.SYNC_FAIL.getValue())) {
                        throw new ServiceException(StockInReturnCode.LOGISTICS_STOCKINID_ERR,"[物流平台-提交入库单] 入库单异常,商品同步不成功：仓库id：" + warehouseId + ",skuId:" + detailDO.getSkuId());
                    }
                }
            }
        }
    }

}
