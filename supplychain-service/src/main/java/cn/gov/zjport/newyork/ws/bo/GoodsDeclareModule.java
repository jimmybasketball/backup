package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * <p>个人物品申报单模块</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/3/4
 * Time: 下午5:25
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"jkfSign","goodsDeclare","goodsDeclareDetails"})
@XmlRootElement(name = "goodsDeclareModule")
public class GoodsDeclareModule {

    @XmlElement(name = "jkfSign")
    private JKFSign jkfSign;

    @XmlElement(name = "goodsDeclare")
    private GoodsDeclare goodsDeclare;

    @XmlElement(name = "goodsDeclareDetail")
    @XmlElementWrapper(name = "goodsDeclareDetails")
    private List<GoodsDeclareDetail> goodsDeclareDetails;

    public JKFSign getJkfSign() {
        return jkfSign;
    }

    public void setJkfSign(JKFSign jkfSign) {
        this.jkfSign = jkfSign;
    }

    public GoodsDeclare getGoodsDeclare() {
        return goodsDeclare;
    }

    public void setGoodsDeclare(GoodsDeclare goodsDeclare) {
        this.goodsDeclare = goodsDeclare;
    }

    public List<GoodsDeclareDetail> getGoodsDeclareDetails() {
        return goodsDeclareDetails;
    }

    public void setGoodsDeclareDetails(List<GoodsDeclareDetail> goodsDeclareDetails) {
        this.goodsDeclareDetails = goodsDeclareDetails;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("jkfSign", jkfSign)
                .append("goodsDeclare", goodsDeclare)
                .append("goodsDeclareDetails", goodsDeclareDetails)
                .toString();
    }
}
