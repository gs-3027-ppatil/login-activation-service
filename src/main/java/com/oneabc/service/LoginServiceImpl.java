package com.oneabc.service;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oneabc.api.model.CreateMpinRequestVO;
import com.oneabc.api.model.OtpResponseVO;
import com.oneabc.api.model.OtpVerificationRequestVO;
import com.oneabc.api.model.ResponseVO;
import com.oneabc.api.model.UpdateMpinRequestVO;
import com.oneabc.model.Customer;
import com.oneabc.model.PinMgt;
import com.oneabc.repository.CustomerRepository;
import com.oneabc.repository.PinMgtRepository;
import com.oneabc.util.ValidationUtils;

@Service
public class LoginServiceImpl implements LoginService {

	@Value("${mpin.expiry.months}")
	private int expiryInMonths;

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private PinMgtRepository pinMgtRepository;

	@Override
	public OtpResponseVO sendOtp(String mobileNumber) {
		if (ValidationUtils.isValidMobileNumber(mobileNumber)) {
			OtpResponseVO res = createLoginResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "123456");
			return res;
		} else {
			OtpResponseVO res = createLoginResponse(HttpStatus.BAD_REQUEST.value(), "Please enter valid mobile number",
					null);
			return res;
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
		if (otpVerificationRequestVO != null && ValidationUtils.isValidOtp(otpVerificationRequestVO.getOtp())
				&& ValidationUtils.isValidMobileNumber(otpVerificationRequestVO.getMobileNumber())) {
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
		if (createMpinRequestVO != null && ValidationUtils.isValidMpin(createMpinRequestVO.getMpin())) {
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

			Customer customerFromDB = customer.get();
			try {
				PinMgt mpin = new PinMgt();
				mpin.setCurrentMpin(hashedMpin);
				mpin.setCreatedBy(getCustomerFullName(customerFromDB.getFirstName(), customerFromDB.getLastName()));
				mpin.setCreatedDate(new Date());
				mpin.setMpinExpiry(getMpinExpiryDate());
				mpin.setCustomer(customerFromDB);
				mpin.setActive(true);
				pinMgtRepository.save(mpin);
			} catch (DataIntegrityViolationException e) {
				return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Duplicate entry. MPIN already set for customerId : " + customerFromDB.getId());
			}
			return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
		} else {
			return new ResponseVO(HttpStatus.NOT_FOUND.value(), "Customer does not exist");
		}
	}

	@Override
	public ResponseVO updateMpin(UpdateMpinRequestVO updateMpinRequestVO) {
		if (updateMpinRequestVO != null && ValidationUtils.isValidMobileNumber(updateMpinRequestVO.getMobileNumber())
				&& ValidationUtils.isValidMpin(updateMpinRequestVO.getMpin())) {
			Optional<Customer> customer = customerRepository.findByMobileNumber(updateMpinRequestVO.getMobileNumber());
			String mobilePin = updateMpinRequestVO.getMpin();
			return updateMpin(customer, mobilePin);
		} else {
			return new ResponseVO(HttpStatus.BAD_REQUEST.value(), "Mobile number or OTP entered is incorrect");
		}
	}

	private ResponseVO updateMpin(Optional<Customer> customer, String mobilePin) {
		if (customer.isPresent()) {
			String hashedMpin = "";
			try {
				hashedMpin = generateMpinHash(mobilePin);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid hashing algorithm");
			}

			Customer customerFromDB = customer.get();
			if (customerFromDB.getMpin() != null) {
				pinMgtRepository.updateMpin(hashedMpin,
						getCustomerFullName(customerFromDB.getFirstName(), customerFromDB.getLastName()), new Date(),
						customerFromDB.getMpin().getId());
				return new ResponseVO(HttpStatus.OK.value(), "SUCCESS");
			} else {
				return new ResponseVO(HttpStatus.NOT_FOUND.value(), "Failed to update MPIN, no entry found");
			}
		} else {
			return new ResponseVO(HttpStatus.NOT_FOUND.value(), "Customer does not exist");
		}
	}

	private String getCustomerFullName(String firstName, String lastName) {
		return firstName + " " + lastName;
	}

	private Date getMpinExpiryDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, expiryInMonths);
		return c.getTime();
	}

	private String generateMpinHash(String mpin) throws NoSuchAlgorithmException {
		return DigestUtils.sha256Hex(mpin);
	}
}
