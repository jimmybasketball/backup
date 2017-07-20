package com.sfebiz.supplychain.exposed.common.code;

/**
 * 货主相关的响应码
 * [1020000 - 1030000)
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-13 15:50
 **/
public class MerchantReturnCode extends SCReturnCode{

    private static final long serialVersionUID = -8805272874697122441L;

    public MerchantReturnCode(String desc, int code) {
        super(desc, code);
    }

    /**
     *
     * 货主相关 [1020000 - 1021000)
     *
     */
    public final static int _C_MERCHANT_CONCURRENT_EXCEPTION = 1020000;
    public final static int _C_MERCHANT_ACCOUNT_ID_ALREADY_EXISTS = 1020001;
    public final static int _C_MERCHANT_NOT_EXIST = 1020002;
    public final static int _C_MERCHANT_UNKNOWN_ERROR = 1020003;
    public final static int _C_MERCHANT_WRONG_STATE = 1020004;
    public final static int _C_MERCHANT_ALREADY_CHANGE_STATE = 1020005;
    public final static int _C_MERCHANT_ALREADY_START_USING_ERROR = 1020006;

    public final static MerchantReturnCode MERCHANT_CONCURRENT_EXCEPTION =  new MerchantReturnCode("并发异常",_C_MERCHANT_CONCURRENT_EXCEPTION);
    public final static MerchantReturnCode MERCHANT_ACCOUNT_ID_ALREADY_EXISTS =  new MerchantReturnCode("货主账户ID已存在",_C_MERCHANT_ACCOUNT_ID_ALREADY_EXISTS);
    public final static MerchantReturnCode MERCHANT_NOT_EXIST =  new MerchantReturnCode("货主不存在",_C_MERCHANT_NOT_EXIST);
    public final static MerchantReturnCode MERCHANT_UNKNOWN_ERROR =  new MerchantReturnCode("货主服务未知异常",_C_MERCHANT_UNKNOWN_ERROR);
    public final static MerchantReturnCode MERCHANT_WRONG_STATE =  new MerchantReturnCode("货主状态值不合法",_C_MERCHANT_WRONG_STATE);
    public final static MerchantReturnCode MERCHANT_ALREADY_CHANGE_STATE =  new MerchantReturnCode("货主状态已被修改",_C_MERCHANT_ALREADY_CHANGE_STATE);
    public final static MerchantReturnCode MERCHANT_ALREADY_START_USING_ERROR =  new MerchantReturnCode("货主启用失败",_C_MERCHANT_ALREADY_START_USING_ERROR);

    /**
     *
     * 货主供应商相关 [1021000 - 1022000)
     *
     */
    public final static int _C_MERCHANT_PROVIDER_UNKNOWN_ERROR = 1021001;
    public final static int _C_MERCHANT_PROVIDER_NOT_EXIST = 1021002;
    public final static int _C_MERCHANT_PROVIDER_WRONG_STATE = 1021003;
    public final static int _C_MERCHANT_PROVIDER_ALREADY_CHANGE_STATE = 1021004;


    public final static MerchantReturnCode MERCHANT_PROVIDER_UNKNOWN_ERROR =  new MerchantReturnCode("货主供应商服务未知异常",_C_MERCHANT_PROVIDER_UNKNOWN_ERROR);
    public final static MerchantReturnCode MERCHANT_PROVIDER_NOT_EXIST =  new MerchantReturnCode("货主供应商不存在",_C_MERCHANT_PROVIDER_NOT_EXIST);
    public final static MerchantReturnCode MERCHANT_PROVIDER_WRONG_STATE =  new MerchantReturnCode("货主供应商状态值不合法",_C_MERCHANT_PROVIDER_WRONG_STATE);
    public final static MerchantReturnCode MERCHANT_PROVIDER_ALREADY_CHANGE_STATE =  new MerchantReturnCode("货主供应商状态已被修改",_C_MERCHANT_PROVIDER_ALREADY_CHANGE_STATE);


