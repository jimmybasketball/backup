package com.sfebiz.supplychain.protocol.wms.nbbs;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"OrderFrom", "Idnum", "Name", "Account", "Phone", "Email"})
@Description("UserReignbody")
public class UserReignBody implements Serializable {
    private static final long serialVersionUID = 463404467634081538L;
    @Description("购物网站代码")
    @XmlElement(nillable = false, required = false)
    public String OrderFrom;

    @Description("账号(身份证)")
    @XmlElement(nillable = false, required = false)
    public String Idnum;

    @Description("真实姓名")
    @XmlElement(nillable = false, required = false)
    public String Name;

    @Description("购物网站账号")
    @XmlElement(nillable = false, required = false)
    public String Account;

    @Description("手机号")
    @XmlElement(nillable = false, required = false)
    public String Phone;

    @Description("邮箱")
    @XmlElement(nillable = false, required = false)
    public String Email;

    public String getOrderFrom() {
        return OrderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        OrderFrom = orderFrom;
    }

    public String getIdnum() {
        return Idnum;
    }

    public void setIdnum(String idnum) {
        Idnum = idnum;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("OrderFrom", OrderFrom)
                .append("Idnum", Idnum)
                .append("Name", Name)
                .append("Account", Account)
                .append("Phone", Phone)
                .append("Email", Email)
                .toString();
    }
}
