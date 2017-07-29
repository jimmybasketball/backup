package cn.gov.zjport.newyork.ws.bo;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "jkfSign", "jkfOrderImportHead", "jkfOrderDetailList",
		"jkfGoodsPurchaser" })
@XmlRootElement(name = "orderInfo")
public class OrderInfo {
	@XmlElement(name = "jkfSign")
	private JKFSign jkfSign;
	@XmlElement(name = "jkfOrderImportHead")
	private JKFOrderImportHead jkfOrderImportHead;
	@XmlElement(name = "jkfOrderDetail")
	@XmlElementWrapper(name = "jkfOrderDetailList")
	private List<JKFOrderDetail> jkfOrderDetailList;
	@XmlElement(name = "jkfGoodsPurchaser")
	private JKFGoodsPurchaser jkfGoodsPurchaser;

	public JKFSign getJkfSign() {
		if (jkfSign == null) {
			jkfSign = new JKFSign();
		}
		return jkfSign;
	}

	public void setJkfSign(JKFSign jkfSign) {
		this.jkfSign = jkfSign;
	}

	public JKFOrderImportHead getJkfOrderImportHead() {
		if (jkfOrderImportHead == null) {
			jkfOrderImportHead = new JKFOrderImportHead();
		}
		return jkfOrderImportHead;
	}

	public void setJkfOrderImportHead(JKFOrderImportHead jkfOrderImportHead) {
		this.jkfOrderImportHead = jkfOrderImportHead;
	}

	public List<JKFOrderDetail> getJkfOrderDetailList() {
		if (jkfOrderDetailList == null) {
			jkfOrderDetailList = new ArrayList<JKFOrderDetail>();
		}
		return jkfOrderDetailList;
	}

	public void setJkfOrderDetailList(List<JKFOrderDetail> jkfOrderDetailList) {
		this.jkfOrderDetailList = jkfOrderDetailList;
	}

	public JKFGoodsPurchaser getJkfGoodsPurchaser() {
		if (jkfGoodsPurchaser == null) {
			jkfGoodsPurchaser = new JKFGoodsPurchaser();
		}
		return jkfGoodsPurchaser;
	}


	public void setJkfGoodsPurchaser(JKFGoodsPurchaser jkfGoodsPurchaser) {
		this.jkfGoodsPurchaser = jkfGoodsPurchaser;
	}

}
