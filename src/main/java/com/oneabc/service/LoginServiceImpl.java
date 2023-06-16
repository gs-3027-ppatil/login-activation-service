package com.oneabc.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.oneabc.api.model.CreateMpinRequestVO;
import com.oneabc.api.model.OtpResponseVO;
import com.oneabc.api.model.OtpVerificationRequestVO;
import com.oneabc.api.model.ResetMpinRequestVO;
import com.oneabc.api.model.ResponseVO;
import com.oneabc.exception.OtpServiceException;

@Service
public class LoginServiceImpl implements LoginService {
	static int attemptCount = 0;

	@Override
	public OtpResponseVO sendOtp(String mobileNumber) {

		if (isValidMobileNumber(mobileNumber)) {

			// TODO Auto-generated method stub
			// Actual REST call to OTP service api
			/*
			 * HttpHeaders headers = new HttpHeaders();
			 * headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			 * HttpEntity<Product> entity = new HttpEntity<Product>(product,headers);
			 * 
			 * return restTemplate.exchange( "http://localhost:8080/products",
			 * HttpMethod.POST, entity, String.class).getBody();
			 */
			// TODO Create custom status code and message enum
			
			if (attemptCount <= 2) {
				OtpResponseVO res = createLoginResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
						"123456");
				attemptCount++;
//				throw new OtpServiceException(HttpStatus.NOT_IMPLEMENTED.value(), "ABC");
				return res;
			} else {
				OtpResponseVO res = createLoginResponse(HttpStatus.TOO_MANY_REQUESTS.value(),
						"Too many attempts for OTP", null);
				return res;
			}
		} else {
			OtpResponseVO res = createLoginResponse(HttpStatus.BAD_REQUEST.value(), "Please enter valid mobile number",
					null);
			return res;
		}
	}

	private boolean isValidMobileNumber(String mobileNumber) {
		if (Strings.isNotEmpty(mobileNumber)) {
			Pattern p = Pattern.compile("^\\d{10}$");
			Matcher m = p.matcher(mobileNumber);
			return m.matches();
		} else {
			return false;
		}
	}

	private OtpResponseVO createLoginResponse(int statusCode, String message, String otp) {
		OtpResponseVO res = new OtpResponseVO();
		res.setStatusCode(statusCode);
		res.setMessage(message);
		res.setOtp(otp);
		return res;
	}

	@Override
	public ResponseVO verifyOtp(OtpVerificationRequestVO otpVerificationRequestVO) {
		if (otpVerificationRequestVO != null && Strings.isNotEmpty(otpVerificationRequestVO.getOtp())
				&& isValidMobileNumber(otpVerificationRequestVO.getMobileNumber())) {
			return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Mobile number or OTP entered is incorrect");
		}
	}

	@Override
	public ResponseVO setMpin(CreateMpinRequestVO createMpinRequestVO) {
		if (createMpinRequestVO != null && Strings.isNotEmpty(createMpinRequestVO.getCustomerId())
				&& Strings.isNotEmpty(createMpinRequestVO.getMobilePin())) {
			String hashedMpin = "";
			try {
				hashedMpin = generateMpinHash(createMpinRequestVO.getMobilePin());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseVO(HttpStatus.OK.value(), hashedMpin);
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Customer Id or OTP entered is incorrect");
		}
	}

	private String generateMpinHash(String mpin) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		/*
		 * MessageDigest md = MessageDigest.getInstance("MD5");
		 * 
		 * byte[] digest = md.digest(mpin.getBytes()); BigInteger no = new BigInteger(1,
		 * digest); String hashtext = no.toString(16); while (hashtext.length() < 32) {
		 * hashtext = "0" + hashtext; } return hashtext;
		 */
		
		return DigestUtils.md5DigestAsHex(mpin.getBytes());
	}

	@Override
	public ResponseVO resetMpin(ResetMpinRequestVO resetMpinRequestVO) {
		if (resetMpinRequestVO != null && Strings.isNotEmpty(resetMpinRequestVO.getMobileNumber())
				&& isValidMobileNumber(resetMpinRequestVO.getMobilePin())) {
			return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Mobile number or OTP entered is incorrect");
		}
	}

}
