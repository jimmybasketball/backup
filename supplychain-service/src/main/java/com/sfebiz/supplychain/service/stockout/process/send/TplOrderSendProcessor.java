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
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderSenderCommand;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/24
 * Time: 下午5:16
 */
@Component("tplOrderSendProcessor")
public class TplOrderSendProcessor extends StockoutProcessAction {

    public static final String  TAG    = "TPL_SEND";
    private static final Logger logger = LoggerFactory.getLogger(TplOrderSendProcessor.class);

    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {
        BaseResult result = new BaseResult();

        if (!checkoutRequestBaseInfo(request)) {
            return new BaseResult(Boolean.FALSE);
        }

        try {
            if (request.getLineBO().getDomesticLogisticsProviderBO() != null) {
                try {
                    ProviderCommand cmd = CommandFactory.createCommand(request.getLineBO()
                        .getDomesticLogisticsProviderBO().getInterfaceType(),
                        WmsMessageType.DELIVER_GOODS.getValue());
                    TplOrderSenderCommand senderCommand = (TplOrderSenderCommand) cmd;
                    senderCommand.setLineBO(request.getLineBO());
                    senderCommand.setStockoutOrderBO(request.getStockoutOrderBO());
                    boolean createResult = senderCommand.execute();
                    result.setSuccess(createResult);
                } catch (ServiceException e) {
                    LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("构建出库命令异常")
                        .setException(e).addParm("订单号", request.getStockoutOrderBO().getBizId())
                        .log();
                    return new BaseResult(Boolean.FALSE);
                }
            } else {
                result.setSuccess(Boolean.TRUE);
            }
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("执行TPL订单发货命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            return new BaseResult(Boolean.FALSE);
        }
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }
}
