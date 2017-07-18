package com.sfebiz.supplychain.exposed.stockout.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * 
 * <p>
 * 任务类型
 * </p>
 * 
 * @author wuyun
 * @Date 2017年7月17日 下午4:59:52
 */
public class TaskType extends Enumerable4StringValue {

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final long serialVersionUID = -7642715395887753254L;
    private static volatile transient Map<String, TaskType> allbyvalue =
            new HashMap<String, TaskType>();
    private static volatile transient Map<String, TaskType> allbyname =
            new HashMap<String, TaskType>();
    private static final Lock lock = new ReentrantLock();

    /** 出库异常 */
    public static TaskType STOCKOUT_EXCEPTION = TaskType.valueOf("STOCKOUT_EXCEPTION", "出库异常");

    /** 损益单确认异常 */
    public static TaskType GAL_ORDER_CONFIRM_EXCEPTION =
            TaskType.valueOf("GAL_ORDER_CONFIRM_EXCEPTION", "损益单确认异常");

    /** 给供应商下发订单异常 */
    public static TaskType STOCKOUT_CREATE_EXCEPTION =
            TaskType.valueOf("STOCKOUT_CREATE_EXCEPTION", "给供应商下发订单异常");

    /** 给供应商下发发货异常 */
    public static TaskType STOCKOUT_SEND_EXCEPTION =
            TaskType.valueOf("STOCKOUT_SEND_EXCEPTION", "给供应商下发发货异常");

    /** 发送消息异常 */
    public static TaskType MESAGE_SEND_EXCEPTION =
            TaskType.valueOf("MESAGE_SEND_EXCEPTION", "发送消息异常");

    /** 创建出库单异常 */
    public static TaskType CREATE_STOCKOUT_EXCEPTION =
            TaskType.valueOf("CREATE_STOCKOUT_EXCEPTION", "创建出库单异常");

    /** 快递100订阅异常 */
    public static TaskType KD100_REGIST_EXCEPTION =
            TaskType.valueOf("KD100_REGIST_EXCEPTION", "快递100订阅异常");

    private TaskType(String value, String name) {
        super(value, name);
    }

    public static TaskType valueOf(String value, String name) {
        TaskType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                // undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                // 命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name
                        + ", new name:" + name);
            }
        }

        Map<String, TaskType> allbyvalue_new = new HashMap<String, TaskType>();
        Map<String, TaskType> allbyname_new = new HashMap<String, TaskType>();
        e = new TaskType(value, name);
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


    public static TaskType valueOf(String value) {
        TaskType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean contain(String value) {
        TaskType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static TaskType[] values() {
        return allbyvalue.values().toArray(new TaskType[0]);
    }

}
