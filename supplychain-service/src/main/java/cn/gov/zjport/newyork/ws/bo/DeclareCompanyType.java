package cn.gov.zjport.newyork.ws.bo;

/**
 * <p>杭州口岸个人物品申报类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/4
 * Time: 下午5:04
 */
public enum DeclareCompanyType {

    E_COMMERCE_COMPANY("个人委托电商企业申报",""),
    LOGISTICS_COMPANY("个人委托物流企业申报", ""),
    THIRD_COMPANY("个人委托第三方申报", "");

    /**
     * 类型值
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    DeclareCompanyType(String type, String description) {
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
