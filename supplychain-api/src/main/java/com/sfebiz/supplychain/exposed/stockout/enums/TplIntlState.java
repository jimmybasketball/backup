package com.sfebiz.supplychain.exposed.stockout.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4IntValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 国际物流下单状态
 *
 * @author tanzx [tanzongxi@ifunq.com]
 * @date 2017-08-01 10:07
 **/
public class TplIntlState extends Enumerable4IntValue {
    private static final long serialVersionUID = 2504548743721319723L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4IntValue.class);
    private static volatile transient Map<Integer, TplIntlState> allbyvalue = new HashMap<Integer, TplIntlState>();
    private static volatile transient Map<String, TplIntlState> allbyname = new HashMap<String, TplIntlState>();
    private static final Lock lock = new ReentrantLock();

    public static TplIntlState UNUSING = TplIntlState.valueOf(-1, "未分配");
    public static TplIntlState NIT = TplIntlState.valueOf(0, "初始化");
    public static TplIntlState CREATE_SUCC = TplIntlState.valueOf(1, "下单成功");
    public static TplIntlState CONFIRM_SUCC = TplIntlState.valueOf(2, "确认订单成功");
    public static TplIntlState CONFIRM_FAIL = TplIntlState.valueOf(3, "确认订单失败");
    public static TplIntlState CANCEL_SUCC = TplIntlState.valueOf(4, "取消成功");
    public static TplIntlState CANCEL_FAIL = TplIntlState.valueOf(5, "取消失败");
    public static TplIntlState CREATE_FAIL = TplIntlState.valueOf(6, "下单失败");

    public TplIntlState(int value, String name) {
        super(value, name);
    }

    public static TplIntlState valueOf(Integer value, String name) {
        TplIntlState e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<Integer, TplIntlState> allbyvalue_new = new HashMap<Integer, TplIntlState>();
        Map<String, TplIntlState> allbyname_new = new HashMap<String, TplIntlState>();
        e = new TplIntlState(value, name);
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


    public static TplIntlState valueOf(int value) {
        TplIntlState e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }


    public static TplIntlState valueOf(String name) {
        TplIntlState e = allbyname.get(name);
        if (e != null) {
            return e;
        } else {
            throw new IllegalArgumentException("No enum defined:" + name);
        }
    }

    public static boolean containValue(int value) {
        TplIntlState e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static TplIntlState[] values() {
        return allbyvalue.values().toArray(new TplIntlState[0]);
    }

}