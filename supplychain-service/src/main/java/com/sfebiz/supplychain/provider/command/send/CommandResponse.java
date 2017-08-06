package com.sfebiz.supplychain.provider.command.send;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 15/5/19 上午11:50
 */
public class CommandResponse {

    Boolean isSuccess;
    String errorMessage;

    public static final CommandResponse SUCCESS_RESPONSE = new CommandResponse(true, null);

    public CommandResponse() {

    }

    public CommandResponse(Boolean isSuccess, String errorMessage) {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
    }

    public Boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
