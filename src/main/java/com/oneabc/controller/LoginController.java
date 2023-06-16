package com.oneabc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oneabc.api.model.CreateMpinRequestVO;
import com.oneabc.api.model.OtpResponseVO;
import com.oneabc.api.model.OtpVerificationRequestVO;
import com.oneabc.api.model.ResetMpinRequestVO;
import com.oneabc.api.model.ResponseVO;
import com.oneabc.service.LoginService;

@RestController
@RequestMapping("/oneabc")
public class LoginController {
	@Autowired
	private LoginService loginService;

	@GetMapping(value = "/otp")
	public ResponseEntity<OtpResponseVO> getOtp(@RequestParam("mobileNumber") String mobileNumber) {
		OtpResponseVO loginResponse = loginService.sendOtp(mobileNumber);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/otp/verification")
	public ResponseEntity<ResponseVO> verifyOtp(@RequestBody OtpVerificationRequestVO otpVerificationRequestVO) {
		ResponseVO response = loginService.verifyOtp(otpVerificationRequestVO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/mpin")
	public ResponseEntity<ResponseVO> setMpin(@RequestBody CreateMpinRequestVO createMpinRequestVO) {
		ResponseVO response = loginService.setMpin(createMpinRequestVO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/mpin/reset")
	public ResponseEntity<ResponseVO> forgotMpin(@RequestBody ResetMpinRequestVO resetMpinRequestVO) {
		ResponseVO response = loginService.resetMpin(resetMpinRequestVO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}