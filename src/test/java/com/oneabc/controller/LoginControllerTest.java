package com.oneabc.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneabc.api.model.CreateMpinRequestVO;
import com.oneabc.api.model.OtpResponseVO;
import com.oneabc.api.model.OtpVerificationRequestVO;
import com.oneabc.api.model.ResponseVO;
import com.oneabc.api.model.UpdateMpinRequestVO;
import com.oneabc.exception.OtpServiceException;
import com.oneabc.service.LoginService;

@AutoConfigureMockMvc
@WebMvcTest(controllers = LoginController.class)
public class LoginControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private LoginService loginService;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void sendOtpTest() throws Exception {
		String mobileNumber = "1234567890";

		OtpResponseVO otpResponseVO = new OtpResponseVO();
		otpResponseVO.setStatusCode(HttpStatus.OK.value());
		otpResponseVO.setMessage("SUCCESS");
		otpResponseVO.setOtp("123456");
		given(loginService.sendOtp(mobileNumber)).willReturn(otpResponseVO);

		mockMvc.perform(get("/v1/login/otp").param("mobileNumber", mobileNumber)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.statusCode", is(200))).andExpect(jsonPath("$.message", is("SUCCESS")))
				.andExpect(jsonPath("$.otp", is(otpResponseVO.getOtp())));
	}

	@Test
	public void sendOtpThrowsExceptionTest() throws Exception {
		String mobileNumber = "1234567890";
		OtpResponseVO otpResponseVO = new OtpResponseVO();
		otpResponseVO.setStatusCode(HttpStatus.OK.value());
		otpResponseVO.setMessage("SUCCESS");
		otpResponseVO.setOtp("123456");
		given(loginService.sendOtp(mobileNumber)).willThrow(new OtpServiceException(1000, "Some Exception"));

		mockMvc.perform(get("/v1/login/otp").param("mobileNumber", mobileNumber)).andDo(print()).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.errors[0].errorCode", is(1000)))
				.andExpect(jsonPath("$.errors[0].status", is(500)))
				.andExpect(jsonPath("$.errors[0].message", is("Some Exception")))
				.andExpect(jsonPath("$.errors[0].source", is("login-activation-service")));
	}
	
	@Test
	public void verifyOtpTest() throws Exception {
		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
		request.setMobileNumber("1234567890");
		request.setOtp("123456");
		ResponseVO responseVO = new ResponseVO();
		responseVO.setStatusCode(HttpStatus.OK.value());
		responseVO.setMessage("SUCCESS");
		given(loginService.verifyOtp(request)).willReturn(responseVO);

		mockMvc.perform(post("/v1/login/otp/verification").contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.statusCode", is(200))).andExpect(jsonPath("$.message", is("SUCCESS")));
	}
	
	@Test
	public void verifyOtpThrowsExceptionTest() throws Exception {
		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
		request.setMobileNumber("234567890");
		request.setOtp(null);
		ResponseVO responseVO = new ResponseVO();
		responseVO.setStatusCode(HttpStatus.OK.value());
		responseVO.setMessage("SUCCESS");

		mockMvc.perform(post("/v1/login/otp/verification").contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andDo(print()).andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.errors", hasSize(2)))
		.andExpect(jsonPath("$.errors[0].errorCode", is(1099)))
		.andExpect(jsonPath("$.errors[0].status", is(400)))
		.andExpect(jsonPath("$.errors[0].message", is("Invalid mobile number")))
		.andExpect(jsonPath("$.errors[0].source", is("login-activation-service")))
		.andExpect(jsonPath("$.errors[1].message", is("OTP cannot be empty")));
	}

	@Test
	public void setMpinTest() throws Exception {
		CreateMpinRequestVO request = new CreateMpinRequestVO();
		request.setCustomerId(1L);
		request.setMpin("1234");
		ResponseVO responseVO = new ResponseVO();
		responseVO.setStatusCode(HttpStatus.OK.value());
		responseVO.setMessage("SUCCESS");
		given(loginService.setMpin(request)).willReturn(responseVO);

		mockMvc.perform(
				post("/v1/login/mpin").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.statusCode", is(200)))
				.andExpect(jsonPath("$.message", is("SUCCESS")));
	}

	@Test
	public void updateMpinTest() throws Exception {
		String mobileNumber = "1234567890";

		UpdateMpinRequestVO request = new UpdateMpinRequestVO();
		request.setMobileNumber(mobileNumber);
		request.setMpin("1234");
		ResponseVO responseVO = new ResponseVO();
		responseVO.setStatusCode(HttpStatus.OK.value());
		responseVO.setMessage("SUCCESS");
		given(loginService.updateMpin(request)).willReturn(responseVO);

		mockMvc.perform(patch("/v1/login/mpin/update").contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andDo(print()).andExpect(status().isNoContent())
				.andExpect(jsonPath("$.statusCode", is(200))).andExpect(jsonPath("$.message", is("SUCCESS")));
	}
}
