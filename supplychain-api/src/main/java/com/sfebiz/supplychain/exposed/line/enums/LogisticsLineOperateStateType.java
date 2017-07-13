package com.sfebiz.supplychain.exposed.line.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线路运营状态
 *
 * @author liujc
 * @create 2017-07-05 16:05
 **/
public class LogisticsLineOperateStateType extends Enumerable4StringValue {

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = -2002750560453167578L;

    private static volatile transient Map<String, LogisticsLineOperateStateType> allbyvalue = new HashMap<String, LogisticsLineOperateStateType>();

    private static volatile transient Map<String, LogisticsLineOperateStateType> allbyname = new HashMap<String, LogisticsLineOperateStateType>();

    public static LogisticsLineOperateStateType NORMAL = LogisticsLineOperateStateType.valueOf("NORMAL", "正常");

    public static LogisticsLineOperateStateType STOPED = LogisticsLineOperateStateType.valueOf("STOPED", "停止");

    public LogisticsLineOperateStateType(String value, String name) {
        super(value, name);
    }

    public static LogisticsLineOperateStateType valueOf(String value, String name) {
        LogisticsLineOperateStateType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, LogisticsLineOperateStateType> allbyvalue_new = new HashMap<String, LogisticsLineOperateStateType>();
        Map<String, LogisticsLineOperateStateType> allbyname_new = new HashMap<String, LogisticsLineOperateStateType>();
        e = new LogisticsLineOperateStateType(value, name);
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

    public static LogisticsLineOperateStateType valueOf(String value) {
        LogisticsLineOperateStateType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        LogisticsLineOperateStateType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static LogisticsLineOperateStateType[] values() {
        return allbyvalue.values().toArray(new LogisticsLineOperateStateType[0]);
    }
    
}
