package com.sfebiz.supplychain.service.stockout.statemachine;

import com.taobao.tbbpm.statemachine.persistence.impl.StateMachineStateServiceImpl;

/**
 * 出库单状态机
 * User: 心远
 * Date: 14/12/18
 * Time: 上午1:39
 */
public class StockoutOrderStateMachineServiceImpl extends StateMachineStateServiceImpl {

    private static final String TABLE_NAME        = "SC_STOCKOUT_ORDER";

    private static final String STATE_COLUMN_NAME = "ORDER_STATE";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getStateColumn() {
        return STATE_COLUMN_NAME;
    }

    @Override
    public String getName() {
        return "stockoutOrderStateMachineService";
    }

}
