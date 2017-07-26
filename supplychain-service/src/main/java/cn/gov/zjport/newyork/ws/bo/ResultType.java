package cn.gov.zjport.newyork.ws.bo;

/**
 * <p>结果类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/12
 * Time: 下午6:03
 */
public enum ResultType {

    CREATE("1", "业务数据"),
    EDIT("2", "回执数据");

    private String type;

    private String description;

    ResultType(String type, String description) {
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
