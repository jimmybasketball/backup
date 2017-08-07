package com.sfebiz.supplychain.provider.command.send.wms.nbbs.xhtd;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.protocol.wms.nbbs.query.NBBSOrderQueryRequest;
import com.sfebiz.supplychain.protocol.wms.nbbs.query.NBBSOrderQueryRequestBody;
import com.sfebiz.supplychain.protocol.wms.nbbs.query.NBBSOrderQueryRequestHead;
import com.sfebiz.supplychain.protocol.wms.nbbs.query.NBBSOrderQueryResponse;
import com.sfebiz.supplychain.provider.command.send.wms.WmsOrderQueryCommand;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import com.sfebiz.supplychain.util.XMLUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Description: 宁波保税仓(鑫海通达) 轮询查询订单状态 Created by yanghua on 2017/3/22.
 */
public class NBBSOrderQueryCommand extends WmsOrderQueryCommand {
    private static StockoutOrderManager stockoutOrderManager;

    private static final String NBBSORDERQUERYMETHOD = "find";
    private static final String STOCKOUTED = "24";
    private static final String STOCKOUT_STATE = "订单完成";
    private static final String STOCKOUTED_STATE = "发货完成";
    /* rule=0或1时，必填，rule=0时（订单号=客户方订单号），rule=1时（订单号=能容订单号[EO开头]） */
    private static final String CUSTORDERNO = "0";
//    private StockoutBizService stockoutBizService;

    @Override
    public boolean execute() {
        if (stockoutOrderBO == null) {
            logger.info("[供应链-向宁波鑫海通达仓查询订单已出库信息] 出库单信息未找到");
            return false;
        }
        try {
//            stockoutBizService = (StockoutBizService) SpringBeanFactory.getBean("stockoutBizService");
            String requestXml = buildOrderQueryParams();
            NBBSOrderQueryResponse response = sendMessageToEDI(requestXml);
            // 订单成功查询 且状态为海关放行的,丰趣订单状态改为已发货
            if ("success".equals(response.getResult())
                    && (STOCKOUTED.equals(response.getOrderInfo().getMftStatus())
                    || STOCKOUT_STATE.equals(response.getOrderInfo().getOrderStatus())
                    || STOCKOUTED_STATE.equals(response.getOrderInfo().getOrderStatus()))) {

                stockoutOrderManager = (StockoutOrderManager) SpringBeanFactory
                        .getBean("stockoutOrderManager");
                // 更新计费重量(包裹重量)
                String weightKg = response.getOrderInfo().getPackageWeight();
                if (!StringUtils.isBlank(weightKg)) {
                    BigDecimal weight = new BigDecimal(weightKg);
                    int weightG = weight.multiply(new BigDecimal(1000))
                            .intValue();
                    stockoutOrderManager.updateCalWeight(
                            stockoutOrderBO.getId(), weightG);
                }
                // 保存运单号
                stockoutOrderManager.updateMailNo(stockoutOrderBO.getId(),
                        response.getOrderInfo().getWaybillNo());
                // 触发出库状态 TODO
                boolean isSuccess = true;
//                boolean isSuccess = stockoutBizService
//                        .triggerStockoutOrderSend(stockoutOrderBO,
//                                new Date().getTime());

                LogBetter
                        .instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(
                                TraceLogEntity.instance(traceLogger,
                                        stockoutOrderBO.getBizId(),
                                        SystemConstants.TRACE_APP))
                        .setMsg("[供应链-向宁波鑫海通达仓查询订单已出库信息] 仓库已出库，进行出库单状态变更")
                        .addParm("订单号", stockoutOrderBO.getBizId())
                        .addParm("返回状态", response.getOrderInfo().getMftStatus())
                        .addParm("订单状态",
                                response.getOrderInfo().getOrderStatus())
                        .addParm("运单号", response.getOrderInfo().getWaybillNo())
                        .addParm("变更出库单为已出库", isSuccess).log();

            } else if ("failure".equals(response.getResult())
                    && "s1000".equals(response.getCode())) {
                LogBetter
                        .instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(
                                TraceLogEntity.instance(traceLogger,
                                        stockoutOrderBO.getBizId(),
                                        SystemConstants.TRACE_APP))
                        .setMsg("[供应链-向宁波鑫海通达仓查询订单已出库信息] 接口提示，订单查询频率过高，请稍后重试")
                        .addParm("订单号", stockoutOrderBO.getBizId()).log();
            } else {
                LogBetter
                        .instance(logger)
                        .setLevel(LogLevel.INFO)
                        .setTraceLogger(
                                TraceLogEntity.instance(traceLogger,
                                        stockoutOrderBO.getBizId(),
                                        SystemConstants.TRACE_APP))
                        .setMsg("[供应链-宁波鑫海通达仓查询]订单暂未出库，请稍后查询，订单号"
                                + stockoutOrderBO.getBizId()).log();
            }
        } catch (Exception e) {
            logger.error("[供应链-向宁波鑫海通达仓查询订单已出库信息] 查询异常：bizId={}, e={}",
                    stockoutOrderBO.getBizId(), e);
            LogBetter
                    .instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setException(e)
                    .setTraceLogger(
                            TraceLogEntity.instance(traceLogger,
                                    stockoutOrderBO.getBizId(),
                                    SystemConstants.TRACE_APP))
                    .setMsg("[供应链-向宁波鑫海通达仓查询订单已出库信息] 查询异常")
                    .addParm("订单号", stockoutOrderBO.getBizId()).log();
        }
        return true;
    }

