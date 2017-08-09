package com.sfebiz.supplychain.service.stockout.process.create;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.gov.zjport.newyork.ws.bo.HzPortBusinessType;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.port.PortOrderBillDeclareCommand;
import com.sfebiz.supplychain.service.line.model.LogisticsLineBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>PORT订单申报</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/20
 * Time: 下午3:31
 */
@Component("portOrderCreateProcessor")
public class PortOrderCreateProcessor extends StockoutProcessAction {

    public static final String   TAG    = "PORT_DECLARE";
    private static final Logger  logger = LoggerFactory.getLogger(PortOrderCreateProcessor.class);
    @Resource
    private StockoutOrderManager stockoutOrderManager;

    /**
     * 如果需要走口岸，给口岸申报订单信息，否则进行下一个Action
     *
     * @param request 出库单请求信息
     * @return result  处理结果
     * @throws ServiceException
     */
    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {

        try {

            BaseResult result = new BaseResult();

            if (!checkoutRequestBaseInfo(request)) {
                return new BaseResult(Boolean.FALSE);
            }
            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();

            LogisticsLineBO lineEntity = request.getLineBO();
            if (lineEntity.getPortBO() != null) {

                ProviderCommand cmd = CommandFactory.createPortCommand(lineEntity.getPortBO()
                    .getType(), HzPortBusinessType.IMPORTORDER.getType());

                PortOrderBillDeclareCommand portOrderBillDeclareCommand = (PortOrderBillDeclareCommand) cmd;
                portOrderBillDeclareCommand.setStockoutOrderBO(stockoutOrderBO);
                portOrderBillDeclareCommand.setPayBillDeclareProviderNid(stockoutOrderBO
                    .getLineBO().getPayDeclareProviderNid());

                boolean declareResult = portOrderBillDeclareCommand.execute();
                result.setSuccess(declareResult);
            } else {
                result.setSuccess(Boolean.TRUE);
            }
            return result;

        } catch (ServiceException e) {

            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("构建出库命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-下发出库单]向口岸推送订单流失败，" + e.getMessage());
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.STOCKOUT_ORDER_PORT_CREATE_FAILURE,
                LogisticsReturnCode.STOCKOUT_ORDER_PORT_CREATE_FAILURE.getDesc()));
            return new BaseResult(Boolean.FALSE);

        }
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }
}
