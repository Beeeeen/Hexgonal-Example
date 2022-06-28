package com.systex.msg.exception;

public class ServiceRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -2294054198084347502L;

	public ServiceRuntimeException(String message) {
		super(message);
	}
	
	public ServiceRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}