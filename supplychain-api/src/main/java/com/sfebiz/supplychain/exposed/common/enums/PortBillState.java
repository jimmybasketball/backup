package com.sfebiz.supplychain.exposed.common.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>口岸订单状态,纯粹的状态集合，状态间可能没有必然关系</p>
 * User: 心远
 * Date: 15/03/09
 * Time: 上午1:29
 */
public class PortBillState extends Enumerable4StringValue {
    private static final long serialVersionUID = 2504548743721319723L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static volatile transient Map<String, PortBillState> allbyvalue = new HashMap<String, PortBillState>();
    private static volatile transient Map<String, PortBillState> allbyname = new HashMap<String, PortBillState>();
    private static final Lock lock = new ReentrantLock();

    public static PortBillState WAIT_SEND = PortBillState.valueOf("WAIT_SEND", "待发送");
    public static PortBillState SEND_EXCEPTION = PortBillState.valueOf("SEND_EXCEPTION", "发送异常");
    public static PortBillState PARAMS_EXCEPTION = PortBillState.valueOf("PARAMS_EXCEPTION", "参数异常");
    public static PortBillState SEND_SUCCESS = PortBillState.valueOf("SEND_SUCCESS", "发送成功");
    public static PortBillState VERIFY_CALLBACK = PortBillState.valueOf("VERIFY_CALLBACK", "审单结果已回传");
    public static PortBillState CANCEL = PortBillState.valueOf("CANCEL", "已取消");

    private PortBillState(String value, String name) {
        super(value, name);
    }

    public static PortBillState valueOf(String value, String name) {
        PortBillState e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, PortBillState> allbyvalue_new = new HashMap<String, PortBillState>();
        Map<String, PortBillState> allbyname_new = new HashMap<String, PortBillState>();
        e = new PortBillState(value, name);
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


    public static PortBillState valueOf(String value) {
        PortBillState e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        PortBillState e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static PortBillState[] values() {
        return allbyvalue.values().toArray(new PortBillState[0]);
    }

}
