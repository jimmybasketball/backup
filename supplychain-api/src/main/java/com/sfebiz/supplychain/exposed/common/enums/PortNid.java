package com.sfebiz.supplychain.exposed.common.enums;

import org.apache.commons.lang.StringUtils;

/**
 * <p>口岸昵称</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/13
 * Time: 下午6:39
 */
public enum PortNid {
    EMPTY(0, "EMPTY", "空口岸"),
    HANGZHOU(1, "HZPORT", "杭州口岸"),
    GUANGZHOU(2, "GZPORT", "广州口岸"),
    NINGBO(3, "NBPORT", "宁波口岸"),
    CUSTOMSOFFICE(4, "CUSTOMSOFFICE", "海关总署"),
    JINAN(5, "JNPORT", "济南口岸"),
    XIAMEN(6, "XMPORT", "厦门口岸"),
    PINGTAN(7, "PTPORT", "平潭口岸"),
    SHATIAN(8, "STPORT", "沙田口岸"),
    CHONGQING(9, "CQPORT", "重庆口岸"),
    QINGDAO(10, "QDPORT", "青岛口岸");

    public static final String typeExceptionDesc = "类型异常";

    private long value;

    private String nid;

    private String description;

    PortNid(int value, String nid, String description) {
        this.value = value;
        this.nid = nid;
        this.description = description;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String getDescByCode(Long code) {
        if (code != null) {
            for (PortNid portNid : PortNid.values()) {
                if (portNid.getValue() == code.intValue()) {
                    return portNid.getDescription();
                }
            }
        }
        return typeExceptionDesc;
    }

    public static String getNidByCode(Long code) {
        if (code != null) {
            for (PortNid portNid : PortNid.values()) {
                if (portNid.getValue() == code.intValue()) {
                    return portNid.getNid();
                }
            }
        }
        return EMPTY.getNid();
    }


    public static Long getCodeByNid(String nid) {
        Long portId = EMPTY.getValue();
        if (StringUtils.isNotBlank(nid)) {
            if (nid.equals(PortNid.GUANGZHOU.getNid())) {
                portId = PortNid.GUANGZHOU.getValue();
            } else if (nid.equals(PortNid.HANGZHOU.getNid())) {
                portId = PortNid.HANGZHOU.getValue();
            } else if (nid.equals(PortNid.NINGBO.getNid())) {
                portId = PortNid.NINGBO.getValue();
            } else if (nid.equals(PortNid.JINAN.getNid())) {
                portId = PortNid.JINAN.getValue();
            } else if (nid.equals(PortNid.XIAMEN.getNid())) {
                portId = PortNid.XIAMEN.getValue();
            } else if (nid.equals(PortNid.PINGTAN.getNid())) {
                portId = PortNid.PINGTAN.getValue();
            } else if (nid.equals(PortNid.SHATIAN.getNid())) {
                portId = PortNid.SHATIAN.getValue();
            } else if (nid.equals(PortNid.EMPTY.getNid())) {
                portId = PortNid.EMPTY.getValue();
            }
        }
        return portId;
    }

    @Override
    public String toString() {
        return this.nid;
    }
}
