package com.sfebiz.supplychain.service.stockout.process.create;

import java.util.List;

import javax.annotation.Resource;

import net.pocrd.entity.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.gov.zjport.newyork.ws.bo.HzPortBusinessType;

import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.common.entity.BaseResult;
import com.sfebiz.supplychain.exposed.common.enums.BillType;
import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.persistence.base.port.domain.PortBillDeclareDO;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.protocol.common.DeclareType;
import com.sfebiz.supplychain.provider.command.CommandFactory;
import com.sfebiz.supplychain.provider.command.ProviderCommand;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderDeclareCommand;
import com.sfebiz.supplychain.service.port.biz.PortBizService;
import com.sfebiz.supplychain.service.port.model.LogisticsPortBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderDetailBO;
import com.sfebiz.supplychain.service.stockout.process.StockoutProcessAction;
import com.sfebiz.supplychain.service.stockout.statemachine.model.StockoutOrderRequest;

/**
 * <p>PAY单口岸申报</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/20
 * Time: 下午3:31
 */
@Component("payOrderCreateProcessor")
public class PayOrderCreateProcessor extends StockoutProcessAction {

    public static final String     TAG    = "PAY_DECLARE";
    private static final Logger    logger = LoggerFactory.getLogger(PayOrderCreateProcessor.class);
    @Resource
    private PortBillDeclareManager portBillDeclareManager;
    @Resource
    private PortBizService         portBizService;

    /**
     * 如果需要走口岸，且口岸需要支付单，且此口岸下的支付类型对应的支付单申报单位存在的话，走自主支付单申报
     * 如果走连连支付，则必须使用连连支付返回的支付单号进行订单申报和下发
     *
     * @param request 出库单请求信息，如果需要代理申报回填写
     * @return result  处理结果
     * @throws ServiceException
     */
    @Override
    public BaseResult doProcess(StockoutOrderRequest request) throws ServiceException {

        try {
            BaseResult result = new BaseResult();
            StockoutOrderBO stockoutOrderBO = request.getStockoutOrderBO();
            List<StockoutOrderDetailBO> detailBOs = stockoutOrderBO.getDetailBOs();

            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .setMsg("PAY单口岸申报PayOrderCreateProcessor参数").addParm("出库单相关信息", stockoutOrderBO)
                .log();

            if (stockoutOrderBO == null || detailBOs == null || stockoutOrderBO.getLineBO() == null
                || stockoutOrderBO.getDeclarePriceBO() == null) {
                request.setExceptionMessage("[供应链-下发出库单]出库单相关参数实体为空");
                request.setServiceException(new ServiceException(
                    LogisticsReturnCode.DATA_PREPARE_ERROR, LogisticsReturnCode.DATA_PREPARE_ERROR
                        .getDesc()));
                return new BaseResult(Boolean.FALSE);
            }

            LogisticsPortBO portBO = stockoutOrderBO.getLineBO().getPortBO();
            if (portBO != null) {
                String payDeclareProviderNid = stockoutOrderBO.getLineBO()
                    .getPayDeclareProviderNid();

                if (StringUtils.isBlank(payDeclareProviderNid)) {
                    result.setSuccess(Boolean.TRUE);
                    return result;
                }

                if ("PROXYPAY".equals(payDeclareProviderNid)) {
                    //电商企业代理支付企业进行支付单申报（当前只能有一个子单）
                    boolean proxyPay = portBizService.payBillProxyDeclare(stockoutOrderBO);
                    if (proxyPay) {
                        result.setSuccess(Boolean.TRUE);
                    } else {
                        request.setExceptionMessage("[供应链-下发出库单]电商企业代理支付企业申报支付流失败");
                        request.setServiceException(new ServiceException(
                            LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION,
                            LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION
                                .getDesc()));
                        result.setSuccess(Boolean.FALSE);
                    }

                    return result;
                }

                // 不存在就先创建
                PortBillDeclareDO portBillDeclareDO = getPayBillDO(stockoutOrderBO, portBO);
                if (portBillDeclareDO == null) {
                    createPayBill(stockoutOrderBO, portBO);
                    portBillDeclareDO = getPayBillDO(stockoutOrderBO, portBO);
                }

                ProviderCommand cmd = CommandFactory.createPayCommand(payDeclareProviderNid,
                    HzPortBusinessType.PAYBILL_DECLAR.getType());
                PayOrderDeclareCommand payDeclardCommand = (PayOrderDeclareCommand) cmd;
                payDeclardCommand.setStockoutOrderBO(stockoutOrderBO);
                payDeclardCommand.setStockoutOrderDeclarePriceBO(stockoutOrderBO
                    .getDeclarePriceBO());
                payDeclardCommand.setPortBillDeclareDO(portBillDeclareDO);
                payDeclardCommand.setLogisticsLineBO(stockoutOrderBO.getLineBO());

                boolean payDeclareResult = payDeclardCommand.execute();
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("订单采用支付单代理申报方式，订单ID：", stockoutOrderBO.getBizId())
                    .addParm("支付方式:", stockoutOrderBO.getMerchantPayType())
                    .addParm("口岸编码:", portBO.getPortNid())
                    .addParm("支付单申报企业编号：", payDeclareProviderNid).log();
                if (payDeclareResult) {
                    result.setSuccess(Boolean.TRUE);
                } else {
                    request.setExceptionMessage(payDeclardCommand.getCreateFailureMessage());
                    if (StringUtils.isNotEmpty(payDeclardCommand.getCreateFailureMessage())
                        && payDeclardCommand.getCreateFailureMessage().contains("收货人的实名认证失败")) {
                        request.setServiceException(new ServiceException(
                            LogisticsReturnCode.CONFIRM_REAL_NAME_ERROR,
                            LogisticsReturnCode.CONFIRM_REAL_NAME_ERROR.getDesc()));
                    } else {
                        request.setServiceException(new ServiceException(
                            LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION,
                            LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION
                                .getDesc()));
                    }
                    result.setSuccess(Boolean.FALSE);
                }
            } else {
                result.setSuccess(Boolean.TRUE);
            }

            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setErrorMsg("构建支付单申报命令异常")
                .setException(e).setParms(request.getStockoutOrderBO().getBizId()).log();
            request.setExceptionMessage("[供应链-下发出库单]推送支付申报异常，" + e.getMessage());
            request.setServiceException(new ServiceException(
                LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION,
                LogisticsReturnCode.PAY_COMPANY_SERVICE_PAY_BILL_DECLARE_EXCEPTION.getDesc()));
            return new BaseResult(Boolean.FALSE);
        }
    }

