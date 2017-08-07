package com.sfebiz.supplychain.protocol.wms.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class FSESkuStockInItem implements Serializable{
    private static final long serialVersionUID = -138400299341809570L;

    /**
     * 采购入库订单
     */
    public List<Porder> PorderList;
}