    /**
     *
     * 货主供应商线路相关 [1022000 - 1023000)
     *
     */
    public final static int _C_MERCHANT_PROVIDER_LINE_UNKNOWN_ERROR = 1022001;
    public final static int _C_MERCHANT_PROVIDER_LINE_ENTITY_VALIDATE_FAIL = 1022002;
    public final static int _C_MERCHANT_PROVIDER_LINE_WRONG_STATE = 1022003;
    public final static int _C_MERCHANT_PROVIDER_LINE_NOT_EXIST = 1022004;
    public final static int _C_MERCHANT_PROVIDER_LINE_ALREADY_CHANGE_STATE = 1022005;

    public final static MerchantReturnCode MERCHANT_PROVIDER_LINE_UNKNOWN_ERROR =  new MerchantReturnCode("货主供应商线路服务未知异常",_C_MERCHANT_PROVIDER_LINE_UNKNOWN_ERROR);
    public final static MerchantReturnCode MERCHANT_PROVIDER_LINE_ENTITY_VALIDATE_FAIL =  new MerchantReturnCode("货主供应商线路实体校验失败",_C_MERCHANT_PROVIDER_LINE_ENTITY_VALIDATE_FAIL);
    public final static MerchantReturnCode MERCHANT_PROVIDER_LINE_WRONG_STATE =  new MerchantReturnCode("货主供应商线路状态值不合法",_C_MERCHANT_PROVIDER_LINE_WRONG_STATE);
    public final static MerchantReturnCode MERCHANT_PROVIDER_LINE_NOT_EXIST =  new MerchantReturnCode("货主供应商线路不存在",_C_MERCHANT_PROVIDER_LINE_NOT_EXIST);
    public final static MerchantReturnCode MERCHANT_PROVIDER_LINE_ALREADY_CHANGE_STATE =  new MerchantReturnCode("货主供应商线路状态已被修改",_C_MERCHANT_PROVIDER_LINE_ALREADY_CHANGE_STATE);


    /**
     *
     * 货主申报方式相关 [1023000 - 1024000)
     *
     */
    public final static int _C_MERCHANT_PAY_DECLARE_UNKNOWN_ERROR = 1023001;
    public final static int _C_MERCHANT_PAY_DECLARE_ALREADY_EXISTS = 1023002;
    public final static int _C_MERCHANT_PAY_DECLARE_ENTITY_VALIDATE_FAIL = 1023003;

    public final static MerchantReturnCode MERCHANT_PAY_DECLARE_UNKNOWN_ERROR =  new MerchantReturnCode("货主申报方式配置未知异常",_C_MERCHANT_PAY_DECLARE_UNKNOWN_ERROR);
    public final static MerchantReturnCode MERCHANT_PAY_DECLARE_ALREADY_EXISTS =  new MerchantReturnCode("货主申报方式配置已存在",_C_MERCHANT_PAY_DECLARE_ALREADY_EXISTS);
    public final static MerchantReturnCode MERCHANT_PAY_DECLARE_ENTITY_VALIDATE_FAIL =  new MerchantReturnCode("货主申报方式实体校验失败",_C_MERCHANT_PAY_DECLARE_ENTITY_VALIDATE_FAIL);


    /**
     *
     * 货主包材配置相关 [1024000 - 1025000)
     *
     */
    public final static int _C_MERCHANT_PACKAGE_MATERIAL_UNKNOWN_ERROR = 1024001;
    public final static int _C_MERCHANT_PACKAGE_MATERIAL_ALREADY_EXISTS = 1024002;

    public final static MerchantReturnCode MERCHANT_PACKAGE_MATERIAL_UNKNOWN_ERROR =  new MerchantReturnCode("货主包材配置未知异常",_C_MERCHANT_PACKAGE_MATERIAL_UNKNOWN_ERROR);
    public final static MerchantReturnCode MERCHANT_PACKAGE_MATERIAL_ALREADY_EXISTS =  new MerchantReturnCode("货主包材配置已存在",_C_MERCHANT_PACKAGE_MATERIAL_ALREADY_EXISTS);

}
