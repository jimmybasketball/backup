package com.sfebiz.supplychain.open.exposed.wms.entity.request;

import java.io.Serializable;

import net.pocrd.annotation.Description;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.sfebiz.supplychain.open.exposed.wms.entity.trade.OpenWmsTradeOrderItem;

/**
 * 云仓交易出库单，创建、修改请求实体对象
 * 
 * @author matt
 * @version $Id: WmsTradeCreateOrderRequest.java, v 0.1 2017年3月23日 下午3:30:06 matt Exp $
 */
@Description("云仓交易出库单，创建、修改请求")
public class OpenWmsTradeOrderCreateRequest implements Serializable {

    /** 序列号 */
    private static final long    serialVersionUID = -6262489094126124957L;

    /** 出库单信息 （必填）*/
    @Description("出库单信息，必填")
    public OpenWmsTradeOrderItem order;

    /** 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
