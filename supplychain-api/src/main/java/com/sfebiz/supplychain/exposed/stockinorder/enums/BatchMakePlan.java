package com.sfebiz.supplychain.exposed.stockinorder.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangyajing on 2017/7/17.
 */
public class BatchMakePlan extends Enumerable4StringValue{

    private static final long serialVersionUID = -1948825424070404315L;
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static volatile transient Map<String, BatchMakePlan> allbyvalue = new HashMap<String, BatchMakePlan>();
    private static volatile transient Map<String, BatchMakePlan> allbyname = new HashMap<String, BatchMakePlan>();
    private static final Lock lock = new ReentrantLock();

    public static BatchMakePlan EXP_SAME = BatchMakePlan.valueOf("EXP_SAME", "过期日期相同");
    public static BatchMakePlan STOCKIN_SAME = BatchMakePlan.valueOf("STOCKIN_SAME", "入库日期相同");

    public BatchMakePlan(String value, String name) {
        super(value, name);
    }

    public static BatchMakePlan valueOf(String value, String name) {
        BatchMakePlan e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, BatchMakePlan> allbyvalue_new = new HashMap<String, BatchMakePlan>();
        Map<String, BatchMakePlan> allbyname_new = new HashMap<String, BatchMakePlan>();
        e = new BatchMakePlan(value, name);
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


    public static BatchMakePlan valueOf(String value) {
        BatchMakePlan e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        BatchMakePlan e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static BatchMakePlan[] values() {
        return allbyvalue.values().toArray(new BatchMakePlan[0]);
    }


    /**
     * 查询合作模式
     * @return
     */
    public static List<BatchMakePlan> getBatchMakePlanType() {
        BatchMakePlan[] types =  values();
        List<BatchMakePlan> typeList = new ArrayList<BatchMakePlan>();
        for (BatchMakePlan batchMakePlan : types) {
            if (!batchMakePlan.getDescription().equals(undefined)) {
                typeList.add(batchMakePlan);
            }
        }
        return typeList;
    }

    public static void main(String[] args){
        List<BatchMakePlan> plans = BatchMakePlan.getBatchMakePlanType();
        System.out.print(plans);
    }
}
