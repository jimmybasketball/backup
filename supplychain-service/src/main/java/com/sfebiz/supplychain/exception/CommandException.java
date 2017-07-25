package com.sfebiz.supplychain.exception;


public class CommandException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8088776787266119398L;
	public CommandException(String error){
		super(200,error);
	}
	
	public CommandException(int code, String error) {
		super(code, error);
	}

}
