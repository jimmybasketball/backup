package com.sfebiz.supplychain.protocol.wms.ptwms;

/**
 * Created by TT on 2016/7/6.
 */
public class Pagination {

    private String page;

    private String pageSize;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "page='" + page + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
