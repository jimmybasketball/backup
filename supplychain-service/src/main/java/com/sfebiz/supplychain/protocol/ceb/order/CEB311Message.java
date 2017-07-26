package com.sfebiz.supplychain.protocol.ceb.order;

import com.sfebiz.supplychain.protocol.ceb.common.BaseSign;
import com.sfebiz.supplychain.protocol.ceb.common.BaseSubscribe;
import com.sfebiz.supplychain.protocol.ceb.common.BaseTransfer;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p>
 * 跨境贸易电子商务通关服务平台
 * 电子订单数据(CEB311Message)
 * </p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/10
 * Time: 下午2:15
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"guid", "version","xmlns" , "xmlnsXsi", "order", "baseTransfer", "baseSubscribe", "baseSign"})
@XmlRootElement(name = "CEB311Message")
public class CEB311Message implements Serializable {

    private static final long serialVersionUID = -5063520780490644528L;

    @XmlAttribute(name = "guid")
    private String guid;

    @XmlAttribute(name = "version")
    private String version = "1.0";

    @XmlAttribute(name = "xmlns")
    private String xmlns = "http://www.chinaport.gov.cn/ceb";

    @XmlAttribute(name = "xmlns:xsi")
    private String xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";

    @XmlElement(name = "Order")
    private Order order;

    @XmlElement(name = "BaseTransfer")
    private BaseTransfer baseTransfer;

    @XmlElement(name = "BaseSubscribe")
    private BaseSubscribe baseSubscribe;

    @XmlElement(name = "BaseSign")
    private BaseSign baseSign;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getXmlnsXsi() {
        return xmlnsXsi;
    }

    public void setXmlnsXsi(String xmlnsXsi) {
        this.xmlnsXsi = xmlnsXsi;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BaseTransfer getBaseTransfer() {
        return baseTransfer;
    }

    public void setBaseTransfer(BaseTransfer baseTransfer) {
        this.baseTransfer = baseTransfer;
    }

    public BaseSubscribe getBaseSubscribe() {
        return baseSubscribe;
    }

    public void setBaseSubscribe(BaseSubscribe baseSubscribe) {
        this.baseSubscribe = baseSubscribe;
    }

    public BaseSign getBaseSign() {
        return baseSign;
    }

    public void setBaseSign(BaseSign baseSign) {
        this.baseSign = baseSign;
    }
}
