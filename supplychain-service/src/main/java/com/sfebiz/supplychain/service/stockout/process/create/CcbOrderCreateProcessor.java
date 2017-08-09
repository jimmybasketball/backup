package com.sfebiz.supplychain.service.stockout.process.create;

import java.util.List;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.BillType;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.ccb.CcbOrderCreateCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.port.model.CustomsBusinessType;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * 
 * <p>清关企业订单创建处理器</p>
 * 
 * @author matt
 * @Date 2017年7月28日 下午6:00:16
 */
@Component("ccbOrderCreateProcessor")
public class CcbOrderCreateProcessor extends StockoutProcessAction {

    public static final String     TAG    = "CCB_CREATE";
    private static final Logger    logger = LoggerFactory.getLogger(CcbOrderCreateProcessor.class);
    @Resource
    private StockoutOrderManager   stockoutOrderManager;

    @Resource
    private PortBillDeclareManager portBillDeclareManager;

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {

        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
        try {
            BaseResult result = new BaseResult();
            boolean isMockAutoCreated = false;
            if (isMockAutoCreated) {
                return new BaseResult(Boolean.TRUE);
            }

            if (!checkoutRequestBaseInfo(request)) {
                return new BaseResult(Boolean.FALSE);
            }

            LogisticsLineBO lineEntity = request.getLineBO();
            List<StockoutOrderDetailBO> detailBOs = stockoutOrderBO.getDetailBOs();

            //如果不走口岸，则不需要跟清关企业进行交互
            if (lineEntity.getClearanceLogisticsProviderBO() == null) {
                return new BaseResult(Boolean.TRUE);
            }

            LogBetter.instance(logger).setLevel(LogLevel.INFO).addParm("路线id", lineEntity.getId())
                .addParm("CCB实体", lineEntity.getClearanceLogisticsProviderBO()).log();

            // 只有销售出库单才需要与关务交互
            if (StockoutOrderType.SALES_STOCK_OUT.getValue() != stockoutOrderBO.getOrderType()) {
                return new BaseResult(Boolean.TRUE);
            }
            //如果清关供应商命令集合未配置，则直接跳过与清关供应商的交互
            if (StringUtils
                .isBlank(lineEntity.getClearanceLogisticsProviderBO().getInterfaceType())
                || "-1".equals(lineEntity.getClearanceLogisticsProviderBO().getInterfaceType())) {
                return new BaseResult(Boolean.TRUE);
            }

            if (StringUtils.isBlank(lineEntity.getClearanceLogisticsProviderBO()
                .getLogisticsProviderNid())) {
                throw new IllegalArgumentException("清关供应商NID不能为空，"
                                                   + lineEntity.getClearanceLogisticsProviderBO()
                                                       .getId());
            }

            if (lineEntity.getClearanceLogisticsProviderBO().getLogisticsProviderNid()
                .startsWith("bsp")) {
                //如果清关供应商为BSP，则直接跳过，海淘未与BSP的关务做系统对接
                return new BaseResult(Boolean.TRUE);
            }

            String msgType = WmsMessageType.CCB_ORDER_CREATE.getValue();
            PortBillDeclareDO portBillDeclareDO = getPersonalGoodsBillCreateDO(stockoutOrderBO,
                lineEntity.getPortBO());
            if (portBillDeclareDO == null) {
                createPersonalGoodsBill(stockoutOrderBO, lineEntity.getPortBO());
                portBillDeclareDO = getPersonalGoodsBillCreateDO(stockoutOrderBO,
                    lineEntity.getPortBO());
            }

            if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
                //幂等性判断
                return new BaseResult(Boolean.TRUE);
            }

            ProviderCommand cmd = CommandFactory.createCommand(lineEntity
                .getClearanceLogisticsProviderBO().getInterfaceType(), msgType);
            CcbOrderCreateCommand ccbOrderCreateCommand = (CcbOrderCreateCommand) cmd;
            ccbOrderCreateCommand.setStockoutOrderDeclarePriceBO(stockoutOrderBO
                .getDeclarePriceBO());
            ccbOrderCreateCommand.setStockoutOrderBO(stockoutOrderBO);
            ccbOrderCreateCommand.setStockoutOrderDetailBOs(detailBOs);
            ccbOrderCreateCommand.setLineBO(lineEntity);
            ccbOrderCreateCommand.setSkuDeclareBOMap(request.getProductDeclareEntityMap());
            ccbOrderCreateCommand.setPortBillDeclareDO(portBillDeclareDO);
            boolean createResult = ccbOrderCreateCommand.execute();
            if (createResult) {
                result.setSuccess(Boolean.TRUE);
            } else {
                request.setExceptionMessage(ccbOrderCreateCommand.getCreateFailureMessage());
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.CCB_ORDER_CREATE_ERROR,
                    LogisticsReturnCode.CCB_ORDER_CREATE_ERROR.getDesc()));
                result.setSuccess(Boolean.FALSE);
            }
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("执行CCB订单创建命令异常")
                .setException(e).setParms(stockoutOrderBO.getBizId()).log();
            request.setExceptionMessage("[供应链-下发出库单]执行CCB订单创建命令异常，" + e.getMessage());
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.CCB_ORDER_CREATE_ERROR,
                LogisticsReturnCode.CCB_ORDER_CREATE_ERROR.getDesc()));
            return new BaseResult(Boolean.FALSE);
        }
    }

    /**
     * 获取 个人物品申报资料下发申报企业结果记录
     *
     * @param stockoutOrderBO
     * @return
     * @throws ServiceException
     */
    public PortBillDeclareDO getPersonalGoodsBillCreateDO(StockoutOrderBO stockoutOrderBO,
                                                          LogisticsPortBO portEntity)
                                                                                     throws ServiceException {
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderBO.getId());
        if (portEntity != null) {
            portBillDeclareDO.setPortId(portEntity.getId());
        } else {
            portBillDeclareDO.setPortId(0L);
        }
        portBillDeclareDO.setBillType(BillType.PERSONAL_GOODS_DECLARE_BILL.getType());
        portBillDeclareDO.setBusinessType(CustomsBusinessType.ORDER_CREATE.getType());
        List<PortBillDeclareDO> portBillDeclareDOs = portBillDeclareManager.query(BaseQuery
            .getInstance(portBillDeclareDO));
        if (portBillDeclareDOs != null && portBillDeclareDOs.size() > 0) {
            return portBillDeclareDOs.get(0);
        }
        return null;
    }

    /**
     * 创建个人物品申报单申报记录，初始状态为待发送
     *
     * @param stockoutOrderDO
     * @return
     * @throws ServiceException
     */
    public boolean createPersonalGoodsBill(StockoutOrderBO stockoutOrderBO,
                                           LogisticsPortBO portEntity) throws ServiceException {
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderBO.getId());
        portBillDeclareDO.setBusinessNo(stockoutOrderBO.getBizId());
        if (portEntity != null) {
            portBillDeclareDO.setPortId(portEntity.getId());
        } else {
            portBillDeclareDO.setPortId(0L);
        }
        portBillDeclareDO.setBillType(BillType.PERSONAL_GOODS_DECLARE_BILL.getType());
        portBillDeclareDO.setBusinessType(CustomsBusinessType.ORDER_CREATE.getType());
        portBillDeclareDO.setBillName(BillType.PERSONAL_GOODS_DECLARE_BILL.getDescription());
        portBillDeclareDO.setDeclareType(DeclareType.CREATE.getType());
        portBillDeclareDO.setState(PortBillState.WAIT_SEND.getValue());
        portBillDeclareManager.insert(portBillDeclareDO);
        return portBillDeclareDO.getId() != null;
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }

}
