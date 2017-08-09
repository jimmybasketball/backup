package com.sfebiz.supplychain.service.sku.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * <p>商品备案状态</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/13
 * Time: 下午7:52
 */
public class ProductDeclareState extends Enumerable4StringValue {

    private static final long                                          serialVersionUID    = -6210561888153478828L;

    private static final Logger                                        log                 = LoggerFactory
                                                                                               .getLogger(Enumerable4StringValue.class);

    private static volatile transient Map<String, ProductDeclareState> allbyvalue          = new HashMap<String, ProductDeclareState>();

    private static volatile transient Map<String, ProductDeclareState> allbyname           = new HashMap<String, ProductDeclareState>();

    private static final Lock                                          lock                = new ReentrantLock();

    /** 待备案 */
    public static ProductDeclareState                                  WAIT_DECLARE        = ProductDeclareState
                                                                                               .valueOf(
                                                                                                   "WAIT_DECLARE",
                                                                                                   "待备案");

    /** 资料收集中 */
    public static ProductDeclareState                                  COLLECTING          = ProductDeclareState
                                                                                               .valueOf(
                                                                                                   "COLLECTING",
                                                                                                   "资料收集中");

    /** 资料收集完毕 */
    public static ProductDeclareState                                  FINISHED_COLLECTING = ProductDeclareState
                                                                                               .valueOf(
                                                                                                   "FINISHED_COLLECTING",
                                                                                                   "资料收集完毕");

    /** 备案中 */
    public static ProductDeclareState                                  DECLARING           = ProductDeclareState
                                                                                               .valueOf(
                                                                                                   "DECLARING",
                                                                                                   "备案中");

    /** 备案通过 */
    public static ProductDeclareState                                  DECLARE_PASS        = ProductDeclareState
                                                                                               .valueOf(
                                                                                                   "DECLARE_PASS",
                                                                                                   "备案通过");

    /** 备案不通过 */
    public static ProductDeclareState                                  DECLARE_NOT_PASS    = ProductDeclareState
                                                                                               .valueOf(
                                                                                                   "DECLARE_NOT_PASS",
                                                                                                   "备案不通过");

    private ProductDeclareState(String value, String name) {
        super(value, name);
    }

    public static ProductDeclareState valueOf(String value, String name) {
        ProductDeclareState e = allbyvalue.get(value);
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

        Map<String, ProductDeclareState> allbyvalue_new = new HashMap<String, ProductDeclareState>();
        Map<String, ProductDeclareState> allbyname_new = new HashMap<String, ProductDeclareState>();
        e = new ProductDeclareState(value, name);
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

    public static ProductDeclareState valueOf(String value) {
        ProductDeclareState e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        ProductDeclareState e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static ProductDeclareState[] values() {
        return allbyvalue.values().toArray(new ProductDeclareState[0]);
    }
}
