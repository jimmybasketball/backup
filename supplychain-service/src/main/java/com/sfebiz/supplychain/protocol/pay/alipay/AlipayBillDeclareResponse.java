package com.sfebiz.supplychain.protocol.pay.alipay;

import net.pocrd.annotation.Description;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * <p>支付宝订单申报返回</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/5/18
 * Time: 下午8:54
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "alipay")
@XmlType(propOrder = {"isSuccess", "signType", "sign", "error", "responseContentWrap"})
public class AlipayBillDeclareResponse implements Serializable {

    private static final long serialVersionUID = 1259228596210794965L;

    @XmlElement(name = "is_success")
    @Description("请求是否成功。请求成功不代表业务处理成功。T代表成功 F代表失败")
    private String isSuccess;

    @XmlElement(name = "sign_type")
    @Description("签名方式,DSA、RSA、MD5 三个值可选, 必须大写。")
    private String signType;

    @XmlElement(name = "sign")
    @Description("签名")
    private String sign;

    @XmlElement(name = "error")
    @Description("错误代码,请求成功时,不存在本参 数;请求失败时,本参数为错误 代码")
    private String error;

    @XmlElement(name = "response")
    @Description("响应")
    private ResponseContentWrap responseContentWrap;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public ResponseContentWrap getResponseContentWrap() {
        return responseContentWrap;
    }

    public void setResponseContentWrap(ResponseContentWrap responseContentWrap) {
        this.responseContentWrap = responseContentWrap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("isSuccess", isSuccess)
                .append("signType", signType)
                .append("sign", sign)
                .append("error", error)
                .toString();
    }
}
