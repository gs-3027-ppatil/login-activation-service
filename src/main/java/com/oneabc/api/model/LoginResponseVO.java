package com.oneabc.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseVO {
	private int statusCode;
	private String message;
	private String mpin;
}
