package com.sfebiz.supplychain.service.statemachine;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/5/18 上午9:52
 */
public class Operator implements Serializable {

    public static final Operator SYSTEM_OPERATOR = Operator.valueOf(0L, "系统");

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Operator valueOf(Long id, String name) {
        if (StringUtils.isEmpty(name)) {
            return SYSTEM_OPERATOR;
        }
        Operator operator = new Operator();
        operator.id = id;
        operator.name = name;
        return operator;
    }

    public static Operator valueOf(String name) {
        Operator operator = new Operator();
        operator.id = 0L;
        operator.name = name;
        return operator;
    }

    @Override
    public String toString() {
        return name;
    }
}
