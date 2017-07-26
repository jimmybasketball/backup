package cn.gov.zjport.newyork.ws.bo;

/**
 * <p>进口类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/12
 * Time: 下午6:03
 */
public enum ImportType {

    BASIC_IN("0", "一般进口"),
    BONDED_IN("1", "保税进口");

    private String type;

    private String description;

    ImportType(String type, String description) {
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
