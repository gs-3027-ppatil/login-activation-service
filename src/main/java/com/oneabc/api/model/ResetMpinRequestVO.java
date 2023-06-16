package com.oneabc.api.model;

import lombok.Data;

@Data
public class ResetMpinRequestVO {
	private String mobileNumber;
	private String mobilePin;
}