package com.sfebiz.supplychain.exposed.common.code;

import net.pocrd.entity.AbstractReturnCode;

/**
 * 
 * <p>
 * 供应链通用错误码 误码范围 （1000000 -- 2000000） 
 * </br>[1000000 - 1001000) 通用接口码
 * </br>[1010000 - 1020000) 仓库相关
 * </br>[1020000 - 1030000) 货主相关 
 * </br>[1030000 - 1040000) 线路相关 
 * </br>[1040000 - 1050000) 商品相关
 * </br>[1050000 - 1060000) 出库相关 
 * </br>[1060000 - 1070000) 入库相关
 * </br>[1070000 - 1080000) 流程引擎
 * </p>
 * 
 * @author matt
 * @Date 2017年7月13日 下午2:26:05
 */
public class SCReturnCode extends AbstractReturnCode {

    private static final long serialVersionUID = -8944910749594636768L;

    public SCReturnCode(String desc, int code) {
	super(desc, code);
    }

    /**
     * 系统公共接口响应码
     * <p/>
     * 错误范围 1000000 -- 1010000
     */
    public final static int _C_COMMON_SUCCESS = 1000000;
    public final static int _C_COMMON_FAIL = 1000001;
    public final static int _C_COMMON_SYSTEM_MAINTAINING = 1000002;

    public final static SCReturnCode COMMON_SUCCESS = new SCReturnCode("成功",
	    _C_COMMON_SUCCESS);
    public final static SCReturnCode COMMON_FAIL = new SCReturnCode("失败",
	    _C_COMMON_FAIL);
    public final static SCReturnCode COMMON_SYSTEM_MAINTAINING = new SCReturnCode("系统维护中，请稍后重试",
	    _C_COMMON_SYSTEM_MAINTAINING);
    
    /**
     * 参数相关
     * <p/>
     * 错误范围 1000100 -- 1000200
     * 
     */
    public final static int _C_PARAM_ILLEGAL_ERR = 1000100;
    
    public final static SCReturnCode PARAM_ILLEGAL_ERR = new SCReturnCode("参数非法",
	    _C_PARAM_ILLEGAL_ERR);

    /**
     * 入库单
     * 错误码范围 1060000 - 1070000
     */
    public final static SCReturnCode STOCKIN_ORDER_NOT_EXIST = new SCReturnCode("入库单不存在", 1060000);
    public final static SCReturnCode STOCKIN_ORDER_INNER_EXCEPTION = new SCReturnCode("入库单系统内部异常", 1060001);

    /**
     * 流程引擎
     * 错误码范围 1070000 - 1080000
     */
    public final static SCReturnCode LOGISTICS_STATE_MACHINE_INTERNAL_EXCEPTION = new SCReturnCode("状态机内部异常", 1070000);
    public final static SCReturnCode STOCKOUT_ORDER_STATE_CHANGE_EXCEPTION = new SCReturnCode("单据状态流转异常", 1070001);
    public final static SCReturnCode STOCKOUT_ORDER_ENGINE_PARAM_ILLAGLE = new SCReturnCode("状态流转参数不合法，缺少ID", 1070002);

}
