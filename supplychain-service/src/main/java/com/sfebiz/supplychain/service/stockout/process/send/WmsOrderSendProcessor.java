package com.sfebiz.supplychain.service.stockout.process.send;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderSenderCommand;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/24
 * Time: 下午5:17
 */
@Component("wmsOrderSendProcessor")
public class WmsOrderSendProcessor extends StockoutProcessAction {

    public static final String  TAG    = "WMS_SEND";
    private static final Logger logger = LoggerFactory.getLogger(WmsOrderSendProcessor.class);

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {
        BaseResult result = new BaseResult();

        if (!checkoutRequestBaseInfo(request)) {
            return new BaseResult(Boolean.FALSE);
        }

        StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();

        try {
            if (request.getLineBO().getInternationalLogisticsProviderBO() != null) {
                try {
                    ProviderCommand cmd = CommandFactory.createCommand(request.getLineBO()
                        .getWarehouseBO().getLogisticsProviderBO().getInterfaceType(),
                        WmsMessageType.DELIVER_GOODS.getValue());
                    WmsOrderSenderCommand senderCommand = (WmsOrderSenderCommand) cmd;
                    senderCommand.setStockoutOrderBO(stockoutOrderBO);
                    senderCommand.setLogisticsLineBO(stockoutOrderBO.getLineBO());
                    boolean createResult = senderCommand.execute();
                    result.setSuccess(createResult);
                } catch (ServiceException e) {
                    LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("构建出库命令异常")
                        .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
                    return new BaseResult(Boolean.FALSE);
                }
            } else {
                result.setSuccess(Boolean.TRUE);
            }
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("执行WMS订单发货命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            return new BaseResult(Boolean.FALSE);
        }
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }
}
