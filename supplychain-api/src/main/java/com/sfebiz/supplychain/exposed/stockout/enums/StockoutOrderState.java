package com.sfebiz.supplychain.exposed.stockout.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * <p>出库单状态</p>
 * User: 心远
 * Date: 14/12/17
 * Time: 上午1:29
 */
public class StockoutOrderState extends Enumerable4StringValue {
    
    private static final long serialVersionUID = 2504548743721319723L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static volatile transient Map<String, StockoutOrderState> allbyvalue = new HashMap<String, StockoutOrderState>();
    private static volatile transient Map<String, StockoutOrderState> allbyname = new HashMap<String, StockoutOrderState>();
    private static final Lock lock = new ReentrantLock();

    public static StockoutOrderState WAIT_AUDITING = StockoutOrderState.valueOf("WAIT_AUDITING", "待审核");
    public static StockoutOrderState WAIT_STOCKOUT = StockoutOrderState.valueOf("WAIT_STOCKOUT", "待出库");
    public static StockoutOrderState STOCKOUTING = StockoutOrderState.valueOf("STOCKOUTING", "出库中");
    public static StockoutOrderState STOCKOUT_FINISH = StockoutOrderState.valueOf("STOCKOUT_FINISH", "已出库");
    public static StockoutOrderState OVERSEA_STOCKIN_FINISH = StockoutOrderState.valueOf("OVERSEA_STOCKIN_FINISH", "海外仓已入库");
    public static StockoutOrderState OVERSEA_STOCKOUT_FINISH = StockoutOrderState.valueOf("OVERSEA_STOCKOUT_FINISH", "海外仓已出库");
    public static StockoutOrderState OVERSEA_DELIVERING = StockoutOrderState.valueOf("OVERSEA_DELIVERING", "海外运输开始");
    public static StockoutOrderState CLEARANCE_PICKING = StockoutOrderState.valueOf("CLEARANCE_PICKING", "清关公司提货");
    public static StockoutOrderState CLEARANCE_BEGIN = StockoutOrderState.valueOf("CLEARANCE_BEGIN", "清关开始");
    public static StockoutOrderState CLEARANCE_CHECKING = StockoutOrderState.valueOf("CLEARANCE_CHECKING", "清关异常");
    public static StockoutOrderState CLEARANCE_END = StockoutOrderState.valueOf("CLEARANCE_END", "清关结束");
    public static StockoutOrderState COLLECTED = StockoutOrderState.valueOf("COLLECTED", "快递已收件");
    public static StockoutOrderState DELIVEING = StockoutOrderState.valueOf("DELIVEING", "快递派件中");
    public static StockoutOrderState SIGNED = StockoutOrderState.valueOf("SIGNED", "用户已签收");
    public static StockoutOrderState STOCKOUT_CANCEL = StockoutOrderState.valueOf("STOCKOUT_CANCEL", "已取消");
    public static StockoutOrderState CLOSED = StockoutOrderState.valueOf("CLOSED", "已关闭");
    public static StockoutOrderState INTERNATIONAL_DELIVEING= StockoutOrderState.valueOf("INTERNATIONAL_DELIVEING", "国际段运输中");

    private StockoutOrderState(String value, String name) {
        super(value, name);
    }

    public static StockoutOrderState valueOf(String value, String name) {
        StockoutOrderState e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, StockoutOrderState> allbyvalue_new = new HashMap<String, StockoutOrderState>();
        Map<String, StockoutOrderState> allbyname_new = new HashMap<String, StockoutOrderState>();
        e = new StockoutOrderState(value, name);
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


    public static StockoutOrderState valueOf(String value) {
        StockoutOrderState e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        StockoutOrderState e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StockoutOrderState[] values() {
        return allbyvalue.values().toArray(new StockoutOrderState[0]);
    }

    public static void main(String[] args) {
        StockoutOrderState stockoutOrderState = StockoutOrderState.STOCKOUT_CANCEL;
        System.out.println(stockoutOrderState);
    }
}
