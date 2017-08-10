package com.sfebiz.supplychain.protocol.fse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */
public class FSESkuSyncResult implements Serializable{

    private static final long serialVersionUID = 6609716791266010477L;

    public String resultCode;
    public  String resultMsg;
    public List<FSESkuSyncError> ERROR;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public List<FSESkuSyncError> getERROR() {
        return ERROR;
    }

    public void setERROR(List<FSESkuSyncError> ERROR) {
        this.ERROR = ERROR;
    }

    @Override
    public String toString() {
        return "FSESkuSyncResult{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", ERROR=" + ERROR +
                '}';
    }
}
