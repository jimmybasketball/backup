package com.sfebiz.supplychain.queue.enums;

/**
 * 
 * <p>出库单消息tag描述</p>
 * @author matt
 * @Date 2017年7月27日 下午5:32:44
 */
public enum StockoutOrderMsgTag {

    /** 通知执行出库单下发流程消息tag */
    TAG_STOCKOUTORDER_SEND_TO_PROVIDER("TAG_STOCKOUTORDER_SEND_TO_PROVIDER", "系统自发", "系统自用",
                                       "通知执行出库单下发流程"),
    /** 开放，获取路由tag */
    TAG_OPEN_WMS_ROUTE_FETCH("TAG_STOCKOUTORDER_SEND_TO_PROVIDER", "系统自发", "系统自用", "开放订单通知路由获取");

    /** 标签 */
    private String tag;
    /** 生产方描述 */
    private String producerDesc;
    /** 消费方描述 */
    private String consumerDesc;
    /** 使用描述 */
    private String useDesc;

    StockoutOrderMsgTag(String tag, String producerDesc, String consumerDesc, String useDesc) {
        this.tag = tag;
        this.consumerDesc = consumerDesc;
        this.useDesc = useDesc;
    }

    public String getTag() {
        return tag;
    }

    public String getProducerDesc() {
        return producerDesc;
    }

    public String getConsumerDesc() {
        return consumerDesc;
    }

    public String getUseDesc() {
        return useDesc;
    }

}
