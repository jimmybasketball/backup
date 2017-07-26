package cn.gov.zjport.newyork.ws.bo;

/**
 * <p>杭州口岸业务类型</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/4
 * Time: 下午5:04
 */
public enum HzPortBusinessType {

    PRODUCT_RECORD("PRODUCT_RECORD", "产品备案","电商企业"),
    IMPORTORDER("IMPORTORDER", "商品订单申报","电商企业"),
    PAYBILL_PROXY_DECLAR("PAYMENT", "电商代理支付企业进行商品支付单申报","电商企业"),
    PAYBILL_DECLAR("PAYBILL_DECLAR", "电商委托支付企业进行商品支付单申报","支付企业"),
    PAYBILL_REDECLAR("PAYBILL_REDECLAR", "商品支付单重新申报","支付企业"),
    PAYBILL_QUERY("PAYBILL_QUERY", "支付单申报状态查询","支付企业"),
    PERSONAL_GOODS_DECLAR("PERSONAL_GOODS_DECLAR", "个人物品申报单","物流企业"),
    IMPORTBILL("IMPORTBILL", "商品运单","物流企业"),
    IMPORTBILLRESULT("IMPORTBILLRESULT", "进口运单出区回执","跨境一步达"),
    LOGISTICS_INFO("LOGISTICS_INFO", "物流状态","物流企业"),
    RESULT("RESULT", "回执","跨境一步达"),
    ;

    /**
     * 类型值 ，为与杭州跨进一步达交互的业务类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    /**
     * 申报单位
     */
    private String declarCompany;

    HzPortBusinessType(String type, String description, String declarCompany) {
        this.type = type;
        this.description = description;
        this.declarCompany = declarCompany;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getDeclarCompany() {
        return declarCompany;
    }

    @Override
    public String toString() {
        return type;
    }
}
