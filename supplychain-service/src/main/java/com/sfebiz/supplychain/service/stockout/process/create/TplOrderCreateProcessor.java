package com.sfebiz.supplychain.service.stockout.process.create;

import java.util.List;

import net.pocrd.entity.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.warehouse.enums.WmsMessageType;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.tpl.TplOrderCreateCommand;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>TPL(国内物流供应商)订单创建</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/20
 * Time: 下午3:31
 */
@Component("tplOrderCreateProcessor")
public class TplOrderCreateProcessor extends StockoutProcessAction {

    public static final String  TAG    = "TPL_CREATE";
    private static final Logger logger = LoggerFactory.getLogger(TplOrderCreateProcessor.class);

    /**
     * 如果存在国内物流运输，给国内物流公司下发出库指令
     *
     * @param request 出库单请求信息
     * @return result   处理结果
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
            List<StockoutOrderDetailBO> stockoutOrderSkuDOs = stockoutOrderBO.getDetailBOs();

            //TODO zhangdi 修改判断逻辑，适应转运邦无单号模式
            if (stockoutOrderBO.getLineBO().getInternationalLogisticsProviderBO() != null) {
                try {
                    ProviderCommand cmd = CommandFactory.createCommand(request.getLineBO()
                        .getDomesticLogisticsProviderBO().getLogisticsProviderNid(),
                        WmsMessageType.STOCK_OUT.getValue());
                    TplOrderCreateCommand tplOrderCreateCommand = (TplOrderCreateCommand) cmd;
                    tplOrderCreateCommand.setStockoutOrderBO(request.getStockoutOrderBO());
                    boolean createResult = tplOrderCreateCommand.execute();
                    if (createResult) {
                        result.setSuccess(Boolean.TRUE);
                    } else {
                        request
                            .setExceptionMessage(tplOrderCreateCommand.getCreateFailureMessage());
                        request.setServiceException(new ServiceException(
                            LogisticsReturnCode.STOCKOUT_ORDER_TPL_CREATE_FAILURE,
                            LogisticsReturnCode.STOCKOUT_ORDER_TPL_CREATE_FAILURE.getDesc()));
                        result.setSuccess(Boolean.FALSE);
                    }
                } catch (ServiceException e) {
                    LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("构建出库命令异常")
                        .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
                    request.setExceptionMessage("[供应链-下发出库单]执行TPL订单创建命令异常,TPL不支持此命令");
                    request.setServiceException(new ServiceException(
                        LogisticsReturnCode.STOCKOUT_ORDER_TPL_CREATE_FAILURE,
                        LogisticsReturnCode.STOCKOUT_ORDER_TPL_CREATE_FAILURE.getDesc()));
                    return new BaseResult(Boolean.FALSE);
                }
            } else {
                result.setSuccess(Boolean.TRUE);
            }

            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("执行TPL订单创建命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-下发出库单]执行TPL订单创建命令异常," + e.getMessage());
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.STOCKOUT_ORDER_TPL_CREATE_FAILURE,
                LogisticsReturnCode.STOCKOUT_ORDER_TPL_CREATE_FAILURE.getDesc()));
            return new BaseResult(Boolean.FALSE);
        }
    }

    @Override
    public String getProcessTag() {
        return TAG;
    }

}
