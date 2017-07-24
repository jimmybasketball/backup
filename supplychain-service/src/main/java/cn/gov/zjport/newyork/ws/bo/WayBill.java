package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/3
 * Time: 下午5:59
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"jkfSign", "wayBillImportDto"})
public class WayBill {

    @XmlElement(name = "jkfSign")
    private JKFSign jkfSign;

    @XmlElement(name = "wayBillImportDto")
    private WayBillImportDto wayBillImportDto;

    public JKFSign getJkfSign() {
        return jkfSign;
    }

    public void setJkfSign(JKFSign jkfSign) {
        this.jkfSign = jkfSign;
    }

    public WayBillImportDto getWayBillImportDto() {
        return wayBillImportDto;
    }

    public void setWayBillImportDto(WayBillImportDto wayBillImportDto) {
        this.wayBillImportDto = wayBillImportDto;
    }
}
