package com.sfebiz.supplychain.exposed.warehouse.enums;

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
 * 仓库合作状态枚举
 * </p>
 * 
 * @author matt
 * @Date 2017年7月7日 下午4:10:04
 */
public class WarehouseCooperationState extends Enumerable4StringValue {

    /** 合作中 */
    public static WarehouseCooperationState IN_PROCESS = WarehouseCooperationState
	    .valueOf("IN_PROCESS", "合作中");
    /** 合作暂停 */
    public static WarehouseCooperationState SUSPEND = WarehouseCooperationState
	    .valueOf("SUSPEND", "合作暂停");

    private static final long serialVersionUID = -3017459278648153767L;

    private static final Logger log = LoggerFactory
	    .getLogger(WarehouseCooperationState.class);

    private static final Lock lock = new ReentrantLock();

    private static volatile transient Map<String, WarehouseCooperationState> allbyvalue = new HashMap<String, WarehouseCooperationState>();

    private static volatile transient Map<String, WarehouseCooperationState> allbyname = new HashMap<String, WarehouseCooperationState>();

    public WarehouseCooperationState(String value, String name) {
	super(value, name);
    }

    public static WarehouseCooperationState valueOf(String value, String name) {
	WarehouseCooperationState e = allbyvalue.get(value);
	if (e != null) {
	    if (e.name.equals(name) || undefined.equals(name))
		return e;
	    else {
		log.warn("Name to be change. value:" + value + ", old name:"
			+ e.name + ", new name:" + name);
	    }
	}

	Map<String, WarehouseCooperationState> allbyvalue_new = new HashMap<String, WarehouseCooperationState>();
	Map<String, WarehouseCooperationState> allbyname_new = new HashMap<String, WarehouseCooperationState>();
	e = new WarehouseCooperationState(value, name);
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

    public static WarehouseCooperationState valueOf(String value) {
	WarehouseCooperationState e = allbyvalue.get(value);
	if (e != null) {
	    return e;
	} else {
	    return valueOf(value, undefined);
	}
    }

    public static boolean containValue(String value) {
	WarehouseCooperationState e = allbyvalue.get(value);
	if (e != null) {
	    return true;
	} else {
	    return false;
	}
    }

    public static WarehouseCooperationState[] values() {
	return allbyvalue.values().toArray(new WarehouseCooperationState[0]);
    }
}
