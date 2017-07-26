package com.sfebiz.supplychain.open.exposed.wms.entity;

import java.io.Serializable;

import net.pocrd.annotation.Description;

/**
 * Created by zhangyajing on 2017/3/16.
 */
@Description("订单路由信息 \nen-us:order route")
public class WmsOrderRouteEntity implements Serializable {
    private static final long serialVersionUID = 3062612907335112082L;

    @Description("发生位置 \nen-us:position")
    public String position;

    @Description("操作时间 \nen-us:operate time")
    public String time;

    @Description("操作内容 \nen-us:operate content")
    public String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "WmsOrderRouteEntity{" +
                "position='" + position + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
