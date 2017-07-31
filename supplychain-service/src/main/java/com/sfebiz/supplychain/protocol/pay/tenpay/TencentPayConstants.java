package com.sfebiz.supplychain.protocol.pay.tenpay;

import com.sfebiz.supplychain.provider.entity.PriceUnit;
import org.apache.commons.lang3.StringUtils;

/**
 * 财付通支付需要的常量
 * Created by zhaojingyang on 2015/5/11.
 */
public class TencentPayConstants {
    public static final String CURRENCY_CNY = PriceUnit.CNY;//币种 人民币
    public static final String REC_SUCCESS = "0"; //财付通REC_CODE 0 表示接口调用成功
    public static final String TENCENT_PAY_VERSION = "1.0"; //财付通服务版本
    public static final String DEF_SIGN_KEY_INDEX = "1";//多密钥支持的密钥序号，默认1


    public enum TencentPayApiUrl {
        PAY_BILL_DECLARE_URL("mch_custom_declare.cgi", "财付通支付支付单申报"),
        PAY_BILL_QUERY_URL("mch_custom_query.cgi", "财付通支付单申报结果查询");

        private String url;
        private String description;

        TencentPayApiUrl(String url, String description) {
            this.url = url;
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return url;
        }
    }


    /**
     * 1 待申报
     * 2 待修改申报（订单已经送海关，商户重新申报，并且海关还有修改接口，那么记录的状态会是这个）
     * 3 申报中
     * 4 申报成功
     * 5 申报失败
     */
    public enum TencentPayState {
        WAIT_DECLARE("1", "待申报"),
        WAIT_MOTIFY("2", "待修改申报"),
        DECLAREING("3", "申报中"),
        DECLARE_SUCCESS("4", "申报成功"),
        DECLARE_FAILURE("5", "申报失败");

        /**
         * 状态
         */
        private String state;

        /**
         * 描述
         */
        private String description;

        TencentPayState(String state, String description) {
            this.state = state;
            this.description = description;
        }

        public String getState() {
            return state;
        }

        public String getDescription() {
            return description;
        }

        public static TencentPayState getByState(String state) {
            if (StringUtils.isBlank(state)) {
                return null;
            }
            for (TencentPayState declareState : TencentPayState.values()) {
                if (declareState.getState().equalsIgnoreCase(state)) {
                    return declareState;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return state;
        }
    }


    /**
     * 编码枚举
     */
    public enum CharSet {
        UTF8("UTF-8"), GBK("GBK");
        private String value;

        private CharSet(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     * 加密方式
     */
    public enum SignType {
        MD5("MD5"), RSA("RSA");
        private String value;

        private SignType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     * 操作类型1：新增 2：修改
     */
    public enum ActionType {
        ADD("1"), MOTIFY("2"), REPUSH("3");
        private String value;

        private ActionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
