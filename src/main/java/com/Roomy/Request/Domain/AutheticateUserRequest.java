package com.Roomy.Request.Domain;

public class AutheticateUserRequest {

	private String otp;
	private String customerToken;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getCustomerToken() {
		return customerToken;
	}

	public void setCustomerToken(String customerToken) {
		this.customerToken = customerToken;
	}

}
