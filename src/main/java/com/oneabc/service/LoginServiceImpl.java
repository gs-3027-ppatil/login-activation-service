package com.oneabc.service;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.oneabc.repository.PinMgtRepository;

@Service
public class LoginServiceImpl implements LoginService {
//	static int attemptCount = 0;
	@Value("${mpin.expiry.days}")
	private int expiryInDays;

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private PinMgtRepository pinMgtRepository;

	@Override
	public OtpResponseVO sendOtp(String mobileNumber) {

		if (isValidMobileNumber(mobileNumber)) {

//			if (attemptCount <= 2) {
			OtpResponseVO res = createLoginResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "123456");
//				attemptCount++;
//				throw new OtpServiceException(HttpStatus.NOT_IMPLEMENTED.value(), "ABC");
			return res;
//			} else {
//				OtpResponseVO res = createLoginResponse(HttpStatus.TOO_MANY_REQUESTS.value(),
//						"Too many attempts for OTP", null);
//				return res;
//			}
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
			if (otpVerificationRequestVO.getOtp().equals("123456")) {
				return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
			} else {
				return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Invalid OTP");
			}
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Mobile number or OTP entered is incorrect");
		}
	}

	@Override
	public ResponseVO setMpin(CreateMpinRequestVO createMpinRequestVO) {
		if (createMpinRequestVO != null && isValidMpin(createMpinRequestVO.getMpin())) {
			Optional<Customer> customer = customerRepository.findById(createMpinRequestVO.getCustomerId());
			String mobilePin = createMpinRequestVO.getMpin();
			return saveMpin(customer, mobilePin, false);
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Customer Id or OTP entered is incorrect");
		}
	}

	private ResponseVO saveMpin(Optional<Customer> customer, String mobilePin, boolean updateExistingMpin) {
		if (customer.isPresent()) {
			String hashedMpin = "";
			try {
				hashedMpin = generateMpinHash(mobilePin);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid hashing algorithm");
			}
			Customer customerFromDB = customer.get();
			// updateExistingMpin flag is added so that, user will not be able to set MPIN if he has
			// not set it once already.
			PinMgt mPin = customerFromDB.getMpin();
			if (updateExistingMpin && mPin != null) {
				pinMgtRepository.updateMpin(hashedMpin, customerFromDB.getName(), new Date(), mPin.getId());
			} else {
				if (!updateExistingMpin) {
					PinMgt mpin = new PinMgt();
					mpin.setCurrentMpin(hashedMpin);
					mpin.setCreatedBy(customerFromDB.getName());
					mpin.setCreatedDate(new Date());
					mpin.setMpinExpiry(getMpinExpiryDate());
					mpin.setCustomer(customerFromDB);
					mpin.setActive(true);
					pinMgtRepository.save(mpin);
				} else {
					return new ResponseVO(HttpStatus.NOT_FOUND.value(), "Failed to update MPIN, no entry found");
				}
			}
			return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
		} else {
			return new ResponseVO(HttpStatus.NOT_FOUND.value(), "Customer does not exist");
		}
	}

	private Date getMpinExpiryDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, expiryInDays);
		return c.getTime();
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
			return saveMpin(customer, mobilePin, true);
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
