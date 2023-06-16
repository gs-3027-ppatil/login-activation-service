package com.oneabc.api.model;

import lombok.Data;

@Data
public class OtpVerificationRequestVO {
	private String mobileNumber;
	private String Otp;
}
