package com.sfebiz.supplychain.exposed.common.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 申报方式
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-19 15:36
 **/
public class DeclarePayType extends Enumerable4StringValue{
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = 5513951343695134810L;

    private static volatile transient Map<String, DeclarePayType> allbyvalue = new HashMap<String, DeclarePayType>();

    private static volatile transient Map<String, DeclarePayType> allbyname = new HashMap<String, DeclarePayType>();

    public static DeclarePayType ALIPAY = DeclarePayType.valueOf("ALIPAY", "支付宝");

    public static DeclarePayType TENPAY = DeclarePayType.valueOf("TENPAY", "财付通");

    public static DeclarePayType LIANLIANPAY = DeclarePayType.valueOf("LIANLIANPAY", "连连");

    public static DeclarePayType YIHUIJINPAY = DeclarePayType.valueOf("YIHUIJINPAY", "易汇金");

    public static DeclarePayType TENPAY_INTL = DeclarePayType.valueOf("TENPAY_INTL", "财付通");

    public static DeclarePayType PTPAY = DeclarePayType.valueOf("PTPAY", "平潭");

    public static DeclarePayType NEWYIHUIJINPAY = DeclarePayType.valueOf("NEWYIHUIJINPAY", "新易汇金");

    public static DeclarePayType YIJIFUPAY = DeclarePayType.valueOf("YIJIFUPAY", "易极付");

    public static DeclarePayType BAOFOOPAY = DeclarePayType.valueOf("BAOFOOPAY", "宝付");

    public DeclarePayType(String value, String name) {
        super(value, name);
    }

    public static DeclarePayType valueOf(String value, String name) {
        DeclarePayType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, DeclarePayType> allbyvalue_new = new HashMap<String, DeclarePayType>();
        Map<String, DeclarePayType> allbyname_new = new HashMap<String, DeclarePayType>();
        e = new DeclarePayType(value, name);
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

    public static DeclarePayType valueOf(String value) {
        DeclarePayType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        DeclarePayType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static DeclarePayType[] values() {
        return allbyvalue.values().toArray(new DeclarePayType[0]);
    }
}
