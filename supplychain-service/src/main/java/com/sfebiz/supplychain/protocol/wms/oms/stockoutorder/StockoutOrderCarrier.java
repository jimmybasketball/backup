package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * User:tu.jie@ifunq.com
 * Date: 15/1/21
 * Time: 下午9:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SaleOrder")
@XmlType(propOrder = {"carrier","carrierProduct","waybillNo","routeNumbers","paymentOfCharge","monthlyAccount","paymentDistict","returnService","returnTracking",
        "ifSelfPickup","carrierAttr1","carrierAttr2","carrierAttr3","carrierAddedServices"})
public class StockoutOrderCarrier implements Serializable {

    private static final long serialVersionUID = -2884335044287855687L;
    @Description("承运商")
    @XmlElement(name = "Carrier", required = false)
    private String carrier;

    @Description("承运商产品")
    @XmlElement(name = "CarrierProduct", required = false)
    private String carrierProduct;

    @Description("运单号")
    @XmlElement(name = "WaybillNo", required = false)
    private String waybillNo;

    @Description("线路编码")
    @XmlElement(name = "RouteNumbers", required = false)
    private String routeNumbers;

    @Description("运费支付方式")
    @XmlElement(name = "PaymentOfCharge", required = false)
    private String paymentOfCharge;

    @Description("月结账号")
    @XmlElement(name = "MonthlyAccount", required = false)
    private String monthlyAccount;

    @Description("付款地区")
    @XmlElement(name = "PaymentDistict", required = false)
    private String paymentDistict;

    @Description("是否需要签回单")
    @XmlElement(name = "ReturnService", required = false)
    private String returnService;

    @Description("签回单号")
    @XmlElement(name = "ReturnTracking", required = false)
    private String returnTracking;

    @Description("是否自取件")
    @XmlElement(name = "IfSelfPickup", required = false)
    private String ifSelfPickup;

    @Description("承运商属性1")
    @XmlElement(name = "CarrierAttr1", required = false)
    private String carrierAttr1;

    @Description("承运商属性2")
    @XmlElement(name = "CarrierAttr2", required = false)
    private String carrierAttr2;

    @Description("承运商属性3")
    @XmlElement(name = "CarrierAttr3", required = false)
    private String carrierAttr3;

    @XmlElement(name = "CarrierAddedService", required = false)
     public List<StockoutOrderCarrierAddedService> carrierAddedServices;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getCarrierProduct() {
        return carrierProduct;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public String getRouteNumbers() {
        return routeNumbers;
    }

    public String getPaymentOfCharge() {
        return paymentOfCharge;
    }

    public String getMonthlyAccount() {
        return monthlyAccount;
    }

    public String getPaymentDistict() {
        return paymentDistict;
    }

    public String getReturnService() {
        return returnService;
    }

    public String getReturnTracking() {
        return returnTracking;
    }

    public String getIfSelfPickup() {
        return ifSelfPickup;
    }

    public String getCarrierAttr1() {
        return carrierAttr1;
    }

    public String getCarrierAttr2() {
        return carrierAttr2;
    }

    public String getCarrierAttr3() {
        return carrierAttr3;
    }

    public List<StockoutOrderCarrierAddedService> getCarrierAddedServices() {
        return carrierAddedServices;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public void setCarrierProduct(String carrierProduct) {
        this.carrierProduct = carrierProduct;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public void setRouteNumbers(String routeNumbers) {
        this.routeNumbers = routeNumbers;
    }

    public void setPaymentOfCharge(String paymentOfCharge) {
        this.paymentOfCharge = paymentOfCharge;
    }

    public void setMonthlyAccount(String monthlyAccount) {
        this.monthlyAccount = monthlyAccount;
    }

    public void setPaymentDistict(String paymentDistict) {
        this.paymentDistict = paymentDistict;
    }

    public void setReturnService(String returnService) {
        this.returnService = returnService;
    }

    public void setReturnTracking(String returnTracking) {
        this.returnTracking = returnTracking;
    }

    public void setIfSelfPickup(String ifSelfPickup) {
        this.ifSelfPickup = ifSelfPickup;
    }

    public void setCarrierAttr1(String carrierAttr1) {
        this.carrierAttr1 = carrierAttr1;
    }

    public void setCarrierAttr2(String carrierAttr2) {
        this.carrierAttr2 = carrierAttr2;
    }

    public void setCarrierAttr3(String carrierAttr3) {
        this.carrierAttr3 = carrierAttr3;
    }

    public void setCarrierAddedServices(List<StockoutOrderCarrierAddedService> carrierAddedServices) {
        this.carrierAddedServices = carrierAddedServices;
    }
}
