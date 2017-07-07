package com.Roomy.Request.Dto;

public class AuthRequest {

	String firstName;
	String contactNumber;
	int Otp;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public int getOtp() {
		return Otp;
	}

	public void setOtp(int otp) {
		Otp = otp;
	}

}
