package com.sfebiz.supplychain.protocol.pay.alipay;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>支付宝订单申报返回</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/5/18
 * Time: 下午8:54
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"responseContent"})
public class ResponseContentWrap implements Serializable {

    private static final long serialVersionUID = 1259228596210794965L;

    @XmlElement(name = "alipay")
    private ResponseContent responseContent;

    public ResponseContent getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(ResponseContent responseContent) {
        this.responseContent = responseContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("responseContent", responseContent)
                .toString();
    }
}
