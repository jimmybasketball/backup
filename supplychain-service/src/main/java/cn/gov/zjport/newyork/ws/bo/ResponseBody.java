package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"list", "jkfSign", "productRecord" ,"jkfGoodsDeclar"})
@XmlRootElement(name = "body")
public class ResponseBody {

    @XmlElement(name = "jkfResult")
    @XmlElementWrapper(name = "list")
    private List<JKFResult> list;

    @XmlElement(name = "jkfSign")
    private JKFSign jkfSign;

    /**
     * 商品备案回执结果
     */
    @XmlElement(name = "productRecord")
    private ResponseProductRecord productRecord;

    /**
     * 个人物品申报单
     */
    @XmlElement(name = "jkfGoodsDeclar")
    private ResponsePersonalGoodsDeclar jkfGoodsDeclar;


    public List<JKFResult> getList() {
        return list;
    }

    public void setList(List<JKFResult> list) {
        this.list = list;
    }

    public ResponseProductRecord getProductRecord() {
        return productRecord;
    }

    public void setProductRecord(ResponseProductRecord productRecord) {
        this.productRecord = productRecord;
    }

    public JKFSign getJkfSign() {
        return jkfSign;
    }

    public void setJkfSign(JKFSign jkfSign) {
        this.jkfSign = jkfSign;
    }

    public ResponsePersonalGoodsDeclar getJkfGoodsDeclar() {
        return jkfGoodsDeclar;
    }

    public void setJkfGoodsDeclar(ResponsePersonalGoodsDeclar jkfGoodsDeclar) {
        this.jkfGoodsDeclar = jkfGoodsDeclar;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("list", list)
                .append("productRecord", productRecord)
                .toString();
    }
}
