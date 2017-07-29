package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"jkfSign", "jkfImportPay"})
@XmlRootElement(name = "importPay")
public class ImportPay {
    @XmlElement(name = "jkfSign")
    private JKFSign jkfSign;
    @XmlElement(name = "jkfImportPay")
    private JKFImportPay jkfImportPay;

    public JKFSign getJkfSign() {
        if (jkfSign == null) {
            jkfSign = new JKFSign();
        }
        return jkfSign;
    }

    public void setJkfSign(JKFSign jkfSign) {
        this.jkfSign = jkfSign;
    }

    public JKFImportPay getJkfImportPay() {
        if (jkfImportPay == null) {
            jkfImportPay = new JKFImportPay();
        }
        return jkfImportPay;
    }

    public void setJkfImportPay(JKFImportPay jkfImportPay) {
        this.jkfImportPay = jkfImportPay;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ImportPay{");
        sb.append("jkfSign=").append(jkfSign);
        sb.append(", jkfImportPay=").append(jkfImportPay);
        sb.append('}');
        return sb.toString();
    }
}
