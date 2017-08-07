package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
@XmlType(propOrder = {"body"})
@Description("dm响应body")
public class ResponseBody4Nbbs implements Serializable {
    private static final long serialVersionUID = 844912865378043994L;
    @XmlElements({
            @XmlElement(name = "Mft", type = Mft.class),
            @XmlElement(name = "Logistics", type = LogisticsInfo.class)})
    @Description("内容信息")
    public NbbsBody body;

    public NbbsBody getBody() {
        return body;
    }

    public void setBody(NbbsBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("body", body)
                .toString();
    }
}
