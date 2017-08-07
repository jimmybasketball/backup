package com.sfebiz.supplychain.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class NumberUtil {

    /** 日志  */
    private static final Logger LOGGER             = LoggerFactory.getLogger(NumberUtil.class);

    /** 18位 */
    private static Pattern      ID_CARD_18_PATTERN = Pattern
                                                       .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$");
    /** 15位 */
    private static Pattern      ID_CARD_15_PATTERN = Pattern
                                                       .compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");

    public static int parseInt(Object val, int def) {
        if (val == null || "".equals(val)) {
            return def;
        }
        if (val != null && (val instanceof String)) {
            String d = (String) val;
            return Integer.parseInt(d);
        }

        if (val != null && (val instanceof Float)) {
            return ((Float) val).intValue();
        }

        if (val != null && (val instanceof Double)) {
            return ((Double) val).intValue();
        }

        if (val != null && (val instanceof Integer)) {
            return (Integer) val;
        }

        if (val != null && (val instanceof Long)) {
            return ((Long) val).intValue();
        }

        if (val != null && (val instanceof BigInteger)) {
            return ((BigInteger) val).intValue();
        }

        return def;
    }

    public static Float floatVal(String str, Float def) {
        if (str == null) {
            return def;
        }
        try {
            Float f = Float.valueOf(str.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
            return f;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return def;
        }

    }

    public static long parseLong(Object val, long def) {
        if (val == null || "".equals(val)) {
            return def;
        }
        if (val != null && (val instanceof String)) {
            String d = (String) val;
            return Long.parseLong(d);
        }

        if (val != null && (val instanceof Float)) {
            return ((Float) val).longValue();
        }

        if (val != null && (val instanceof Double)) {
            return ((Double) val).longValue();
        }

        if (val != null && (val instanceof Integer)) {
            return (Integer) val;
        }

        if (val != null && (val instanceof Long)) {
            return (Long) val;
        }

        if (val != null && (val instanceof BigInteger)) {
            return ((BigInteger) val).longValue();
        }

        return def;
    }

    /**
     * 默认将分转换为元，并保留两位有效数字，如果为0，直接返回0
     *
     * @param priceOnFeng 金额（分）
     * @return
     */
    public static String defaultParsePriceFeng2Yuan(Integer priceOnFeng) {
        return  defaultParsePriceFeng2Yuan(priceOnFeng, 2);
    }

    /**
     * 默认将分转换为元，并保留两位有效数字，如果为0，直接返回0
     * 
     * @param priceOnFeng 金额（分）
     * @param scale 转为元后的精确度，不传默认保留两位
     * @return
     */
    public static String defaultParsePriceFeng2Yuan(Integer priceOnFeng, Integer scale) {
        if (priceOnFeng == null || priceOnFeng == 0) {
            return "0";
        } else {
            BigDecimal oneHundred = new BigDecimal(100);
            scale = null != scale ? scale : 2;
            String priceOnYuan = new BigDecimal(priceOnFeng).divide(oneHundred, scale,
                BigDecimal.ROUND_CEILING).toString();
            return priceOnYuan;
        }
    }

    public static int parsePriceYuan2Feng(String priceOnYuan) {
        if (StringUtils.isBlank(priceOnYuan)) {
            throw new IllegalArgumentException("参数不能为空");
        } else {
            BigDecimal oneHundred = new BigDecimal(100);
            int priceOnFeng = new BigDecimal(priceOnYuan).multiply(oneHundred).intValue();
            return priceOnFeng;
        }
    }

    /**
     * KG 字符串转换为 g
     *
     * @param weightOnKg
     * @return
     */
    public static int parseKg2g(String weightOnKg) {
        if (StringUtils.isBlank(weightOnKg)) {
            return 0;
        } else {
            BigDecimal thousand = new BigDecimal(1000);
            int weightOnG = new BigDecimal(weightOnKg).multiply(thousand).intValue();
            return weightOnG;
        }
    }

    public static String truncate2Str(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO.toString();
        }
        if (bigDecimal.scale() > 2) {
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        }
        String result = bigDecimal.toPlainString();
        if (result.endsWith(".0")) {
            result = result.substring(0, result.indexOf("."));
        }
        return result;
    }

    /**
     * 默认将g转换为kg，并保留三位有效数字，如果为0，直接返回0
     *
     * @return
     */
    public static String defaultParseWeightG2KG(Integer weightOnG) {
        if (weightOnG == null || weightOnG == 0) {
            return "0";
        } else {
            BigDecimal oneHundred = new BigDecimal(1000);
            String priceOnYuan = new BigDecimal(weightOnG).divide(oneHundred, 3,
                BigDecimal.ROUND_CEILING).toString();
            return priceOnYuan;
        }
    }

    /**
     * 把毫米转换为排版单位磅
     * 备注： 
     * 1in（英寸） = 2.54cm 
     * 1in = 72pt（排版单位）
     * 
     * @param mmV
     *            毫米
     * @return 磅
     * 
     */
    public static float parseMmToPt(float mmV) {
        BigDecimal mmValue = new BigDecimal(mmV);
        BigDecimal mmTocmScale = new BigDecimal(10);
        BigDecimal cmToinScale = new BigDecimal(2.54);
        BigDecimal inToUScale = new BigDecimal(72);

        return mmValue.multiply(inToUScale).divide(mmTocmScale).divide(cmToinScale, 2).floatValue();
    }

    /**
     * 转换mm为cm
     * 
     * @param mmV
     * @return
     */
    public static String parseMmToCm(Integer mmV) {
        if (mmV == null || mmV == 0) {
            return "0";
        } else {
            BigDecimal oneHundred = new BigDecimal(100);
            String cmV = new BigDecimal(mmV).divide(oneHundred, 2, BigDecimal.ROUND_CEILING)
                .toString();
            return cmV;
        }
    }

    /**
     * 修复5位的邮编，如果是5位，则前面补零
     * 
     * @param zipCode
     * @return
     */
    public static String repairZipCode(String zipCode) {
        String repairZipCode = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(zipCode)) {
            if (zipCode.length() == 5) {
                repairZipCode = "0" + zipCode;
                LOGGER.info("修复邮编！修复前={}，修复后={}", zipCode, repairZipCode);
                zipCode = repairZipCode;
            }
        }
        return zipCode;
    }

    /**
     * 保留两位小数
     * @param d
     * @return
     */
    public static double roundDown(double d) {
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 校验身份证的格式
     * 
     * @param idNum 身份证号
     * @return
     */
    public static boolean checkIDNumFormat(String idNum) {
        return ID_CARD_18_PATTERN.matcher(idNum).matches()
               || ID_CARD_15_PATTERN.matcher(idNum).matches();
    }
}
