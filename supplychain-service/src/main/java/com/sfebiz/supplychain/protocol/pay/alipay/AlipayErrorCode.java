package com.sfebiz.supplychain.protocol.pay.alipay;

/**
 * <p>支付宝错误码</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/5/19
 * Time: 下午2:03
 */
public enum AlipayErrorCode {

    REASON_TRADE_BEEN_FREEZEN("REASON_TRADE_BEEN_FREEZEN" , "交易已经被冻结"),
    BUYER_ERROR("BUYER_ERROR" , "买家不存在"),
    SELLER_ERROR("SELLER_ERROR" , "卖家不存在"),
    TRADE_NOT_EXIST("TRADE_NOT_EXIST" , "交易不存在"),
    TRADE_STATUS_ERROR("TRADE_STATUS_ERROR" , "交易状态不合法"),
    TRADE_HAS_FINISHED("TRADE_HAS_FINISHED" , "交易已结束"),
    INVALID_PARAMETER("INVALID_PARAMETER" , "参数不合法"),
    DISCORDANT_REPEAT_REQUEST("DISCORDANT_REPEAT_REQUEST" , "同一笔请求参数不一致"),
    CUSTOMER_VALIDATE_ERROR("CUSTOMER_VALIDATE_ERROR" , "用户状态未实名认证"),
    ILLEGAL_SIGN("ILLEGAL_SIGN" , "签名不正确"),
    ILLEGAL_DYN_MD5_KEY("ILLEGAL_DYN_MD5_KEY" , "动态密钥信息错误"),
    ILLEGAL_ENCRYPT("ILLEGAL_ENCRYPT" , "加密不正确"),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT" , "参数不正确"),
    ILLEGAL_SERVICE("ILLEGAL_SERVICE" , "Service 参数不正确"),
    ILLEGAL_USER("ILLEGAL_USER" , "用户 ID 不正确"),
    ILLEGAL_PARTNER("ILLEGAL_PARTNER" , "合作伙伴 ID 不正确"),
    ILLEGAL_EXTERFACE("ILLEGAL_EXTERFACE" , "接口配置不正确"),
    ILLEGAL_PARTNER_EXTERFACE("ILLEGAL_PARTNER_EXTERFACE" , "合作伙伴接口信息不正确"),
    ILLEGAL_SECURITY_PROFILE("ILLEGAL_SECURITY_PROFILE" , "未找到匹配的密钥配置"),
    ILLEGAL_AGENT("ILLEGAL_AGENT" , "代理 ID 不正确"),
    ILLEGAL_SIGN_TYPE("ILLEGAL_SIGN_TYPE" , "签名类型不正确"),
    ILLEGAL_CHARSET("ILLEGAL_CHARSET" , "字符集不合法"),
    HAS_NO_PRIVILEGE("HAS_NO_PRIVILEGE" , "无权访问"),
    INVALID_CHARACTER_SET("INVALID_CHARACTER_SET" , "字符集无效"),
    SYSTEM_ERROR("SYSTEM_ERROR" , "支付宝系统错误"),
    SESSION_TIMEOUT("SESSION_TIMEOUT" , "session 超时"),
    ILLEGAL_TARGET_SERVICE("ILLEGAL_TARGET_SERVICE" , "错误的 target_service"),
    ILLEGAL_ACCESS_SWITCH_SYSTEM("ILLEGAL_ACCESS_SWITCH_SYSTEM" , "partner 不允许访问该类型的系统"),
    EXTERFACE_IS_CLOSED("EXTERFACE_IS_CLOSED" , "接口已关闭"),

    ;

    private String code;

    private String description;


    AlipayErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据Code获取对应描述，如果code不存在，则返回“系统错误”
     * @param code
     * @return
     */
    public static String getDescriptionByCode(String code){
        for(AlipayErrorCode errorCode : AlipayErrorCode.values()){
            if(errorCode.code.equalsIgnoreCase(code)){
                return errorCode.description;
            }
        }
        return "系统错误";
    }
}
