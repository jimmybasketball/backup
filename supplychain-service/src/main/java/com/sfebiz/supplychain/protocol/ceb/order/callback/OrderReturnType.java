package com.sfebiz.supplychain.protocol.ceb.order.callback;

/**
 * <p>
 * 操作结果（2电子口岸申报中/3发送海关成功/4发送海关失败/100海关退单/399海关审结）,若小于0数字表示处理异常回执
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/4/7
 * Time: 下午9:16
 */
public enum OrderReturnType {

    CUSTOMS_DECLEARING(2, "电子口岸申报中"),
    CUSTOMS_SEND_SUCCESS(3, "发送海关成功"),
    CUSTOMS_SEND_FAIL(4, "发送海关失败"),
    CUSTOMS_RETURN(100, "海关退单"),
    CUSTOMS_PASS(399, "海关审结"),;

    private int code;

    private String desc;

    OrderReturnType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 状态是否成功
     *
     * @param code
     * @return
     */
    public static boolean isSuccess(int code) {
        if (code == OrderReturnType.CUSTOMS_DECLEARING.getCode()
                || code == OrderReturnType.CUSTOMS_SEND_SUCCESS.getCode()
                || code == OrderReturnType.CUSTOMS_PASS.getCode()
                ) {
            return true;
        } else {
            return false;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
