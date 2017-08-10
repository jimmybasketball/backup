package com.sfebiz.supplychain.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public final class DateUtil {
    public static final String DEF_PATTERN = "yyyy-MM-dd HH:mm:ss";//默认时间格式

    public static final String DATE_PATTERN = "yyyy-MM-dd";//日期格式

    public static final long MILLISECOND_OF_DAY = 86400000l;//一天的毫秒数

    //启始日期或 表示为 NULL 1970-01-01 08:00:00
    public static final Date START_NULL_DATE = parseDate("1970-01-01 08:00:00", DATE_PATTERN);


    // RFC 822 Date Format
    public static final String RFC822_DATE_FORMAT                     = "EEE, dd MMM yyyy HH:mm:ss z";

    // ISO 8601 format
    public static final String ISO8601_DATE_FORMAT                    = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // Alternate ISO 8601 format without fractional seconds
    public static final String ALTERNATIVE_ISO8601_DATE_FORMAT        = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String SHORT_ALTERNATIVE_ISO8601_DATE_FORMAT  = "yyyy-MM-dd'T'HH:mm'Z'";

    public static final String SIMPLE_ALTERNATIVE_ISO8601_DATE_FORMAT = "yyyy-MM-dd'Z'";

    public static final String DATE_DEF_PATTERN = "yyyyMMddHHmmss";//日期格式

    public static final String DATE_PATTERN_UNDEF = "yyyyMMdd";

    public static final String DATETIME_FORMAT                        = "yyyy-MM-dd HH:mm:ss";

    /**
     * 方法说明：获得指定格式当前系统时间字符串
     *
     * @param pattern
     * @return
     * @throws Exception
     */
    public static String getCDateString(String pattern) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(new Date());
    }
    /**
     * 方法说明：获得指定格式当前系统时间字符串 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentDateTime(){
        String result = "";
        try {
            result = getCDateString("yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    /**
     * 方法说明：获得指定格式当前系统时间字符串 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentDateTimeByPattern(String pattern){
        String result = "";
        try {
            result = getCDateString(pattern);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static long getDatetime(String pattern,String dt) throws Exception{
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.parse(dt).getTime();
    }

    public static String getDateForLong(long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    public static String getDateForLong(String pattern,long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        return sdf.format(c.getTime());
    }

    public static int getTwoDay(Date date1,Date date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);

        return day1 - day2;
    }

    /**
     * 按指定格式格式化日期
     * @param d
     * @param parttern
     * @return
     */
    public static String formatDateStr(Date d, String parttern) {
        SimpleDateFormat df = new SimpleDateFormat(parttern);
        return df.format(d);
    }

    /**
     * 默认格式时间转字符串
     * @param d
     * @return
     */
    public static String defFormatDateStr(Date d) {
        return formatDateStr(d,DEF_PATTERN);
    }

    /**
     * 解析Date字符串为Date
     * @param dataStr
     * @param pattern
     * @return
     * @throws Exception
     */
    public static Date parseDate(String dataStr, String pattern) {
        if (StringUtils.isEmpty(dataStr)) {
            return null;
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = DateUtil.DEF_PATTERN;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dataStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date buildByHMSE(Date time, int h, int m, int s, int ms) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, s);
        calendar.set(Calendar.MILLISECOND, ms);
        return calendar.getTime();
    }

    public static Date getDateBeforeOrAfter(Date date, int pre) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, pre);
        return c.getTime();
    }

    public static Date getBeforOrAfterByHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hour);
        return c.getTime();
    }

    public static Date getBeforOrAfterByMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    /**
     * 获取口岸申报时间段
     * 根据Diamond上配置的 起始时分秒 获取当前时间的 年月日-时分秒，并向后推24小时，算出结束时间的年月日-时分秒
     * @param timeFm
     * @return  返回 起始时间 和 结束时间 数组
     */
    public static Date[] getPortTimeBw(Date nowTime ,String timeFm) {
        String[] timeArray = timeFm.split(":");
        Date [] dates = new Date[2];
        //当前时间的截止时间
        Date nowfmTime = DateUtil.buildByHMSE(nowTime, Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]), Integer.valueOf(timeArray[2]), 0);
        if (nowTime.after(nowfmTime)) {
            dates [0] = nowfmTime;
            dates [1] = DateUtil.getDateBeforeOrAfter(dates [0], 1);
        } else {
            dates [1] = nowfmTime;
            dates [0] = DateUtil.getDateBeforeOrAfter(dates [1], -1);
        }
        return dates;
    }

    /**
     *
     * @param stockinCreateDate YYYY-MM-dd HH24:MM:SS
     * @return
     */
