package com.oneabc.api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateMpinRequestVO {
	@NotEmpty(message = "Mobile number cannot be empty")
	@Size(min = 10, max = 10, message = "Invalid mobile number")
	private String mobileNumber;
	@NotEmpty(message = "MPIN cannot be empty")
	private String mpin;
}