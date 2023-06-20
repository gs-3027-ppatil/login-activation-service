package com.oneabc.api.model;

import lombok.Data;

@Data
public class CreateMpinRequestVO {
	private Long customerId;
	private String mpin;
}