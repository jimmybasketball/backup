package com.sfebiz.supplychain.persistence.base.stockin.domain;

import com.sfebiz.common.dao.domain.BaseDO;

/**
 * <p></p>
 * User: 张雅静
 * Date: 17/07/10
 * Time: 下午3:40
 */

public class StockinOrderStateLogDO extends BaseDO {

    //入库单号
    private Long stockinOrderId;

    //运营后台用户id
    private Long userId;

    //运营后台用户名
    private String userName;

    //入库单状态
    private String state;

    //额外信息
    private String extend;

    @Override
    public String toString() {
        return "StockinOrderStateLogDO{" +
                "stockinOrderId=" + stockinOrderId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", state='" + state + '\'' +
                ", extend='" + extend + '\'' +
                '}';
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
