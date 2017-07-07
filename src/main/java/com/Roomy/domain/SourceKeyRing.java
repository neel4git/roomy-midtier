package com.Roomy.domain;


public class SourceKeyRing {
	private UserMaster userMaster;
	private int otp;
	private String otpIssuedTime;

	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
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
