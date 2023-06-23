package com.oneabc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oneabc.api.model.CreateMpinRequestVO;
import com.oneabc.api.model.OtpResponseVO;
import com.oneabc.api.model.OtpVerificationRequestVO;
import com.oneabc.api.model.ResponseVO;
import com.oneabc.api.model.UpdateMpinRequestVO;
import com.oneabc.service.LoginService;

@RestController
@RequestMapping("/login")
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

	@PatchMapping(value = "/mpin/update")
	public ResponseEntity<ResponseVO> updateMpin(@RequestBody UpdateMpinRequestVO updateMpinRequestVO) {
		ResponseVO response = loginService.updateMpin(updateMpinRequestVO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}