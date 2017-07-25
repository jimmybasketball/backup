package com.sfebiz.supplychain.protocol.bsp;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cargo", propOrder = { "name", "serl", "count", "unit",
		"weight", "amount", "currency", "sourceArea", "visualInspection" })
@Description("BSP货物")
public class BSPCargo  implements Serializable {
    private static final long serialVersionUID = 448948033590676914L;
    @XmlAttribute(name = "name")
	@Description("货物名称")
	public String name;
	@XmlAttribute(name = "serl")
	@Description("商品编号")
	public String serl;
	@XmlAttribute(name = "count")
	@Description("货物数量")
	public Integer count;
	@XmlAttribute(name = "unit")
	@Description("货物单位")
	public String unit;
	@XmlAttribute(name = "weight")
	@Description("货物单位重量")
	public Double weight;
	@XmlAttribute(name = "amount")
	@Description("货物单价")
	public String amount;
	@XmlAttribute(name = "visual_inspection")
	@Description("货物名称")
	public Integer visualInspection;
	@XmlAttribute(name = "currency")
	@Description("货物单价的币别: CNY: 人民币 HKD: 港币 USD: 美元 NTD: 新台币 RUB: 卢布 EUR: 欧元 MOP: 澳门元 SGD: 新元 JPY: 日元 KRW: 韩元 MYR: 马币 VND: 越南盾 THB: 泰铢 AUD: 澳大利亚元 MNT: 图格里克")
	public String currency;
	@XmlAttribute(name = "source_area")
	@Description("原产地国别")
	public String sourceArea;
	@XmlAttribute(name = "check_remark")
	@Description("收货备注")
	public String checkRemark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}



	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSourceArea() {
		return sourceArea;
	}

	public void setSourceArea(String sourceArea) {
		this.sourceArea = sourceArea;
	}

	public String getSerl() {
		return serl;
	}

	public void setSerl(String serl) {
		this.serl = serl;
	}


	public String getCheckRemark() {
		return checkRemark;
	}

	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}


	public Integer getVisualInspection() {
		return visualInspection;
	}

	public void setVisualInspection(Integer visualInspection) {
		this.visualInspection = visualInspection;
	}

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BSPCargo{");
        sb.append("name='").append(name).append('\'');
        sb.append(", serl='").append(serl).append('\'');
        sb.append(", count=").append(count);
        sb.append(", unit='").append(unit).append('\'');
        sb.append(", weight=").append(weight);
        sb.append(", amount=").append(amount);
        sb.append(", visualInspection=").append(visualInspection);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", sourceArea='").append(sourceArea).append('\'');
        sb.append(", checkRemark='").append(checkRemark).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
