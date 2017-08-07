package com.sfebiz.supplychain.protocol.wms.oms.stockoutorder;

import net.pocrd.annotation.Description;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p></p>
 * User:tu.jie@ifunq.com
 * Date: 15/1/21
 * Time: 下午9:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SaleOrder")
@XmlType(propOrder = {"userDef1","userDef2"})
public class StockoutOrderExtendAttribute implements Serializable {

    private static final long serialVersionUID = -5864370028322101798L;
    @Description("寄件方公司")
    @XmlElement(name = "UserDef1", required = false)
    private String userDef1;

    @Description("寄件方名称")
    @XmlElement(name = "UserDef2", required = false)
    private String userDef2;

    public String getUserDef1() {
        return userDef1;
    }

    public String getUserDef2() {
        return userDef2;
    }

    public void setUserDef1(String userDef1) {
        this.userDef1 = userDef1;
    }

    public void setUserDef2(String userDef2) {
        this.userDef2 = userDef2;
    }
}
