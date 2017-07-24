package com.sfebiz.supplychain.exposed.common.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 包材类型
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-19 15:36
 **/
public class PackageMaterialType extends Enumerable4StringValue{
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = -7655868653620598595L;

    private static volatile transient Map<String, PackageMaterialType> allbyvalue = new HashMap<String, PackageMaterialType>();

    private static volatile transient Map<String, PackageMaterialType> allbyname = new HashMap<String, PackageMaterialType>();

    public static PackageMaterialType FQ = PackageMaterialType.valueOf("FQ", "丰趣");

    public static PackageMaterialType NEUTER = PackageMaterialType.valueOf("NEUTER", "中性");



    public PackageMaterialType(String value, String name) {
        super(value, name);
    }

    public static PackageMaterialType valueOf(String value, String name) {
        PackageMaterialType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, PackageMaterialType> allbyvalue_new = new HashMap<String, PackageMaterialType>();
        Map<String, PackageMaterialType> allbyname_new = new HashMap<String, PackageMaterialType>();
        e = new PackageMaterialType(value, name);
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

    public static PackageMaterialType valueOf(String value) {
        PackageMaterialType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        PackageMaterialType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static PackageMaterialType[] values() {
        return allbyvalue.values().toArray(new PackageMaterialType[0]);
    }
}