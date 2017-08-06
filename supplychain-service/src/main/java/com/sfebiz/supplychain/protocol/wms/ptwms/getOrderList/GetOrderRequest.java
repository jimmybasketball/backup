package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderList;

/**
 * Created by TT on 2016/7/6.
 */
public class GetOrderRequest {

    private Integer page = 1;

    private Integer pageSize =1;

    /**
     * 订单号
     */
    private String order_code;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    @Override
    public String toString() {
        return "GetOrderRequest{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", order_code='" + order_code + '\'' +
                '}';
    }
}
