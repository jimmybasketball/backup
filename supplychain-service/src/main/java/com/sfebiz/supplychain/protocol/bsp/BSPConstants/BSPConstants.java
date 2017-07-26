package com.sfebiz.supplychain.protocol.bsp.BSPConstants;

/**
 * Created by zhaojingyang on 2015/4/21.
 */
public class BSPConstants {

    public enum BSPRoute {
        ClearPortStart("605", "开始清关"),
        ClearPortEnd("606","清关结束"),
        Collected("50","已收取快件"),
        Delivering("44","派件中"),
        Signed("80", "已签收");
        private String code;
        private String codeDesc;

        private BSPRoute(String code, String codeDesc) {
            this.code = code;
            this.codeDesc = codeDesc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodeDesc() {
            return codeDesc;
        }

        public void setCodeDesc(String codeDesc) {
            this.codeDesc = codeDesc;
        }
    }
}
