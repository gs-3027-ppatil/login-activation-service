package com.oneabc.api.model;

import lombok.Data;

@Data
public class CreateMpinRequestVO {
	private String customerId;
	private String mobilePin;
}