    private String buildOrderQueryParams() throws Exception {
        // 构造订单查询数据
        NBBSOrderQueryRequest request = new NBBSOrderQueryRequest();
        NBBSOrderQueryRequestHead head = new NBBSOrderQueryRequestHead();
        head.setBusCode(logisticsProviderBO.getInterfaceMeta().get("custId"));
        head.setMethodType(NBBSORDERQUERYMETHOD);
        head.setRule(CUSTORDERNO);
        NBBSOrderQueryRequestBody body = new NBBSOrderQueryRequestBody();
        body.setOrderNo(stockoutOrderBO.getBizId());
        request.setHead(head);
        request.setBody(body);
        return XMLUtil.convertToXml(request);
    }

    private NBBSOrderQueryResponse sendMessageToEDI(String request)
            throws Exception {

        Map<String, Object> wmsMeta = JSONUtil
                .parseJSONMessage(logisticsProviderBO.getInterfaceMeta().get("meta"));
        String date = DateUtil.getCurrentDate(DateUtil.DATETIME_FORMAT);

        StringBuffer buf = new StringBuffer();
        buf.append("method=").append("order.query&");
        buf.append("serialno=").append(stockoutOrderBO.getBizId()).append("&");
        buf.append("datetime=").append(date).append("&");
        buf.append("msgtype=xml&");
        buf.append("customer=").append(wmsMeta.get("customer")).append("&");
        buf.append("version=0.0.1&");
        buf.append("sign=")
                .append(URLEncoder.encode(
                        MD5AndBase64(request + wmsMeta.get("secret_key") + date),
                        "utf-8")).append("&");
        buf.append("sign_method=md5&");
        buf.append("data=").append(URLEncoder.encode(request, "utf-8"))
                .append("&");
        buf.append("appkey=").append(wmsMeta.get("app_key"));

        String responseString = HttpUtil.postFormByHttp(
                logisticsProviderBO.getInterfaceMeta().get("interfaceUrl"), buf.toString());
        // 处理乱码
        responseString = new String(responseString.getBytes("ISO-8859-1"),
                "utf-8");

        LogBetter
                .instance(logger)
                .setLevel(LogLevel.INFO)
                .setTraceLogger(
                        TraceLogEntity.instance(traceLogger,
                                stockoutOrderBO.getBizId(),
                                SystemConstants.TRACE_APP))
                .addParm("[供应链-宁波鑫海通达仓查询开始]订单ID", stockoutOrderBO.getBizId())
                .addParm("请求报文", buf.toString())
                .addParm("回复报文", responseString).log();
        NBBSOrderQueryResponse orderResponse = null;
        try {
            orderResponse = XMLUtil.converyToJavaBean(responseString,
                    NBBSOrderQueryResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderResponse;
    }

    public static String MD5AndBase64(String str) throws Exception {
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        messagedigest.update((str).getBytes("UTF-8"));
        byte[] abyte0 = messagedigest.digest();
        String data_digest = new String(Base64.encodeBase64(abyte0));
        return data_digest.toUpperCase();
    }

    public static void main(String[] args) throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><request><head><methodType>find</methodType><rule>0</rule><busCode>FQW</busCode></head><body><orderNo>3201704191209127574S0001</orderNo></body></request>";
        String date = "2017-04-26 11:39:00";
        String key = "X00vPW203H39GWTF";

        System.out.println(URLEncoder.encode(
                MD5AndBase64(request + key + date), "utf-8"));
    }

}