//    public static boolean beforePublishStockinOrderCreate(Date stockinCreateDate){
//        boolean flag = false;
//        Map<String,String> times = PublishTimeHelper.getInstance().getAllTimes();
//        String myString = null;
//        for (String str : times.keySet()){
//            myString = times.get(str);
//        }
//        //diamond获取上线时间
//        Date d = DateUtil.parseDate(myString, DateUtil.DEF_PATTERN);
//
//        flag = stockinCreateDate.before(d);
//        return flag;
//    }

    /**
     * 获取当前时间后一个小时的时间
     *
     * @return
     */
    public static Date getOneHoursAgoOnCurrentDate() {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        return cal.getTime();
    }


    /**
     * 两个时间的相差的天数
     * @param d1
     * @param d2
     * @return
     */
    public static long getDateBetween(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            int d1OfY = c1.get(Calendar.DAY_OF_YEAR);
            int d2OfY = c2.get(Calendar.DAY_OF_YEAR);
            return d2OfY - d1OfY;
        } else {
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            c1.set(Calendar.MILLISECOND, 0);

            c2.set(Calendar.HOUR_OF_DAY, 0);
            c2.set(Calendar.MINUTE, 0);
            c2.set(Calendar.SECOND, 0);
            c2.set(Calendar.MILLISECOND, 0);
            return (c2.getTimeInMillis() - c1.getTimeInMillis()) / MILLISECOND_OF_DAY;
        }
    }

    public static void main(String[] args) {
        Date d1 = DateUtil.parseDate("2015-05-22 19:00:00", DateUtil.DEF_PATTERN);
        Date d2 = DateUtil.parseDate("2016-05-22 19:00:00", DateUtil.DEF_PATTERN);

        Date d = new Date(0);

        System.out.println(defFormatDateStr(START_NULL_DATE));
    }





    /**
     * 字符串转换为日期格式
     *
     * @param strDate
     * @param pattern
     * @return
     */
    public static Date stringToDate(String strDate, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期格式转换为字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        try {
            if (date == null)
                return null;
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获得指定格式的当前日期
     *
     * @param pattern
     * @return
     */
    public static String getCurrentDate(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    /**
     * 获得 GMT 格式的当前日期
     *
     * @return
     */
    public static String getCurrentGMT() {
        return formatRfc822Date(new Date());
    }

    /**
     * Formats Date to GMT string.
     *
     * @param date
     * @return
     */
    public static String formatRfc822Date(Date date) {
        return getRfc822DateFormat().format(date);
    }

    /**
     * Parses a GMT-format string.
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseRfc822Date(String dateString) throws ParseException {
        return getRfc822DateFormat().parse(dateString);
    }

    private static DateFormat getRfc822DateFormat() {
        SimpleDateFormat rfc822DateFormat = new SimpleDateFormat(RFC822_DATE_FORMAT, Locale.US);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return rfc822DateFormat;
    }

    /**
     * Parse a date string in the format of ISO 8601.
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseIso8601Date(String dateString) throws ParseException {
        try {
            return getIso8601DateFormat().parse(dateString);
        } catch (ParseException e) {
            return getAlternativeIso8601DateFormat(ALTERNATIVE_ISO8601_DATE_FORMAT).parse(dateString);
        }
    }

    /**
     * Parse a date string in the Simple format of ISO 8601. format like
     * yyyy-MM-dd'T'HH:mm'Z''
     *
     * @param dateString eg. 2012-11-07T09:24Z
     * @return
     * @throws ParseException
     */
    public static Date parseShortIso8601Date(String dateString) throws ParseException {
        try {
            return getIso8601DateFormat().parse(dateString);
        } catch (ParseException e) {
            return getAlternativeIso8601DateFormat(SHORT_ALTERNATIVE_ISO8601_DATE_FORMAT).parse(dateString);
        }
    }

    /**
     * Parse a date string in the Simple date of ISO 8601. format like
     * yyyy-MM-dd'Z'
     *
     * @param dateString eg 2012-11-07Z
     * @return
     * @throws ParseException
     */
    public static Date parseSimpleIso8601Date(String dateString) throws ParseException {
        try {
            return getIso8601DateFormat().parse(dateString);
        } catch (ParseException e) {
            return getAlternativeIso8601DateFormat(SIMPLE_ALTERNATIVE_ISO8601_DATE_FORMAT).parse(dateString);
        }
    }

    private static DateFormat getIso8601DateFormat() {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT, Locale.US);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return df;
    }

    private static DateFormat getAlternativeIso8601DateFormat(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.US);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return df;
    }

    /**
     * 获取到期日与当前日期的天数间隔
     *
     * @param endDate
     * @return
     */
    public static long getDays(Date startDate, Date endDate) {

        Calendar c = Calendar.getInstance();

        c.setTime(startDate);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long l1 = c.getTimeInMillis();

        c.setTime(endDate);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long l2 = c.getTimeInMillis();

        long days = (l2 - l1) / (1000 * 60 * 60 * 24);//化为天

        return days;
    }

    public static Date[] getAboutDateBy0to23Hour(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date hour0 = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date hour23 = calendar.getTime();

        return new Date[] { hour0, hour23 };
    }

    /**
     * 返回当前时间按下列参数进行增量后的对应时间点
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date getDateIncreasedBy(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        if (year != 0) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year);
        }
        if (month != 0) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + month);
        }
        if (day != 0) {
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
        }
        if (hour != 0) {
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + hour);
        }
        if (minute != 0) {
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
        }
        if (second != 0) {
            calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + second);
        }

        return calendar.getTime();
    }
}
