package com.systex.msg.exception;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -2792892891910571181L;

	public BadRequestException(String message) {
		super(message);
	}
}
