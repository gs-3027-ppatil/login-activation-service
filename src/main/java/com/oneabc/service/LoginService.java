package com.oneabc.service;

import com.oneabc.api.model.CreateMpinRequestVO;
import com.oneabc.api.model.OtpResponseVO;
import com.oneabc.api.model.OtpVerificationRequestVO;
import com.oneabc.api.model.ResetMpinRequestVO;
import com.oneabc.api.model.ResponseVO;

public interface LoginService {
	public OtpResponseVO sendOtp(String mobileNumber);
	public ResponseVO verifyOtp(OtpVerificationRequestVO otpVerificationRequestVO);
	public ResponseVO setMpin(CreateMpinRequestVO createMpinRequestVO);
	public ResponseVO resetMpin(ResetMpinRequestVO resetMpinRequestVO);
}
