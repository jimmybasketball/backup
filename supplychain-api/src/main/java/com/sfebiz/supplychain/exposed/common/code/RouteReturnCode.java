package com.sfebiz.supplychain.exposed.common.code;

/**
 * 路由服务响应码
 *
 * </br>[1080000 - 1090000) 路由相关
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-28 13:41
 **/
public class RouteReturnCode extends SCReturnCode{

    private static final long serialVersionUID = 8282132555663938469L;

    public RouteReturnCode(String desc, int code) {
        super(desc, code);
    }

    /**
     *
     * 用户路由 [1080000 - 1081000)
     *
     */
    public final static int _C_USER_ROUTE_CONCURRENT_EXCEPTION = 1080000;
    public final static int _C_USER_ROUTE_UNKNOWN_ERROR = 1080001;
    public final static int _C_USER_ROUTE_STOCKOUT_ORDER_NOT_EXIST = 1080002;

    public final static RouteReturnCode USER_ROUTE_CONCURRENT_EXCEPTION =  new RouteReturnCode("并发异常",_C_USER_ROUTE_CONCURRENT_EXCEPTION);
    public final static RouteReturnCode USER_ROUTE_UNKNOWN_ERROR =  new RouteReturnCode("用户路由服务未知异常",_C_USER_ROUTE_UNKNOWN_ERROR);
    public final static RouteReturnCode USER_ROUTE_STOCKOUT_ORDER_NOT_EXIST =  new RouteReturnCode("出库单不存在",_C_USER_ROUTE_STOCKOUT_ORDER_NOT_EXIST);



    public final static int _C_REGIST_KD100_ROUTES_FAIL = 1081000;
    public final static int _C_MAIL_NO_FIND_MULTI_STOCKOUT_ORDER = 1081001;
    public final static int _C_SEND_FETCH_ROUTE_MSG_ERROR= 1081002;
    public final static RouteReturnCode REGIST_KD100_ROUTES_FAIL =  new RouteReturnCode("注册快递100失败",_C_REGIST_KD100_ROUTES_FAIL);
    public final static RouteReturnCode MAIL_NO_FIND_MULTI_STOCKOUT_ORDER =  new RouteReturnCode("根据运单号查出多个执行中的出库单",_C_MAIL_NO_FIND_MULTI_STOCKOUT_ORDER);
    public final static RouteReturnCode SEND_FETCH_ROUTE_MSG_ERROR =  new RouteReturnCode("根据运单号查出多个执行中的出库单",_C_SEND_FETCH_ROUTE_MSG_ERROR);



}
