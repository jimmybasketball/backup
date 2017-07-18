package com.sfebiz.supplychain.exposed.stockout.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4IntValue;

/**
 * 
 * <p>出库单服务类型</p>
 * 
 * 10:丰配产品  
 * 20:丰速产品 
 * 30:丰运产品 
 * 31:丰运产品（丰集+VMI)  
 * 40:丰集产品（一单到底)   
 * 41:丰集产品（顺丰)    
 * 42:丰集产品（非一单到底，集包物流，提供面单）  
 * 43:丰集产品（非一单到底 集包物流）  
 * 44:丰集产品（USPS）C客户用丰趣系统 小量发件  
 * 45:丰集产品（USPS）B客户有系统 发货
 * 
 * @author matt
 * @Date 2017年7月17日 下午10:39:56
 */
public class ServiceType extends Enumerable4IntValue {

    /** 序号 */
    private static final long                                   serialVersionUID = 99488873819769361L;

    private static final Logger                                 log              = LoggerFactory
                                                                                     .getLogger(Enumerable4IntValue.class);
    private static volatile transient Map<Integer, ServiceType> allbyvalue       = new HashMap<Integer, ServiceType>();
    private static volatile transient Map<String, ServiceType>  allbyname        = new HashMap<String, ServiceType>();
    private static final Lock                                   lock             = new ReentrantLock();

    public static ServiceType                                   SERVICE_10       = ServiceType
                                                                                     .valueOf(10,
                                                                                         "丰配产品");
    public static ServiceType                                   SERVICE_20       = ServiceType
                                                                                     .valueOf(20,
                                                                                         "丰速产品 ");
    public static ServiceType                                   SERVICE_30       = ServiceType
                                                                                     .valueOf(30,
                                                                                         "丰运产品");
    public static ServiceType                                   SERVICE_31       = ServiceType
                                                                                     .valueOf(31,
                                                                                         "丰运产品（丰集+VMI)");
    public static ServiceType                                   SERVICE_40       = ServiceType
                                                                                     .valueOf(40,
                                                                                         "丰集产品（一单到底)");
    public static ServiceType                                   SERVICE_41       = ServiceType
                                                                                     .valueOf(41,
                                                                                         "丰集产品（顺丰)");
    public static ServiceType                                   SERVICE_42       = ServiceType
                                                                                     .valueOf(42,
                                                                                         "丰集产品（非一单到底，集包物流，提供面单）");
    public static ServiceType                                   SERVICE_43       = ServiceType
                                                                                     .valueOf(43,
                                                                                         "丰集产品（非一单到底 集包物流）");
    public static ServiceType                                   SERVICE_44       = ServiceType
                                                                                     .valueOf(44,
                                                                                         "丰集产品（USPS）C客户用丰趣系统 小量发件");
    public static ServiceType                                   SERVICE_45       = ServiceType
                                                                                     .valueOf(45,
                                                                                         "丰集产品（USPS）B客户有系统 发货");

    private ServiceType(int value, String name) {
        super(value, name);
    }

    public static ServiceType valueOf(Integer value, String name) {
        ServiceType e = allbyvalue.get(value);
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

        Map<Integer, ServiceType> allbyvalue_new = new HashMap<Integer, ServiceType>();
        Map<String, ServiceType> allbyname_new = new HashMap<String, ServiceType>();
        e = new ServiceType(value, name);
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

    public static ServiceType valueOf(int value) {
        ServiceType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static ServiceType valueOf(String name) {
        ServiceType e = allbyname.get(name);
        if (e != null) {
            return e;
        } else {
            throw new IllegalArgumentException("No enum defined:" + name);
        }
    }

    public static boolean containValue(int value) {
        ServiceType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static ServiceType[] values() {
        return allbyvalue.values().toArray(new ServiceType[0]);
    }
}