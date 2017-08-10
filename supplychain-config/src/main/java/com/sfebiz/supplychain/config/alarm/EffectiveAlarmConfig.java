package com.sfebiz.supplychain.config.alarm;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.config.LogisticsDynamicConfig;
import com.sfebiz.supplychain.exposed.stockout.enums.StockoutOrderState;
import com.sfebiz.supplychain.util.DateUtil;
import com.sfebiz.supplychain.util.JSONUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p>物流时效预警配置</p>
 * User: <a href="mailto:zhangdi@ifunq.com">张弟</a>
 * Date: 2015-08-03
 * Time: 13:30
 */
public class EffectiveAlarmConfig {

    private static final Logger logger = LoggerFactory.getLogger(EffectiveAlarmConfig.class);

    /**
     * 获取时间在星期中的昵称
     *
     * @param calendar
     * @return
     */
    private static String getDayWeekNick(Calendar calendar) {
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            return "sunday";
        } else if (weekday == 7) {
            return "saturday";
        } else if (weekday == 6) {
            return "friday";
        } else {
            return "workday";
        }
    }

    /**
     * 获取达到下个状态预警的时间阀值
     *
     * @param lineId 线路ID
     * @param date   当前时间
     * @param state  当前状态
     * @return
     */
    public static Date getAlarmDate(Long lineId, Date date, String state) {
        LogBetter.instance(logger).setLevel(LogLevel.INFO)
                .addParm("开始计算预警时间，线路ID为", lineId)
                .addParm("事件时间", date)
                .addParm("出库状态", state)
                .log();
        try {
            if (StockoutOrderState.STOCKOUTING.getValue().equals(state)) {
                Date alarmDate = getAlarmDateByRuleName(lineId, date, "stockOutingAlarmConfig");
                //如果未配置，设置为已出库超时24小时
                if (null == alarmDate) {
                    int alermHoure = 24;
                    Date date1 = DateUtil.getBeforOrAfterByHour(date, alermHoure);
                    LogBetter.instance(logger).setLevel(LogLevel.INFO)
                            .addParm("线路未配置出库超时时间，线路ID为", lineId)
                            .addParm("默认使用24小时超时，预警时间为", date1)
                            .log();
                    return date1;
                }
                return alarmDate;
            } else if (StockoutOrderState.WAIT_STOCKOUT.getValue().equals(state)) {
                String lineIds = LogisticsDynamicConfig.getEffectiveAlarm().getRule("waitStockOutAlarmConfig", "lineId");
                int alarmHour = 6;
                if (StringUtils.isNotBlank(lineIds)) {
                    String[] lineIdArr = lineIds.split(",");
                    for (String id : lineIdArr) {
                        if (Long.toString(lineId).equalsIgnoreCase(StringUtils.trim(id))) {
                            String alarmDuration = LogisticsDynamicConfig.getEffectiveAlarm().getRule("waitStockOutAlarmConfig", "alarmDuration");
                            alarmHour = StringUtils.isNotBlank(alarmDuration) ? Integer.valueOf(alarmDuration) : 6;
                            break;
                        }
                    }
                }
                return DateUtil.getBeforOrAfterByHour(date, alarmHour);
            } else if (StockoutOrderState.STOCKOUT_FINISH.getValue().equals(state)) {
                Date alarmDate = getAlarmDateByRuleName(lineId, date, "stockoutFinishAlarmConfig");
                //如果未配置，设置为已出库超时24小时
                if (null == alarmDate) {
                    int alermHoure = 24;
                    Date date1 = DateUtil.getBeforOrAfterByHour(date, alermHoure);
                    LogBetter.instance(logger).setLevel(LogLevel.INFO)
                            .addParm("线路未配置收件超时时间，线路ID为", lineId)
                            .addParm("默认使用24小时超时，预警时间为", date1)
                            .log();
                    return date1;
                }
                return alarmDate;
            } else if (StockoutOrderState.COLLECTED.getValue().equals(state)) {
                //已出库超过10*24个小时
                int alermHoure = 10 * 24;
                return DateUtil.getBeforOrAfterByHour(date, alermHoure);
            } else if (StockoutOrderState.DELIVEING.getValue().equals(state)) {
                //已出库超过3*24个小时
                int alermHoure = 3 * 24;
                return DateUtil.getBeforOrAfterByHour(date, alermHoure);
            }
        } catch (Exception e) {
            logger.error("[供应链]获取出库预警信息异常", e);
        }

        return null;

    }

    private static Date getAlarmDateByRuleName(Long lineId, Date date, String ruleName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String dayWeekNick = getDayWeekNick(calendar);

        String alermConfigByLine = LogisticsDynamicConfig.getEffectiveAlarm().getRule(ruleName, lineId.toString());
        if (StringUtils.isBlank(alermConfigByLine)) {
            return null;
        }
        Map<String, Object> mapAlarm = JSONUtil.parseJSONMessage(alermConfigByLine);
        Map<String, Object> mapDayConfig = (Map) mapAlarm.get(dayWeekNick);

        if (!mapDayConfig.containsKey("timepoint") || !mapDayConfig.containsKey("stockoutPlanTime")
                || !mapDayConfig.containsKey("waitDayAfter") || !mapDayConfig.containsKey("waitDayBefore")) {
            return null;
        }

        String[] startTime = mapDayConfig.get("timepoint").toString().split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int startMinute = Integer.parseInt(startTime[1]);
        int startSecond = Integer.parseInt(startTime[2]);

        //当前时间是否超过当天出库时间点
        boolean isBeyondStartTime = false;
        if (calendar.get(Calendar.HOUR_OF_DAY) > startHour) {
            isBeyondStartTime = true;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) == startHour && calendar.get(Calendar.MINUTE) > startMinute) {
            isBeyondStartTime = true;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) == startHour && calendar.get(Calendar.MINUTE) == startMinute && calendar.get(Calendar.SECOND) > startSecond) {
            isBeyondStartTime = true;
        }


        String alermTime = mapDayConfig.get("stockoutPlanTime").toString();
        String[] alermTimeSplit = alermTime.split(":");
        int hour = Integer.parseInt(alermTimeSplit[0]);
        int minute = Integer.parseInt(alermTimeSplit[1]);
        int second = Integer.parseInt(alermTimeSplit[2]);

        if (isBeyondStartTime) {
            int waitDayAfter = Integer.parseInt(mapDayConfig.get("waitDayAfter").toString());
            calendar.add(Calendar.DATE, waitDayAfter);
        } else {
            int waitDayBefore = Integer.parseInt(mapDayConfig.get("waitDayBefore").toString());
            calendar.add(Calendar.DATE, waitDayBefore);
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, minute, second);
        Date alarmDate = calendar.getTime();
        return alarmDate;
    }

    public static Long getEffectiveAlarmStaticLimitCount() {
        try {
            String limitCount = LogisticsDynamicConfig.getEffectiveAlarm().getRule("staticTaskConfig", "maxStaticCount");
            return Long.parseLong(limitCount);
        } catch (Exception e) {
            logger.error("[供应链]获取出库预警统计任务配置信息异常", e);
        }
        return null;
    }

    public static boolean getisNeedEffRoute() {
        try {
            String flag = LogisticsDynamicConfig.getEffectiveAlarm().getRule("staticTaskConfig", "isNeedEffRoute");
            return Boolean.parseBoolean(flag);
        } catch (Exception e) {
            logger.error("[供应链]获取出库预警统计任务配置信息异常,获取不到isNeedEffRoute", e);
        }
        return false;
    }

    public static String getNotNeedEffRouteLines() {
        try {
            String notNeedEffRouteLines = LogisticsDynamicConfig.getEffectiveAlarm().getRule("staticTaskConfig", "notNeedEffRouteLines");
            return notNeedEffRouteLines;
        } catch (Exception e) {
            logger.error("[供应链]不需要预警路由信息线路id异常,获取不到NotNeedEffRouteLines", e);
        }
        return null;
    }

    public static String getEffRouteAheadTime() {
        try {
            String effRouteAheadTime = LogisticsDynamicConfig.getEffectiveAlarm().getRule("staticTaskConfig", "effRouteAheadTime");
            return effRouteAheadTime;
        } catch (Exception e) {
            logger.error("[供应链]获取出库预警统计任务配置信息异常,获取不到effRouteAheadTime", e);
        }
        return "0";
    }

    public static String getFirstEffRouteAlarmTime() {
        try {
            String firstEffRouteAlarmTime = LogisticsDynamicConfig.getEffectiveAlarm().getRule("staticTaskConfig", "firstEffRouteAlarmTime");
            return firstEffRouteAlarmTime;
        } catch (Exception e) {
            logger.error("[供应链]获取出库预警统计任务配置信息异常,获取不到firstEffRouteAlarmTime", e);
        }
        return "0";
    }

    public static String getSecondEffRouteAlarmTime() {
        try {
            String secondEffRouteAlarmTime = LogisticsDynamicConfig.getEffectiveAlarm().getRule("staticTaskConfig", "secondEffRouteAlarmTime");
            return secondEffRouteAlarmTime;
        } catch (Exception e) {
            logger.error("[供应链]获取出库预警统计任务配置信息异常,获取不到secondEffRouteAlarmTime", e);
        }
        return "0";
    }

    public static Integer getEffectiveAlarmStaticPageSize() {
        try {
            String staticPageSize = LogisticsDynamicConfig.getEffectiveAlarm().getRule("staticTaskConfig", "staticPageSize");
            return Integer.parseInt(staticPageSize);
        } catch (Exception e) {
            logger.error("[供应链]获取出库预警统计任务配置信息异常", e);
        }
        return null;
    }

    public static Integer getEffectiveAlarmStaticLogState() {
        try {
            String logState = LogisticsDynamicConfig.getEffectiveAlarm().getRule("staticTaskConfig", "logState");
            return Integer.parseInt(logState);
        } catch (Exception e) {
            logger.error("[供应链]获取出库预警统计任务配置信息异常", e);
        }
        return null;
    }


    public static void main(String args[]) {
        Date date = getAlarmDate(11112L, new Date(), StockoutOrderState.STOCKOUT_FINISH.getValue());
        String alermDate = DateUtil.defFormatDateStr(date);
        System.out.println(alermDate);
    }

}
