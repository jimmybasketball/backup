package com.sfebiz.supplychain.exposed.common.entity.code;

import net.pocrd.entity.AbstractReturnCode;

/**
 * 
 * <p>
 * 供应链通用错误码 误码范围 （1000000 -- 2000000） 1000 以下为通用错误码 [1010000 - 1020000) 仓库相关
 * [1020000 - 1030000) 货主相关 [1030000 - 1040000) 线路相关 [1040000 - 1050000) 商品相关
 * [1050000 - 1060000) 出库相关 [1060000 - 1070000) 入库相关
 * 
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

    public final static SCReturnCode COMMON_SUCCESS = new SCReturnCode("成功",
	    _C_COMMON_SUCCESS);
    public final static SCReturnCode COMMON_FAIL = new SCReturnCode("失败",
	    _C_COMMON_FAIL);
}
