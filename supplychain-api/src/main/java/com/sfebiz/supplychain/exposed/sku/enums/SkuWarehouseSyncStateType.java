package com.sfebiz.supplychain.exposed.sku.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 商品同步仓库状态
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-07-25 10:13
 **/
public class SkuWarehouseSyncStateType extends Enumerable4StringValue {

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = 1244584150755160676L;

    private static volatile transient Map<String, SkuWarehouseSyncStateType> allbyvalue = new HashMap<String, SkuWarehouseSyncStateType>();

    private static volatile transient Map<String, SkuWarehouseSyncStateType> allbyname = new HashMap<String, SkuWarehouseSyncStateType>();

    public static SkuWarehouseSyncStateType SYNC_SUCCESS = valueOf("SYNC_SUCCESS", "同步成功");
    public static SkuWarehouseSyncStateType SYNC_FAIL = valueOf("SYNC_FAIL", "同步失败");
    public static SkuWarehouseSyncStateType SYNC_UPDATE_SUCCESS = valueOf("SYNC_UPDATE_SUCCESS", "同步更新成功");
    public static SkuWarehouseSyncStateType SYNC_UPDATE_FAIL = valueOf("SYNC_UPDATE_FAIL", "同步更新失败");


    public SkuWarehouseSyncStateType(String value, String name) {
        super(value, name);
    }

    public static SkuWarehouseSyncStateType valueOf(String value, String name) {
        SkuWarehouseSyncStateType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, SkuWarehouseSyncStateType> allbyvalue_new = new HashMap<String, SkuWarehouseSyncStateType>();
        Map<String, SkuWarehouseSyncStateType> allbyname_new = new HashMap<String, SkuWarehouseSyncStateType>();
        e = new SkuWarehouseSyncStateType(value, name);
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

    public static SkuWarehouseSyncStateType valueOf(String value) {
        SkuWarehouseSyncStateType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        SkuWarehouseSyncStateType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static SkuWarehouseSyncStateType[] values() {
        return allbyvalue.values().toArray(new SkuWarehouseSyncStateType[0]);
    }
}