package com.Roomy.Request.Domain;

import java.io.Serializable;

public class ForgetPassword implements Serializable{
	
	private String password;
	private String mobilenNumber;
	private String emailId;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobilenNumber() {
		return mobilenNumber;
	}
	public void setMobilenNumber(String mobilenNumber) {
		this.mobilenNumber = mobilenNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	
	
	
	

}
