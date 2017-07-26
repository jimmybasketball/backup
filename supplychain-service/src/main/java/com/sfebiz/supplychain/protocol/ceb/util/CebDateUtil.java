package com.sfebiz.supplychain.protocol.ceb.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 由于SimpleDateFormat不是线程安全的，所以在作为静态工具类使用的时候需要特殊处理
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午3:38
 */
public class CebDateUtil {

    private static final Object lock = new Object();

    /**
     * CEB 默认时间格式
     */
    public static final String defaultCebDateStringFormat = "yyyyMMddHHmmss";

    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();


    /**
     * 格式化时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat format = getSdf(pattern);
        return format.format(date);
    }

    /**
     * 获取当前年月日，用于目录名称展示
     *
     * @return
     */
    public static String getCurrentYearDateForDirName() {
        SimpleDateFormat format = getSdf("yyyy-MM-dd");
        return format.format(new Date());
    }

    /**
     * 根据Pattern获取SimpleDateFormat
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = sdfMap.get(pattern);
        if (simpleDateFormatThreadLocal == null) {
            synchronized (lock) {
                if (simpleDateFormatThreadLocal == null) {
                    simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, simpleDateFormatThreadLocal);
                }
            }
        }
        return simpleDateFormatThreadLocal.get();
    }

    public static void main(String[] args) {
        System.out.println(CebDateUtil.format(new Date(), CebDateUtil.defaultCebDateStringFormat));
    }

}
