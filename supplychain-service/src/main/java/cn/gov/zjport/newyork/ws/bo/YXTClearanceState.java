package cn.gov.zjport.newyork.ws.bo;

/**
 * Created by MrChang on 2015/12/23.
 */
public enum YXTClearanceState {

    DECLAREING("21", "已提交申报"),
    MESSAGE_CHECK_FAIL("22", "报文校验失败"),
    NOT_ACCEPT("32", "不接受申报"),
    ENTER_RED_ROAD("33", "电子审单进入红通道"),
    ENTER_GREEN_ROAD("34", "电子审单进入绿通道"),
    ACCEPT_REFUSE("42", "接单拒绝"),
    CANCEL("80", "申报单撤销"),
    LINE_AUTO_PASS("51", "流水线自动放行"),
    MANUAL_PASS("52", "手工放行"),
    DIRECT_PASS("53", "直接放行"),
    COUNTRY_CHECK_AEECPT_DECLARE("N1", "国检接收申报"),
    COUNTRY_CHECK_PASS("N2", "国检放行"),
    COUNTRY_CHECK_NOT_PASS("N3", "国检审批不通过");

    /**
     * 类型值
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    YXTClearanceState(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static YXTClearanceState getByCode(String code) {
        for (YXTClearanceState state : YXTClearanceState.values()) {
            if(state.getCode().equalsIgnoreCase(code)){
                return state;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code;
    }
}
