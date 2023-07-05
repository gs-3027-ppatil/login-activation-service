package com.oneabc.api.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMpinRequestVO {
	@NotNull(message = "Customer Id cannot be null")
	private Long customerId;
	@NotEmpty(message = "MPIN cannot be empty")
	private String mpin;
}