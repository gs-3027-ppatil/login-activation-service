package com.oneabc.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.oneabc.api.model.CreateMpinRequestVO;
import com.oneabc.api.model.LoginResponseVO;
import com.oneabc.api.model.OtpResponseVO;
import com.oneabc.api.model.OtpVerificationRequestVO;
import com.oneabc.api.model.ResponseVO;
import com.oneabc.api.model.UpdateMpinRequestVO;
import com.oneabc.constant.ResponseEnum;
import com.oneabc.entity.Customer;
import com.oneabc.entity.PinMgt;
import com.oneabc.exception.CustomException;
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
		if (Strings.isBlank(mobileNumber)) {
			throw new CustomException(ResponseEnum.EMPTY_MOBILE_NUMBER.getStatusCode(),
					ResponseEnum.EMPTY_MOBILE_NUMBER.getMessage());
		}
		if (ValidationUtils.isValidMobileNumber(mobileNumber)) {
			return createLoginResponse(ResponseEnum.SUCCESS.getStatusCode(), ResponseEnum.SUCCESS.getMessage(),
					"123456");
		} else {
			throw new CustomException(ResponseEnum.INVALID_MOBILE_NUMBER.getStatusCode(),
					ResponseEnum.INVALID_MOBILE_NUMBER.getMessage());
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
		if (otpVerificationRequestVO != null) {
			if (otpVerificationRequestVO.getOtp().equals("123456")) {
				return new ResponseVO(ResponseEnum.SUCCESS.getStatusCode(), ResponseEnum.SUCCESS.getMessage());
			} else {
				return new ResponseVO(ResponseEnum.INVALID_OTP.getStatusCode(), ResponseEnum.INVALID_OTP.getMessage());
			}
		} else {
			return new ResponseVO(ResponseEnum.INVALID_REQUEST_BODY.getStatusCode(),
					ResponseEnum.INVALID_REQUEST_BODY.getMessage());
		}
	}

	@Override
	public ResponseVO setMpin(CreateMpinRequestVO createMpinRequestVO) {
		if (createMpinRequestVO != null) {
			Optional<Customer> customer = customerRepository.findById(createMpinRequestVO.getCustomerId());
			String mobilePin = createMpinRequestVO.getMpin();
			return saveMpin(customer, mobilePin);
		} else {
			return new ResponseVO(ResponseEnum.INVALID_REQUEST_BODY.getStatusCode(),
					ResponseEnum.INVALID_REQUEST_BODY.getMessage());
		}
	}

	private ResponseVO saveMpin(Optional<Customer> customer, String mpin) {
		if (customer.isPresent()) {
			Customer customerFromDB = customer.get();
			try {
				PinMgt mpinFromDB = new PinMgt();
				mpinFromDB.setCurrentMpin(mpin);
				mpinFromDB
						.setCreatedBy(getCustomerFullName(customerFromDB.getFirstName(), customerFromDB.getLastName()));
				mpinFromDB.setCreatedDate(new Date());
				mpinFromDB.setMpinExpiry(getMpinExpiryDate());
				mpinFromDB.setCustomer(customerFromDB);
				mpinFromDB.setActive(true);
				pinMgtRepository.save(mpinFromDB);
				return new ResponseVO(ResponseEnum.SUCCESS.getStatusCode(), ResponseEnum.SUCCESS.getMessage());
			} catch (DataIntegrityViolationException e) {
				throw new CustomException(ResponseEnum.DUPLICATE_MPIN_ENTRY.getStatusCode(),
						ResponseEnum.DUPLICATE_MPIN_ENTRY.getMessage());
			}
		} else {
			return new ResponseVO(ResponseEnum.CUSTOMER_NOT_FOUND.getStatusCode(),
					ResponseEnum.CUSTOMER_NOT_FOUND.getMessage());
		}
	}

	@Override
	public ResponseVO updateMpin(UpdateMpinRequestVO updateMpinRequestVO) {
		if (updateMpinRequestVO != null) {
			Optional<Customer> customer = customerRepository.findByMobileNumber(updateMpinRequestVO.getMobileNumber());
			String mobilePin = updateMpinRequestVO.getMpin();
			return updateMpin(customer, mobilePin);
		} else {
			return new ResponseVO(ResponseEnum.INVALID_REQUEST_BODY.getStatusCode(),
					ResponseEnum.INVALID_REQUEST_BODY.getMessage());
		}
	}

	private ResponseVO updateMpin(Optional<Customer> customer, String mpin) {
		if (customer.isPresent()) {
			Customer customerFromDB = customer.get();
			if (customerFromDB.getMpin() != null) {
				pinMgtRepository.updateMpin(mpin,
						getCustomerFullName(customerFromDB.getFirstName(), customerFromDB.getLastName()), new Date(),
						customerFromDB.getMpin().getId());
				return new ResponseVO(ResponseEnum.SUCCESS.getStatusCode(), ResponseEnum.SUCCESS.getMessage());
			} else {
				return new ResponseVO(ResponseEnum.MPIN_NOT_FOUND.getStatusCode(),
						ResponseEnum.MPIN_NOT_FOUND.getMessage());
			}
		} else {
			return new ResponseVO(ResponseEnum.CUSTOMER_NOT_FOUND.getStatusCode(),
					ResponseEnum.CUSTOMER_NOT_FOUND.getMessage());
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

	@Override
	public LoginResponseVO login(String mobileNumber) {
		if (Strings.isBlank(mobileNumber)) {
			throw new CustomException(ResponseEnum.EMPTY_MOBILE_NUMBER.getStatusCode(),
					ResponseEnum.EMPTY_MOBILE_NUMBER.getMessage());
		}
		if (ValidationUtils.isValidMobileNumber(mobileNumber)) {
			Optional<Customer> customer = customerRepository.findByMobileNumber(mobileNumber);
			if (customer.isPresent()) {
				return validateMpin(customer.get());
			} else {
				return new LoginResponseVO(ResponseEnum.CUSTOMER_NOT_FOUND.getStatusCode(),
						ResponseEnum.CUSTOMER_NOT_FOUND.getMessage(), null);
			}
		} else {
			throw new CustomException(ResponseEnum.INVALID_MOBILE_NUMBER.getStatusCode(),
					ResponseEnum.INVALID_MOBILE_NUMBER.getMessage());
		}
	}

	private LoginResponseVO validateMpin(Customer customerFromDB) {
		if (customerFromDB.getMpin() != null && !Strings.isBlank(customerFromDB.getMpin().getCurrentMpin())) {
			Date mpinExpiry = customerFromDB.getMpin().getMpinExpiry();
			Date currentDate = new Date();
			if (mpinExpiry != null && currentDate.compareTo(mpinExpiry) < 0) {
				return new LoginResponseVO(ResponseEnum.SUCCESS.getStatusCode(), ResponseEnum.SUCCESS.getMessage(),
						customerFromDB.getMpin().getCurrentMpin());
			} else {
				return new LoginResponseVO(ResponseEnum.MPIN_EXPIRED.getStatusCode(),
						ResponseEnum.MPIN_EXPIRED.getMessage(), null);
			}
		} else {
			return new LoginResponseVO(ResponseEnum.MPIN_NOT_FOUND.getStatusCode(),
					ResponseEnum.MPIN_NOT_FOUND.getMessage(), null);
		}
	}
}
