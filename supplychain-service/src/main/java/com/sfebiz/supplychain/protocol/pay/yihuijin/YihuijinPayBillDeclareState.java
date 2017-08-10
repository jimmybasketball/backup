package com.sfebiz.supplychain.protocol.pay.yihuijin;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangdi on 2015/9/17.
 */
public enum YihuijinPayBillDeclareState {

    DECLAREING("PROCESSING", "海关申报中"),
    DECLARE_SUCCESS("SUCCESS", "海关申报成功"),
    DECLARE_FAILURE("FAILED", "海关申报失败");

    /**
     * 状态
     */
    private String state;

    /**
     * 描述
     */
    private String description;

    YihuijinPayBillDeclareState(String state, String description) {
        this.state = state;
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public static YihuijinPayBillDeclareState getByState(String state) {
        if (StringUtils.isBlank(state)) {
            return null;
        }
        for (YihuijinPayBillDeclareState declareState : YihuijinPayBillDeclareState.values()) {
            if (declareState.getState().equalsIgnoreCase(state)) {
                return declareState;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return state;
    }
}
