package com.oneabc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;

public class ValidationUtils {
	public static boolean isValidMobileNumber(String mobileNumber) {
		if (Strings.isNotEmpty(mobileNumber)) {
			Pattern p = Pattern.compile("^\\d{10}$");
			Matcher m = p.matcher(mobileNumber);
			return m.matches();
		} else {
			return false;
		}
	}

	public static boolean isValidOtp(String otp) {
		if (Strings.isNotEmpty(otp)) {
			Pattern p = Pattern.compile("^\\d{6}$");
			Matcher m = p.matcher(otp);
			return m.matches();
		} else {
			return false;
		}
	}

	public static boolean isValidMpin(String mPin) {
		if (Strings.isNotEmpty(mPin)) {
			Pattern p = Pattern.compile("^\\d{4}$");
			Matcher m = p.matcher(mPin);
			return m.matches();
		} else {
			return false;
		}
	}
}
