package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/7/28
 * Time: 下午5:06
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Extra", propOrder = {"e5", "e4"})
@Description("BSP货物")
public class BSPExtra {

    @XmlAttribute(name = "e5")
    @Description("扩展字段")
    public String e5;


    @XmlAttribute(name = "e4")
    @Description("扩展字段")
    public String e4;

    public String getE5() {
        return e5;
    }

    public void setE5(String e5) {
        this.e5 = e5;
    }

    public String getE4() {
        return e4;
    }

    public void setE4(String e4) {
        this.e4 = e4;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BSPExtra{");
        sb.append("e5='").append(e5).append('\'');
        sb.append(", e4='").append(e4).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
