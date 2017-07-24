package com.sfebiz.supplychain.exposed.common.code;

import com.sfebiz.supplychain.exposed.common.enums.LogisticsReturnCode;

public class StockoutReturnCode extends SCReturnCode {

    private static final long serialVersionUID = 1276395419162099579L;

    public StockoutReturnCode(String desc, int code) {
        super(desc, code);
    }

    /**
     * StockoutServiceErrorCode
     * <p/>
     * 错误范围 2110000 -- 2120000
     */
    public final static int                 STOCKOUT_ORDER_WAVENO_UPDATE_ERROR_CODE           = 2110000;
    public final static int                 STOCKOUT_ORDER_ORDER_STATE_UPDATE_ERROR_CODE      = 2110001;
    public final static int                 STOCKOUT_ORDER_UPDATE_WAVENO_EXISTS_ERROR_CODE    = 2110002;
    public final static int                 STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION_CODE        = 2110003;
    public final static int                 STOCKOUT_ORDER_PARAM_ILLIGAL_CODE                 = 2110004;
    public final static int                 STOCKOUT_ORDER_STOCKOUT_EXCEPTION_CODE            = 2110005;
    public final static int                 STOCKOUT_ORDER_SEND_EXCEPTION_CODE                = 2110006;
    public final static int                 STOCKOUT_ORDER_REQUEST_PARAM_ILLEGAL_CODE         = 2110007;
    public final static int                 STOCKOUT_ORDER_TPL_CREATE_FAILURE_CODE            = 2110010;
    public final static int                 STOCKOUT_ORDER_PAY_CREATE_FAILURE_CODE            = 2110011;
    public final static int                 STOCKOUT_ORDER_PORT_CREATE_FAILURE_CODE           = 2110012;
    public final static int                 STOCKOUT_ORDER_WMS_CREATE_FAILURE_CODE            = 2110013;
    public final static int                 STOCKOUT_ORDER_CANNOT_CLOSE_CODE                  = 2110014;
    public final static int                 STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE_CODE          = 2110015;
    public final static int                 STOCKOUT_ORDER_TPL_SEND_FAILURE_CODE              = 2110016;
    public final static int                 STOCKOUT_ORDER_WMS_SEND_FAILURE_CODE              = 2110017;
    public final static int                 STOCKOUT_COMMON_DECLARE_TYPE_NOT_FOUND_CODE       = 2110018;
    public final static int                 STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE_CODE     = 2110019;
    public final static int                 STOCKOUT_ORDER_CONFIG_FAILURE_CODE                = 2110020;
    public final static int                 COMMAND_NOT_SUPPORT_CODE                          = 2110021;
    public final static int                 STOCKOUT_ORDER_STATE_TO_SINGED_NOT_ALLOW_CODE     = 2110022;
    public final static int                 STOCKOUT_ORDER_OBJECT_CONVERT_ERROR_CODE          = 2110023;
    public final static int                 STOCKOUT_ORDER_OBJECT_CONVERT_PARAMS_ILLEGAL_CODE = 2110024;
    public final static int                 STOCKOUT_ORDER_NOT_ALLOW_RESPLIT_CODE             = 2110025;
    public final static int                 STOCKOUT_ORDER_NOT_GET_ZTO_REMARK_CODE            = 2110026;
    public final static int                 PRESELL_ORDER_CREATE_ERROR_CODE                   = 2110027;
    public final static int                 STOCKOUT_ORDER_CCB_CONFIRM_WEIGHT_ERROR_CODE      = 2110028;
    public final static int                 STOCKOUT_ORDER_CCB_CONFIRM_ERROR_CODE             = 2110029;
    public final static int                 STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL_CODE     = 2110030;
    public final static int                 STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR_CODE  = 2110031;

