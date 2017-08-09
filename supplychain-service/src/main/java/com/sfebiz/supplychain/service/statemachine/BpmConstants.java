package com.sfebiz.supplychain.service.statemachine;

/**
 * <p>状态机相关常量</p>
 * Created by zhangyajing on 2017/7/18.
 */
public class BpmConstants {

    /**
     * 状态机起始状态，当状态机启动时，会默认从 NULL 到用户定义的第一个状态；
     */
    public static final String NULL_STATE                          = "NULL";

    /**
     * 出库单下发处理流程Process Code
     */
    public static final String STOCKOUTORDER_SEND_PROCESS_BPM_CODE = "process.stockout.StockoutOrderSendProcess";
}
