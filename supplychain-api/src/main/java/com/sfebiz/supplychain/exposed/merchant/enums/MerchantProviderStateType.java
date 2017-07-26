package com.sfebiz.supplychain.exposed.merchant.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 开放物流平台货主供应商状态类型
 *
 * @author liujc
 * @create 2017-07-05 16:05
 **/
public class MerchantProviderStateType extends Enumerable4StringValue {


    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = 6414474358252572778L;

    private static volatile transient Map<String, MerchantProviderStateType> allbyvalue = new HashMap<String, MerchantProviderStateType>();

    private static volatile transient Map<String, MerchantProviderStateType> allbyname = new HashMap<String, MerchantProviderStateType>();

    public static MerchantProviderStateType ENABLE = MerchantProviderStateType.valueOf("ENABLE", "启用");

    public static MerchantProviderStateType DISABLE = MerchantProviderStateType.valueOf("DISABLE", "禁用");

    public MerchantProviderStateType(String value, String name) {
        super(value, name);
    }

    public static MerchantProviderStateType valueOf(String value, String name) {
        MerchantProviderStateType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, MerchantProviderStateType> allbyvalue_new = new HashMap<String, MerchantProviderStateType>();
        Map<String, MerchantProviderStateType> allbyname_new = new HashMap<String, MerchantProviderStateType>();
        e = new MerchantProviderStateType(value, name);
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

    public static MerchantProviderStateType valueOf(String value) {
        MerchantProviderStateType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        MerchantProviderStateType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static MerchantProviderStateType[] values() {
        return allbyvalue.values().toArray(new MerchantProviderStateType[0]);
    }
    
}
