package com.sfebiz.supplychain.protocol.ceb.order;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午3:35
 */
public enum  AppType {

    ADD("1","新增"),
    UPDATE("2","变更"),
    DELETE("3","删除"),

    ;

    private String type;

    private String desc;

    AppType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
