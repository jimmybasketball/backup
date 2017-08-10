package com.sfebiz.supplychain.service.stockout.biz;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.openapi.common.config.OpenApiConfig;
import com.sfebiz.openapi.common.config.OpenApiConfigKeys;
import com.sfebiz.supplychain.factory.SpringBeanFactory;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderManager;
import com.sfebiz.supplychain.queue.MessageConstants;
import com.sfebiz.supplychain.queue.MessageProducer;
import com.sfebiz.supplychain.queue.enums.StockoutOrderMsgTag;
import com.sfebiz.supplychain.service.stockout.config.LogisticsConfig;

/**
 * 
 * <p>出库单消息队列相关通知服务</p>
 * 
 * @author matt
 * @Date 2017年7月26日 下午6:57:13
 */
@Service("stockoutOrderNoticeBizService")
public class StockoutOrderNoticeBizService {

    private static final Logger  LOGGER = LoggerFactory
                                            .getLogger(StockoutOrderNoticeBizService.class);

    @Resource
    private StockoutOrderManager stockoutOrderManager;

    /**
     * 通知执行出库单下发流程，下发出库单指令给三方系统
     *
     * @param stockoutOrderId
     */
    public void noticeExecStockoutSendProcess(long stockoutOrderId) {
        Integer delayTime = LogisticsConfig.getInstance().getSendStockoutDelayTime();
        if (null == delayTime || delayTime == 0) {
            delayTime = 60000;
        }
        noticeExecStockoutSendProcessWithDelayTime(stockoutOrderId, delayTime);
    }

    /**
     * 通知执行出库单下发流程，下发出库单指令给三方系统, 带 Delay 延迟时间方法
     *
     * @param stockoutOrderId
     * @param delayTime
     */
    public void noticeExecStockoutSendProcessWithDelayTime(long stockoutOrderId, long delayTime) {
        try {
            Message message = new Message();
            message.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_EVENT);
            message.setTag(StockoutOrderMsgTag.TAG_STOCKOUTORDER_SEND_TO_PROVIDER.getTag());
            Properties properties = new Properties();
            properties.put("stockoutOrderId", String.valueOf(stockoutOrderId));
            message.setUserProperties(properties);
            message.setStartDeliverTime(System.currentTimeMillis() + delayTime);
            try {
                message.setBody(" ".getBytes("UTF-8"));//BODY不能为空
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("消息内容设置异常", e);
            }
            SpringBeanFactory.getBean("supplyChainMessageProducer", MessageProducer.class).send(
                message);
        } catch (Exception e) {
            LogBetter.instance(LOGGER).setLevel(LogLevel.ERROR)
                .addParm("发送出库单下发供应商消息异常，出库单id：", stockoutOrderId).setException(e).log();
        }
    }

    /**
     * 出库单状态更新时，作为仓库通知商户状态更新
     */
    public void sendMsgStockoutFinish2Merchant(String bizId, int errorCode, String errorMessage) {
        //异步调用
        StockoutOrderDO stockoutOrderDO = stockoutOrderManager.getByBizId(bizId);
        try {
            if (null == stockoutOrderDO) {
                return;
            }
            LogBetter.instance(LOGGER).setLevel(LogLevel.INFO).setMsg("[供应链-供应链]：作为仓库发送状态更新信息给商户")
                .addParm("子订单号", stockoutOrderDO.getBizId())
                .addParm("商户订单号", stockoutOrderDO.getMerchantOrderNo())
                .addParm("商户ID", stockoutOrderDO.getMerchantId()).log();

            String notifyUrl = OpenApiConfig.getInstance().getRule(
                String.valueOf(stockoutOrderDO.getMerchantId()), OpenApiConfigKeys.NOTIFY_URL); // TODO matt
            if (StringUtils.isNotBlank(notifyUrl)) {
                Message msg = new Message();
                //msg.setTopic(MessageConstants.TOPIC_OPEN_WMS_ROUTE_EVENT);
                msg.setTag(StockoutOrderMsgTag.TAG_OPEN_WMS_ROUTE_FETCH.getTag());
                //消息体没有内容
                msg.setBody(" ".getBytes("UTF-8"));
                msg.setStartDeliverTime(System.currentTimeMillis());

                Properties properties = new Properties();
                properties.put("bizId", bizId);
                properties.put("errorCode", errorCode);
                properties.put("errorMessage", errorMessage);
                msg.setUserProperties(properties);
                //openWmsRouteMessageProducer.send(msg); // TODO
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
