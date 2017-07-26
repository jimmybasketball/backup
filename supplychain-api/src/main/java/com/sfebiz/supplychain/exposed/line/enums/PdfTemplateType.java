package com.sfebiz.supplychain.exposed.line.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sfebiz.supplychain.exposed.common.enums.Enumerable4StringValue;

/**
 * 面单格式
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-11 17:59
 **/
public class PdfTemplateType extends Enumerable4StringValue{
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);

    private static final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = -6001445929092301832L;

    private static volatile transient Map<String, PdfTemplateType> allbyvalue = new HashMap<String, PdfTemplateType>();

    private static volatile transient Map<String, PdfTemplateType> allbyname = new HashMap<String, PdfTemplateType>();

    public static PdfTemplateType SIMPLE = PdfTemplateType.valueOf("SIMPLE", "简单格式");
    public static PdfTemplateType YZYW_SIMPLE = PdfTemplateType.valueOf("YZYW_SIMPLE", "原汁原味简单格式");
    public static PdfTemplateType YZYW_SIMPLE_TITLE = PdfTemplateType.valueOf("YZYW_SIMPLE_TITLE", "原汁原味-简易面单-头部");
    public static PdfTemplateType SF_SF = PdfTemplateType.valueOf("SF_SF", "顺丰清关-顺丰落地配");
    public static PdfTemplateType SF_ZTO = PdfTemplateType.valueOf("SF_ZTO", "顺丰清关-中通落地配");
    public static PdfTemplateType GAOJIE_ZTO = PdfTemplateType.valueOf("GAOJIE_ZTO", "高捷清关-中通落地配");
    public static PdfTemplateType GAOJIE_ZTO_SIMPLE = PdfTemplateType.valueOf("GAOJIE_ZTO_SIMPLE", "高捷清关-中通落地配-简单格式(不带SKU信息)");
    public static PdfTemplateType WTD_ZTO = PdfTemplateType.valueOf("WTD_ZTO", "威时沛清关-中通落地配");
    public static PdfTemplateType WTD_ZTO_SIMPLE = PdfTemplateType.valueOf("WTD_ZTO_SIMPLE", "威时沛清关-中通落地配-简单格式(不带SKU信息)");
    public static PdfTemplateType YXT_ZTO = PdfTemplateType.valueOf("YXT_ZTO", "优先投-中通落地配");
    public static PdfTemplateType YXT_ZTO_2 = PdfTemplateType.valueOf("YXT_ZTO_2", "优先投-中通落地配");
    public static PdfTemplateType YXT_ZTO_SIMPLE = PdfTemplateType.valueOf("YXT_ZTO_SIMPLE", "优先投-中通落地配-简单格式(不带SKU信息)");
    public static PdfTemplateType RHF_YUNDA = PdfTemplateType.valueOf("RHF_YUNDA", "润亨丰-韵达落地配");
    public static PdfTemplateType SH_YUNDA = PdfTemplateType.valueOf("SH_YUNDA", "上海-韵达落地配");
    public static PdfTemplateType ACS_ACS = PdfTemplateType.valueOf("ACS_ACS", "ACS-中华快递落地配");
    public static PdfTemplateType NEW_ACS = PdfTemplateType.valueOf("NEW_ACS", "新ACS-中华快递落地配");
    public static PdfTemplateType ACS_ZTO = PdfTemplateType.valueOf("ACS_ZTO", "ACS-中通快递落地配");
    public static PdfTemplateType GAOJIE_SF = PdfTemplateType.valueOf("GAOJIE_SF", "ACS-顺丰快递落地配");
    public static PdfTemplateType RHF_SF = PdfTemplateType.valueOf("RHF_SF", "润亨丰-顺丰快递落地配");
    public static PdfTemplateType GAOJIE_YT = PdfTemplateType.valueOf("GAOJIE_YT", "高捷清关-圆通落地配");

    public PdfTemplateType(String value, String name) {
        super(value, name);
    }

    public static PdfTemplateType valueOf(String value, String name) {
        PdfTemplateType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, PdfTemplateType> allbyvalue_new = new HashMap<String, PdfTemplateType>();
        Map<String, PdfTemplateType> allbyname_new = new HashMap<String, PdfTemplateType>();
        e = new PdfTemplateType(value, name);
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

    public static PdfTemplateType valueOf(String value) {
        PdfTemplateType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        } else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        PdfTemplateType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static PdfTemplateType[] values() {
        return allbyvalue.values().toArray(new PdfTemplateType[0]);
    }
}
