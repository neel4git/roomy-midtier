package com.Roomy.domain;

import com.Roomy.Request.Domain.UserRegistrationRequest;


public class SourceKeyRing {
	private UserRegistrationRequest registrationRequest;
	private int otp;
	private String otpIssuedTime;

	

	public UserRegistrationRequest getRegistrationRequest() {
		return registrationRequest;
	}

	public void setRegistrationRequest(UserRegistrationRequest registrationRequest) {
		this.registrationRequest = registrationRequest;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public String getOtpIssuedTime() {
		return otpIssuedTime;
	}

	public void setOtpIssuedTime(String string) {
		this.otpIssuedTime = string;
	}

}
