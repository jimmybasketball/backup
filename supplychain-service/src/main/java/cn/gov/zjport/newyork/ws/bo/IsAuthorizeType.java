package cn.gov.zjport.newyork.ws.bo;

/**
 * <p>代表是否个人买家授权电商申报数据</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/4
 * Time: 下午5:04
 */
public enum IsAuthorizeType {

    NO("0", "否"),
    YES("1", "是");

    /**
     * 类型值
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    IsAuthorizeType(String type, String description) {
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
