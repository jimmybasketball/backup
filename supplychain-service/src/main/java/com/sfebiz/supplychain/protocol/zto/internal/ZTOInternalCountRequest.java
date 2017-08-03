package com.sfebiz.supplychain.protocol.zto.internal;

import com.sfebiz.supplychain.protocol.zto.ZTORequest;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 15/12/21
 * Time: 下午4:25
 */
public class ZTOInternalCountRequest implements ZTORequest {

    private static final long serialVersionUID = -1065486456037667608L;

    private String lastno;

    public String getLastno() {
        return lastno;
    }

    public void setLastno(String lastno) {
        this.lastno = lastno;
    }
}
