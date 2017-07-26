package com.sfebiz.supplychain.config;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/4/16
 * Time: 下午6:49
 */
public class SystemConstants {

    /**
     * 请求key
     */
    public static final String REQUEST = "request";

    /**
     * 返回key
     */
    public static final String RESULT = "result";

    /**
     * tracelog使用的traceApp
     */
    public static final String TRACE_APP = "supplychain";


    /**
     * 国际段路由信息
     */
    public static final String TRACE_APP_INTERNATIONAL = "supplychainInternational";

    /**
     * 清关段路由信息
     */
    public static final String TRACE_APP_CLEARANCE = "supplychainClearance";


    /**
     * 自定义路由信息
     */
    public static final String TRACE_APP_USERDEFINE = "supplychainUserDefine";

    /**
     * 申报方式为空
     */
    public static final String DECLARE_EMPTY = "DECLARE_EMPTY";

    /**
     * 申报方式--连连
     */
    public static final String DECLARE_LIANLIAN = "LIANLIANPAY";

    /**
     * 申报方式--易汇金
     */
    public static final String DECLARE_YIHUIJINPAY = "YIHUIJINPAY";

    /**
     * 申报方式--易极付
     */
    public static final String DECLARE_YIJIFUPAY = "YIJIFUPAY";

    /**
     * 申报方式--宝付
     */
    public static final String DECLARE_BAOFOOPAY = "BAOFOOPAY";

    /**
     * 申报方式--拉卡拉
     */
    public static final String DECLARE_LAKALAPAY = "LAKALAPAY";

    /**
     * 申报方式--易汇金（第三方使用）
     */
    public static final String DECLARE_NEWYIHUIJINPAY = "NEWYIHUIJINPAY";

    /**
     * 申报方式--财付通
     */
    public static final String DECLARE_TENPAY = "TENPAY";

    /**
     * 销售退后时，商品ID前缀
     */
    public static final String SKUID_PREFIX = "T";

    /**
     * 创建出库单时，包裹起始编号
     */
    public static final int START_INDEX = 1;

    /**
     * 线上开发环境
     */
    public static final String ENV_ONLINE = "ONLINE";

    public static final String INFO_LEVEL = "INFO";
    public static final String WARN_LEVEL = "WARN";
    public static final String ERROR_LEVEL = "ERROR";

    /**
     * 回复报文时使用
     */
    public static final String RESULT_TRUE = "T";
    public static final String RESULT_FALSE = "F";

    /**
    * 数字常量
    */
    public static final int ONE_HUNDRED =100;
    public static final int ONE_HUNDRED_AND_TEN=110;
    public static final int SIX_HUNDRED =600;

    /**
     * 海尚汇常量
    * */
    public static final int HSH_BRAND_ID =3664;
    public static final String HSH_BRAND_NAME ="诺德斯特龙Nordstrom";
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String UNISEX = "Unisex";
    public static final String HSH_PROVIDER_NAME="海尚汇";

    /**
     * 货主代码
     */
    public static final String CUSTOMER_CODE="SFHT";
}
