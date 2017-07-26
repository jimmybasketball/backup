package com.sfebiz.supplychain.exposed.warehouse.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * 仓库操作类型
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-24 14:52
 **/
public class WmsOperaterType extends Enumerable4StringValue {

    private static volatile transient Map<String, WmsOperaterType> allbyvalue = new HashMap<String, WmsOperaterType>();

    private static volatile transient Map<String, WmsOperaterType> allbyname = new HashMap<String, WmsOperaterType>();

    private static final Logger log = LoggerFactory.getLogger(WmsOperaterType.class);

    private static final Lock lock = new ReentrantLock();

    /** 序号 */
    private static final long serialVersionUID = -8236642457747480538L;

    /** 新增 */
    public static WmsOperaterType ADD = WmsOperaterType.valueOf("NEW","新增");

    /** 修改 */
    public static WmsOperaterType UPDATE = WmsOperaterType.valueOf("SAVE", "修改");

    public WmsOperaterType(String value, String name) {
        super(value, name);
    }

    public static WmsOperaterType valueOf(String value, String name) {
        WmsOperaterType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                // undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新;
                // 但是不能用undefined覆盖已有值
                return e;
            else {
                // 命名不相同
                log.warn("Name to be change. value:" + value + ", old name:"
                        + e.name + ", new name:" + name);
            }
        }

        Map<String, WmsOperaterType> allbyvalue_new = new HashMap<String, WmsOperaterType>();
        Map<String, WmsOperaterType> allbyname_new = new HashMap<String, WmsOperaterType>();
        e = new WmsOperaterType(value, name);
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

    public static WmsOperaterType valueOf(String value) {
        WmsOperaterType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        WmsOperaterType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static WmsOperaterType[] values() {
        return allbyvalue.values().toArray(new WmsOperaterType[0]);
    }

}