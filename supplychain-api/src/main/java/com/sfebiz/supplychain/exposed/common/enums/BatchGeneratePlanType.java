package com.sfebiz.supplychain.exposed.common.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 批次生成方案
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-13 9:55
 **/
public class BatchGeneratePlanType extends Enumerable4StringValue {

    private static final long serialVersionUID = 4533661515427998761L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, BatchGeneratePlanType> allbyvalue = new HashMap<String, BatchGeneratePlanType>();

    private static volatile transient Map<String, BatchGeneratePlanType> allbyname = new HashMap<String, BatchGeneratePlanType>();

    public static BatchGeneratePlanType EXPIRE_SAME = BatchGeneratePlanType.valueOf("EXPIRE_SAME", "过期日期相同");

    public static BatchGeneratePlanType STOCKIN_SAME = BatchGeneratePlanType.valueOf("STOCKIN_SAME", "入库日期相同");

    public BatchGeneratePlanType(String value, String name) {
        super(value, name);
    }

    public static BatchGeneratePlanType valueOf(String value, String name) {
        BatchGeneratePlanType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, BatchGeneratePlanType> allbyvalue_new = new HashMap<String, BatchGeneratePlanType>();
        Map<String, BatchGeneratePlanType> allbyname_new = new HashMap<String, BatchGeneratePlanType>();
        e = new BatchGeneratePlanType(value, name);
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

    public static BatchGeneratePlanType valueOf(String value) {
        BatchGeneratePlanType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        BatchGeneratePlanType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static BatchGeneratePlanType[] values() {
        return allbyvalue.values().toArray(new BatchGeneratePlanType[0]);
    }

}