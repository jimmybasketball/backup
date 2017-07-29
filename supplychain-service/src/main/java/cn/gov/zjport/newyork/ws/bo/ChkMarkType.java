package cn.gov.zjport.newyork.ws.bo;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/10
 * Time: 上午9:59
 */
public enum ChkMarkType {

    SUCCESS("1", "成功"),
    FAILURE("2", "失败");

    private String type;

    private String description;

    ChkMarkType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return type;
    }
}
