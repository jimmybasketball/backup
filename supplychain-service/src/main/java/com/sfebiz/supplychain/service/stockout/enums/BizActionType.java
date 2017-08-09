package com.sfebiz.supplychain.service.stockout.enums;

/**
 * 
 * <p>业务动作类型</p>
 * 
 * @author matt
 * @Date 2017年8月7日 下午4:50:27
 */
public enum BizActionType {

    /** 通知执行出库单下发流程消息tag */
    STOCKOUTORDER_CREATE("STOCKOUTORDER_CREATE", "出库单创建");

    /** 标签 */
    private String action;
    /** 生产方描述 */
    private String actionDesc;

    BizActionType(String action, String actionDesc) {
        this.action = action;
        this.actionDesc = actionDesc;
    }

    public String getAction() {
        return action;
    }

    public String getActionDesc() {
        return actionDesc;
    }

}
