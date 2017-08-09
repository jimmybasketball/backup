package com.sfebiz.supplychain.service.stockout.process.create;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.BillType;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.exposed.warehouse.enums.TransitWarehouseType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.tws.TwsOrderCreateCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;
import com.sfebiz.supplychain.service.warehouse.enums.TwsBusinessType;
import com.sfebiz.supplychain.util.line.LogisticsLineServiceTypeUtil;
import com.yiji.openapi.tool.util.StringUtils;

/**
 * <p>TWS订单创建</p>
 * User: zhangdi
 * Date: 16/01/16
 */
@Component("twsOrderCreateProcessor")
public class TwsOrderCreateProcessor extends StockoutProcessAction {

    public static final String     TAG    = "TWS_CREATE";
    private static final Logger    logger = LoggerFactory.getLogger(TwsOrderCreateProcessor.class);
    @Resource
    private StockoutOrderManager   stockoutOrderManager;

    @Resource
    private PortBillDeclareManager portBillDeclareManager;

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {

        PortBillDeclareDO portBillDeclareDO = null;
        BaseResult result = new BaseResult();
        try {

            if (!checkoutRequestBaseInfo(request)) {
                return new BaseResult(Boolean.FALSE);
            }

            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
            List<StockoutOrderDetailBO> detailBOs = stockoutOrderBO.getDetailBOs();
            LogisticsLineBO lineEntity = request.getLineBO();

            if (null == lineEntity.getTransitWarehouseBO()) {
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("路线id", lineEntity.getId()).addParm("仓库", lineEntity.getWarehouseBO())
                    .setMsg("无需与中转仓交互").log();
                return new BaseResult(Boolean.TRUE);
            }

            LogBetter.instance(logger).setLevel(LogLevel.INFO).addParm("路线id", lineEntity.getId())
                .addParm("WMS实体", lineEntity.getInternationalLogisticsProviderBO())
                .addParm("TWS实体", lineEntity.getTransitWarehouseBO())
                .addParm("TPL实体", lineEntity.getDomesticLogisticsProviderBO()).log();

            TransitWarehouseType transitWarehouseType = LogisticsLineServiceTypeUtil
                .getTransitWarehouseTypeByServiceType(lineEntity.getServiceType());
            String msgType = StringUtils.EMPTY;
            if (TransitWarehouseType.TRANSPORT == transitWarehouseType) {
                msgType = WmsMessageType.TWS_TRANS_ORDER_FORECAST.getValue();
            } else if (TransitWarehouseType.STOREGOODS == transitWarehouseType) {
                msgType = WmsMessageType.TWS_STORE_ORDER_FORECAST.getValue();
            } else {
                throw new IllegalArgumentException("中转仓仓库类型未知，路线ID：" + lineEntity.getId());
            }

            if (portBillDeclareDO == null) {
                createTwsBill(stockoutOrderBO, lineEntity.getPortBO());
                portBillDeclareDO = getTwsPortBillCreateDO(stockoutOrderBO, lineEntity.getPortBO());
            }
            if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
                //幂等性判断
                return new BaseResult(Boolean.TRUE);
            }
            portBillDeclareDO = getTwsPortBillCreateDO(stockoutOrderBO, lineEntity.getPortBO());
            logger.info("命令集合："
                        + lineEntity.getTransitWarehouseBO().getLogisticsProviderBO()
                            .getInterfaceType() + ",消息类型：" + msgType);

            ProviderCommand cmd = CommandFactory.createCommand(lineEntity.getTransitWarehouseBO()
                .getLogisticsProviderBO().getInterfaceType(), msgType);
            TwsOrderCreateCommand twsOrderCreateCommand = (TwsOrderCreateCommand) cmd;
            twsOrderCreateCommand.setStockoutOrderDeclarePriceBO(stockoutOrderBO
                .getDeclarePriceBO());
            twsOrderCreateCommand.setStockoutOrderBO(stockoutOrderBO);
            twsOrderCreateCommand.setStockoutOrderDetailBOs(detailBOs);
            twsOrderCreateCommand.setLineBO(lineEntity);
            twsOrderCreateCommand.setSkuDeclareBOMap(request.getProductDeclareEntityMap());
            boolean createResult = twsOrderCreateCommand.execute();
            if (createResult) {
                result.setSuccess(Boolean.TRUE);
                portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
                portBillDeclareDO.setBillSendTime(new Date());
                portBillDeclareManager.update(portBillDeclareDO);
            } else {
                request.setExceptionMessage(twsOrderCreateCommand.getCreateFailureMessage());
                result.setSuccess(Boolean.FALSE);
                portBillDeclareDO.setState(PortBillState.SEND_EXCEPTION.getValue());
                portBillDeclareDO.setBillSendTime(new Date());
                portBillDeclareDO.setNote(twsOrderCreateCommand.getCreateFailureMessage());
                portBillDeclareManager.update(portBillDeclareDO);
            }

            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("执行WMS订单创建命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-下发出库单]执行TWS订单创建命令异常，" + e.getMessage());
            portBillDeclareDO.setState(PortBillState.PARAMS_EXCEPTION.getValue());
            portBillDeclareManager.update(portBillDeclareDO);
            return new BaseResult(Boolean.FALSE);
        }
    }

    public boolean createTwsBill(StockoutOrderBO stockoutOrderBO, LogisticsPortBO portEntity)
                                                                                             throws ServiceException {
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderBO.getId());
        portBillDeclareDO.setBusinessNo(stockoutOrderBO.getBizId());
        if (portEntity != null) {
            portBillDeclareDO.setPortId(portEntity.getId());
        } else {
            portBillDeclareDO.setPortId(0L);
        }
        portBillDeclareDO.setBillType(BillType.TWS_BILL.getType());
        portBillDeclareDO.setBusinessType(TwsBusinessType.TWS_CREATE.getType());
        portBillDeclareDO.setBillName(BillType.TWS_BILL.getDescription());
        portBillDeclareDO.setDeclareType(DeclareType.CREATE.getType());
        portBillDeclareDO.setState(PortBillState.WAIT_SEND.getValue());
        portBillDeclareManager.insert(portBillDeclareDO);
        return portBillDeclareDO.getId() != null;
    }

    /**
     * 获取中转下发结果记录
     *
     * @param stockoutOrderBO
     * @return
     * @throws ServiceException
     */
    public PortBillDeclareDO getTwsPortBillCreateDO(StockoutOrderBO stockoutOrderBO,
                                                    LogisticsPortBO portEntity)
                                                                               throws ServiceException {
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderBO.getId());
        if (portEntity != null) {
            portBillDeclareDO.setPortId(portEntity.getId());
        }
        portBillDeclareDO.setBillType(BillType.TWS_BILL.getType());
        portBillDeclareDO.setBusinessType(TwsBusinessType.TWS_CREATE.getType());
        List<PortBillDeclareDO> portBillDeclareDOs = portBillDeclareManager.query(BaseQuery
            .getInstance(portBillDeclareDO));
        if (portBillDeclareDOs != null && portBillDeclareDOs.size() > 0) {
            return portBillDeclareDOs.get(0);
        }
        return null;
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }

}
