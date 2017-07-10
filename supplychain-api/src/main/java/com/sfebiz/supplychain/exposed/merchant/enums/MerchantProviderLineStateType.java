package com.sfebiz.supplychain.exposed.merchant.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 开放物流平台货主供应商线路状态类型
 *
 * @author liujc
 * @create 2017-07-05 16:05
 **/
public class MerchantProviderLineStateType extends Enumerable4StringValue {
    private static final long serialVersionUID = 3732079449160619098L;

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, MerchantProviderLineStateType> allbyvalue = new HashMap<String, MerchantProviderLineStateType>();

    private static volatile transient Map<String, MerchantProviderLineStateType> allbyname = new HashMap<String, MerchantProviderLineStateType>();

    public static MerchantProviderLineStateType ENABLE = MerchantProviderLineStateType.valueOf("ENABLE", "启用");

    public static MerchantProviderLineStateType DISABLE = MerchantProviderLineStateType.valueOf("DISABLE", "禁用");

    public MerchantProviderLineStateType(String value, String name) {
        super(value, name);
    }

    public static MerchantProviderLineStateType valueOf(String value, String name) {
        MerchantProviderLineStateType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, MerchantProviderLineStateType> allbyvalue_new = new HashMap<String, MerchantProviderLineStateType>();
        Map<String, MerchantProviderLineStateType> allbyname_new = new HashMap<String, MerchantProviderLineStateType>();
        e = new MerchantProviderLineStateType(value, name);
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

    public static MerchantProviderLineStateType valueOf(String value) {
        MerchantProviderLineStateType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        MerchantProviderLineStateType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static MerchantProviderLineStateType[] values() {
        return allbyvalue.values().toArray(new MerchantProviderLineStateType[0]);
    }
    
}
