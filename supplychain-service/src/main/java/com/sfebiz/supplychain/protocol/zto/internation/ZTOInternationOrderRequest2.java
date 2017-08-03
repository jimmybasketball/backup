package com.sfebiz.supplychain.protocol.zto.internation;

import com.sfebiz.supplychain.protocol.zto.ZTORequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/1
 * Time: 下午4:21
 */
public class ZTOInternationOrderRequest2 implements ZTORequest {

    private static final long serialVersionUID = -6072657507097100826L;

    /**
     * 订单编号
     */
    private String orderno;

    /**
     * 发货人名称
     */
    private String shipper;

    /**
     * 发货人省份
     */
    private String shipperprov;

    /**
     * 发货人市
     */
    private String shippercity;

    /**
     * 发货人区县
     */
    private String shipperdistrict;

    /**
     * 发货人地址
     */
    private String shipperaddress;

    /**
     * 发货人手机(手机与电话二选 一)
     */
    private String shippermobile;

    /**
     * 发货人电话(手机与电话二选 一)
     */
    private String shippertelephone;

    /**
     * 发货人所在国
     */
    private String shippercountry;

    /**
     * 发货人邮编
     */
    private String shipperzipcode;

    /**
     * 收货人名称
     */
    private String consignee;

    /**
     * 收货人省份
     */
    private String consigneeprov;


    /**
     * 收货人市
     */
    private String consigneecity;

    /**
     * 收货人区县
     */
    private String consigneedistrict;

    /**
     * 收货人地址
     */
    private String consigneeaddress;

    /**
     * 收货人手机(手机与电话二选 一)
     */
    private String consigneemobile;

    /**
     * 收货人电话(手机与电话二选 一)
     */
    private String consigneetelephone;

    /**
     * 收货人所在国(进口为中国)
     */
    private String consigneecountry;

    /**
     * 收货人邮编
     */
    private String consigneezipcode;

    /**
     * 证件类型(1-身份证 2-军官证 3-护照 4-其它)
     */
    private Integer idtype;

    /**
     * 证件号码
     */
    private String customerid;

    /**
     * 毛重(kg)
     */
    private BigDecimal weight;

    /**
     * 进出口标志(I-进口 E-出口)
     */
    private String ietype;

    /**
     * 集货/备货(1-集货 2-备货)
     */
    private Integer stockflag;


    /**
     * 海关编号(控制订单推送的电 子口岸)(参考海关编码表)
     */
    private String cumstomscode;

    /**
     * 平台来源(接口对接时确认)
     */
    private Integer platformSource;

    /**
     * 净重(kg) 一定小于毛重的内 容(WEIGHT)
     */
    private BigDecimal netweight;

    /**
     * 运输方式
     */
    private String shipType;

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 总运单号(集货必填,备货选填)
     */
    private String totallogisticsno;

    /**
     * 航班号(集货必填,备货选填)
     */
    private String flightCode;

    /**
     * 大头笔
     */
    private String sortContent;

    /**
     * 保税业务必填实体
     */
    private ZTOInternationBillEntity billEntity;


    /**
     * 直邮业务的订单实体
     */
    private ZTOInternationOrderEntity2 orderEntity;

    /**
     *
     */
    private List<ZTOInternationIntlOrderItemEntity> intlOrderItemList;


    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperprov() {
        return shipperprov;
    }

    public void setShipperprov(String shipperprov) {
        this.shipperprov = shipperprov;
    }

    public String getShippercity() {
        return shippercity;
    }

    public void setShippercity(String shippercity) {
        this.shippercity = shippercity;
    }

    public String getShipperdistrict() {
        return shipperdistrict;
    }

    public void setShipperdistrict(String shipperdistrict) {
        this.shipperdistrict = shipperdistrict;
    }

    public String getShipperaddress() {
        return shipperaddress;
    }

    public void setShipperaddress(String shipperaddress) {
        this.shipperaddress = shipperaddress;
    }

