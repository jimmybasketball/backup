package com.sfebiz.supplychain.service.stockin.statemachine.process;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.exposed.common.code.StockInReturnCode;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.stockinorder.enums.StockinOrderState;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDO;
import com.sfebiz.supplychain.persistence.base.stockin.domain.StockinOrderDetailDO;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderDetailManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderManager;
import com.sfebiz.supplychain.persistence.base.stockin.manager.StockinOrderStateLogManager;
import com.sfebiz.supplychain.persistence.base.warehouse.domain.WarehouseDO;
import com.sfebiz.supplychain.persistence.base.warehouse.manager.WarehouseManager;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSkuStockInCommand;
import com.sfebiz.supplychain.service.stockin.modle.StockinOrderRequest;
import net.pocrd.entity.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyajing on 2017/7/19.
 */
@Component("stockinOrderSubmitProcessor")
public class StockinOrderSubmitProcessor extends StockinAbstractProcessor{

    private static Logger logger = LoggerFactory.getLogger(StockinOrderSubmitProcessor.class);

    @Resource
    WarehouseManager warehouseManager;
    @Resource
    StockinOrderDetailManager stockinOrderDetailManager;
    @Resource
    StockinOrderManager stockinOrderManager;
    @Resource
    StockinOrderStateLogManager stockinOrderStateLogManager;

    @Override
    public BaseResult process(StockinOrderRequest request) throws ServiceException {
        //1. 验证请求参数
        validateRequest(request);

        //2. 检测仓库是否支持入库命令
        StockinOrderDO stockinOrderDO = request.getStockinOrderDO();
        WarehouseDO warehouseDO = warehouseManager.getById(stockinOrderDO.getWarehouseId());
        submitStockinOrderToWarehouse(request, warehouseDO);//更新入库单状态日志记录
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
        //检查商品是否已经同步到仓库 // TODO: 2017/7/19
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
        String key = "supplychain.event.wms.skustockin";
        cmd = CommandFactory.createCommand(version, key);
        WmsOrderSkuStockInCommand wmsOrderSkuStockInCommand = (WmsOrderSkuStockInCommand) cmd;
        wmsOrderSkuStockInCommand.setStockinOrderDetailDOs(stockinOrderDetailDOs);
        wmsOrderSkuStockInCommand.setStockinOrderDO(stockinOrderDO);
        wmsOrderSkuStockInCommand.setWarehouseDO(warehouseDO);

        return wmsOrderSkuStockInCommand;
    }
}
