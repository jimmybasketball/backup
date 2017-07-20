package com.sfebiz.supplychain.exposed.common.code;

/**
 * 流程引擎相关的响应码
 * [1070000 - 1080000)
 * @author zhangyajing
 * @date 2017-07-13 15:50
 **/
public class StatemationReturnCode extends SCReturnCode{
    private static final long serialVersionUID = 8830174189175238260L;

    public StatemationReturnCode(String desc, int code) {
        super(desc, code);
    }

    public final static StatemationReturnCode LOGISTICS_STATE_MACHINE_INTERNAL_EXCEPTION = new StatemationReturnCode("状态机内部异常", 1070000);
    public final static StatemationReturnCode STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION = new StatemationReturnCode("单据状态流转异常", 1070001);
    public final static StatemationReturnCode STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE = new StatemationReturnCode("状态流转参数不合法，缺少ID", 1070002);
}
