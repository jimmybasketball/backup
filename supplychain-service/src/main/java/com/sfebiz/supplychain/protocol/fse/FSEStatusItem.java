package com.sfebiz.supplychain.protocol.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangyajing on 2016/11/28.
 */
public class FSEStatusItem implements Serializable{
    private static final long serialVersionUID = -7530645934576509209L;

    /**
     * 订单生产状态list
     */
    public List<OrderStatus> statusList;
}
