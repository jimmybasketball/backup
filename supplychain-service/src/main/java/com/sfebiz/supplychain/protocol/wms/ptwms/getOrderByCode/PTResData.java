package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderByCode;

import java.util.List;

/**
 * 根据订单号获取单票订单信息 反馈回来的data json文件转换的对象
 * 
 * @author Administrator
 *
 */
public class PTResData {

	public String order_code;

	public String reference_no;

	public String platform;

	public String order_status;

	public String shipping_method;

	public String tracking_no;

	public String warehouse_code;

	public float order_weight;

	public String order_desc;

	public String date_create;

	public String date_release;

	public String date_shipping;

	public String date_modify;

	public String consignee_country_code;

	public String consignee_country_name;

	public String consignee_state;

	public String consignee_city;

	public String consignee_district;

	public String consignee_address1;

	public String consignee_address2;

	public String consignee_address3;

	public String consigne_zipcode;

	public String consignee_doorplate;

	public String consignee_company;

	public String consignee_name;
	
	public String consignee_region;
	
	public String consignee_identityNo;
	
	public String consignee_phone;

	public String consignee_email;

	public PTResFeeDetails fee_details;

	public List<PTResItem> items;

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getReference_no() {
		return reference_no;
	}

	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getShipping_method() {
		return shipping_method;
	}

	public void setShipping_method(String shipping_method) {
		this.shipping_method = shipping_method;
	}

	public String getTracking_no() {
		return tracking_no;
	}

	public void setTracking_no(String tracking_no) {
		this.tracking_no = tracking_no;
	}

	public String getWarehouse_code() {
		return warehouse_code;
	}

	public void setWarehouse_code(String warehouse_code) {
		this.warehouse_code = warehouse_code;
	}

	public float getOrder_weight() {
		return order_weight;
	}

	public void setOrder_weight(float order_weight) {
		this.order_weight = order_weight;
	}

	public String getOrder_desc() {
		return order_desc;
	}

	public void setOrder_desc(String order_desc) {
		this.order_desc = order_desc;
	}

	public String getDate_create() {
		return date_create;
	}

	public void setDate_create(String date_create) {
		this.date_create = date_create;
	}

	public String getDate_release() {
		return date_release;
	}

	public void setDate_release(String date_release) {
		this.date_release = date_release;
	}

	public String getDate_shipping() {
		return date_shipping;
	}

	public void setDate_shipping(String date_shipping) {
		this.date_shipping = date_shipping;
	}

	public String getDate_modify() {
		return date_modify;
	}

	public void setDate_modify(String date_modify) {
		this.date_modify = date_modify;
	}

	public String getConsignee_country_code() {
		return consignee_country_code;
	}

	public void setConsignee_country_code(String consignee_country_code) {
		this.consignee_country_code = consignee_country_code;
	}

	public String getConsignee_country_name() {
		return consignee_country_name;
	}

	public void setConsignee_country_name(String consignee_country_name) {
		this.consignee_country_name = consignee_country_name;
	}

	public String getConsignee_state() {
		return consignee_state;
	}

	public void setConsignee_state(String consignee_state) {
		this.consignee_state = consignee_state;
	}

	public String getConsignee_city() {
		return consignee_city;
	}

	public void setConsignee_city(String consignee_city) {
		this.consignee_city = consignee_city;
	}

	public String getConsignee_district() {
		return consignee_district;
	}

	public void setConsignee_district(String consignee_district) {
		this.consignee_district = consignee_district;
	}

	public String getConsignee_address1() {
		return consignee_address1;
	}

	public void setConsignee_address1(String consignee_address1) {
		this.consignee_address1 = consignee_address1;
	}

	public String getConsignee_address2() {
		return consignee_address2;
	}

	public void setConsignee_address2(String consignee_address2) {
		this.consignee_address2 = consignee_address2;
	}

	public String getConsignee_address3() {
		return consignee_address3;
	}

	public void setConsignee_address3(String consignee_address3) {
		this.consignee_address3 = consignee_address3;
	}

	public String getConsigne_zipcode() {
		return consigne_zipcode;
	}

	public void setConsigne_zipcode(String consigne_zipcode) {
		this.consigne_zipcode = consigne_zipcode;
	}

	public String getConsignee_doorplate() {
		return consignee_doorplate;
	}

	public void setConsignee_doorplate(String consignee_doorplate) {
		this.consignee_doorplate = consignee_doorplate;
	}

	public String getConsignee_company() {
		return consignee_company;
	}

	public void setConsignee_company(String consignee_company) {
		this.consignee_company = consignee_company;
	}

	public String getConsignee_name() {
		return consignee_name;
	}

	public void setConsignee_name(String consignee_name) {
		this.consignee_name = consignee_name;
	}

	public String getConsignee_phone() {
		return consignee_phone;
	}

	public void setConsignee_phone(String consignee_phone) {
		this.consignee_phone = consignee_phone;
	}

	public String getConsignee_email() {
		return consignee_email;
	}

	public void setConsignee_email(String consignee_email) {
		this.consignee_email = consignee_email;
	}

	public PTResFeeDetails getFee_details() {
		return fee_details;
	}

	public void setFee_details(PTResFeeDetails fee_details) {
		this.fee_details = fee_details;
	}

	public List<PTResItem> getItems() {
		return items;
	}

	public void setItems(List<PTResItem> items) {
		this.items = items;
	}

	public String getConsignee_region() {
		return consignee_region;
	}

	public void setConsignee_region(String consignee_region) {
		this.consignee_region = consignee_region;
	}

	public String getConsignee_identityNo() {
		return consignee_identityNo;
	}

	public void setConsignee_identityNo(String consignee_identityNo) {
		this.consignee_identityNo = consignee_identityNo;
	}

}
