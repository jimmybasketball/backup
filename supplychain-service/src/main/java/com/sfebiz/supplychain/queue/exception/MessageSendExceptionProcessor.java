package com.sfebiz.supplychain.queue.exception;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskStatus;
import com.sfebiz.supplychain.exposed.stockout.enums.TaskType;
import com.sfebiz.supplychain.persistence.base.stockout.domain.StockoutOrderTaskDO;
import com.sfebiz.supplychain.persistence.base.stockout.manager.StockoutOrderTaskManager;
import com.sfebiz.supplychain.queue.MessageConstants;
import com.sfebiz.supplychain.util.GsonUtil;
import com.sfebiz.supplychain.util.ListUtil;
import com.sfebiz.supplychain.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by zhaojingyang on 2015/7/4.
 */
@Component("messageSendExceptionProcessor")
public class MessageSendExceptionProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendExceptionProcessor.class);
    public static final String RE_SEND_MSG_USER_PROPERTIES = "RE_SEND_MSG_USER_PROPERTIES";

    @Resource
    private StockoutOrderTaskManager stockoutOrderTaskManager;

    public void addOrUpdateErrorTaskWhenSendError(Message message) {
        try {
            String reSendFlag = message.getUserProperties(RE_SEND_MSG_USER_PROPERTIES);
            if (RE_SEND_MSG_USER_PROPERTIES.equals(reSendFlag)) {
                LogBetter.instance(logger).setLevel(LogLevel.INFO)
                        .setMsg("消息是从Task表中重新发送异常，无须再次记录")
                        .log();
                return;
            }
            message.setStartDeliverTime(0L);//设置成0 获取时候过大会异常

            String jsonMegStr = GsonUtil.getGsonInstance().toJson(message);

            String uniqueNo = MD5Util.md5Hex(jsonMegStr);
            List<StockoutOrderTaskDO> taskDOList = stockoutOrderTaskManager.querySendMsgErrorLog(uniqueNo, TaskType.MESAGE_SEND_EXCEPTION.getValue());
            if (ListUtil.isNullOrEmpty(taskDOList)) {
                addErrorTask(uniqueNo, jsonMegStr);
            }
            LogBetter.instance(logger).setLevel(LogLevel.INFO)
                    .setMsg("供应链统一发送消息异常记录到task成功")
                    .log();
        } catch (Exception e) {
            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
                    .setErrorMsg("供应链统一发送消息异常记录到task异常" + e.getMessage())
                    .setException(e)
                    .log();
        }
    }

    /**
     * 新增发送消息日志
     *
     * @param uniqueNo
     * @param message
     */
    private void addErrorTask(String uniqueNo, String message) {
        StockoutOrderTaskDO errorTask = new StockoutOrderTaskDO();
        errorTask.setStockoutOrderId(-1L);
        errorTask.setBizId(uniqueNo);
        errorTask.setStockoutOrderState("NULL");
        errorTask.setTaskType(TaskType.MESAGE_SEND_EXCEPTION.getValue());
        errorTask.setTaskState(TaskStatus.WAIT_HANDLE.getValue());
        errorTask.setRetryExcuteTime(new Date());
        errorTask.setTaskOperator("system");
        errorTask.setTaskOwner("system");
        errorTask.setFeatures(message);
        errorTask.setTaskMemo("0");
        stockoutOrderTaskManager.insert(errorTask);
    }

}
