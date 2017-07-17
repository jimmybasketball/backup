package com.sfebiz.supplychain.open.exposed.api;

import net.pocrd.entity.AbstractReturnCode;

/**
 * [1900000 -- 2000000) 错误码范围
 * 
 * <p>类描述</p>
 * @author matt
 * @Date 2017年7月17日 上午11:42:00
 */
public class SCOpenReturnCode extends AbstractReturnCode {

    /** 序号 */
    private static final long serialVersionUID = 7328439687605401276L;
    
    public SCOpenReturnCode(String desc, int code) {
	super(desc, code);
    }
    
    /** 公共异常码 */
    public final static int _C_COMMON_SUCCESS = 1900000;
    public final static int _C_COMMON_FAIL = 1900001;
    public final static int _C_COMMON_SYSTEM_MAINTAINING = 1900002;
    public final static int _C_COMMON_PARAMS_ILLEGAL = 1900003;
    
    public final static SCOpenReturnCode COMMON_SUCCESS = new SCOpenReturnCode("成功",
	    _C_COMMON_SUCCESS);
    public final static SCOpenReturnCode COMMON_FAIL = new SCOpenReturnCode("失败",
	    _C_COMMON_FAIL);
    public final static SCOpenReturnCode COMMON_SYSTEM_MAINTAINING = new SCOpenReturnCode("系统维护中，请稍后重试",
	    _C_COMMON_SYSTEM_MAINTAINING);
    public final static SCOpenReturnCode COMMON_PARAMS_ILLEGAL = new SCOpenReturnCode("请求参数非法",
	    _C_COMMON_PARAMS_ILLEGAL);
    
}
