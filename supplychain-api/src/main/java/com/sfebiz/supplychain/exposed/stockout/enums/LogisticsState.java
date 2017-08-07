package com.sfebiz.supplychain.exposed.stockout.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4IntValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogisticsState extends Enumerable4IntValue {

    private static final long serialVersionUID = 2504548743721319723L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4IntValue.class);
    private static final Lock lock = new ReentrantLock();
    private static volatile transient Map<Integer, LogisticsState> allbyvalue = new HashMap<Integer, LogisticsState>();
    private static volatile transient Map<String, LogisticsState> allbyname = new HashMap<String, LogisticsState>();
    public static LogisticsState LOGISTICS_STATE_INIT = LogisticsState.valueOf(0, "初始化");
    public static LogisticsState LOGISTICS_STATE_CREATE_SUCCESS = LogisticsState.valueOf(1, "物流下单成功");
    public static LogisticsState LOGISTICS_STATE_REFUSE = LogisticsState.valueOf(2, "物流已拒单");
    public static LogisticsState LOGISTICS_STATE_ACCEPT = LogisticsState.valueOf(3, "海外仓已接受订单");
    public static LogisticsState LOGISTICS_STATE_PACK_INSTOCK = LogisticsState.valueOf(4, "海外仓包裹已收齐");

    public static LogisticsState LOGISTICS_STATE_PACK_INSTOCK_ERROR = LogisticsState.valueOf(5, "海外包裹异常");
    public static LogisticsState LOGISTICS_STATE_GOODS_WEIGHT = LogisticsState.valueOf(6, "海外仓已称重回传");
    public static LogisticsState LOGISTICS_STATE_EXCEPTION = LogisticsState.valueOf(7, "回传仓内异常信息");
    public static LogisticsState LOGISTICS_STATE_STOCKOUT_FREIGHT = LogisticsState.valueOf(8, "海外仓运费已回传");
    public static LogisticsState LOGISTICS_STATE_SEND_SUCCESS = LogisticsState.valueOf(9, "下发发货指令成功");
    public static LogisticsState LOGISTICS_STATE_STOCKOUT = LogisticsState.valueOf(10, "海外仓已发货");
    public static LogisticsState LOGISTICS_STATE_CANCEL = LogisticsState.valueOf(11, "订单取消中");
    public static LogisticsState LOGISTICS_STATE_CANCEL_SUCCESS = LogisticsState.valueOf(12, "订单已取消");
    public static LogisticsState LOGISTICS_STATE_CANCEL_ERROR = LogisticsState.valueOf(13, "物流取消失败");
    // 14 的状态已废弃
    public static LogisticsState LOGISTICS_STATE_CREATE_ERROR = LogisticsState.valueOf(14, "物流下单失败");
    // 15 的状态已废弃
    public static LogisticsState LOGISTICS_STATE_SEND_ERROR = LogisticsState.valueOf(15, "物流发货失败");
   
    public static LogisticsState LOGISTICS_STATE_TAX = LogisticsState.valueOf(16, "关税回传");
    public static LogisticsState LOGISTICS_STATE_WMS_CANCEL = LogisticsState.valueOf(17, "仓库订单已取消");

    public static LogisticsState LOGISTICS_STATE_COMPLETE = LogisticsState.valueOf(200, "用户已签收");


    private LogisticsState(int value, String name) {
        super(value, name);
    }

    public static LogisticsState valueOf(Integer value, String name) {
        LogisticsState e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<Integer, LogisticsState> allbyvalue_new = new HashMap<Integer, LogisticsState>();
        Map<String, LogisticsState> allbyname_new = new HashMap<String, LogisticsState>();
        e = new LogisticsState(value, name);
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


    public static LogisticsState valueOf(int value) {
        LogisticsState e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }


    public static LogisticsState valueOf(String name) {
        LogisticsState e = allbyname.get(name);
        if (e != null) {
            return e;
        } else {
            throw new IllegalArgumentException("No enum defined:" + name);
        }
    }

    public static boolean containValue(int value) {
        LogisticsState e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static LogisticsState[] values() {
        return allbyvalue.values().toArray(new LogisticsState[0]);
    }
}
