package com.sfebiz.supplychain.service.statemachine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/5/17 下午7:40
 *
 * 这些状态机类型需要配置响应的状态机服务和模板，请参考application-statemachine.xml配置
 *
 */
public class EngineType extends Enumerable4StringValue {

    private static final long                                 serialVersionUID = 2504548743721319723L;
    private static final Logger                               log              = LoggerFactory
                                                                                   .getLogger(Enumerable4StringValue.class);
    private static final Lock                                 lock             = new ReentrantLock();
    private static volatile transient Map<String, EngineType> allbyvalue       = new HashMap<String, EngineType>();
    private static volatile transient Map<String, EngineType> allbyname        = new HashMap<String, EngineType>();

    /** 入库单状态机 */
    public static EngineType                                  STOCKIN_ORDER    = EngineType
                                                                                   .valueOf(
                                                                                       "STOCKIN_ORDER",
                                                                                       "入库单状态机");

    public static EngineType                                  STOCKOUT_ORDER   = EngineType
                                                                                   .valueOf(
                                                                                       "STOCKOUT_ORDER",
                                                                                       "出库单状态机");

    private EngineType(String value, String name) {
        super(value, name);
    }

    public static EngineType valueOf(String value, String name) {
        EngineType e = allbyvalue.get(value);
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

        Map<String, EngineType> allbyvalue_new = new HashMap<String, EngineType>();
        Map<String, EngineType> allbyname_new = new HashMap<String, EngineType>();
        e = new EngineType(value, name);
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

    public static EngineType valueOf(String value) {
        EngineType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static EngineType[] values() {
        return allbyvalue.values().toArray(new EngineType[0]);
    }
}
