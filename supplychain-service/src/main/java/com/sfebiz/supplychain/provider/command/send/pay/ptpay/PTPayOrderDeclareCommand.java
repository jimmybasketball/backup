package com.sfebiz.supplychain.provider.command.send.pay.ptpay;

import com.sfebiz.common.tracelog.HaitaoTraceLogger;
import com.sfebiz.common.tracelog.HaitaoTraceLoggerFactory;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.common.utils.log.TraceLogEntity;
import com.sfebiz.supplychain.config.SystemConstants;
import com.sfebiz.supplychain.config.pay.PayConfig;
import com.sfebiz.supplychain.exposed.common.enums.PortBillState;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import com.sfebiz.supplychain.provider.command.common.CommonUtil;
import com.sfebiz.supplychain.provider.command.send.pay.PayOrderDeclareCommand;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.HttpUtil;
import com.sfebiz.supplychain.util.JSONUtil;
import net.pocrd.entity.ServiceException;
import net.pocrd.util.Md5Util;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by TT on 2016/9/25.
 */
public class PTPayOrderDeclareCommand extends PayOrderDeclareCommand {

    private static final HaitaoTraceLogger traceLogger = HaitaoTraceLoggerFactory.getTraceLogger("order");
    public static String mcht_md5_key ="1234567890";  //商户MD5key

