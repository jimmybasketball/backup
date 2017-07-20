/**
 * Copyright (C) 2017 上海牵趣网络科技有限公司 版权所有
 */
package com.sfebiz.supplychain.exposed.common.code;

/**
 * <p>出库异常单相关服务的错误码定义</p>
 * 错误码范围：[1050000 - 1060000]
 * @author wuyun
 * @Date 2017年7月19日 上午11:58:07
 */
public class StockoutTaskReturnCode  extends SCReturnCode {

    /**
     * 
     */
    private static final long serialVersionUID = -706319727344018487L;

    /**
     * @param desc
     * @param code
     */
    public StockoutTaskReturnCode(String desc, int code) {
        super(desc, code);
    }
    
    public final static int _C_STOCKOUTTASK_CONCURRENT_EXCEPTION = 1050001;
    public final static SCReturnCode STOCKOUTTASK_CONCURRENT_EXCEPTION = new SCReturnCode("出库异常单重试服务并发异常",
            _C_STOCKOUTTASK_CONCURRENT_EXCEPTION);
    
    public final static int _C_STOCKOUTTASK_UNKNOWN_ERROR = 1050001;
    public final static SCReturnCode STOCKOUTTASK_UNKNOWN_ERROR = new SCReturnCode("出库异常单重试服务未知异常",
            _C_STOCKOUTTASK_UNKNOWN_ERROR);
    
    public final static int _C_STOCKOUTTASK_NOT_EXIST = 1050002;
    public final static SCReturnCode STOCKOUTTASK_NOT_EXIST = new SCReturnCode("出库异常单重试服务task任务不存在",
            _C_STOCKOUTTASK_NOT_EXIST);

}