    public String getShippermobile() {
        return shippermobile;
    }

    public void setShippermobile(String shippermobile) {
        this.shippermobile = shippermobile;
    }

    public String getShippertelephone() {
        return shippertelephone;
    }

    public void setShippertelephone(String shippertelephone) {
        this.shippertelephone = shippertelephone;
    }

    public String getShippercountry() {
        return shippercountry;
    }

    public void setShippercountry(String shippercountry) {
        this.shippercountry = shippercountry;
    }

    public String getShipperzipcode() {
        return shipperzipcode;
    }

    public void setShipperzipcode(String shipperzipcode) {
        this.shipperzipcode = shipperzipcode;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeprov() {
        return consigneeprov;
    }

    public void setConsigneeprov(String consigneeprov) {
        this.consigneeprov = consigneeprov;
    }

    public String getConsigneecity() {
        return consigneecity;
    }

    public void setConsigneecity(String consigneecity) {
        this.consigneecity = consigneecity;
    }

    public String getConsigneedistrict() {
        return consigneedistrict;
    }

    public void setConsigneedistrict(String consigneedistrict) {
        this.consigneedistrict = consigneedistrict;
    }

    public String getConsigneeaddress() {
        return consigneeaddress;
    }

    public void setConsigneeaddress(String consigneeaddress) {
        this.consigneeaddress = consigneeaddress;
    }

    public String getConsigneemobile() {
        return consigneemobile;
    }

    public void setConsigneemobile(String consigneemobile) {
        this.consigneemobile = consigneemobile;
    }

    public String getConsigneetelephone() {
        return consigneetelephone;
    }

    public void setConsigneetelephone(String consigneetelephone) {
        this.consigneetelephone = consigneetelephone;
    }

    public String getConsigneecountry() {
        return consigneecountry;
    }

    public void setConsigneecountry(String consigneecountry) {
        this.consigneecountry = consigneecountry;
    }

    public String getConsigneezipcode() {
        return consigneezipcode;
    }

    public void setConsigneezipcode(String consigneezipcode) {
        this.consigneezipcode = consigneezipcode;
    }

    public Integer getIdtype() {
        return idtype;
    }

    public void setIdtype(Integer idtype) {
        this.idtype = idtype;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getIetype() {
        return ietype;
    }

    public void setIetype(String ietype) {
        this.ietype = ietype;
    }

    public Integer getStockflag() {
        return stockflag;
    }

    public void setStockflag(Integer stockflag) {
        this.stockflag = stockflag;
    }

    public String getCumstomscode() {
        return cumstomscode;
    }

    public void setCumstomscode(String cumstomscode) {
        this.cumstomscode = cumstomscode;
    }

    public Integer getPlatformSource() {
        return platformSource;
    }

    public void setPlatformSource(Integer platformSource) {
        this.platformSource = platformSource;
    }

    public BigDecimal getNetweight() {
        return netweight;
    }

    public void setNetweight(BigDecimal netweight) {
        this.netweight = netweight;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getTotallogisticsno() {
        return totallogisticsno;
    }

    public void setTotallogisticsno(String totallogisticsno) {
        this.totallogisticsno = totallogisticsno;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public ZTOInternationOrderEntity2 getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(ZTOInternationOrderEntity2 orderEntity) {
        this.orderEntity = orderEntity;
    }

    public String getSortContent() {
        return sortContent;
    }

    public void setSortContent(String sortContent) {
        this.sortContent = sortContent;
    }

    public List<ZTOInternationIntlOrderItemEntity> getIntlOrderItemList() {
        return intlOrderItemList;
    }

    public void setIntlOrderItemList(List<ZTOInternationIntlOrderItemEntity> intlOrderItemList) {
        this.intlOrderItemList = intlOrderItemList;
    }

    public ZTOInternationBillEntity getBillEntity() {
        return billEntity;
    }

    public void setBillEntity(ZTOInternationBillEntity billEntity) {
        this.billEntity = billEntity;
    }
}
