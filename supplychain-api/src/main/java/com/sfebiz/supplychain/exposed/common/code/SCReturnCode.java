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
 * </br>[1080000 - 1090000) 路由相关
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
     * command相关
     * 错误范围 1000200 -- 1000300
     */
    public final static int _C_VERSION_ERR = 1000200;
    public final static int _C_KEY_ERR = 1000201;
    public final static int _C_COMMAND_INNER_EXCEPTION = 1000202;

    public final static SCReturnCode VERSION_ERR = new SCReturnCode("version未找到，或version与key不匹配", _C_VERSION_ERR);
    public final static SCReturnCode KEY_ERROR = new SCReturnCode("根据key未找到对应class", _C_KEY_ERR);
    public final static SCReturnCode COMMAND_INNER_EXCEPTION = new SCReturnCode("创建command内部异常", _C_COMMAND_INNER_EXCEPTION);
}
