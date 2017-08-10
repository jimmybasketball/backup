package com.sfebiz.supplychain.exposed.user.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实名认证服务类型
 *
 * @author liujc
 * @create 2017-07-05 16:05
 **/
public class RNAType extends Enumerable4StringValue {


    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = 1799447059913812945L;

    private static volatile transient Map<String, RNAType> allbyvalue = new HashMap<String, RNAType>();

    private static volatile transient Map<String, RNAType> allbyname = new HashMap<String, RNAType>();

    public static RNAType YIJIFU = RNAType.valueOf("YIJIFU", "易极付");

    public static RNAType YIHUIJIN = RNAType.valueOf("YIHUIJIN", "易汇金");

    public RNAType(String value, String name) {
        super(value, name);
    }

    public static RNAType valueOf(String value, String name) {
        RNAType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, RNAType> allbyvalue_new = new HashMap<String, RNAType>();
        Map<String, RNAType> allbyname_new = new HashMap<String, RNAType>();
        e = new RNAType(value, name);
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

    public static RNAType valueOf(String value) {
        RNAType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        RNAType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static RNAType[] values() {
        return allbyvalue.values().toArray(new RNAType[0]);
    }
    
}
