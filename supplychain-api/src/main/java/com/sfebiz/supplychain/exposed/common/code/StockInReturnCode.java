package com.sfebiz.supplychain.exposed.common.code;

/**
 * 入库相关的响应码
 * [1060000 - 1070000)
 * @author zhangyajing
 * @date 2017-07-13 15:50
 **/
public class StockInReturnCode extends SCReturnCode{

    private static final long serialVersionUID = -8280232703481218839L;

    public StockInReturnCode(String desc, int code) {
        super(desc, code);
    }

    public final static StockInReturnCode STOCKIN_ORDER_NOT_EXIST = new StockInReturnCode("入库单不存在", 1060000);
    public final static StockInReturnCode STOCKIN_ORDER_INNER_EXCEPTION = new StockInReturnCode("入库单系统内部异常", 1060001);
    public final static StockInReturnCode STOCKIN_ORDER_NOT_ALLOW_FINISH = new StockInReturnCode("当前状态不允许入库单完成", 1060002);
    public final static StockInReturnCode STOCKIN_ORDER_NOT_ALLOW_SUBMIT = new StockInReturnCode("当前状态不允许入库单提交", 1060003);
    public final static StockInReturnCode STOCKIN_ORDER_SENDSTOCK_FAIL = new StockInReturnCode("调用仓库接口失败", 1060004);
    public final static StockInReturnCode LOGISTICS_STOCKINID_ERR = new StockInReturnCode("SKU入库失败", 1060005);
    public final static StockInReturnCode STOCKIN_ORDER_NOT_ALLOW_UPDATE = new StockInReturnCode("当前状态不允许入库单修改", 1060006);
    public final static StockInReturnCode STOCKIN_ORDER_SKU_NOT_FOUND = new StockInReturnCode("入库单商品未找到", 1060007);
}
