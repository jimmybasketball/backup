package com.sfebiz.supplychain.protocol.zto.internal;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午7:00
 */
public class ZTOInternalDistMarkResponse implements Serializable {

    private static final long serialVersionUID = 4887500387242777172L;

    private String mark;

    private String marke;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }
}
