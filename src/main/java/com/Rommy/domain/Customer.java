package com.Rommy.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "Customer")
public class Customer implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@GeneratedValue
	@Column(name = "customer_id", unique = true, nullable = false)
	private String customerid;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "customer_password")
	private String customerPassword;

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

}