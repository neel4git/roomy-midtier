package com.Roomy.Request.Domain;

import java.io.Serializable;

public class UserRegistrationRequest implements Serializable {

	String registrationType;
	String emailId;
	String conactNumber;
	String pasword;
	String Name;
	String userType;
	public String getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getConactNumber() {
		return conactNumber;
	}
	public void setConactNumber(String conactNumber) {
		this.conactNumber = conactNumber;
	}
	public String getPasword() {
		return pasword;
	}
	public void setPasword(String pasword) {
		this.pasword = pasword;
	}

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
}
