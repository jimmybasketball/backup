package com.sfebiz.supplychain.exception;

public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8365325110829815680L;

	private int errorCode;
	private String error;
	
	public BaseException(int errorCode,String error){
		this.errorCode = errorCode;
		this.setError(error);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
