package com.sfebiz.supplychain.provider.command.send.pay.tencent;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.tracelog.TraceLog;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.mock.MockConfig;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.persistence.base.port.manager.PortBillDeclareManager;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.pay.tenpay.RequestHandler;
import com.sfebiz.supplychain.protocol.pay.tenpay.TencentPayConstants;
import com.sfebiz.supplychain.protocol.pay.tenpay.client.TenpayHttpClient;
import com.sfebiz.supplychain.protocol.pay.tenpay.client.XMLParseResponseHandler;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderReDeclareCommand;
import com.sfebiz.supplychain.util.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * 财付通支付申报
 * 我们请求设置的什么字符编码 返回xml编码按照请求设置的返回
 * Created by zhaojingyang on 2015/5/11.
 */
public class TenPayOrderReDeclareCommand extends PayOrderReDeclareCommand {
    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");

    private StockoutOrderManager stockoutOrderManager;

    @Override
    public boolean execute() {
        if (portBillDeclareDO == null || stockoutOrderBO == null || StringUtils.isBlank(payBillDeclareProviderNid)
                || logisticsLineBO == null || logisticsLineBO.getPortBO() == null) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("财付通支付支付单申报单参数缺失")
                    .addParm("出库单号：", stockoutOrderBO.getBizId())
                    .log();
            return false;
        }

