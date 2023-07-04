package com.oneabc.constant;

public enum ResponseEnum {
	SUCCESS(1000, "Success"),
	FAILURE(1001, "Failure"),
	INVALID_MOBILE_NUMBER(1002, "Invalid mobile number"),
	INVALID_OTP(1003, "Invalid OTP"),
	INVALID_REQUEST_BODY(1004, "Request body cannot be empty"),
	MPIN_NOT_FOUND(1005, "MPIN does not exist"),
	CUSTOMER_NOT_FOUND(1006, "Customer does not exist"),
	MPIN_EXPIRED(1007, "MPIN expired"),
	INCORRECT_MPIN(1008, "Incorrect MPIN"),
	DUPLICATE_MPIN_ENTRY(1009, "MPIN is already saved"),
	EMPTY_MOBILE_NUMBER(1010, "Empty mobile number");

	private final Integer statusCode;
	private final String message;

	private ResponseEnum(Integer statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}
}
