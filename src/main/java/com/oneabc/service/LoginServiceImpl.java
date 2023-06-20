package com.oneabc.service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oneabc.api.model.CreateMpinRequestVO;
import com.oneabc.api.model.OtpResponseVO;
import com.oneabc.api.model.OtpVerificationRequestVO;
import com.oneabc.api.model.ResponseVO;
import com.oneabc.api.model.SaveCustomerRequestVO;
import com.oneabc.api.model.UpdateMpinRequestVO;
import com.oneabc.model.Customer;
import com.oneabc.model.PinMgt;
import com.oneabc.repository.CustomerRepository;

@Service
public class LoginServiceImpl implements LoginService {
	static int attemptCount = 0;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public OtpResponseVO sendOtp(String mobileNumber) {

		if (isValidMobileNumber(mobileNumber)) {

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

	private boolean isValidOtp(String otp) {
		if (Strings.isNotEmpty(otp)) {
			Pattern p = Pattern.compile("^\\d{6}$");
			Matcher m = p.matcher(otp);
			return m.matches();
		} else {
			return false;
		}
	}

	private boolean isValidMpin(String mPin) {
		if (Strings.isNotEmpty(mPin)) {
			Pattern p = Pattern.compile("^\\d{4}$");
			Matcher m = p.matcher(mPin);
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
		if (otpVerificationRequestVO != null && isValidOtp(otpVerificationRequestVO.getOtp())
				&& isValidMobileNumber(otpVerificationRequestVO.getMobileNumber())) {
			return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Mobile number or OTP entered is incorrect");
		}
	}

	@Override
	public ResponseVO setMpin(CreateMpinRequestVO createMpinRequestVO) {
		if (createMpinRequestVO != null && isValidMpin(createMpinRequestVO.getMpin())) {
			Optional<Customer> customer = customerRepository.findById(createMpinRequestVO.getCustomerId());
			String mobilePin = createMpinRequestVO.getMpin();
			return saveMpin(customer, mobilePin);
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Customer Id or OTP entered is incorrect");
		}
	}

	private ResponseVO saveMpin(Optional<Customer> customer, String mobilePin) {
		if (customer.isPresent()) {
			String hashedMpin = "";
			try {
				hashedMpin = generateMpinHash(mobilePin);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid hashing algorithm");
			}
			PinMgt mpin = new PinMgt();
			mpin.setActive(true);
			mpin.setCurrentMpin(hashedMpin);
			Customer customerFromDb = customer.get();
			customerFromDb.setMPin(mpin);
			customerRepository.save(customerFromDb);
			return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
		} else {
			return new ResponseVO(HttpStatus.NOT_FOUND.value(), "Customer does not exist");
		}
	}

	private String generateMpinHash(String mpin) throws NoSuchAlgorithmException {
		return DigestUtils.sha256Hex(mpin);
	}

	@Override
	public ResponseVO updateMpin(UpdateMpinRequestVO updateMpinRequestVO) {
		if (updateMpinRequestVO != null && isValidMobileNumber(updateMpinRequestVO.getMobileNumber())
				&& isValidMpin(updateMpinRequestVO.getMpin())) {
			Optional<Customer> customer = customerRepository.findByMobileNumber(updateMpinRequestVO.getMobileNumber());
			String mobilePin = updateMpinRequestVO.getMpin();
			return saveMpin(customer, mobilePin);
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Mobile number or OTP entered is incorrect");
		}
	}

	@Override
	public ResponseVO saveCustomer(SaveCustomerRequestVO saveCustomerRequestVO) {
		Customer customer = new Customer();
		customer.setName(saveCustomerRequestVO.getName());
		customer.setMobileNumber(saveCustomerRequestVO.getMobileNumber());
		customerRepository.save(customer);
		return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
	}
}
