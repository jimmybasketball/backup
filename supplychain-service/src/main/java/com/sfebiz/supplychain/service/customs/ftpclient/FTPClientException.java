package com.sfebiz.supplychain.service.customs.ftpclient;

/**
 * <p></p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 16/3/31
 * Time: 下午5:31
 */
public class FTPClientException extends RuntimeException {

    private static final long serialVersionUID = 8967167238545090892L;

    /**
     * Constructor for FTPClientException.
     *
     * @param msg the detail message
     */
    public FTPClientException(String msg) {
        super(msg);
    }

    /**
     * Constructor for FTPClientException.
     *
     * @param msg the detail message
     * @param cause
     */
    public FTPClientException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructor for DataAccessException.
     FTPClientException
     * @param cause
     */
    public FTPClientException(Throwable cause) {
        super(cause);
    }
}
