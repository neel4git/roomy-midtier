package com.Roomy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity(name = "USER_INFO")
public class User_Info implements Serializable {
	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@Column(name = "USERID", unique = true)
	private int userId;

	@Column(name = "IDENTITY_CARD_TYPE")
	private String identityCardType;

	@Column(name = "IDENTITY_CARD_NUMBER")
	private String identityCardNumber;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "MEMBERSHIP_TYPE")
	private String membershipType;

	@Column(name = "EMERGENCY_CONTECT_NUMBER_1")
	private String emergencyContectNumber_1;

	@Column(name = "EMERGENCY_CONTECT_NUMBER_2")
	private String emergencyContectNumber_2;

	@Column(name = "CITY_PREFERENCE")
	private String cityPreference;

	@Column(name = "SMS_NOTIFICATION_PREFERENCE")
	private String smsNotification;

	@Column(name = "EMAIL_NOTIFICATION_PREFERENCE")
	private String emailNotification;

	@Column(name = "USER_STATUS")
	private String userStatus;

	@Column(name = "USER_TOKEN_VALUE")
	private String customerToken;

	@Column(name = "USER_TOKEN_CREATED_ON")
	private Date customerTokenUpdatedDate;

	@OneToOne
	@JoinColumn(name = "USERID")
	private UserMaster userMaster;

	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getIdentityCardType() {
		return identityCardType;
	}

	public void setIdentityCardType(String identityCardType) {
		this.identityCardType = identityCardType;
	}

	public String getIdentityCardNumber() {
		return identityCardNumber;
	}

	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMembershipType() {
		return membershipType;
	}

	public void setMembershipType(String membershipType) {
		this.membershipType = membershipType;
	}

	public String getEmergencyContectNumber_1() {
		return emergencyContectNumber_1;
	}

	public void setEmergencyContectNumber_1(String emergencyContectNumber_1) {
		this.emergencyContectNumber_1 = emergencyContectNumber_1;
	}

	public String getEmergencyContectNumber_2() {
		return emergencyContectNumber_2;
	}

	public void setEmergencyContectNumber_2(String emergencyContectNumber_2) {
		this.emergencyContectNumber_2 = emergencyContectNumber_2;
	}

	public String getCityPreference() {
		return cityPreference;
	}

	public void setCityPreference(String cityPreference) {
		this.cityPreference = cityPreference;
	}

	public String getSmsNotification() {
		return smsNotification;
	}

	public void setSmsNotification(String smsNotification) {
		this.smsNotification = smsNotification;
	}

	public String getEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(String emailNotification) {
		this.emailNotification = emailNotification;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getCustomerToken() {
		return customerToken;
	}

	public void setCustomerToken(String customerToken) {
		this.customerToken = customerToken;
	}

	public Date getCustomerTokenUpdatedDate() {
		return customerTokenUpdatedDate;
	}

	public void setCustomerTokenUpdatedDate(Date customerTokenUpdatedDate) {
		this.customerTokenUpdatedDate = customerTokenUpdatedDate;
	}
}
