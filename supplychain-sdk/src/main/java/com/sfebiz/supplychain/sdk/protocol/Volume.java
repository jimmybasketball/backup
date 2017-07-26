package com.sfebiz.supplychain.sdk.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 体积描述
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"length", "width", "height"})
public class Volume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 长(单位:cm)
     */
    @XmlElement(nillable = false, required = false)
    public Double length = 0.0;

    /**
     * 宽(单位:cm)
     */
    @XmlElement(nillable = false, required = false)
    public Double width = 0.0;

    /**
     * 高(单位:cm)
     */
    @XmlElement(nillable = false, required = false)
    public Double height = 0.0;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }


}
