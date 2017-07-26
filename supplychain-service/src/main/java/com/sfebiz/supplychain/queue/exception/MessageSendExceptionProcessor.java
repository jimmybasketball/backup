package com.sfebiz.supplychain.queue.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zhaojingyang on 2015/7/4.
 */
@Component("messageSendExceptionProcessor")
public class MessageSendExceptionProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendExceptionProcessor.class);
    public static final String RE_SEND_MSG_USER_PROPERTIES = "RE_SEND_MSG_USER_PROPERTIES";

//    @Resource
//    private StockoutOrderTaskManager stockoutOrderTaskManager;
//
//    public void addOrUpdateErrorTaskWhenSendError(Message message) {
//        try {
//            String reSendFlag = message.getUserProperties(RE_SEND_MSG_USER_PROPERTIES);
//            if (RE_SEND_MSG_USER_PROPERTIES.equals(reSendFlag)) {
//                LogBetter.instance(logger).setLevel(LogLevel.INFO)
//                        .setMsg("消息是从Task表中重新发送异常，无须再次记录")
//                        .log();
//                return;
//            }
//            message.setStartDeliverTime(0L);//设置成0 获取时候过大会异常
//
//            String jsonMegStr = GsonUtil.getGsonInstance().toJson(message);
//
//            String uniqueNo = MD5Util.md5Hex(jsonMegStr);
//            List<StockoutOrderTaskDO> taskDOList = stockoutOrderTaskManager.querySendMsgErrorLog(uniqueNo, TaskType.MESAGE_SEND_EXCEPTION.getValue());
//            if (ListUtil.isNullOrEmpty(taskDOList)) {
//                addErrorTask(uniqueNo, jsonMegStr);
//            }
//            LogBetter.instance(logger).setLevel(LogLevel.INFO)
//                    .setMsg("供应链统一发送消息异常记录到task成功")
//                    .log();
//        } catch (Exception e) {
//            LogBetter.instance(logger).setLevel(LogLevel.ERROR)
//                    .setErrorMsg("供应链统一发送消息异常记录到task异常" + e.getMessage())
//                    .setException(e)
//                    .log();
//        }
//    }
//
//    /**
//     * 新增发送消息日志
//     *
//     * @param uniqueNo
//     * @param message
//     */
//    private void addErrorTask(String uniqueNo, String message) {
//        StockoutOrderTaskDO errorTask = new StockoutOrderTaskDO();
//        errorTask.setStockoutOrderId(-1L);
//        errorTask.setBizId(uniqueNo);
//        errorTask.setStockoutOrderStatus("NULL");
//        errorTask.setTaskType(TaskType.MESAGE_SEND_EXCEPTION.getValue());
//        errorTask.setTaskStatus(TaskStatus.WAIT_HANDLE.getValue());
//        errorTask.setExcuteTime(new Date());
//        errorTask.setOperator("system");
//        errorTask.setOwner("system");
//        errorTask.setFeatures(message);
//        errorTask.setMemo("0");
//        stockoutOrderTaskManager.insert(errorTask);
//    }
//
//    public static void main(String args[])throws Exception{
//        Message message = new Message();
//        message.setTopic(MessageConstants.TOPIC_SUPPLY_CHAIN_EVENT);
//        message.setTag(MessageConstants.TAG_CONSIGMENT_SKU_STOCKOUT);
//        Properties properties = new Properties();
//        properties.put("stockoutOrderId", String.valueOf(360104L));
//        message.setUserProperties(properties);
//        message.setBody("1".getBytes("UTF-8"));//BODY不能为空
//
//        String dd = JSON.toJSONString(message);
//        System.out.println(dd);
//
//        Message msg = JSON.parseObject(dd, Message.class);
//        System.out.println(msg.getTag());
//
//    }
}
