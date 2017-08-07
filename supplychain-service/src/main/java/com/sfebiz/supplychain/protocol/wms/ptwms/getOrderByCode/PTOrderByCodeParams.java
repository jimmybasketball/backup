package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

/**
 * 根据订单号获取单票订单信息 请求paramsJson的json类型转换的对象
 * @author Administrator
 *
 */
public class PTOrderByCodeParams {

	public String order_code;

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
}
