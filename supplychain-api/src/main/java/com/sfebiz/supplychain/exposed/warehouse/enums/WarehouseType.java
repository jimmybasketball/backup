package com.sfebiz.supplychain.exposed.warehouse.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * 
 * <p>仓库类型</p>
 * 
 * @author matt
 * @Date 2017年7月7日 下午6:15:52
 */
public class WarehouseType extends Enumerable4StringValue {

    private static final long                                    serialVersionUID = 2504548743721319723L;
    private static final Logger                                  log              = LoggerFactory
                                                                                      .getLogger(Enumerable4StringValue.class);
    private static volatile transient Map<String, WarehouseType> allbyvalue       = new HashMap<String, WarehouseType>();
    private static volatile transient Map<String, WarehouseType> allbyname        = new HashMap<String, WarehouseType>();
    private static final Lock                                    lock             = new ReentrantLock();
    
    /** 国内仓 */
    public static WarehouseType                                  DOMESTIC         = WarehouseType
                                                                                      .valueOf(
                                                                                          "DOMESTIC",
                                                                                          "国内仓");

    /** 保税仓 */
    public static WarehouseType                                  BONDED           = WarehouseType
                                                                                      .valueOf(
                                                                                          "BONDED",
                                                                                          "保税仓");

    /** 海外仓 */
    public static WarehouseType                                  OVERSEAS         = WarehouseType
                                                                                      .valueOf(
                                                                                          "OVERSEAS",
                                                                                          "海外仓");

    private WarehouseType(String value, String name) {
        super(value, name);
    }

    public static WarehouseType valueOf(String value, String name) {
        WarehouseType e = allbyvalue.get(value);
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

        Map<String, WarehouseType> allbyvalue_new = new HashMap<String, WarehouseType>();
        Map<String, WarehouseType> allbyname_new = new HashMap<String, WarehouseType>();
        e = new WarehouseType(value, name);
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

    public static WarehouseType valueOf(String value) {
        WarehouseType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static WarehouseType[] values() {
        return allbyvalue.values().toArray(new WarehouseType[0]);
    }

}
