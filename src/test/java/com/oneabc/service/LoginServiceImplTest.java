package com.oneabc.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

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

@SpringBootTest
public class LoginServiceImplTest {
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private PinMgtRepository pinMgtRepository;
	@InjectMocks
	private LoginServiceImpl loginServiceImpl;

	@Test
	public void sendOtpTest() {
		OtpResponseVO response = loginServiceImpl.sendOtp("1234567890");

		Assertions.assertEquals(ResponseEnum.SUCCESS.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.SUCCESS.getMessage(), response.getMessage());
		Assertions.assertEquals("123456", response.getOtp());
	}

	@Test
	public void verifyOtpTest() {
		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
		request.setMobileNumber("1234567890");
		request.setOtp("123456");

		ResponseVO response = loginServiceImpl.verifyOtp(request);

		Assertions.assertEquals(ResponseEnum.SUCCESS.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.SUCCESS.getMessage(), response.getMessage());
	}

	@Test
	public void verifyIncorrectOtpTest() {
		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
		request.setMobileNumber("1234567890");
		request.setOtp("111111");

		ResponseVO response = loginServiceImpl.verifyOtp(request);

		Assertions.assertEquals(ResponseEnum.INVALID_OTP.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.INVALID_OTP.getMessage(), response.getMessage());
	}

	@Test
	public void verifyInvalidOtpTest() {
		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
		request.setMobileNumber("1234567890");
		request.setOtp("12345");

		ResponseVO response = loginServiceImpl.verifyOtp(request);

		Assertions.assertEquals(ResponseEnum.INVALID_OTP.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.INVALID_OTP.getMessage(), response.getMessage());
	}

	@Test
	public void setMpinTest() {
		CreateMpinRequestVO request = new CreateMpinRequestVO();
		request.setCustomerId(1L);
		request.setMpin("1234");

		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Parker");
		customer.setId(1);
		when(customerRepository.findById(request.getCustomerId())).thenReturn(Optional.of(customer));
		when(pinMgtRepository.save(any(PinMgt.class))).thenReturn(new PinMgt());

		ResponseVO response = loginServiceImpl.setMpin(request);

		Assertions.assertEquals(ResponseEnum.SUCCESS.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.SUCCESS.getMessage(), response.getMessage());
	}

	@Test
	public void setMpinWhenCustomerNotPresentTest() {
		CreateMpinRequestVO request = new CreateMpinRequestVO();
		request.setCustomerId(1L);
		request.setMpin("1234");

		when(customerRepository.findById(request.getCustomerId())).thenReturn(Optional.empty());

		ResponseVO response = loginServiceImpl.setMpin(request);

		Assertions.assertEquals(ResponseEnum.CUSTOMER_NOT_FOUND.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.CUSTOMER_NOT_FOUND.getMessage(), response.getMessage());
	}

	@Test
	public void setMpinForCustomerWithExistingMpinTest() {
		CreateMpinRequestVO request = new CreateMpinRequestVO();
		request.setCustomerId(1L);
		request.setMpin("1234");

		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Parker");
		customer.setId(1);
		when(customerRepository.findById(request.getCustomerId())).thenReturn(Optional.of(customer));
		when(pinMgtRepository.save(any(PinMgt.class))).thenThrow(DataIntegrityViolationException.class);

		CustomException exception = Assertions.assertThrows(CustomException.class,
				() -> loginServiceImpl.setMpin(request));

		Assertions.assertEquals(ResponseEnum.DUPLICATE_MPIN_ENTRY.getStatusCode(), exception.getStatusCode());
		Assertions.assertEquals(ResponseEnum.DUPLICATE_MPIN_ENTRY.getMessage(), exception.getMessage());
	}

	@Test
	public void updateMpinTest() {
		UpdateMpinRequestVO request = new UpdateMpinRequestVO();
		request.setMobileNumber("1234567890");
		request.setMpin("1234");

		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Parker");
		customer.setId(1);
		PinMgt pinMgt = new PinMgt();
		pinMgt.setCustomer(customer);
		pinMgt.setId(1);
		customer.setMpin(pinMgt);
		when(customerRepository.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.of(customer));
		Mockito.doNothing().when(pinMgtRepository).updateMpin(anyString(), anyString(), any(Date.class), anyLong());

		ResponseVO response = loginServiceImpl.updateMpin(request);

		Assertions.assertEquals(ResponseEnum.SUCCESS.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.SUCCESS.getMessage(), response.getMessage());
	}

