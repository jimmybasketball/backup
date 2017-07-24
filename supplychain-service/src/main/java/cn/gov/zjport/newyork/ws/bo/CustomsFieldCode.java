package cn.gov.zjport.newyork.ws.bo;

/**
 * <p>杭州口岸码头/货场代码</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/10
 * Time: 下午4:13
 */
public enum CustomsFieldCode {

    XIACHENG("292801","下城园区"),
    XIASHA("299102", "下沙园区"),
    SHAOSHAN("299201", "萧山园区");

    /**
     * 类型值
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    CustomsFieldCode(String code, String description) {
        this.code = code;
        this.description = description;
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
