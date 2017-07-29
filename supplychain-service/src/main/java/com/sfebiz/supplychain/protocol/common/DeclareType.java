package com.sfebiz.supplychain.protocol.common;

/**
 * <p>申报类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/12
 * Time: 下午6:03
 */
public enum DeclareType {

    CREATE("1", "新增","create"),
    EDIT("2", "修改","modify"),
    DELETE("3", "删除","delete");

    private String type;

    private String description;

    private String value;

    DeclareType(String type, String description, String value) {
        this.type = type;
        this.description = description;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type;
    }
}
