package com.sfebiz.supplychain.exposed.sku.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线路类型
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-18 16:39
 **/
public class SkuDeclareStateType extends Enumerable4StringValue {

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = 1244584150755160676L;

    private static volatile transient Map<String, SkuDeclareStateType> allbyvalue = new HashMap<String, SkuDeclareStateType>();

    private static volatile transient Map<String, SkuDeclareStateType> allbyname = new HashMap<String, SkuDeclareStateType>();

    public static SkuDeclareStateType DECLARE_WAIT = valueOf("DECLARE_WAIT", "待备案");
    public static SkuDeclareStateType DECLARE_PASS = valueOf("DECLARE_PASS", "备案通过");
    public static SkuDeclareStateType DECLARE_NOT_PASS = valueOf("DECLARE_NOT_PASS", "备案不通过");


    public SkuDeclareStateType(String value, String name) {
        super(value, name);
    }

    public static SkuDeclareStateType valueOf(String value, String name) {
        SkuDeclareStateType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, SkuDeclareStateType> allbyvalue_new = new HashMap<String, SkuDeclareStateType>();
        Map<String, SkuDeclareStateType> allbyname_new = new HashMap<String, SkuDeclareStateType>();
        e = new SkuDeclareStateType(value, name);
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

    public static SkuDeclareStateType valueOf(String value) {
        SkuDeclareStateType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        SkuDeclareStateType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static SkuDeclareStateType[] values() {
        return allbyvalue.values().toArray(new SkuDeclareStateType[0]);
    }

}