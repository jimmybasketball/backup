package com.sfebiz.supplychain.config.rz;

/**
 * 易极付认证配置
 *
 * @author liujc [liujunchi@ifunq.com]
 * @date 2017-07-26 18:54
 **/
public class YiJiFuRz {
    public String url;

    public String partnerid;

    public String partnerkey;

    public YiJiFuRz(String url, String partnerid, String partnerkey) {
        this.url = url;
        this.partnerid = partnerid;
        this.partnerkey = partnerkey;
    }

    @Override
    public String toString() {
        return "YJFConfig{" +
                "url='" + url + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", partnerkey='" + partnerkey + '\'' +
                '}';
    }
}