        boolean isMockAutoCreated = MockConfig.isMocked("tenPay", "createCommand");
        if (isMockAutoCreated) {
            //直接返回仓库已发货
            logger.info("MOCK：财付通支付申报 采用MOCK实现");
            return mockPayBillDeclareSuccess();
        }
        String requestUrl = "";
        String resContent = "";
        try {
            String payType = PayConfig.getPayProviderNidByPayType(stockoutOrderBO.getDeclarePayType());
            if (!payType.equals(this.payBillDeclareProviderNid)) {
                throw new Exception("财付通只能为其支付方式申报,该订单支付类型为" + payType);
            }

            //创建查询请求对象
            RequestHandler reqHandler = new RequestHandler(null, null);
            //设置参数
            buildReqHeader(reqHandler);

            //通信对象
            TenpayHttpClient httpClient = new TenpayHttpClient();
            //设置请求返回的等待时间
            httpClient.setTimeOut(10);
            //设置发送类型POST
            httpClient.setMethod("POST");

            httpClient.setCharset(TencentPayConstants.CharSet.UTF8.getValue());

            Map<String, Object> payMeta = PayConfig.getPayProviderMeta(payBillDeclareProviderNid);
            if (null == payMeta) {
                throw new Exception("未设置" + payBillDeclareProviderNid + "支付的meta信息");
            }
            String caInfo = (String) payMeta.get("caInfo");
            String certInfo = (String) payMeta.get("certInfo");
            String certPwd = (String) payMeta.get("certPwd");
            if (StringUtils.isEmpty(caInfo) || StringUtils.isEmpty(certInfo) || StringUtils.isEmpty(certPwd)) {
                throw new Exception(payBillDeclareProviderNid + "未设置证书信息");
            }

            //设置ca证书
            httpClient.setCaInfo(new File(caInfo));
            //设置个人(商户)证书
            httpClient.setCertInfo(new File(certInfo), certPwd);

            //设置请求内容
            requestUrl = reqHandler.getRequestURL();
            httpClient.setReqContent(requestUrl);
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("财付通支付支付单申报请求报文", requestUrl)
                    .log();

            boolean callResault = httpClient.call();
            if (!callResault) {
                throw new Exception("财付通支付申报异常" + httpClient.getResponseCode() + httpClient.getErrInfo());
            }
            resContent = httpClient.getResContent();


            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[供应链报文-向财付通支付下发支付单申报指令成功]")
                    .addParm("出库单号", stockoutOrderBO.getBizId())
                    .addParm("请求报文", requestUrl)
                    .addParm("回复报文", resContent)
                    .log();

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                    "[供应链报文-向财付通支付下发支付单申报指令成功]: "
                            + "[请求报文: " + requestUrl
                            + ", 回复报文: " + resContent
                            + "]"));

            //应答处理
            XMLParseResponseHandler resHandler = new XMLParseResponseHandler();
            resHandler.setContent(resContent);
            resHandler.setKey(reqHandler.getKey());
            resHandler.setCharset(reqHandler.getParameter("input_charset"));
            PortBillDeclareManager portBillDeclareManager = (PortBillDeclareManager) CommandConfig.getSpringBean("portBillDeclareManager");
            String retcode = resHandler.getParameter("retcode");

            if (TencentPayConstants.REC_SUCCESS.equalsIgnoreCase(retcode)) {
                if (!resHandler.isTenpaySign()) {
                    throw new Exception("返回报文签名不相等");
                }

                //财付通子单号记录到 pay_no
                String tencentPayNo = resHandler.getParameter("sub_order_id");
                String expNo = stockoutOrderBO.getBizId();
                String actNo = resHandler.getParameter("sub_order_no");

                if (StringUtils.isBlank(tencentPayNo)) {
                    throw new Exception("财付通支付返回支付单号为空");
                }

                if (!expNo.equals(actNo)) {
                    throw new Exception("财付通申报回传单号不相等，期望单号:" + expNo + ",返回单号:" + actNo);
                }

                StockoutOrderManager stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
                stockoutOrderBO.setDeclarePayNo(tencentPayNo);
                stockoutOrderManager.updatePayNo(stockoutOrderBO.getId(), tencentPayNo);
                portBillDeclareDO.setState(PortBillState.SEND_SUCCESS.getValue());
                portBillDeclareDO.setResult(resHandler.getParameter("retmsg"));
                portBillDeclareDO.setBillSendTime(new Date());
                portBillDeclareManager.update(portBillDeclareDO);

                return true;
            } else {
                portBillDeclareDO.setState(PortBillState.PARAMS_EXCEPTION.getValue());
                portBillDeclareDO.setResult(resHandler.getParameter("retmsg"));
                portBillDeclareDO.setBillSendTime(new Date());
                portBillDeclareManager.update(portBillDeclareDO);
                setCreateFailureMessage("财付通申报失败：" + resHandler.getParameter("retmsg"));
                LogBetter.instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setMsg("[供应链报文-向财付通支付下发支付单申报指令反馈延长]")
                        .addParm("出库单号", stockoutOrderBO.getBizId())
                        .addParm("请求报文", requestUrl)
                        .addParm("回复报文", resContent)
                        .log();

                traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.INFO,
                        "[供应链报文-向财付通支付下发支付单申报指反馈异常]: "
                                + "[请求报文: " + resContent
                                + ", 回复报文: " + resContent
                                + "]"));
                return false;
            }
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setMsg("[供应链报文-向财付通支付下发支付单申报指令异常]")
                    .addParm("出库单", stockoutOrderBO.getBizId())
                    .addParm("请求报文", requestUrl)
                    .setException(e)
                    .log();

            traceLogger.log(new TraceLog(stockoutOrderBO.getBizId(), "supplychain", new Date(), TraceLog.TraceLevel.ERROR,
                    "[供应链报文-向财富通支付下发支付单申报指令异常]: "
                            + "[请求报文: " + resContent
                            + ", 异常信息: " + e.getMessage()
                            + "]"
            ));
            return false;
        }
    }

    /**
     * 构建请求对象
     *
     * @param reqHandler
     */
    private void buildReqHeader(RequestHandler reqHandler) throws Exception {
        reqHandler.init();

        if (!Integer.valueOf(1).equals(stockoutOrderBO.getDeclarePayerCertType())) {
            throw new IllegalArgumentException("财付通只支持身份证申报,出库单:" + stockoutOrderBO.getBizId());
        }

        //电商企业 在 支付企业 中的备案编码
        String ecp = PayConfig.getECodeOnPayProvider(payBillDeclareProviderNid);
        if (StringUtils.isBlank(ecp)) {
            throw new IllegalArgumentException("财付通企业备案号[e_code_on_pay]未配置");
        }

        //各个口岸的meta信息中需要填写在财付通支付中的海关编码
        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());
        String ePortCode = (String) portMeta.get("tencent_eport_code");
        if (StringUtils.isBlank(ePortCode)) {
            throw new IllegalArgumentException("财付通在口岸" + logisticsLineBO.getPortBO().getPortNid() + "的海关编码[tencent_eport_code]未配置");
        }

        //支付企业 系统交互的接入码
        String key = PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid);
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("财付通支付KEY[interface_key]未配置");
        }

        String interFaceBaseUrl = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);
        if (StringUtils.isBlank(interFaceBaseUrl)) {
            throw new IllegalArgumentException("与财付通交互的URL[interface_url]未配置");
        }

        reqHandler.setKey(key); //设置key
        reqHandler.setGateUrl(interFaceBaseUrl + TencentPayConstants.TencentPayApiUrl.PAY_BILL_DECLARE_URL.getUrl()); //请求地址

        reqHandler.setParameter("sign_type", TencentPayConstants.SignType.MD5.getValue());
        reqHandler.setParameter("service_version", TencentPayConstants.TENCENT_PAY_VERSION);
        reqHandler.setParameter("input_charset", TencentPayConstants.CharSet.UTF8.getValue());
        reqHandler.setParameter("sign_key_index", TencentPayConstants.DEF_SIGN_KEY_INDEX);
        reqHandler.setParameter("partner", ecp);//商户号 由财付通统一分配的10位正整数

        reqHandler.setParameter("transaction_id", stockoutOrderBO.getMerchantPayNo());//财付通交易号

        //备注：tenpay要求如果子单数量大于1，则按照子单申报，如果子单数等于1，需要按主单申报，但是由于这个对我们业务变动较大，所以我们
        //全部按照子单申报，金额上，在实付上全部减一分钱，跳过支付企业的限制；
        reqHandler.setParameter("out_trade_no", stockoutOrderBO.getMerchantOrderNo());//商户订单号
        reqHandler.setParameter("sub_order_no", stockoutOrderBO.getBizId());//子单号
        reqHandler.setParameter("fee_type", TencentPayConstants.CURRENCY_CNY);//币种 人民币
        //orderFee =  transport_fee +  product_fee
        Integer orderFee = stockoutOrderDeclarePriceBO.getOrderActualPrice();
        orderFee = orderFee == null ? Integer.valueOf(0) : orderFee;
        reqHandler.setParameter("order_fee", orderFee.toString());//子单金额 人民币 分
        //由于财付通要求order_fee=transport_fee+product_fee，而支付单中的运费、商品费用海关不进行限制，所以这里可以不实用真实数据
        reqHandler.setParameter("transport_fee", "0");//物流费 分
        reqHandler.setParameter("product_fee", String.valueOf(orderFee));//商品费
        Integer tariff = stockoutOrderDeclarePriceBO.getTaxFee();
        if (tariff != null) {
            reqHandler.setParameter("duty", String.valueOf(tariff.intValue()));//关税 人民币,单位分
        } else {
            reqHandler.setParameter("duty", "0");//关税 人民币
        }

        reqHandler.setParameter("customs", ePortCode);//海关编码
        reqHandler.setParameter("mch_customs_no", logisticsLineBO.getPortBO().geteCommerceCode());//商户海关备案号

        //证件 购买人如果我们传则用我们的报关 否则用财付通收集的
        String userName = CommonUtil.getBuyerName(stockoutOrderBO);
        String userIdNo = CommonUtil.getBuyerIdNo(stockoutOrderBO);
        reqHandler.setParameter("cert_type", null); //证件类型
        reqHandler.setParameter("cert_id", null); //证件号
        reqHandler.setParameter("name", null);//姓名
        reqHandler.setParameter("action_type", actionType);//操作类型 2，修改 3，重推
    }
}
