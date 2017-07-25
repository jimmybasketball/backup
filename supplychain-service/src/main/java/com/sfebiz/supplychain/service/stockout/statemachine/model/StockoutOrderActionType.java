package com.sfebiz.supplychain.service.stockout.statemachine.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * <p>出库单状态转换事件,都是动作，所以为动词</p>
 * User: 心远
 * Date: 14/12/18
 * Time: 上午1:39
 */
public class StockoutOrderActionType extends Enumerable4StringValue {

    private static final long                                              serialVersionUID    = 2504548743721319723L;
    private static final Logger                                            log                 = LoggerFactory
                                                                                                   .getLogger(Enumerable4StringValue.class);
    private static volatile transient Map<String, StockoutOrderActionType> allbyvalue          = new HashMap<String, StockoutOrderActionType>();
    private static volatile transient Map<String, StockoutOrderActionType> allbyname           = new HashMap<String, StockoutOrderActionType>();
    private static final Lock                                              lock                = new ReentrantLock();

    public static StockoutOrderActionType                                  CREATE              = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "CREATE",
                                                                                                       "创建出库单");
    public static StockoutOrderActionType                                  AUDIT               = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "AUDIT",
                                                                                                       "运营审核通过");
    public static StockoutOrderActionType                                  STOCKOUT            = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "STOCKOUT",
                                                                                                       "订单已下发仓库");
    public static StockoutOrderActionType                                  SEND                = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "SEND",
                                                                                                       "仓库已发货");
    public static StockoutOrderActionType                                  OVERSEA_STOCKIN     = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "OVERSEA_STOCKIN",
                                                                                                       "海外仓入库");
    public static StockoutOrderActionType                                  OVERSEA_STOCKOUT    = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "OVERSEA_STOCKOUT",
                                                                                                       "海外仓出库");
    public static StockoutOrderActionType                                  OVERSEA_SEND        = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "OVERSEA_SEND",
                                                                                                       "海外运输");
    public static StockoutOrderActionType                                  CLEARANCE_PICK      = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "CLEARANCE_PICK",
                                                                                                       "清关公司提货");
    public static StockoutOrderActionType                                  CLEARANCE_DO        = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "CLEARANCE_DO",
                                                                                                       "开始清关");
    public static StockoutOrderActionType                                  CLEARANCE_CHECK     = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "CLEARANCE_CHECK",
                                                                                                       "结束异常");
    public static StockoutOrderActionType                                  CLEARANCE_DONE      = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "CLEARANCE_DONE",
                                                                                                       "结束清关");
    public static StockoutOrderActionType                                  POST                = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "POST",
                                                                                                       "快递已揽收");
    public static StockoutOrderActionType                                  DELIVER             = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "DELIVER",
                                                                                                       "快递已派件");
    public static StockoutOrderActionType                                  SIGN                = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "SIGN",
                                                                                                       "用户已签收");
    public static StockoutOrderActionType                                  CANCEL              = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "CANCEL",
                                                                                                       "订单取消");
    public static StockoutOrderActionType                                  CLOSE               = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "CLOSE",
                                                                                                       "订单关闭");
    public static StockoutOrderActionType                                  STOCKOUT_PUR_RETURN = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "STOCKOUT_PUR_RETURN",
                                                                                                       "采退出库单无需下发仓库，直接出库");
    public static StockoutOrderActionType                                  STOCKOUT_SALES_SLIP = StockoutOrderActionType
                                                                                                   .valueOf(
                                                                                                       "STOCKOUT_SALES_SLIP",
                                                                                                       "销售出库单无需下发仓库，直接出库");

    private StockoutOrderActionType(String value, String name) {
        super(value, name);
    }

    public static StockoutOrderActionType valueOf(String value, String name) {
        StockoutOrderActionType e = allbyvalue.get(value);
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

        Map<String, StockoutOrderActionType> allbyvalue_new = new HashMap<String, StockoutOrderActionType>();
        Map<String, StockoutOrderActionType> allbyname_new = new HashMap<String, StockoutOrderActionType>();
        e = new StockoutOrderActionType(value, name);
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

    public static StockoutOrderActionType valueOf(String value) {
        StockoutOrderActionType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static StockoutOrderActionType[] values() {
        return allbyvalue.values().toArray(new StockoutOrderActionType[0]);
    }

}
