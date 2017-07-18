package com.sfebiz.supplychain.exposed.stockout.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * 
 * <p>
 * 任务状态
 * </p>
 * 
 * @author wuyun
 * @Date 2017年7月17日 下午4:59:21
 */
public class TaskStatus extends Enumerable4StringValue {

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final long serialVersionUID = -7642715395887753254L;
    private static volatile transient Map<String, TaskStatus> allbyvalue =
            new HashMap<String, TaskStatus>();
    private static volatile transient Map<String, TaskStatus> allbyname =
            new HashMap<String, TaskStatus>();
    private static final Lock lock = new ReentrantLock();

    public static TaskStatus WAIT_HANDLE = TaskStatus.valueOf("WAIT_HANDLE", "待处理");
    public static TaskStatus HANDLE_SUCCESS = TaskStatus.valueOf("HANDLE_SUCCESS", "处理成功");

    private TaskStatus(String value, String name) {
        super(value, name);
    }

    public static TaskStatus valueOf(String value, String name) {
        TaskStatus e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                // undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                // 命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name
                        + ", new name:" + name);
            }
        }

        Map<String, TaskStatus> allbyvalue_new = new HashMap<String, TaskStatus>();
        Map<String, TaskStatus> allbyname_new = new HashMap<String, TaskStatus>();
        e = new TaskStatus(value, name);
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


    public static TaskStatus valueOf(String value) {
        TaskStatus e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean contain(String value) {
        TaskStatus e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static TaskStatus[] values() {
        return allbyvalue.values().toArray(new TaskStatus[0]);
    }

}