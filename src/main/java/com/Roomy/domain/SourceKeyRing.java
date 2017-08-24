package com.Roomy.domain;

import com.Roomy.Request.Domain.UserRequest;


public class SourceKeyRing {
	private UserRequest userRequest;
	private int otp;
	private String otpIssuedTime;

	
	public UserRequest getUserRequest() {
		return userRequest;
	}

	public void setUserRequest(UserRequest userRequest) {
		this.userRequest = userRequest;
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
