package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 16/1/21 下午1:04
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryPageResult", propOrder = {"totalItem", "pageSize", "currentPage"})
public class QueryPageResult implements Serializable {

    private static final long serialVersionUID = -239798546745921085L;

    @XmlElement(name = "totalItem", nillable = false, required = false)
    private Integer totalItem;

    @XmlElement(name = "pageSize", nillable = false, required = false)
    private Integer pageSize;

    @XmlElement(name = "currentPage", nillable = false, required = false)
    private Integer currentPage;

    public Integer getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Integer totalItem) {
        this.totalItem = totalItem;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "totalItem=" + totalItem +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                '}';
    }
}
