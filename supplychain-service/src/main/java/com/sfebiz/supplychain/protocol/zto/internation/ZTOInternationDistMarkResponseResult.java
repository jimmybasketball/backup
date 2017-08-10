package com.sfebiz.supplychain.protocol.zto.internation;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午7:43
 */
public class ZTOInternationDistMarkResponseResult implements Serializable{

    private static final long serialVersionUID = -3464414137464277552L;

    private String mark;

    private String print_mark;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPrint_mark() {
        return print_mark;
    }

    public void setPrint_mark(String print_mark) {
        this.print_mark = print_mark;
    }
}
