package com.Roomy.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.omg.CORBA.ServerRequest;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity(name = "User_Master")
public class UserMaster implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@Column(name = "USERID", unique = true)
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
	private String userType = "NonCorporate";

	@JsonIgnore
	@Column(name = "Login_Password", nullable = true)
	private String loginPassword;

	@Transient
	@OneToOne(mappedBy = "userMaster")	
	private User_Info userInfo;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public User_Info getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User_Info userInfo) {
		this.userInfo = userInfo;
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

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

}