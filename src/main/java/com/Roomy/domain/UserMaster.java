package com.Roomy.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "User_Master")
public class UserMaster implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@Column(name = "userid", unique = true)
	private String userId;

	@Column(name = "emailaddress")
	private String emailAddress;

	@Column(name = "contactnumber")
	private String contactNumber;

	@Column(name = "firstname", nullable = true)
	private String firstName;

	@Column(name = "middlename", nullable = true)
	private String middleName;

	@Column(name = "lastname", nullable = true)
	private String lastName;

	@Column(name = "usertype", nullable = true)
	private String userType;

	@Column(name = "loginpassword", nullable = true)
	private String loginPassword;

	@Column(name = "createdon", nullable = true)
	private String createdOn;

	@Column(name = "passwordchangedon", nullable = true)
	private String passwordChangedOn;

	@Transient
	private String customerToken;

	public String getCustomerToken() {
		return customerToken;
	}

	public void setCustomerToken(String customerToken) {
		this.customerToken = customerToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getPasswordChangedOn() {
		return passwordChangedOn;
	}

	public void setPasswordChangedOn(String passwordChangedOn) {
		this.passwordChangedOn = passwordChangedOn;
	}

}