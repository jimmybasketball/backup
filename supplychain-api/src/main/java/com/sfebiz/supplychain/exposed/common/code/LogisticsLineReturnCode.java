package com.sfebiz.supplychain.exposed.common.code;

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

    public final static int _C_LINE_CONCURRENT_EXCEPTION = 1030000;
    public final static int _C_LINE_UNKNOWN_ERROR = 1030001;
    public final static int _C_LINE_WRONG_STATE = 1030002;
    public final static int _C_LINE_NOT_EXIST = 1030003;
    public final static int _C_LINE_ALREADY_CHANGE_STATE = 1030004;
    public final static int _C_LINE_ALREADY_CHANGE_OPERATE_STATE = 1030005;
    public final static int _C_LINE_WRONG_OPERATE_STATE = 1030005;

    public final static LogisticsLineReturnCode LINE_CONCURRENT_EXCEPTION = new LogisticsLineReturnCode("线路服务并发异常", _C_LINE_CONCURRENT_EXCEPTION);
    public final static LogisticsLineReturnCode LINE_UNKNOWN_ERROR = new LogisticsLineReturnCode("线路服务未知异常", _C_LINE_UNKNOWN_ERROR);
    public final static LogisticsLineReturnCode LINE_WRONG_STATE = new LogisticsLineReturnCode("线路状态值不合法", _C_LINE_WRONG_STATE);
    public final static LogisticsLineReturnCode LINE_NOT_EXIST = new LogisticsLineReturnCode("线路不存在", _C_LINE_NOT_EXIST);
    public final static LogisticsLineReturnCode LINE_ALREADY_CHANGE_STATE = new LogisticsLineReturnCode("线路状态已被修改", _C_LINE_ALREADY_CHANGE_STATE);
    public final static LogisticsLineReturnCode LINE_ALREADY_CHANGE_OPERATE_STATE = new LogisticsLineReturnCode("线路运营状态已被修改", _C_LINE_ALREADY_CHANGE_OPERATE_STATE);
    public final static LogisticsLineReturnCode LINE_WRONG_OPERATE_STATE = new LogisticsLineReturnCode("线路运营状态不合法", _C_LINE_WRONG_OPERATE_STATE);


}
