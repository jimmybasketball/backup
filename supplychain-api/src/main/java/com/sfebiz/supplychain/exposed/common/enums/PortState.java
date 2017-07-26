package com.sfebiz.supplychain.exposed.common.enums;

/**
 * <p>口岸状态</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/10
 * Time: 下午11:11
 */
public enum PortState {

    NO(-1, "无口岸状态"),
    INIT(0, "需口岸订单备案"),
    SUCCESS(1, "口岸备案成功"),
    FAILURE(2, "口岸备案失败");

    private int state;
    private String description;

    PortState(int state, String description) {
        this.state = state;
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }
}
