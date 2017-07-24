package cn.gov.zjport.newyork.ws.bo;

/**
 * Created by MrChang on 2015/12/23.
 */
public enum YXTCcbState {

    CUSTOMS_CLEARANCE_DELIVERY("S1", "清关公司提货"),
    CUSTOMS_DECLARED("S2", "清关开始"),
    CUSTOMS_GOODS_PASS("S3", "清关完成"),

    CUSTOMS_CHECK_NOTPASS("S4", "海关异常"),
    ;

    /**
     * 类型值
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    YXTCcbState(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static YXTCcbState getByCode(String code) {
        for (YXTCcbState state : YXTCcbState.values()) {
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
