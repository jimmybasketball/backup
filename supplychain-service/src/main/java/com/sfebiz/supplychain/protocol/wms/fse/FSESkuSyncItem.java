package com.sfebiz.supplychain.protocol.wms.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */
public class FSESkuSyncItem implements Serializable{
    private static final long serialVersionUID = -9013051302693522414L;

    private List<FSESkuItem> Commoditys;

    public List<FSESkuItem> getCommoditys() {
        return Commoditys;
    }

    public void setCommoditys(List<FSESkuItem> commoditys) {
        Commoditys = commoditys;
    }

    @Override
    public String toString() {
        return "FSESkuSyncItem{" +
                "Commoditys=" + Commoditys +
                '}';
    }
}
