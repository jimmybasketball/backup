package com.sfebiz.supplychain.exposed.line.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4IntValue;

/**
 * 物流线路服务类型
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-11 10:59
 **/
public class LogisticsLineServiceType extends Enumerable4IntValue {

    private static final Logger                                              log               = LoggerFactory
                                                                                                   .getLogger(Enumerable4IntValue.class);
    private static final long                                                serialVersionUID  = -5534613490863134458L;
    private static volatile transient Map<Integer, LogisticsLineServiceType> allbyvalue        = new HashMap<Integer, LogisticsLineServiceType>();
    private static volatile transient Map<String, LogisticsLineServiceType>  allbyname         = new HashMap<String, LogisticsLineServiceType>();
    private static final Lock                                                lock              = new ReentrantLock();

    //客户入库-丰趣国内大贸-仓配服务
    public static LogisticsLineServiceType                                   FENG_PEI          = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       10,
                                                                                                       "丰配产品");
    //客户入库-丰趣国内保税-仓配服务
    public static LogisticsLineServiceType                                   FENG_SU           = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       20,
                                                                                                       "丰速产品");
    //客户入库-丰趣海外仓配（一单到底）
    public static LogisticsLineServiceType                                   FENG_YUN          = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       30,
                                                                                                       "丰运产品");
    //客户入库-丰趣海外仓配（非一单到底）
    public static LogisticsLineServiceType                                   FENG_YUN_VMI      = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       31,
                                                                                                       "丰运产品(丰集+VMI)");
    //BC，快件C类客户-丰趣集货-丰趣清关（一单到底）
    public static LogisticsLineServiceType                                   FENG_JI           = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       40,
                                                                                                       "丰集产品(一单到底)");
    //客户-丰趣清关（一单到底）
    public static LogisticsLineServiceType                                   FENG_JI_SF        = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       41,
                                                                                                       "丰集产品(顺丰)");
    //客户-丰趣集货-丰趣清关（非一单到底）
    public static LogisticsLineServiceType                                   FENG_JI_JIBAO_PDF = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       42,
                                                                                                       "丰集产品(非一单到底，集包物流，提供面单)");
    //客户-丰趣集货-丰趣清关（非一单到底）
    public static LogisticsLineServiceType                                   FENG_JI_JIBAO     = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       42,
                                                                                                       "丰集产品(非一单到底，集包物流)");
    //客户供应商-丰趣转运邮政
    public static LogisticsLineServiceType                                   FENG_JI_USPS_C    = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       43,
                                                                                                       "丰集产品(USPS)-C客户用丰趣系统小量发件");
    //客户国外网站-丰趣转运邮政
    public static LogisticsLineServiceType                                   FENG_JI_USPS_B    = LogisticsLineServiceType
                                                                                                   .valueOf(
                                                                                                       44,
                                                                                                       "丰集产品(USPS)-B客户有系统发货");

    private LogisticsLineServiceType(int value, String name) {
        super(value, name);
    }

    public static LogisticsLineServiceType valueOf(Integer value, String name) {
        LogisticsLineServiceType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name
                         + ", new name:" + name);
            }
        }

        Map<Integer, LogisticsLineServiceType> allbyvalue_new = new HashMap<Integer, LogisticsLineServiceType>();
        Map<String, LogisticsLineServiceType> allbyname_new = new HashMap<String, LogisticsLineServiceType>();
        e = new LogisticsLineServiceType(value, name);
        lock.lock();
        try {
            allbyvalue_new.putAll(allbyvalue);
            allbyname_new.putAll(allbyname);
            allbyvalue_new.put(value, e);
            allbyname_new.put(name, e);
            allbyvalue = allbyvalue_new;
            allbyname = allbyname_new;
        } finally {
            lock.unlock();
        }
        return e;
    }

    public static LogisticsLineServiceType valueOf(int value) {
        LogisticsLineServiceType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static LogisticsLineServiceType valueOf(String name) {
        LogisticsLineServiceType e = allbyname.get(name);
        if (e != null) {
            return e;
        } else {
            throw new IllegalArgumentException("No enum defined:" + name);
        }
    }

    public static boolean containValue(int value) {
        LogisticsLineServiceType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static LogisticsLineServiceType[] values() {
        return allbyvalue.values().toArray(new LogisticsLineServiceType[0]);
    }

    /**
     * 获取枚举的字符串列表
     * 
     * @return name:value;name:value;...
     */
    public static String getCodeListStr() {
        StringBuilder sb = new StringBuilder();
        for (LogisticsLineServiceType each : values()) {
            sb.append(each.name).append(":").append(each.value).append(",");
        }
        return sb.toString();
    }

}
