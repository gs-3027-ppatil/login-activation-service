//package com.oneabc.service;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//import java.sql.Date;
//import java.util.Optional;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//
//import com.oneabc.api.model.CreateMpinRequestVO;
//import com.oneabc.api.model.OtpResponseVO;
//import com.oneabc.api.model.OtpVerificationRequestVO;
//import com.oneabc.api.model.ResponseVO;
//import com.oneabc.api.model.UpdateMpinRequestVO;
//import com.oneabc.model.Customer;
//import com.oneabc.model.PinMgt;
//import com.oneabc.repository.CustomerRepository;
//import com.oneabc.repository.PinMgtRepository;
//
//@SpringBootTest
//public class LoginServiceImplTest {
//	@Mock
//	private CustomerRepository customerRepository;
//	@Mock
//	private PinMgtRepository pinMgtRepository;
//	@InjectMocks
//	private LoginServiceImpl loginServiceImpl;
//
//	@Before
//	public void setup() {
//		MockitoAnnotations.initMocks(this);
//	}
//
//	@Test
//	public void sendOtpTest() {
//		OtpResponseVO expectedResponse = new OtpResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.OK.value());
//		expectedResponse.setMessage("SUCCESS");
//		expectedResponse.setOtp("123456");
//
//		OtpResponseVO response = loginServiceImpl.sendOtp("1234567890");
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//		Assertions.assertEquals(expectedResponse.getOtp(), response.getOtp());
//	}
//
//	@Test
//	public void sendOtpInvalidMobileNumberTest() {
//		OtpResponseVO expectedResponse = new OtpResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//		expectedResponse.setMessage("Please enter valid mobile number");
//
//		OtpResponseVO response = loginServiceImpl.sendOtp("123456789");
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void verifyOtpTest() {
//		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
//		request.setMobileNumber("1234567890");
//		request.setOtp("123456");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.OK.value());
//		expectedResponse.setMessage("SUCCESS");
//
//		ResponseVO response = loginServiceImpl.verifyOtp(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void verifyIncorrectOtpTest() {
//		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
//		request.setMobileNumber("1234567890");
//		request.setOtp("111111");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//		expectedResponse.setMessage("Invalid OTP");
//
//		ResponseVO response = loginServiceImpl.verifyOtp(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void verifyInvalidOtpTest() {
//		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
//		request.setMobileNumber("1234567890");
//		request.setOtp("12345");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//		expectedResponse.setMessage("Mobile number or OTP entered is incorrect");
//
//		ResponseVO response = loginServiceImpl.verifyOtp(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void verifyOtpInvalidMobileNumberTest() {
//		OtpVerificationRequestVO request = new OtpVerificationRequestVO();
//		request.setMobileNumber("123456789");
//		request.setOtp("123456");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//		expectedResponse.setMessage("Mobile number or OTP entered is incorrect");
//
//		ResponseVO response = loginServiceImpl.verifyOtp(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void setMpinTest() {
//		CreateMpinRequestVO request = new CreateMpinRequestVO();
//		request.setCustomerId(1L);
//		request.setMpin("1234");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.OK.value());
//		expectedResponse.setMessage("SUCCESS");
//		Customer customer = new Customer();
//		customer.setFirstName("John");
//		customer.setLastName("Parker");
//		customer.setId(1);
//		when(customerRepository.findById(request.getCustomerId())).thenReturn(Optional.of(customer));
//		when(pinMgtRepository.save(any(PinMgt.class))).thenReturn(new PinMgt());
//
//		ResponseVO response = loginServiceImpl.setMpin(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void setMpinInvalidMpinTest() {
//		CreateMpinRequestVO request = new CreateMpinRequestVO();
//		request.setCustomerId(1L);
//		request.setMpin("123");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//		expectedResponse.setMessage("Customer Id or OTP entered is incorrect");
//
//		ResponseVO response = loginServiceImpl.setMpin(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void setMpinWhenCustomerNotPresentTest() {
//		CreateMpinRequestVO request = new CreateMpinRequestVO();
//		request.setCustomerId(1L);
//		request.setMpin("1234");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
//		expectedResponse.setMessage("Customer does not exist");
//		when(customerRepository.findById(request.getCustomerId())).thenReturn(Optional.empty());
//
//		ResponseVO response = loginServiceImpl.setMpin(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void setMpinForCustomerWithExistingMpinTest() {
//		CreateMpinRequestVO request = new CreateMpinRequestVO();
//		request.setCustomerId(1L);
//		request.setMpin("1234");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		expectedResponse.setMessage("Duplicate entry. MPIN already set for customerId : 1");
//		Customer customer = new Customer();
//		customer.setFirstName("John");
//		customer.setLastName("Parker");
//		customer.setId(1);
//		when(customerRepository.findById(request.getCustomerId())).thenReturn(Optional.of(customer));
//		when(pinMgtRepository.save(any(PinMgt.class))).thenThrow(DataIntegrityViolationException.class);
//
//		ResponseVO response = loginServiceImpl.setMpin(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void updateMpinTest() {
//		UpdateMpinRequestVO request = new UpdateMpinRequestVO();
//		request.setMobileNumber("1234567890");
//		request.setMpin("1234");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.OK.value());
//		expectedResponse.setMessage("SUCCESS");
//		Customer customer = new Customer();
//		customer.setFirstName("John");
//		customer.setLastName("Parker");
//		customer.setId(1);
//		PinMgt pinMgt = new PinMgt();
//		pinMgt.setCustomer(customer);
//		pinMgt.setId(1);
//		customer.setMpin(pinMgt);
//		when(customerRepository.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.of(customer));
//		Mockito.doNothing().when(pinMgtRepository).updateMpin(anyString(), anyString(), any(Date.class), anyLong());
//
//		ResponseVO response = loginServiceImpl.updateMpin(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void updateMpinInvalidMobileNumberMpinTest() {
//		UpdateMpinRequestVO request = new UpdateMpinRequestVO();
//		request.setMobileNumber("123456789");
//		request.setMpin("12342");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
//		expectedResponse.setMessage("Mobile number or MPIN entered is incorrect");
//
//		ResponseVO response = loginServiceImpl.updateMpin(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void updateMpinWhenCustomerNotPresentTest() {
//		UpdateMpinRequestVO request = new UpdateMpinRequestVO();
//		request.setMobileNumber("1234567890");
//		request.setMpin("1234");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
//		expectedResponse.setMessage("Customer does not exist");
//
//		when(customerRepository.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.empty());
//
//		ResponseVO response = loginServiceImpl.updateMpin(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//
//	@Test
//	public void updateMpinWhenMpinIsNotSetAlreadyTest() {
//		UpdateMpinRequestVO request = new UpdateMpinRequestVO();
//		request.setMobileNumber("1234567890");
//		request.setMpin("1234");
//
//		ResponseVO expectedResponse = new ResponseVO();
//		expectedResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
//		expectedResponse.setMessage("Failed to update MPIN, no entry found");
//		Customer customer = new Customer();
//		customer.setFirstName("John");
//		customer.setLastName("Parker");
//		customer.setId(1);
//
//		when(customerRepository.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.of(customer));
//
//		ResponseVO response = loginServiceImpl.updateMpin(request);
//
//		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
//		Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
//	}
//}
