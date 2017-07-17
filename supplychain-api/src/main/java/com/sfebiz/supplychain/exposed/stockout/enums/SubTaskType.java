package com.sfebiz.supplychain.exposed.stockout.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * 
 * <p>
 * 子任务类型
 * </p>
 * 
 * @author wuyun
 * @Date 2017年7月17日 下午4:58:44
 */
public class SubTaskType extends Enumerable4StringValue {

    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final long serialVersionUID = -7642715395887753254L;
    private static volatile transient Map<String, SubTaskType> allbyvalue =
            new HashMap<String, SubTaskType>();
    private static volatile transient Map<String, SubTaskType> allbyname =
            new HashMap<String, SubTaskType>();
    private static final Lock lock = new ReentrantLock();

    public static SubTaskType ID_CARD_PHOTO_NOT_UPLOADED =
            SubTaskType.valueOf("ID_CARD_PHOTO_NOT_UPLOADED", "身份证未上传");
    public static SubTaskType ID_CARD_PHOTO_NOT_AUDITED =
            SubTaskType.valueOf("ID_CARD_PHOTO_NOT_AUDITED", "身份证未审核");
    public static SubTaskType ID_CARD_PHOTO_AUDIT_NOT_PASS =
            SubTaskType.valueOf("ID_CARD_PHOTO_AUDIT_NOT_PASS", "身份证审核未通过");

    private SubTaskType(String value, String name) {
        super(value, name);
    }

    public static SubTaskType valueOf(String value, String name) {
        SubTaskType e = allbyvalue.get(value);
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

        Map<String, SubTaskType> allbyvalue_new = new HashMap<String, SubTaskType>();
        Map<String, SubTaskType> allbyname_new = new HashMap<String, SubTaskType>();
        e = new SubTaskType(value, name);
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


    public static SubTaskType valueOf(String value) {
        SubTaskType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean contain(String value) {
        SubTaskType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static SubTaskType[] values() {
        return allbyvalue.values().toArray(new SubTaskType[0]);
    }

    public static List<String> allSubTaskType() {
        List<String> subTaskTypeList = new ArrayList<String>();
        for (SubTaskType subTaskType : values()) {
            subTaskTypeList.add(subTaskType.getValue());
        }
        return subTaskTypeList;
    }

}