    /**
     * 获取 支付单申报结果记录
     *
     * @param stockoutOrderBO
     * @return
     * @throws ServiceException
     */
    public PortBillDeclareDO getPayBillDO(StockoutOrderBO stockoutOrderBO, LogisticsPortBO portBO)
                                                                                                  throws ServiceException {
        if (stockoutOrderBO == null || portBO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                .addParm("[出库单或口岸不存在]参数：", stockoutOrderBO).log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PARAMS_ILLEGAL,
                LogisticsReturnCode.LOGISTICS_COMPANY_SERVICE_PARAMS_ILLEGAL.getDesc());
        }
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderBO.getId());
        portBillDeclareDO.setPortId(portBO.getId());
        portBillDeclareDO.setBillType(BillType.PAY_BILL.getType());
        portBillDeclareDO.setBusinessType(HzPortBusinessType.PAYBILL_DECLAR.getType());
        List<PortBillDeclareDO> portBillDeclareDOs = portBillDeclareManager.query(BaseQuery
            .getInstance(portBillDeclareDO));
        if (portBillDeclareDOs != null && portBillDeclareDOs.size() > 0) {
            return portBillDeclareDOs.get(0);
        }
        return null;
    }

    /**
     * 创建支付单申报记录，初始状态为待发送
     *
     * @param stockoutOrderBO
     * @return
     * @throws ServiceException
     */
    public boolean createPayBill(StockoutOrderBO stockoutOrderBO, LogisticsPortBO portBO)
                                                                                         throws ServiceException {
        if (stockoutOrderBO == null || portBO == null) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                .addParm("[出库单或口岸不存在]参数：", stockoutOrderBO).log();
            throw new ServiceException(LogisticsReturnCode.PAY_COMPANY_SERVICE_PARAMS_ILLEGAL,
                LogisticsReturnCode.LOGISTICS_COMPANY_SERVICE_PARAMS_ILLEGAL.getDesc());
        }
        PortBillDeclareDO portBillDeclareDO = new PortBillDeclareDO();
        portBillDeclareDO.setBillId(stockoutOrderBO.getId());
        portBillDeclareDO.setBusinessNo(stockoutOrderBO.getBizId());
        portBillDeclareDO.setPortId(portBO.getId());
        portBillDeclareDO.setBillType(BillType.PAY_BILL.getType());
        portBillDeclareDO.setBusinessType(HzPortBusinessType.PAYBILL_DECLAR.getType());
        portBillDeclareDO.setBillName(BillType.PAY_BILL.getDescription());
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
