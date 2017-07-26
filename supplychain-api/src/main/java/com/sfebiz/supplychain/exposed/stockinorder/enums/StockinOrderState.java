package com.sfebiz.supplychain.exposed.stockinorder.enums;

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
public class StockinOrderState extends Enumerable4StringValue {

    private static final long serialVersionUID = 2504548743721319723L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final Lock lock = new ReentrantLock();
    private static volatile transient Map<String, StockinOrderState> allbyvalue = new HashMap<String, StockinOrderState>();
    private static volatile transient Map<String, StockinOrderState> allbyname = new HashMap<String, StockinOrderState>();
    public static StockinOrderState TO_BE_SUBMITED = StockinOrderState.valueOf("TO_BE_SUBMITED", "待提交");
    public static StockinOrderState WAREHOUSING = StockinOrderState.valueOf("WAREHOUSING", "干线运输中");
    public static StockinOrderState STOCKIN_FINISH = StockinOrderState.valueOf("STOCKIN_FINISH", "收货完成");
    public static StockinOrderState STOCKIN_CANCLE = StockinOrderState.valueOf("STOCKIN_CANCLE", "已取消");

    public StockinOrderState(String value, String name) {
        super(value, name);
    }

    public static StockinOrderState valueOf(String value,String name) {
        StockinOrderState e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, StockinOrderState> allbyvalue_new = new HashMap<String, StockinOrderState>();
        Map<String, StockinOrderState> allbyname_new = new HashMap<String, StockinOrderState>();
        e = new StockinOrderState(value, name);
        lock.lock();
        try {
            allbyvalue_new.putAll(allbyvalue);
            allbyname_new.putAll(allbyname);
            allbyvalue_new.put(value, e);
            allbyname_new.put(name, e);
            allbyvalue = allbyvalue_new;
            allbyname = allbyname_new;
        }
        finally {
            lock.unlock();
        }
        return e;
    }


    public static StockinOrderState valueOf(String value) {
        StockinOrderState e = allbyvalue.get(value);
        if (e != null) {
            return e;
        }
        else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        StockinOrderState e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockinOrderState[] values() {
        return allbyvalue.values().toArray(new StockinOrderState[0]);
    }
}
