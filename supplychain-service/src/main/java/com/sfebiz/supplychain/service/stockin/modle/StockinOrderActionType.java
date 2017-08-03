package com.sfebiz.supplychain.service.stockin.modle;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangyajing on 2017/7/17.
 */
public class StockinOrderActionType extends Enumerable4StringValue{

    private static final long serialVersionUID = 2504548743721319723L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final Lock lock = new ReentrantLock();
    private static volatile transient Map<String, StockinOrderActionType> allbyvalue = new HashMap<String, StockinOrderActionType>();
    private static volatile transient Map<String, StockinOrderActionType> allbyname = new HashMap<String, StockinOrderActionType>();
    public static StockinOrderActionType STOCKIN_TO_CREATE = StockinOrderActionType.valueOf("STOCKIN_TO_CREATE","创建入库单");
    public static StockinOrderActionType STOCKIN_TO_SUBMIT = StockinOrderActionType.valueOf("STOCKIN_TO_SUBMIT","提交给仓库");
    public static StockinOrderActionType STOCKIN_TO_FINISH = StockinOrderActionType.valueOf("STOCKIN_TO_FINISH","手工完成入库单");
    public static StockinOrderActionType STOCKIN_TO_CANCEL = StockinOrderActionType.valueOf("STOCKIN_TO_CANCEL","取消入库单");

    public StockinOrderActionType(String value, String name) {
        super(value, name);
    }

    public static StockinOrderActionType valueOf(String value, String name) {
        StockinOrderActionType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name)) {
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            } else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }
        Map<String, StockinOrderActionType> allbyvalue_new = new HashMap<String, StockinOrderActionType>();
        Map<String, StockinOrderActionType> allbyname_new = new HashMap<String, StockinOrderActionType>();
        e = new StockinOrderActionType(value, name);
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

    public static StockinOrderActionType valueOf(String value) {
        StockinOrderActionType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static StockinOrderActionType[] values() {
        return allbyvalue.values().toArray(new StockinOrderActionType[0]);
    }
}
