package com.Roomy.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "Customer_Master")
public class CustomerMaster implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@Column(name = "customer_id", unique = true)
	private String customerid;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "customer_password")
	private String customerPassword;

	@Column(name = "SSO", nullable = true)
	private String SSO;

	@Column(name = "First_Name", nullable = true)
	private String firstName;

	@Column(name = "Middle_Name", nullable = true)
	private String middleName;

	@Column(name = "Last_Name", nullable = true)
	private String lastName;

	@Column(name = "Mobile_Number", nullable = true)
	private String Mobile_Number;

	@Column(name = "Email_Id", nullable = true)
	private String Email_Id;

	@Column(name = "ID_card_Type", nullable = true)
	private String ID_card_Type;

	@Column(name = "ID_card_Number", nullable = true)
	private String ID_card_Number;

	@Column(name = "City", nullable = true)
	private String City;

	@Column(name = "Country", nullable = true)
	private String Country;
	@Column(name = "User_Photo", nullable = true)
	private String User_Photo;

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPassword() {
		return customerPassword;
	}

	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}

	public String getSSO() {
		return SSO;
	}

	public void setSSO(String sSO) {
		SSO = sSO;
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

	public String getMobile_Number() {
		return Mobile_Number;
	}

	public void setMobile_Number(String mobile_Number) {
		Mobile_Number = mobile_Number;
	}

	public String getEmail_Id() {
		return Email_Id;
	}

	public void setEmail_Id(String email_Id) {
		Email_Id = email_Id;
	}

	public String getID_card_Type() {
		return ID_card_Type;
	}

	public void setID_card_Type(String iD_card_Type) {
		ID_card_Type = iD_card_Type;
	}

	public String getID_card_Number() {
		return ID_card_Number;
	}

	public void setID_card_Number(String iD_card_Number) {
		ID_card_Number = iD_card_Number;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getUser_Photo() {
		return User_Photo;
	}

	public void setUser_Photo(String user_Photo) {
		User_Photo = user_Photo;
	}

}