package com.sfebiz.supplychain.service.stockin.statemachine;

import com.taobao.tbbpm.statemachine.persistence.impl.StateMachineStateServiceImpl;

/**
 * Created by zhangyajing on 2017/7/14.
 */
public class StockinOrderStateMachineServiceImpl extends StateMachineStateServiceImpl{

    private static final String TABLE_NAME = "SC_STOCKIN_ORDER";

    private static final String STATE_COLUM_NAME = "STATE";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getStateColumn() {
        return STATE_COLUM_NAME;
    }

    @Override
    public String getName() {
        return "stockinOrderStateMachineService";
    }
}
