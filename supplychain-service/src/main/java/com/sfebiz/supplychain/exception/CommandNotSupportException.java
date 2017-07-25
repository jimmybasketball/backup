package com.sfebiz.supplychain.exception;

public class CommandNotSupportException extends CommandException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -507399438266209378L;
	public CommandNotSupportException(String error) {
		super(2000, error);
	}
	
	public CommandNotSupportException(int code, String error) {
		super(code, error);
	}
}
