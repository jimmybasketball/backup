package com.sfebiz.supplychain.open.exposed.wms.entity;

import java.io.Serializable;
import java.util.List;

import net.pocrd.annotation.Description;

/**
 * Created by zhangyajing on 2017/3/16.
 */
@Description("订单路由信息 \nen-us:order routes")
public class WmsOrderRoutesResult implements Serializable {

    private static final long serialVersionUID = 395616751492904555L;
    @Description("客户销售订单号 \nen-us:customer order number")
    public String merchantOrderId;

    @Description("订单是否存在 T:存在 F:不存在")
    public String isExist;

    @Description("快递公司代码 \nen-us:express company code")
    public String carrierCode;

    @Description("运单号 \nen-us:mail number")
    public String mailNo;

    @Description("包裹重量（千克）\nen-us:package weight(kg)")
    public String weight;

    @Description("路由信息 \nen-us:order routes")
    public List<WmsOrderRouteEntity> wmsOrderRouteEntities;

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<WmsOrderRouteEntity> getWmsOrderRouteEntities() {
        return wmsOrderRouteEntities;
    }

    public void setWmsOrderRouteEntities(List<WmsOrderRouteEntity> wmsOrderRouteEntities) {
        this.wmsOrderRouteEntities = wmsOrderRouteEntities;
    }

    public String getIsExist() {
        return isExist;
    }

    public void setIsExist(String isExist) {
        this.isExist = isExist;
    }

    @Override
    public String toString() {
        return "WmsOrderRoutesResult{" +
                "merchantOrderId='" + merchantOrderId + '\'' +
                ", isExist='" + isExist + '\'' +
                ", carrierCode='" + carrierCode + '\'' +
                ", mailNo='" + mailNo + '\'' +
                ", weight='" + weight + '\'' +
                ", wmsOrderRouteEntities=" + wmsOrderRouteEntities +
                '}';
    }
}