    public final static LogisticsReturnCode STOCKOUT_ORDER_WAVENO_UPDATE_ERROR                = new LogisticsReturnCode(
                                                                                                  "出库单波次号更新失败",
                                                                                                  STOCKOUT_ORDER_WAVENO_UPDATE_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_ORDER_STATE_UPDATE_ERROR           = new LogisticsReturnCode(
                                                                                                  "供应链推送订单状态更改失败",
                                                                                                  STOCKOUT_ORDER_ORDER_STATE_UPDATE_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_UPDATE_WAVENO_EXISTS_ERROR         = new LogisticsReturnCode(
                                                                                                  "波次号已存在，更新波次号失败",
                                                                                                  STOCKOUT_ORDER_UPDATE_WAVENO_EXISTS_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION             = new LogisticsReturnCode(
                                                                                                  "单据状态流转异常",
                                                                                                  STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_PARAM_ILLIGAL                      = new LogisticsReturnCode(
                                                                                                  "出库单参数不合法",
                                                                                                  STOCKOUT_ORDER_PARAM_ILLIGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_STOCKOUT_EXCEPTION                 = new LogisticsReturnCode(
                                                                                                  "出库单下发出库异常",
                                                                                                  STOCKOUT_ORDER_STOCKOUT_EXCEPTION_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_SEND_EXCEPTION                     = new LogisticsReturnCode(
                                                                                                  "出库单发货异常",
                                                                                                  STOCKOUT_ORDER_SEND_EXCEPTION_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_REQUEST_PARAM_ILLEGAL              = new LogisticsReturnCode(
                                                                                                  "出库单报文请求参数不合法",
                                                                                                  STOCKOUT_ORDER_REQUEST_PARAM_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_TPL_CREATE_FAILURE                 = new LogisticsReturnCode(
                                                                                                  "三方物流TPL订单下发失败",
                                                                                                  STOCKOUT_ORDER_TPL_CREATE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_TPL_SEND_FAILURE                   = new LogisticsReturnCode(
                                                                                                  "三方物流TPL运单确认失败",
                                                                                                  STOCKOUT_ORDER_TPL_SEND_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_PAY_CREATE_FAILURE                 = new LogisticsReturnCode(
                                                                                                  "支付单申报下发失败",
                                                                                                  STOCKOUT_ORDER_PAY_CREATE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_PORT_CREATE_FAILURE                = new LogisticsReturnCode(
                                                                                                  "口岸订单申报下发失败",
                                                                                                  STOCKOUT_ORDER_PORT_CREATE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_WMS_CREATE_FAILURE                 = new LogisticsReturnCode(
                                                                                                  "仓库订单下发失败",
                                                                                                  STOCKOUT_ORDER_WMS_CREATE_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_WMS_SEND_FAILURE                   = new LogisticsReturnCode(
                                                                                                  "仓库订单下发发货失败",
                                                                                                  STOCKOUT_ORDER_WMS_SEND_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CANNOT_CLOSE                       = new LogisticsReturnCode(
                                                                                                  "出库单状态不允许关闭",
                                                                                                  STOCKOUT_ORDER_CANNOT_CLOSE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE               = new LogisticsReturnCode(
                                                                                                  "出库单状态流转参数不合法，缺少ID",
                                                                                                  STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE_CODE);
    public final static LogisticsReturnCode STOCKOUT_COMMON_DECLARE_TYPE_NOT_FOUND            = new LogisticsReturnCode(
                                                                                                  "未找到共通的申报方式",
                                                                                                  STOCKOUT_COMMON_DECLARE_TYPE_NOT_FOUND_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CONFIG_EXCEPTION                   = new LogisticsReturnCode(
                                                                                                  "系统配置出错",
                                                                                                  STOCKOUT_ORDER_CONFIG_FAILURE_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE          = new LogisticsReturnCode(
                                                                                                  "口岸避税验证失败",
                                                                                                  STOCKOUT_ORDER_WMS_PORT_VALIDATE_FAILURE_CODE);
    public final static LogisticsReturnCode COMMAND_NOT_SUPPORT                               = new LogisticsReturnCode(
                                                                                                  "命令不支持",
                                                                                                  COMMAND_NOT_SUPPORT_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_STATE_TO_SINGED_NOT_ALLOW          = new LogisticsReturnCode(
                                                                                                  "出库单状态不允许流转到已签收",
                                                                                                  STOCKOUT_ORDER_STATE_TO_SINGED_NOT_ALLOW_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_OBJECT_CONVERT_ERROR               = new LogisticsReturnCode(
                                                                                                  "对象转换异常",
                                                                                                  STOCKOUT_ORDER_OBJECT_CONVERT_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_OBJECT_CONVERT_PARAMS_ILLEGAL      = new LogisticsReturnCode(
                                                                                                  "对象转换参数不合法",
                                                                                                  STOCKOUT_ORDER_OBJECT_CONVERT_PARAMS_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_NOT_ALLOW_RESPLIT                  = new LogisticsReturnCode(
                                                                                                  "出库单状态不允许重新分包",
                                                                                                  STOCKOUT_ORDER_NOT_ALLOW_RESPLIT_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_NOT_GET_ZTO_REMARK                 = new LogisticsReturnCode(
                                                                                                  "获取中通大头笔失败",
                                                                                                  STOCKOUT_ORDER_NOT_GET_ZTO_REMARK_CODE);
    public final static LogisticsReturnCode PRESELL_ORDER_CREATE_ERROR                        = new LogisticsReturnCode(
                                                                                                  "预售订单暂不能创建出库单",
                                                                                                  PRESELL_ORDER_CREATE_ERROR_CODE);
    public final static LogisticsReturnCode DELAY_ORDER_CREATE_ERROR                          = new LogisticsReturnCode(
                                                                                                  "G20杭州订单暂不能创建出库单",
                                                                                                  PRESELL_ORDER_CREATE_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CCB_CONFIRM_WEIGHT_ERROR           = new LogisticsReturnCode(
                                                                                                  "出库单下发清关确认重量参数不合法",
                                                                                                  STOCKOUT_ORDER_CCB_CONFIRM_WEIGHT_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CCB_CONFIRM_ERROR                  = new LogisticsReturnCode(
                                                                                                  "出库单下发清关确认失败",
                                                                                                  STOCKOUT_ORDER_CCB_CONFIRM_ERROR_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL          = new LogisticsReturnCode(
                                                                                                  "订单下发海关总署，参数不合法",
                                                                                                  STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_ILLEGAL_CODE);
    public final static LogisticsReturnCode STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR       = new LogisticsReturnCode(
                                                                                                  "订单下发海关总署，发送异常",
                                                                                                  STOCKOUT_ORDER_CUSTOMSOFFICE_MSG_SEND_ERROR_CODE);
}
