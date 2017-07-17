package com.sfebiz.supplychain.exposed.line.enums;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 路线编码
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-11 17:59
 **/
public class RouteCodeType extends Enumerable4StringValue{
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = -8933427328138329005L;

    private static volatile transient Map<String, RouteCodeType> allbyvalue = new HashMap<String, RouteCodeType>();

    private static volatile transient Map<String, RouteCodeType> allbyname = new HashMap<String, RouteCodeType>();

    public static RouteCodeType SF_GZ_BC = RouteCodeType.valueOf("SF-GZ-BC", "COE高捷广州");
    public static RouteCodeType GAOJIE_GZ_YTO = RouteCodeType.valueOf("GAOJIE-GZ-YTO", "COE广州圆通");
    public static RouteCodeType GAOJIE_GZ = RouteCodeType.valueOf("GAOJIE-GZ", "斑马高捷广州");
    public static RouteCodeType GAOJIE_GZ_ZTO = RouteCodeType.valueOf("GAOJIE-GZ-ZTO", "高捷广州中通");
    public static RouteCodeType YTO_GZ = RouteCodeType.valueOf("YTO-GZ", "高捷圆通广州");
    public static RouteCodeType ETK = RouteCodeType.valueOf("ETK", "ETK");
    public static RouteCodeType ZTO = RouteCodeType.valueOf("ZTO", "中通");
    public static RouteCodeType SF_NB = RouteCodeType.valueOf("SF-NB", "顺丰宁波口岸");
    public static RouteCodeType YXT_HZ_ZTO = RouteCodeType.valueOf("YXT-HZ-ZTO", "优先投杭州口岸中通配送");
    public static RouteCodeType ZYB_EMPTY_FREE = RouteCodeType.valueOf("ZYB-EMPTY-FREE", "转运邦");
    public static RouteCodeType EMPTY_SH_ZTO = RouteCodeType.valueOf("EMPTY-SH-ZTO", "上海中通");
    public static RouteCodeType EMPTY_SH_SF = RouteCodeType.valueOf("EMPTY-SH-SF", "上海顺丰");
    public static RouteCodeType EMPTY_SH_DBWL = RouteCodeType.valueOf("EMPTY-SH-DBWL", "上海德邦物流");
    public static RouteCodeType EMPTY_SH_DBKD = RouteCodeType.valueOf("EMPTY-SH-DBKD", "上海德邦快递");
    public static RouteCodeType NYC_CN_USPS = RouteCodeType.valueOf("NYC-CN-USPS", "斑马DE转运");
    public static RouteCodeType LAX_CN_USPS = RouteCodeType.valueOf("LAX-CN-USPS", "斑马LA转运");
    public static RouteCodeType EMS = RouteCodeType.valueOf("EMS", "EMS");
    public static RouteCodeType JPN = RouteCodeType.valueOf("JPN", "ACS日本仓");
    public static RouteCodeType NZUE_GC_HZ = RouteCodeType.valueOf("NZUE-GC-HZ", "新西兰仓杭州");
    public static RouteCodeType LAX_CN_XJJ = RouteCodeType.valueOf("LAX-CN-XJJ", "斑马集货");
    public static RouteCodeType LAX_CN_SFHT = RouteCodeType.valueOf("LAX-CN-SFHT", "斑马LA仓储");

    public RouteCodeType(String value, String name) {
        super(value, name);
    }

    public static RouteCodeType valueOf(String value, String name) {
        RouteCodeType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, RouteCodeType> allbyvalue_new = new HashMap<String, RouteCodeType>();
        Map<String, RouteCodeType> allbyname_new = new HashMap<String, RouteCodeType>();
        e = new RouteCodeType(value, name);
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

    public static RouteCodeType valueOf(String value) {
        RouteCodeType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        RouteCodeType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static RouteCodeType[] values() {
        return allbyvalue.values().toArray(new RouteCodeType[0]);
    }
}
