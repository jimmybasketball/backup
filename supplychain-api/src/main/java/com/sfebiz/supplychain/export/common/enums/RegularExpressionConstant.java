package com.sfebiz.supplychain.export.common.enums;

/**
 *
 * 常用正则表达式常量
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017/7/5 10:04
 */
public class RegularExpressionConstant {
    /** 座机电话格式验证 **/
    public static final String PHONE_CALL_PATTERN =
            "^(?:\\(\\d{3,4}\\)|\\d{3,4}-)?\\d{7,8}(?:-\\d{1,4})?$";

    /**
     * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700,173
     **/
    public static final String CHINA_TELECOM_PATTERN =
            "(?:^(?:\\+86)?1(?:33|53|7[37]|8[019])\\d{8}$)|(?:^(?:\\+86)?1700\\d{7}$)";

    /**
     * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1707,1708,1709,175
     **/
    public static final String CHINA_UNICOM_PATTERN =
            "(?:^(?:\\+86)?1(?:3[0-2]|4[5]|5[56]|7[56]|8[56])\\d{8}$)|(?:^(?:\\+86)?170[7-9]\\d{7}$)";
    /**
     * 简单手机号码校验，校验手机号码的长度和1开头
     */
    public static final String SIMPLE_PHONE_CHECK = "^(?:\\+86)?1\\d{10}$";
    /**
     * 中国移动号码格式验证 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184
     * ,187,188,147,178,1705
     *
     **/
    public static final String CHINA_MOBILE_PATTERN =
            "(?:^(?:\\+86)?1(?:3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(?:^(?:\\+86)?1705\\d{7}$)";

    /**
     * 仅手机号格式校验 CHINA_MOBILE_PATTERN + "|" + CHINA_TELECOM_PATTERN + "|" + CHINA_UNICOM_PATTERN
     */
    public static final String PHONE_PATTERN =
            CHINA_MOBILE_PATTERN + "|" + CHINA_TELECOM_PATTERN + "|" + CHINA_UNICOM_PATTERN;

    /**
     * 手机和座机号格式校验 PHONE_PATTERN + "|(" + PHONE_CALL_PATTERN + ")";
     */
    public static final String PHONE_TEL_PATTERN = PHONE_PATTERN + "|(" + PHONE_CALL_PATTERN + ")";

}
