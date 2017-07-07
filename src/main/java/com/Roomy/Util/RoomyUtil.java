package com.Roomy.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class RoomyUtil {

	private static final int LENGTHOFOTP = 6;

	public static Long getOtpIssueTimeDiffrence(String otpIssueTime)
			throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		Date currentDate = new Date();
		Date issuedDate = dateFormat.parse(otpIssueTime);
		long duration = currentDate.getTime() - issuedDate.getTime();
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		return diffInMinutes;
	}

	public static int generateOTP() {
		// Using numeric values
		String numbers = "0123456789";
		// Using random method
		Random rndm_method = new Random();
		char[] otp = new char[LENGTHOFOTP];
		for (int i = 0; i < LENGTHOFOTP; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		int generatedOTP = Integer.parseInt(new String(otp));
		return generatedOTP;
	}
}
