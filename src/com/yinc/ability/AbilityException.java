package com.yinc.ability;

public class AbilityException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AbilityException(String message) {
		super(message);
	}

	public AbilityException(String message, Throwable cause) {
		super(message, cause);
	}
}
