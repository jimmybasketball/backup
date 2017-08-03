package com.sfebiz.supplychain.protocol.zto.internation;

import com.sfebiz.supplychain.protocol.zto.ZTORequest;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/2
 * Time: 下午3:10
 */
public class ZTOInternationRoutesRequest implements ZTORequest {
    private static final long serialVersionUID = -2396399138562531261L;

    /**
     * 运单号
     */
    private String logisticsId;

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }
}
