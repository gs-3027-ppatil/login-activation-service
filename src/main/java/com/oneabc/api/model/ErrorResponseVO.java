package com.oneabc.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseVO {

	private int errorCode;
	private int status;
	private String message;
	private String source;
}