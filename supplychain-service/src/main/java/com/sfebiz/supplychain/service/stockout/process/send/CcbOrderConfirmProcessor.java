package com.sfebiz.supplychain.service.stockout.process.send;

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
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderType;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.ccb.CcbOrderConfirmCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.port.model.CustomsBusinessType;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/24
 * Time: 下午5:16
 */
@Component("ccbOrderConfirmProcessor")
public class CcbOrderConfirmProcessor extends StockoutProcessAction {

    public static final String     TAG    = "CCB_CONFIRM";
    private static final Logger    logger = LoggerFactory.getLogger(CcbOrderConfirmProcessor.class);

    @Resource
    private PortBillDeclareManager portBillDeclareManager;

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {

        BaseResult result = new BaseResult();
        if (request.getStockoutOrderBO() == null
            || request.getStockoutOrderBO().getLineBO() == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR).setMsg("通知关务企业进行订单确认Processor参数异常")
                .addParm("请求信息", request.getStockoutOrderBO()).log();
            return new BaseResult(Boolean.FALSE);
        }
        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
        LogisticsLineBO lineBO = stockoutOrderBO.getLineBO();

        //不走口岸的订单不需要跟报关企业进行交互
        if (lineBO.getClearanceLogisticsProviderBO() == null) {
            return new BaseResult(Boolean.TRUE);
        }

        //只有销售出库单才需要与关务交互
        if (StockoutOrderType.SALES_STOCK_OUT.getValue() != stockoutOrderBO.getOrderType()) {
            return new BaseResult(Boolean.TRUE);
        }

        //如果没有配置清关企业 或 清关企业没有配置命令集合 或 清关为BSP的话，直接跳过
        if (lineBO.getClearanceLogisticsProviderBO() == null
            || "-1".equals(lineBO.getClearanceLogisticsProviderBO().getInterfaceType())
            || lineBO.getClearanceLogisticsProviderBO().getLogisticsProviderNid().startsWith("bsp")) {
            return new BaseResult(Boolean.TRUE);
        }

        try {
            PortBillDeclareDO portBillDeclareDO = getPersonalGoodsBillConfirmDO(
                request.getStockoutOrderBO(), lineBO.getPortBO());
            if (portBillDeclareDO == null) {
                createPersonalGoodsBillConfirm(stockoutOrderBO, lineBO.getPortBO());
                portBillDeclareDO = getPersonalGoodsBillConfirmDO(stockoutOrderBO,
                    lineBO.getPortBO());
            }

            if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
                //幂等性判断
                return new BaseResult(Boolean.TRUE);
            }
            try {
                ProviderCommand cmd = CommandFactory.createCommand(lineBO
                    .getClearanceLogisticsProviderBO().getInterfaceType(),
                    WmsMessageType.CCB_ORDER_CONFIRM.getValue());
                CcbOrderConfirmCommand orderConfirmCommand = (CcbOrderConfirmCommand) cmd;
                orderConfirmCommand.setLineBO(lineBO);
                orderConfirmCommand.setStockoutOrderBO(stockoutOrderBO);
                orderConfirmCommand.setPortBillDeclareDO(portBillDeclareDO);
                orderConfirmCommand.setStockoutOrderDetailBOs(stockoutOrderBO.getDetailBOs());
                orderConfirmCommand.setStockoutOrderDeclarePriceBO(stockoutOrderBO
                    .getDeclarePriceBO());
                boolean confirmResult = orderConfirmCommand.execute();
                result.setSuccess(confirmResult);
            } catch (ServiceException e) {
                LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("构建出库命令异常")
                    .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
                return new BaseResult(Boolean.FALSE);
            }
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("执行TPL订单发货命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            return new BaseResult(Boolean.FALSE);
        }
    }

    /**
     * 获取 个人物品申报资料订单确认申报企业结果记录
     *
     * @param stockoutOrderBO
     * @return
     * @throws ServiceException
     */
    public PortBillDeclareDO getPersonalGoodsBillConfirmDO(StockoutOrderBO stockoutOrderBO,
                                                           LogisticsPortBO portBO)
                                                                                  throws ServiceException {
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderBO.getId());
        if (portBO != null) {
            portBillDeclareDO.setPortId(portBO.getId());
        } else {
            portBillDeclareDO.setPortId(0L);
        }
        portBillDeclareDO.setBillType(BillType.PERSONAL_GOODS_DECLARE_BILL.getType());
        portBillDeclareDO.setBusinessType(CustomsBusinessType.ORDER_CONFIRM.getType());
        List<PortBillDeclareDO> portBillDeclareDOs = portBillDeclareManager.query(BaseQuery
            .getInstance(portBillDeclareDO));
        if (portBillDeclareDOs != null && portBillDeclareDOs.size() > 0) {
            return portBillDeclareDOs.get(0);
        }
        return null;
    }

    /**
     * 创建个人物品申报单申报确认记录，初始状态为待发送
     *
     * @param stockoutOrderBO
     * @return
     * @throws ServiceException
     */
    public boolean createPersonalGoodsBillConfirm(StockoutOrderBO stockoutOrderBO,
                                                  LogisticsPortBO portBO) throws ServiceException {
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderBO.getId());
        portBillDeclareDO.setBusinessNo(stockoutOrderBO.getBizId());
        if (portBO != null) {
            portBillDeclareDO.setPortId(portBO.getId());
        } else {
            portBillDeclareDO.setPortId(0L);
        }
        portBillDeclareDO.setBillType(BillType.PERSONAL_GOODS_DECLARE_BILL.getType());
        portBillDeclareDO.setBusinessType(CustomsBusinessType.ORDER_CONFIRM.getType());
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
