package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderList;

import com.sfebiz.supplychain.protocol.wms.ptwms.PTStockoutResponse;
import com.sfebiz.supplychain.protocol.wms.ptwms.Pagination;

/**
 * Created by TT on 2016/7/6.
 */
public class GetOrderResponse extends PTStockoutResponse {

    private Integer count;

    private String nextPage;

    private ResponseData responseData;

    private Pagination pagination;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return "GetOrderResponse{" +
                "count=" + count +
                ", nextPage='" + nextPage + '\'' +
                ", responseData=" + responseData +
                ", pagination=" + pagination +
                '}';
    }
}
