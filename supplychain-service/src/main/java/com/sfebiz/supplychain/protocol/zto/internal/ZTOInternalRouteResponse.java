package com.sfebiz.supplychain.protocol.zto.internal;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/17
 * Time: 下午6:50
 */
public class ZTOInternalRouteResponse implements Serializable {
    private static final long serialVersionUID = -2374936547211746386L;

    private List<ZTOInternalRouteNode> traces;

    public List<ZTOInternalRouteNode> getTraces() {
        return traces;
    }

    public void setTraces(List<ZTOInternalRouteNode> traces) {
        this.traces = traces;
    }
}
