package com.sfebiz.supplychain.exposed.line.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 路线业务编码
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-11 17:59
 **/
public class RouteBizCodeType extends Enumerable4StringValue{
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = 7336583056619540791L;

    private static volatile transient Map<String, RouteBizCodeType> allbyvalue = new HashMap<String, RouteBizCodeType>();

    private static volatile transient Map<String, RouteBizCodeType> allbyname = new HashMap<String, RouteBizCodeType>();

    public static RouteBizCodeType SHA_CN_FQHT = RouteBizCodeType.valueOf("SHA-CN-FQHT", "上海斑马");
    public static RouteBizCodeType HKH_CN_SFHT = RouteBizCodeType.valueOf("HKH-CN-SFHT", "香港斑马");
    public static RouteBizCodeType NYC_CN_USPS = RouteBizCodeType.valueOf("NYC-CN-USPS", "斑马DE转运");
    public static RouteBizCodeType LAX_CN_SFHT = RouteBizCodeType.valueOf("LAX-CN-SFHT", "斑马LA仓储");
    public static RouteBizCodeType LAX_CN_PCS = RouteBizCodeType.valueOf("LAX-CN-PCS", "斑马LA集货");
    public static RouteBizCodeType LAX_CN_USPS = RouteBizCodeType.valueOf("LAX-CN-USPS", "斑马LA转运");
    public static RouteBizCodeType LAX_CN_XJJ = RouteBizCodeType.valueOf("LAX-CN-XJJ", "斑马集货");
    public static RouteBizCodeType JPN = RouteBizCodeType.valueOf("JPN", "ACS日本仓");
    public static RouteBizCodeType AUSYD = RouteBizCodeType.valueOf("AUSYD", "澳洲乾丰");
    public static RouteBizCodeType AMS_DDP = RouteBizCodeType.valueOf("AMS-DDP", "荷兰B2CEurope");
    public static RouteBizCodeType DEU_DDP = RouteBizCodeType.valueOf("DEU-DDP", "德国B2CEurope");
    public static RouteBizCodeType JP_GZ_BC = RouteBizCodeType.valueOf("jp-gz-bc", "日本广州BC清关");
    public static RouteBizCodeType JP_POST = RouteBizCodeType.valueOf("jp-post", "日本邮政集包清关");

    public RouteBizCodeType(String value, String name) {
        super(value, name);
    }

    public static RouteBizCodeType valueOf(String value, String name) {
        RouteBizCodeType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, RouteBizCodeType> allbyvalue_new = new HashMap<String, RouteBizCodeType>();
        Map<String, RouteBizCodeType> allbyname_new = new HashMap<String, RouteBizCodeType>();
        e = new RouteBizCodeType(value, name);
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

    public static RouteBizCodeType valueOf(String value) {
        RouteBizCodeType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        RouteBizCodeType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static RouteBizCodeType[] values() {
        return allbyvalue.values().toArray(new RouteBizCodeType[0]);
    }
}
