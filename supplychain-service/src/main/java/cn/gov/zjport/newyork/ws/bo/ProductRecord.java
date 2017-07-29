package cn.gov.zjport.newyork.ws.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * <p>商品信息</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/1/12
 * Time: 下午2:26
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"jkfSign","productRecordDto"})
@XmlRootElement(name = "productRecord")
public class ProductRecord {

    @XmlElement(name = "jkfSign")
    private JKFSign jkfSign;

    @XmlElement(name = "productRecordDto")
    private ProductRecordDto productRecordDto;

    public JKFSign getJkfSign() {
        if (jkfSign == null) {
            jkfSign = new JKFSign();
        }
        return jkfSign;
    }

    public void setJkfSign(JKFSign jkfSign) {
        this.jkfSign = jkfSign;
    }

    public ProductRecordDto getProductRecordDto() {
        return productRecordDto;
    }

    public void setProductRecordDto(ProductRecordDto productRecordDto) {
        this.productRecordDto = productRecordDto;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("jkfSign", jkfSign)
                .append("productRecordDto", productRecordDto)
                .toString();
    }
}
