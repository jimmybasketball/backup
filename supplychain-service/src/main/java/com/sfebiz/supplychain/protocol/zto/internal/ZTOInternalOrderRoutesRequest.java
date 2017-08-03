package com.sfebiz.supplychain.protocol.zto.internal;

import com.sfebiz.supplychain.protocol.zto.ZTORequest;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/17
 * Time: 下午6:43
 */
public class ZTOInternalOrderRoutesRequest implements ZTORequest {
    private static final long serialVersionUID = -7552134121868450831L;

    private String mailno;

    public String getMailno() {
        return mailno;
    }

    public void setMailno(String mailno) {
        this.mailno = mailno;
    }
}
