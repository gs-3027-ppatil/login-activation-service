package com.oneabc.api.model;

import lombok.Data;

@Data
public class UpdateMpinRequestVO {
	private String mobileNumber;
	private String mpin;
}