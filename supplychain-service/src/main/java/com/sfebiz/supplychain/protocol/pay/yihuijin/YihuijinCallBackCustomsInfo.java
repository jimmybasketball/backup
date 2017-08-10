package com.sfebiz.supplychain.protocol.pay.yihuijin;

import net.pocrd.annotation.Description;

import java.io.Serializable;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/6/8
 * Time: 下午9:30
 */
@Description("连连支付回调报关参数")
public class YihuijinCallBackCustomsInfo implements Serializable {

    private static final long serialVersionUID = -3642750445098981797L;

    @Description("报关通道")
    public String customsChannel;

    @Description("报关金额")
    public Long amount;

    @Description("运费")
    public Long freight;

    @Description("支付货款")
    public Long goodsAmount;

    @Description("支付税款")
    public Long tax;

    @Description("报关状态:PROCESSING：处理中;SUCCESS:成功;FAILED:失败")
    public String status;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("YihuijinCallBackCustomsInfo{");
        sb.append("customsChannel='").append(customsChannel).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", freight=").append(freight);
        sb.append(", goodsAmount=").append(goodsAmount);
        sb.append(", tax=").append(tax);
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
