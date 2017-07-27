package com.sfebiz.supplychain.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则类
 * Created by jianyuanyang on 15/8/3.
 */
public class RegUtil {

    /**
     * 手机正则
     */
    private static Pattern mobilePattern= Pattern.compile("^(\\+86)?1[123456789]\\d{9}$");

    /**
     * 身份证正则
     */
    private static Pattern id18 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$");

    private static final int[] wi = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
    private static final String[] ck = {"1","0","X","9","8","7","6","5","4","3","2"};

    /**
     * 字符串中包含 手机号 正则
     */
    private static Pattern containMobilePattern= Pattern.compile(".*(\\+86)?1[123456789]\\d{9}.*$");

    public static boolean isMobileNum(String mobile) {
        if (StringUtils.isBlank(mobile))
            return Boolean.FALSE;

        Matcher m = mobilePattern.matcher(mobile);
        return m.matches();
    }



    /**
     * 身份证号校验
     * @param idNum 身份证号
     * @return
     */
    public static boolean isIdNum(String idNum) {
        if (idNum == null || idNum.length() != 18) {
            return false;
        }

        if (!id18.matcher(idNum).matches()) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < idNum.length() - 1; i++) {
            sum += Integer.parseInt(String.valueOf(idNum.charAt(i))) * wi[i];
        }

        String ckValue = ck[sum % 11];//获取校验位的值
        if (!ckValue.equals(idNum.substring(17).toUpperCase())) {
            return false;
        }
        return true;
    }

    /**
     * 检查字符串手否包含手机号
     * @param str
     * @return
     */
    public static boolean containMobileNum(String str) {
        if (StringUtils.isBlank(str))
            return Boolean.FALSE;

        Matcher m = containMobilePattern.matcher(str);
        return m.matches();
    }


}
