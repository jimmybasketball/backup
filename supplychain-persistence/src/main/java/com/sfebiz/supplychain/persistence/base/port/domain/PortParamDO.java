package com.sfebiz.supplychain.persistence.base.port.domain;

import com.sfebiz.common.dao.domain.BaseDO;

public class PortParamDO extends BaseDO {
    /**
     *
     */
    private static final long serialVersionUID = -63668093456985646L;
    private Long portId;
    private Integer type;
    private String code;
    private String value;


    public long getPortId() {
        return portId;
    }

    public void setPortId(long portId) {
        this.portId = portId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
