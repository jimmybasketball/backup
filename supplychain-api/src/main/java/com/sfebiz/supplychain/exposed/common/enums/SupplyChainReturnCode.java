package com.sfebiz.supplychain.exposed.common.enums;

import net.pocrd.entity.AbstractReturnCode;

/**
 * 响应码
 */
public class SupplyChainReturnCode extends AbstractReturnCode{


    public SupplyChainReturnCode(String desc, int code) {
        super(desc, code);
    }

    /**
     * 系统异常
     */
    public final static SupplyChainReturnCode SUCCESS = new SupplyChainReturnCode("成功", 1010001);
    public final static SupplyChainReturnCode FAIL = new SupplyChainReturnCode("失败", 1010002);

    /**
     * 流程引擎
     */
    public final static SupplyChainReturnCode LOGISTICS_STATE_MACHINE_INTERNAL_EXCEPTION = new SupplyChainReturnCode("状态机内部异常", 1070000);
    public final static SupplyChainReturnCode STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION = new SupplyChainReturnCode("单据状态流转异常", 1070001);
    public final static SupplyChainReturnCode STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE = new SupplyChainReturnCode("出库单状态流转参数不合法，缺少ID", 1070002);
    public final static SupplyChainReturnCode STOCKIN_ORDER_PARAMS_ILLEGAL = new SupplyChainReturnCode("入库单参数不合法",1070003);
    public final static SupplyChainReturnCode STOCKIN_ORDER_NOT_EXIST = new SupplyChainReturnCode("入库单不存在", 1070004);
    public final static SupplyChainReturnCode STOCKIN_ORDER_INNER_EXCEPTION = new SupplyChainReturnCode("入库单系统内部异常", 1070005);
}
