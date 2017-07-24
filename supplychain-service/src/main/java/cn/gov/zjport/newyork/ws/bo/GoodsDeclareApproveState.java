package cn.gov.zjport.newyork.ws.bo;

/**
 * <p>个人商品备案的审单状态</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/10
 * Time: 下午4:17
 */
public enum GoodsDeclareApproveState {

    NOT_ACCEPT("32", "不接受申报"),
    E_PASS("99", "电子审单通过"),
    ACCEPT_REFUSE("42", "接单拒绝"),
    CANCEL("80", "申报单撤销"),
    LINE_AUTO_PASS("51", "流水线自动放行"),
    DIRECT_PASS("51", "直接放行"),
    MANUAL_PASS("52", "手工放行"),
    COUNTRY_CHECK_AEECPT_DECLARE("N1", "国检接收申报"),
    COUNTRY_CHECK_PASS("N1", "国检放行"),
    COUNTRY_CHECK_NOT_PASS("N1", "国检审批不通过");

    /**
     * 类型值
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    GoodsDeclareApproveState(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static GoodsDeclareApproveState getByCode(String code) {
        for (GoodsDeclareApproveState state : GoodsDeclareApproveState.values()) {
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
