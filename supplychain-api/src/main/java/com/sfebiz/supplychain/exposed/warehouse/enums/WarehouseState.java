package com.sfebiz.supplychain.exposed.warehouse.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 仓库状态枚举
 * </p>
 * 
 * @author matt
 * @Date 2017年7月7日 下午4:09:41
 */
public class WarehouseState extends Enumerable4StringValue {

	private static volatile transient Map<String, WarehouseState> allbyvalue = new HashMap<String, WarehouseState>();

	private static volatile transient Map<String, WarehouseState> allbyname = new HashMap<String, WarehouseState>();

	private static final Lock lock = new ReentrantLock();

	private static final Logger log = LoggerFactory.getLogger(WarehouseState.class);

	private static final long serialVersionUID = -5549123648103158733L;

    /** 启用 */
    public static WarehouseState ENABLE = WarehouseState.valueOf("ENABLE", "启用");
    /** 禁用 */
    public static WarehouseState DISABLE = WarehouseState.valueOf("DISABLE", "禁用");

    public WarehouseState(String value, String name) {
	super(value, name);
    }

    public static WarehouseState valueOf(String value, String name) {
	WarehouseState e = allbyvalue.get(value);
	if (e != null) {
	    if (e.name.equals(name) || undefined.equals(name))
		// undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新;
		// 但是不能用undefined覆盖已有值
		return e;
	    else {
		// 命名不相同
		log.warn("Name to be change. value:" + value + ", old name:"
			+ e.name + ", new name:" + name);
	    }
	}

	Map<String, WarehouseState> allbyvalue_new = new HashMap<String, WarehouseState>();
	Map<String, WarehouseState> allbyname_new = new HashMap<String, WarehouseState>();
	e = new WarehouseState(value, name);
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

    public static WarehouseState valueOf(String value) {
	WarehouseState e = allbyvalue.get(value);
	if (e != null) {
	    return e;
	} else {
	    return valueOf(value, undefined);
	}
    }

    public static boolean containValue(String value) {
	WarehouseState e = allbyvalue.get(value);
	if (e != null) {
	    return true;
	} else {
	    return false;
	}
    }

    public static WarehouseState[] values() {
	return allbyvalue.values().toArray(new WarehouseState[0]);
    }
}
