package com.sfebiz.supplychain.protocol.fse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/8.
 */
public class FSEResponse implements Serializable{
    private static final long serialVersionUID = 6609716791266010477L;

    public FSESkuSyncResult ROWSET;

    public FSESkuSyncResult getROWSET() {
        return ROWSET;
    }

    public void setROWSET(FSESkuSyncResult ROWSET) {
        this.ROWSET = ROWSET;
    }

    @Override
    public String toString() {
        return "FSEResponse{" +
                "ROWSET=" + ROWSET +
                '}';
    }
}
