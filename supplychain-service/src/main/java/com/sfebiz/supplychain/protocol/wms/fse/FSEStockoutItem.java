package com.sfebiz.supplychain.protocol.wms.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class FSEStockoutItem implements Serializable{
    private static final long serialVersionUID = -6684028088640510749L;

    /**
     * 仓库出库回传
     */
    public List<Shipporder> SHIPPORDERS;
}
