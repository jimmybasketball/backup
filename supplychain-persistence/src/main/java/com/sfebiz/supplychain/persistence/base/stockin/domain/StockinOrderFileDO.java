package com.sfebiz.supplychain.persistence.base.stockin.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * Created by zhangyajing on 7/20/17.
 * Email: zhang.yajing@ifunq.com
 */

public class StockinOrderFileDO extends BaseDO{

    private static final long serialVersionUID = -1927134457292805345L;

    private Long stockinOrderId;

    private String fileName;

    private String url;

    private Long userId;

    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getStockinOrderId() {
        return stockinOrderId;
    }

    public void setStockinOrderId(Long stockinOrderId) {
        this.stockinOrderId = stockinOrderId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
