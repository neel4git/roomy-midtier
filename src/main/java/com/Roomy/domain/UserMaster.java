package com.Roomy.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Nationalized;

@Entity(name = "User_Master")
public class UserMaster implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@Column(name = "Id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column(name = "Email_Address")
	private String emailAddress;

	@Column(name = "Contact_Number", length = 1, columnDefinition = "CHAR")
	private String contactNumber;

	@Column(name = "First_Name", nullable = true)
	private String firstName;

	@Column(name = "Middle_Name", nullable = true)
	private String middleName;

	@Column(name = "Last_Name", nullable = true)
	private String lastName;

	@Column(name = "User_Type", nullable = true)
	private String userType="NonCorporate";

	@Nationalized
	@Column(name = "Login_Password", nullable = true)
	private String loginPassword;

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

}