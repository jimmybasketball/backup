package com.sfebiz.supplychain.service.sku.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4IntValue;

public class SkuType extends Enumerable4IntValue {

    private static final long                               serialVersionUID     = 2504548743721319723L;
    private static final Logger                             log                  = LoggerFactory
                                                                                     .getLogger(SkuType.class);
    private static volatile transient Map<Integer, SkuType> allbyvalue           = new HashMap<Integer, SkuType>();
    private static volatile transient Map<String, SkuType>  allbyname            = new HashMap<String, SkuType>();
    private static final Lock                               lock                 = new ReentrantLock();

    public static SkuType                                   BASIC_SKU            = SkuType.valueOf(
                                                                                     0, "销售基本商品");
    public static SkuType                                   MIX_SKU              = SkuType.valueOf(
                                                                                     1, "销售组合商品");
    public static SkuType                                   BASIC_SKU_OF_MIX_SKU = SkuType.valueOf(
                                                                                     2,
                                                                                     "销售组合商品内基本商品");

    private SkuType(int value, String name) {
        super(value, name);
    }

    public static SkuType valueOf(Integer value, String name) {
        SkuType e = allbyvalue.get(value);
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

        Map<Integer, SkuType> allbyvalue_new = new HashMap<Integer, SkuType>();
        Map<String, SkuType> allbyname_new = new HashMap<String, SkuType>();
        e = new SkuType(value, name);
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

    public static SkuType valueOf(int value) {
        SkuType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static SkuType valueOf(String name) {
        SkuType e = allbyname.get(name);
        if (e != null) {
            return e;
        } else {
            throw new IllegalArgumentException("No enum defined:" + name);
        }
    }

    public static boolean containValue(int value) {
        SkuType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static SkuType[] values() {
        return allbyvalue.values().toArray(new SkuType[0]);
    }

}
