package com.sfebiz.supplychain.queue.consumer.process.stockout;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.queue.consumer.AbstractMessageProcesser;
import com.sfebiz.supplychain.queue.enums.StockoutOrderMsgTag;
import com.sfebiz.supplychain.service.stockout.biz.StockoutOrderBizService;
import com.sfebiz.supplychain.service.stockout.biz.model.StockoutOrderBO;
import com.sfebiz.supplychain.service.stockout.model.StockoutOrderBOFactory;
import com.sfebiz.supplychain.service.stockout.process.create.PortOrderValidateProcessor;

/**
 * <p>下发出库单给TPL、PAY、PORT、WMS</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/23
 * Time: 下午3:08
 */
@Component("execSendProcessEventProcesser")
public class ExecSendProcessEventProcesser extends AbstractMessageProcesser {

    @Resource
    private StockoutOrderBizService    stockoutOrderBizService;
    @Resource
    private PortOrderValidateProcessor portOrderValidateProcessor;
    @Resource
    private String                     env;
    @Resource
    private StockoutOrderBOFactory     stockoutOrderBOFactory;

    @Override
    public Action process(Message message) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("消费创建出库单消息开始")
            .addParm("消息", message).log();
        try {
            String stockoutOrderId = message.getUserProperties("stockoutOrderId");

            // 因阿里云测试环境无延迟消息，此处在测试上做类似延迟消息发送用
            if (env.startsWith("TEST")) {
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            StockoutOrderBO stockoutOrderBO = stockoutOrderBOFactory.loadStockoutBOById(Long
                .valueOf(stockoutOrderId));

            if (stockoutOrderBO != null) {
                LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("出库单信息")
                    .addParm("出库单Id", stockoutOrderId)
                    .addParm("出库单类型:", stockoutOrderBO.getOrderType()).log();

                // 销售出库单接受到消息的时候需要进行先进先出规则
                stockoutOrderBO = portOrderValidateProcessor
                    .getShouldExecuteStockoutOrderByFirstInFistOut(stockoutOrderBO);
                stockoutOrderBizService.executeStockoutSendProcesses(stockoutOrderBO, null, null);

                LogBetter.instance(logger).setLevel(LogLevel.INFO).setMsg("消费创建出库单消息成功")
                    .addParm("出库单ID", stockoutOrderId).log();
            } else {
                LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setErrorMsg("消费创建出库单消异常, 出库单不存在").addParm("消息", message)
                    .addParm("出库单ID", stockoutOrderId).addParm("查询出的出库单对象", stockoutOrderBO).log();
            }

            return Action.CommitMessage;
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.WARN).setMsg("消费创建出库单消异常").setException(e)
                .log();
            return Action.ReconsumeLater;
        }
    }

    @Override
    public String getTag() {
        return StockoutOrderMsgTag.TAG_STOCKOUTORDER_SEND_TO_PROVIDER.getTag();
    }

}
