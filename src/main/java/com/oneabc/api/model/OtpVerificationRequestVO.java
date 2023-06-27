package com.oneabc.api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OtpVerificationRequestVO {
	@NotEmpty(message = "Mobile number cannot be empty")
	@Size(min = 10, max = 10, message = "Invalid mobile number")
	private String mobileNumber;
	@NotEmpty(message = "OTP cannot be empty")
	@Size(min = 6, max = 6, message = "Invalid OTP")
	private String otp;
}
