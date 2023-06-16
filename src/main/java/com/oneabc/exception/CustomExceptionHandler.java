package com.oneabc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.oneabc.api.model.ErrorResponseVO;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(OtpServiceException.class)
	protected ResponseEntity<Object> handleOtpServiceException(OtpServiceException ex) {

		ErrorResponseVO res = new ErrorResponseVO();
		res.setStatusCode(ex.getStatusCode());
		res.setMessage(ex.getMessage());;
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
