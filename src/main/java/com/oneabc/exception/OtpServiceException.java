package com.oneabc.exception;

import lombok.Data;

@Data
public class OtpServiceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int statusCode;
	private String message;

	public OtpServiceException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
		this.message = message;
	}

	public OtpServiceException() {
		super();
	}
}
