package com.Roomy.Response.Domain;

import java.io.Serializable;

import javax.persistence.Transient;

public class UseRDetails implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	private Object response;
	private Object userID;
	private Object contactNumber;
	private Object emailAddress;
	private Object first_Name;
	private Object midle_Name;
	private Object last_Name;
	private Object user_type;
	private Object memberShip_type;
	private Object identityCardType;
	private Object identityCardNumber;
	private Object companyName;
	private Object emergencyContactNumber1;
	private Object emergencyContactNumber2;
	private Object dateOfBirth;
	private Object cityPrefrence;
	private Object smsNotificationPrefrences;
	private Object emailNotificationPrefrences;
	private Object userStatus;
	private Object userTokenValue;

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public Object getUserID() {
		return userID;
	}

	public void setUserID(Object userID) {
		this.userID = userID;
	}

	public Object getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Object contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Object getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(Object emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Object getFirst_Name() {
		return first_Name;
	}

	public void setFirst_Name(Object first_Name) {
		this.first_Name = first_Name;
	}

	public Object getMidle_Name() {
		return midle_Name;
	}

	public void setMidle_Name(Object midle_Name) {
		this.midle_Name = midle_Name;
	}

	public Object getLast_Name() {
		return last_Name;
	}

	public void setLast_Name(Object last_Name) {
		this.last_Name = last_Name;
	}

	public Object getUser_type() {
		return user_type;
	}

	public void setUser_type(Object user_type) {
		this.user_type = user_type;
	}

	public Object getMemberShip_type() {
		return memberShip_type;
	}

	public void setMemberShip_type(Object memberShip_type) {
		this.memberShip_type = memberShip_type;
	}

	public Object getIdentityCardType() {
		return identityCardType;
	}

	public void setIdentityCardType(Object identityCardType) {
		this.identityCardType = identityCardType;
	}

	public Object getIdentityCardNumber() {
		return identityCardNumber;
	}

	public void setIdentityCardNumber(Object identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}

	public Object getCompanyName() {
		return companyName;
	}

	public void setCompanyName(Object companyName) {
		this.companyName = companyName;
	}

	public Object getEmergencyContactNumber1() {
		return emergencyContactNumber1;
	}

	public void setEmergencyContactNumber1(Object emergencyContactNumber1) {
		this.emergencyContactNumber1 = emergencyContactNumber1;
	}

	public Object getEmergencyContactNumber2() {
		return emergencyContactNumber2;
	}

	public void setEmergencyContactNumber2(Object emergencyContactNumber2) {
		this.emergencyContactNumber2 = emergencyContactNumber2;
	}

	public Object getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Object dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Object getCityPrefrence() {
		return cityPrefrence;
	}

	public void setCityPrefrence(Object cityPrefrence) {
		this.cityPrefrence = cityPrefrence;
	}

	public Object getSmsNotificationPrefrences() {
		return smsNotificationPrefrences;
	}

	public void setSmsNotificationPrefrences(Object smsNotificationPrefrences) {
		this.smsNotificationPrefrences = smsNotificationPrefrences;
	}

	public Object getEmailNotificationPrefrences() {
		return emailNotificationPrefrences;
	}

	public void setEmailNotificationPrefrences(Object emailNotificationPrefrences) {
		this.emailNotificationPrefrences = emailNotificationPrefrences;
	}

	public Object getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Object userStatus) {
		this.userStatus = userStatus;
	}

	public Object getUserTokenValue() {
		return userTokenValue;
	}

	public void setUserTokenValue(Object userTokenValue) {
		this.userTokenValue = userTokenValue;
	}

}