	@Test
	public void updateMpinWhenCustomerNotPresentTest() {
		UpdateMpinRequestVO request = new UpdateMpinRequestVO();
		request.setMobileNumber("1234567890");
		request.setMpin("1234");

		when(customerRepository.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.empty());

		ResponseVO response = loginServiceImpl.updateMpin(request);

		Assertions.assertEquals(ResponseEnum.CUSTOMER_NOT_FOUND.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.CUSTOMER_NOT_FOUND.getMessage(), response.getMessage());
	}

	@Test
	public void updateMpinWhenMpinIsNotSetAlreadyTest() {
		UpdateMpinRequestVO request = new UpdateMpinRequestVO();
		request.setMobileNumber("1234567890");
		request.setMpin("1234");

		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Parker");
		customer.setId(1);

		when(customerRepository.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.of(customer));

		ResponseVO response = loginServiceImpl.updateMpin(request);

		Assertions.assertEquals(ResponseEnum.MPIN_NOT_FOUND.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.MPIN_NOT_FOUND.getMessage(), response.getMessage());
	}

	@Test
	public void loginWithMpinTest() {
		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Parker");
		customer.setId(1);
		PinMgt pinMgt = new PinMgt();
		pinMgt.setCustomer(customer);
		pinMgt.setId(1);
		pinMgt.setCurrentMpin("1234");
		pinMgt.setMpinExpiry(setMpinExpiryDate(1));
		customer.setMpin(pinMgt);
		when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(customer));

		LoginResponseVO response = loginServiceImpl.login("1234567890");

		Assertions.assertEquals(ResponseEnum.SUCCESS.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.SUCCESS.getMessage(), response.getMessage());
	}

	@Test
	public void loginWithExpiredMpinTest() {
		ResponseVO expectedResponse = new ResponseVO();
		expectedResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
		expectedResponse.setMessage("MPIN expired");
		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Parker");
		customer.setId(1);
		PinMgt pinMgt = new PinMgt();
		pinMgt.setCustomer(customer);
		pinMgt.setId(1);
		pinMgt.setCurrentMpin("1234");
		pinMgt.setMpinExpiry(setMpinExpiryDate(-1));
		customer.setMpin(pinMgt);
		when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(customer));

		LoginResponseVO response = loginServiceImpl.login("1234567890");

		Assertions.assertEquals(ResponseEnum.MPIN_EXPIRED.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.MPIN_EXPIRED.getMessage(), response.getMessage());
	}

	@Test
	public void loginWhenMpinNotSetTest() {
		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Parker");
		customer.setId(1);
		PinMgt pinMgt = new PinMgt();
		pinMgt.setCustomer(customer);
		pinMgt.setId(1);
		pinMgt.setMpinExpiry(setMpinExpiryDate(1));
		customer.setMpin(pinMgt);
		when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(customer));

		LoginResponseVO response = loginServiceImpl.login("1234567890");

		Assertions.assertEquals(ResponseEnum.MPIN_NOT_FOUND.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.MPIN_NOT_FOUND.getMessage(), response.getMessage());
	}

	@Test
	public void loginWhenCustomerNotPresentTest() {
		when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.empty());

		LoginResponseVO response = loginServiceImpl.login("1234567890");

		Assertions.assertEquals(ResponseEnum.CUSTOMER_NOT_FOUND.getStatusCode(), response.getStatusCode());
		Assertions.assertEquals(ResponseEnum.CUSTOMER_NOT_FOUND.getMessage(), response.getMessage());
	}

	@Test
	public void loginWhenMpinMobileNumberNotValidTest() {
		CustomException exception = Assertions.assertThrows(CustomException.class,
				() -> loginServiceImpl.login("123456789"));

		Assertions.assertEquals(ResponseEnum.INVALID_MOBILE_NUMBER.getStatusCode(), exception.getStatusCode());
		Assertions.assertEquals(ResponseEnum.INVALID_MOBILE_NUMBER.getMessage(), exception.getMessage());
	}

	private Date setMpinExpiryDate(int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, days);
		return c.getTime();
	}
}
