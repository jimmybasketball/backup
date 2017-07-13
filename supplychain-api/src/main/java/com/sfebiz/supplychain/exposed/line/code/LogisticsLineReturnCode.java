package com.sfebiz.supplychain.exposed.line.code;

import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;

/**
 * 物流线路服务响应码
 * <p>
 * [1030000 - 1040000)
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-13 16:38
 **/
public class LogisticsLineReturnCode extends SCReturnCode {

    private static final long serialVersionUID = -5495050149059597799L;

    public LogisticsLineReturnCode(String desc, int code) {
        super(desc, code);
    }

    public final static int _C_LINE_UNKNOWN_ERROR = 1030000;

    public final static LogisticsLineReturnCode LINE_UNKNOWN_ERROR = new LogisticsLineReturnCode("线路服务未知异常", _C_LINE_UNKNOWN_ERROR);


}