    @Override
    public boolean execute() throws ServiceException {
        if (portBillDeclareDO == null || stockoutOrderBO == null || StringUtils.isBlank(payBillDeclareProviderNid)
                || logisticsLineBO == null || logisticsLineBO.getPortBO() == null) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("平潭支付单申报单参数缺失")
                    .addParm("订单号：", stockoutOrderBO.getBizId())
                    .log();
            return false;
        }
        if (PortBillState.SEND_SUCCESS.getValue().equals(portBillDeclareDO.getState())
                || PortBillState.VERIFY_CALLBACK.getValue().equals(portBillDeclareDO.getState())) {
            return true;
        }
        try {
            Map<String, String> stringStringMap = buildRequest();
            boolean result = sendMessage2DeclarePayBill(stringStringMap);
            return result;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .addParm("支付宝支付支付单申报请求报文", e)
                    .setException(e)
                    .log();
        }
        return false;
    }


    private Map<String,String> buildRequest() {

        //各个口岸的meta信息中需要填写在支付宝支付中的海关编码
        Map<String, Object> portMeta = JSONUtil.parseJSONMessage(logisticsLineBO.getPortBO().getMeta());

        Map<String,String> parameters = new HashMap<String,String>();
        parameters.put("sign_type", "0"); //固定；0代表MD5
        parameters.put("service_version", "1.0");//固定 1.0
        parameters.put("input_charset", "UTF-8");
        parameters.put("request_id", stockoutOrderBO.getBizId()); //请求流水
        parameters.put("mcht_id", PayConfig.getPayProviderMeta(payBillDeclareProviderNid).get("mcht_id").toString()); //商户在开联通申请开户的商户号
        parameters.put("mcht_customs_code",logisticsLineBO.getPortBO().geteCommerceCode());//商户在海关备案的编号
        parameters.put("mcht_customs_name", logisticsLineBO.getPortBO().geteCommerceName()); //商户海关备案名称
        parameters.put("currency", "156"); //币种,暂只支持156(人民币)
        parameters.put("amount", stockoutOrderDeclarePriceBO.getOrderActualPrice().toString()); //订单金额，以分为单位，不拆单时为原订单金额，拆单时为子订单金额且不能超过原订单金额order_fee=transport_fee+product_fee+tax_fee
        parameters.put("transport_amt",stockoutOrderDeclarePriceBO.getFreightFee().toString()); //物流费用，以分为单位，不拆单时为原订单物流费用，拆单时为子订单订单物流费用
        parameters.put("product_amt", stockoutOrderDeclarePriceBO.getDeclareTotalPrice().toString());//商品费用，以分为单位，不拆单时为原订单商品费用，拆单时为子订单订单商品费用
        parameters.put("tax_amt", stockoutOrderDeclarePriceBO.getTaxFee().toString());//关税，以分为单位
        parameters.put("insured_amt", "0");//保险费，以分为单位
        parameters.put("notify_url", "http://www.fengqu.com/m.api?_mt=logistics.ptPayCallback");
        parameters.put("is_split", "Y");
        parameters.put("sub_order_no",stockoutOrderBO.getBizId() );
        parameters.put("sub_order_time", DateUtil.formatDateStr(new Date(),DateUtil.DATE_DEF_PATTERN));
        parameters.put("customs_type", portMeta.get("customs_type").toString());//海关
        parameters.put("id_type", "01"); //证件类型,暂只支持01身份证
        parameters.put("id_no", CommonUtil.getBuyerName(stockoutOrderBO)); //证件号
        parameters.put("id_name", CommonUtil.getBuyerIdNo(stockoutOrderBO)); //姓名
        parameters.put("business_type", "I10"); //业务类型,I20:保税进口 ,I10：直邮进口 如果不填，默认为I20, 保税进口
        return parameters;
    }

    private Boolean sendMessage2DeclarePayBill(Map<String,String> requestMap){

        if(requestMap == null){
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("平潭支付支付单申报单参数缺失")
                    .addParm("订单号：", stockoutOrderBO.getBizId())
                    .log();
            return false;
        }

        StringBuilder request = new StringBuilder();
        try {
            mapToRequest(requestMap,request);
            String url = PayConfig.getPayProviderInterfaceUrl(payBillDeclareProviderNid);

            String responseString = HttpUtil.postFormByHttp(url, request.toString());
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setTraceLogger(TraceLogEntity.instance(traceLogger, stockoutOrderBO.getBizId(), SystemConstants.TRACE_APP))
                    .addParm("[供应链报文-向平潭发送指令完成]订单ID", stockoutOrderBO.getBizId())
                    .addParm("请求报文", request)
                    .addParm("回复报文", responseString)
                    .log();
            return  responseToString(responseString);
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.ERROR)
                    .setErrorMsg("[供应链报文-向平潭发送指令异常]")
                    .setException(e)
                    .addParm("请求报文", request)
                    .log();
            return false;
        }
    }

    private String generateDeclareSign(Map<String,String> signMap) throws UnsupportedEncodingException{
        StringBuilder sb = new StringBuilder(128);
        List<String> list = new ArrayList<String>(signMap.keySet());
        Collections.sort(list);
        for(String key:list){
            if(StringUtils.isNotBlank(signMap.get(key))){
                sb.append(key);
                sb.append("=");
                sb.append(signMap.get(key));
                sb.append("&");
            }
        }
        return Md5Util.computeToHex(sb.append("key=").append( PayConfig.getPayProviderInterfaceKey(payBillDeclareProviderNid)).toString()
                .getBytes("utf-8")).toUpperCase();
    }

    private void mapToRequest(Map<String,String> map,StringBuilder sb) throws Exception{
            for (Map.Entry en : map.entrySet()) {
                sb.append((String) en.getKey())
                        .append("=")
                        .append((null == en.getValue())
                                || ("".equals(en.getValue())) ? ""
                                : URLEncoder.encode((String) en.getValue(),
                                "UTF-8")).append("&").toString();
            }
            sb.append("sign_msg").append("=").append(generateDeclareSign(map));
    }

    private boolean responseToString(String resultString) throws UnsupportedEncodingException{
        StockoutOrderManager stockoutOrderManager = (StockoutOrderManager) CommandConfig.getSpringBean("stockoutOrderManager");
        Map<String, String> resData = new HashMap<String, String>();
        if (null != resultString && !"".equals(resultString)) {
            if(resultString.contains("实名验证失败")){
                logger.info("支付通申报失败: "+resultString);
                createFailureMessage = "收货人的实名认证失败,购买人姓名:" + CommonUtil.getBuyerName(stockoutOrderBO);
                return  false;
            }
            String str = URLDecoder.decode(resultString, "UTF-8");
            String[] parameters = str.split("&");
            for (int i = 0; i < parameters.length; i++) {
                String msg = parameters[i];
                int index = msg.indexOf('=');
                if (index > 0) {
                    String name = msg.substring(0, index);
                    String value = msg.substring(index + 1);
                    resData.put(name, value);
                }
            }
        }
        if("0000".equals(resData.get("retcode"))){
//            stockoutOrderBO.setPayTradeNo(resData.get("declare_payment_no"));
//            stockoutOrderManager.update(stockoutOrderBO);
            stockoutOrderManager.updatePayNo(stockoutOrderBO.getId(), resData.get("declare_payment_no"));
            return true;
        }
        return false;
    }

}
