package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/21
 * Time: 下午9:11
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Head")
@XmlType(propOrder = {"accessCode","checkword"})
public class StockoutRequestHead implements Serializable {


    private static final long serialVersionUID = -4753331949866114828L;
    @Description("接入编码")
    @XmlElement(name = "AccessCode", required = true)
    private String accessCode;

    @Description("校验码")
    @XmlElement(name = "Checkword", required = true)
    private String checkword;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getCheckword() {
        return checkword;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public void setCheckword(String checkword) {
        this.checkword = checkword;
    }
}
