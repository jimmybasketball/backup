package com.sfebiz.supplychain.provider.command.send.port.hangzhou;

import cn.gov.zjport.newyork.ws.CheckReceived;
import cn.gov.zjport.newyork.ws.CheckReceivedResponse;
import cn.gov.zjport.newyork.ws.bo.*;
import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.send.port.PortPayBillDeclareCommand;
import com.sfebiz.supplychain.service.port.PortUtil;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.XMLUtil;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class HzPortPayBillDeclareCommand extends PortPayBillDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    @Override
    public boolean execute() {
        try {
            if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                    || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
                return true;
            }

            boolean isMockAutoPayDeclare = MockConfig.isMocked("hzport", "payCommand");
            if (isMockAutoPayDeclare) {
                //直接返回仓库已发货
                logger.info("MOCK：杭州口岸支付流申报 采用MOCK实现");
                return mockPortStockoutCreateSuccess();
            }

            Request request = buildPayBill();
            String importPay2XmlFormat = XMLUtil.convertToXml(request);
            if (importPay2XmlFormat == null) {
                return false;
            }
            boolean result = sendMessage2ImportPay(importPay2XmlFormat);
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .addParm("[供应链-口岸]订单口岸申报异常,订单ID", stockoutOrderBO.getBizId())
                    .setException(e)
                    .log();
            return false;
        }
    }

    /**
     * 给杭州电子口岸发送支付单申报信息
     *
     * @param importPay
     * @return
     */
    private boolean sendMessage2ImportPay(String importPay) {
        JAXBContext jaxb;
        try {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向杭州口岸代发支付单指令]")
                    .addParm("线路信息", lineBO)
                    .addParm("口岸信息", lineBO.getPortBO())
                    .addParm("url", lineBO.getPortBO().getUrl())
                    .log();

            jaxb = JAXBContext.newInstance("cn.gov.zjport.newyork.ws");
            QName serviceName = new QName("http://impl.ws.newyork.zjport.gov.cn/", "ReceivedDeclareServiceImplService");
            QName portName = new QName("http://impl.ws.newyork.zjport.gov.cn/", "ReceivedDeclareServiceImplPort");
            Service service = Service.create(new URL(lineBO.getPortBO().getUrl()), serviceName);
            Dispatch<Object> dispatch = service.createDispatch(portName, jaxb, Service.Mode.PAYLOAD);
            CheckReceived checkReceived = new CheckReceived();
            checkReceived.setXmlStr(importPay);
            checkReceived.setXmlType(HzPortBusinessType.PAYBILL_PROXY_DECLAR.getType());
            checkReceived.setSourceType("1");
            JAXBElement<CheckReceivedResponse> resp = (JAXBElement<CheckReceivedResponse>) dispatch.invoke(checkReceived);
            String responseString = resp.getValue().getReturn();

            PortBillDeclareManager portBillDeclareManager = (PortBillDeclareManager) CommandConfig.getSpringBean("portBillDeclareManager");
            Response response = XMLUtil.converyToJavaBean(responseString, Response.class);
            if (response != null && response.getBody().getList().size() > 0 && "1".equals(response.getBody().getList().get(0).getChkMark())) {
                portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
                portBillDeclareDO.setResult("推送成功");
                portBillDeclareDO.setBillSendTime(new Date());
                portBillDeclareManager.update(portBillDeclareDO);

                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-向杭州口岸代发支付单指令成功]")
                        .addParm("请求报文", importPay)
                        .addParm("回复报文", responseString)
                        .log();

                return true;
            } else if (response != null && response.getBody().getList().size() > 0 &&
                    ("2".equals(response.getBody().getList().get(0).getChkMark()) || "3".equals(response.getBody().getList().get(0).getChkMark()))) {
                portBillDeclareDO.setState(PortBillState.PARAMS_EXCEPTION.getValue());
                portBillDeclareDO.setResult("推送失败，支付信息已存在");
                portBillDeclareDO.setBillSendTime(new Date());
                portBillDeclareManager.update(portBillDeclareDO);
                LogBetter.instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-向杭州口岸代发支付单指令失败]")
                        .addParm("请求报文", importPay)
                        .addParm("回复报文", responseString)
                        .addParm("失败原因", response.getBody().getList().get(0).getNote())
                        .log();

                return false;
            } else {
                StringBuilder errorMsg = new StringBuilder();
                if (response != null && response.getBody().getList().size() > 0) {
                    List<JKFResultDetail> jkfResultDetails = response.getBody().getList().get(0).getJkfResultDetail();
                    for (JKFResultDetail resultDetail : jkfResultDetails) {
                        errorMsg.append(resultDetail.getResultInfo()).append(";");
                    }
                }
                portBillDeclareDO.setState(PortBillState.PARAMS_EXCEPTION.getValue());
                portBillDeclareDO.setResult("推送失败：" + errorMsg.toString());
                portBillDeclareDO.setBillSendTime(new Date());
                portBillDeclareManager.update(portBillDeclareDO);

                LogBetter.instance(logger)
                        .setLevel(LogLevel.WARN)
                        .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                        .setMsg("[供应链报文-向杭州口岸代发支付单指令失败]")
                        .addParm("请求报文", importPay)
                        .addParm("回复报文", responseString)
                        .log();
                return false;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .setMsg("[供应链报文-向杭州口岸代发支付单指令异常]")
                    .addParm("请求报文", importPay)
                    .addParm("异常信息", e.getMessage())
                    .setException(e)
                    .log();
        }
        return false;
    }


    /**
     * 构建 口岸需要的支付单数据
     *
     * @return
     */
    private Request buildPayBill() {
        Request request = new Request();
        request.getHead().setBusinessType(HzPortBusinessType.PAYBILL_PROXY_DECLAR.getType());
        ImportPay importPay = new ImportPay();
        request.getBody().getImportPayList().add(importPay);

        importPay.getJkfSign().setCompanyCode(PortUtil.getCustomsCode(lineBO));
        importPay.getJkfSign().setBusinessNo(stockoutOrderBO.getDeclarePayNo());
        importPay.getJkfSign().setBusinessType(HzPortBusinessType.PAYBILL_PROXY_DECLAR.getType());
        importPay.getJkfSign().setDeclareType(declareType.getType());
        importPay.getJkfSign().setNote("");

        importPay.getJkfImportPay().setOrderNo(stockoutOrderBO.getBizId());
        //杭州电子口岸需要这里填写平台编码，否则出现“申报数据中电商平台不一致”的问题
        importPay.getJkfImportPay().seteCommerceCode(PortUtil.getCustomsCode(lineBO));
        //支付流水号，注意事项，电商自助推送支付流，只能推一个支付流对应一个包裹的情况
        importPay.getJkfImportPay().setPayTransactionNo(stockoutOrderBO.getDeclarePayNo());

        //订单成交总价＝订单总金额 - 折扣
        String userPayTotalPrice = String.format("%.02f", (stockoutOrderBO.getDeclarePriceBO().getOrderActualPrice()) / 100.0f);
        importPay.getJkfImportPay().setPayAmount(userPayTotalPrice);

        //订单货款
        String goodsAmount = String.format("%.02f", stockoutOrderBO.getDeclarePriceBO().getGoodsTotalPrice() / 100.0f);
        importPay.getJkfImportPay().setPayGoodsAmount(goodsAmount);

        //订单税款
        String taxAmount = String.format("%.02f", stockoutOrderBO.getDeclarePriceBO().getTaxFee() / 100.0f);
        importPay.getJkfImportPay().setPayTaxAmount(taxAmount);

        //订单运费
        String feeAmount = String.format("%.02f", stockoutOrderBO.getDeclarePriceBO().getFreightFee() / 100.0f);
        importPay.getJkfImportPay().setPayFeeAmount(feeAmount);

        importPay.getJkfImportPay().setPayTimeStr(DateUtil.defFormatDateStr(stockoutOrderBO.getGmtCreate()));
        importPay.getJkfImportPay().setCurrCode("142");
        String userName = stockoutOrderBO.getDeclarePayerName();
        if (StringUtils.isBlank(userName)) {
            userName = stockoutOrderBO.getBuyerBO().getBuyerName();
        }

        String userIdNo = stockoutOrderBO.getDeclarePayerCertNo();
        if (StringUtils.isBlank(userIdNo)) {
            userIdNo = stockoutOrderBO.getBuyerBO().getBuyerCertNo();
        }
        importPay.getJkfImportPay().setPayerName(userName);
        importPay.getJkfImportPay().setPaperType("01");
        importPay.getJkfImportPay().setPaperNumber(userIdNo);
        importPay.getJkfImportPay().setPayerPhoneNumber(stockoutOrderBO.getBuyerBO().getBuyerTelephone());

        String payCodeOnPort = PayConfig.getPayCodeOnPort(PayConfig.getPayProviderNidByPayType(stockoutOrderBO.getMerchantPayType()), lineBO.getPortBO().getPortNid());
        importPay.getJkfImportPay().setPayCompanyCode(payCodeOnPort);
        String payCompanyName = PayConfig.getPayCompanyName(PayConfig.getPayProviderNidByPayType(stockoutOrderBO.getMerchantPayType()));
        importPay.getJkfImportPay().setPayEnterpriseName(payCompanyName);

        return request;
    }
}
