package com.oneabc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;

public final class ValidationUtils {
	private ValidationUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

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
}
