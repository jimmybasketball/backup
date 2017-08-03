package com.sfebiz.supplychain.protocol.zto.internation;

/**
 * Created by zhangdi on 2015/11/17.
 */
public class ZTOSubmitRespKeys {

    private String orderid;
    private String mailno;

    /**
     * 大头笔
     */
    private String mark;

    /**
     * 网点编号
     */
    private String sitecode;

    /**
     * 网点名称
     */
    private String sitename;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getMailno() {
        return mailno;
    }

    public void setMailno(String mailno) {
        this.mailno = mailno;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getSitecode() {
        return sitecode;
    }

    public void setSitecode(String sitecode) {
        this.sitecode = sitecode;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }
}
