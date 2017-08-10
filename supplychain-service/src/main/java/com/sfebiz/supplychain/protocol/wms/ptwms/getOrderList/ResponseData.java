package com.sfebiz.supplychain.protocol.wms.ptwms.getOrderList;

import com.sfebiz.supplychain.protocol.wms.ptwms.FeeDetail;
import com.sfebiz.supplychain.protocol.wms.ptwms.SkuItem;

import java.util.Date;

/**
 * Created by TT on 2016/7/6.
 */
public class ResponseData {

    /**
     *订单号
     */
    private String order_code;
    /**
     *客户参考号
     */
    private String reference_no;
    /**
     *平台
     */
    private String platform;
    /**
     *订单状态
     */
    private String order_status;
    /**
     *运输方式
     */
    private String shipping_method;
    /**
     *跟踪号
     */
    private String tracking_no;
    /**
     *仓库代码
     */
    private String warehouse_code;
    /**
     *订单重量
     */
    private float order_weight;
    /**
     *订单说明
     */
    private String order_desc;
    /**
     *创建时间
     */
    private Date date_create;
    /**
     *审核时间
     */
    private Date date_release;
    /**
     *出库时间
     */
    private Date date_shipping;
    /**
     *修改时间
     */
    private Date date_modify;
    /**
     *收件人国家二字码
     */
    private String consignee_country_code;
    /**
     *收件人国家
     */
    private String consignee_country_name;
    /**
     *省
     */
    private String consignee_state;
    /**
     *城市
     */
    private String consignee_city;
    /**
     *区域
     */
    private String consignee_district;
    /**
     *地址1
     */
    private String consignee_address1;
    /**
     *地址2
     */
    private String consignee_address2;
    /**
     *地址3
     */
    private String consignee_address3;
    /**
     *邮编
     */
    private String consigne_zipcode;
    /**
     *门牌号
     */
    private String consignee_doorplate;
    /**
     *公司
     */
    private String consignee_company;
    /**
     *收件人名称
     */
    private String consignee_name;
    /**
     *收件人电话
     */
    private String consignee_phone;
    /**
     *收件人邮箱
     */
    private String consignee_email;
    /**
     *订单明细
     */
    private SkuItem items;
    /**
     *订单费用
     */
    private FeeDetail fee_details;

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

    public Date getDate_create() {
        return date_create;
    }

    public void setDate_create(Date date_create) {
        this.date_create = date_create;
    }

    public Date getDate_release() {
        return date_release;
    }

    public void setDate_release(Date date_release) {
        this.date_release = date_release;
    }

    public Date getDate_shipping() {
        return date_shipping;
    }

    public void setDate_shipping(Date date_shipping) {
        this.date_shipping = date_shipping;
    }

    public Date getDate_modify() {
        return date_modify;
    }

    public void setDate_modify(Date date_modify) {
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

    public SkuItem getItems() {
        return items;
    }

    public void setItems(SkuItem items) {
        this.items = items;
    }

    public FeeDetail getFee_details() {
        return fee_details;
    }

    public void setFee_details(FeeDetail fee_details) {
        this.fee_details = fee_details;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "order_code='" + order_code + '\'' +
                ", reference_no='" + reference_no + '\'' +
                ", platform='" + platform + '\'' +
                ", order_status='" + order_status + '\'' +
                ", shipping_method='" + shipping_method + '\'' +
                ", tracking_no='" + tracking_no + '\'' +
                ", warehouse_code='" + warehouse_code + '\'' +
                ", order_weight=" + order_weight +
                ", order_desc='" + order_desc + '\'' +
                ", date_create=" + date_create +
                ", date_release=" + date_release +
                ", date_shipping=" + date_shipping +
                ", date_modify=" + date_modify +
                ", consignee_country_code='" + consignee_country_code + '\'' +
                ", consignee_country_name='" + consignee_country_name + '\'' +
                ", consignee_state='" + consignee_state + '\'' +
                ", consignee_city='" + consignee_city + '\'' +
                ", consignee_district='" + consignee_district + '\'' +
                ", consignee_address1='" + consignee_address1 + '\'' +
                ", consignee_address2='" + consignee_address2 + '\'' +
                ", consignee_address3='" + consignee_address3 + '\'' +
                ", consigne_zipcode='" + consigne_zipcode + '\'' +
                ", consignee_doorplate='" + consignee_doorplate + '\'' +
                ", consignee_company='" + consignee_company + '\'' +
                ", consignee_name='" + consignee_name + '\'' +
                ", consignee_phone='" + consignee_phone + '\'' +
                ", consignee_email='" + consignee_email + '\'' +
                ", items=" + items +
                ", fee_details=" + fee_details +
                '}';
    }
}
