package com.sfebiz.supplychain.protocol.pay.yihuijin;

import net.pocrd.annotation.Description;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/6/8
 * Time: 下午9:30
 */
@Description("易汇金支付回调实体")
public class YihuijinCallBackRequest implements Serializable {

    private static final long serialVersionUID = -3642750445098981797L;

    @Description("商户编号")
    public String merchantId;

    @Description("支付流水号")
    public String paySerialNumber;

    @Description("报关参数信息")
    public List<YihuijinCallBackCustomsInfo> customsInfos;

    @Description("签名信息")
    public String hmac;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("YihuijinCallBackRequest{");
        sb.append("merchantId='").append(merchantId).append('\'');
        sb.append(", paySerialNumber='").append(paySerialNumber).append('\'');
        sb.append(", customsInfos=").append(customsInfos);
        sb.append(", hmac='").append(hmac).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
