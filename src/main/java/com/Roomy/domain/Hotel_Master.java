package com.Roomy.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity(name = "Hotel_Master")
public class Hotel_Master implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@Column(name = "Hotel_Id", unique = true)
	private int hotel_Id;

	@Column(name = "Hotel_Name")
	private String hotel_Name;

	@Column(name = "Hotel_Description")
	private String hotel_Description;

	@Column(name = "Longitude")
	private double longitude;

	@Column(name = "Latitude")
	private double latitude;

	@Column(name = "Hotel_Address")
	private String hotel_Address;

	@Column(name = "Hotel_Location")
	private String hotel_Location;

	@Column(name = "HOTEL_CITY")
	private String hotel_City;

	@Column(name = "HOTEL_STATE")
	private String hotel_State;

	@Column(name = "HOTEL_COUNTRY")
	private String hotel_Country;

	@Column(name = "PIN_CODE", columnDefinition = "char(6)")
	private String pin_Code;

	@Column(name = "WEBSITE")
	private String web_site;

	@Column(name = "CONTACT_NUMBER_1")
	private String contact_No1;

	@Column(name = "CONTACT_NUMBER_2")
	private String contact_No2;

	@Column(name = "CONTACT_NUMBER_3")
	private String contact_No3;

	@OneToMany(mappedBy = "hotelMaster", cascade = CascadeType.ALL, targetEntity = Hotel_Pricing_Info.class, fetch = FetchType.EAGER)
	private List<Hotel_Pricing_Info> pricinginfo;

	public List<Hotel_Pricing_Info> getPricinginfo() {
		return pricinginfo;
	}

	public void setPricinginfo(List<Hotel_Pricing_Info> pricinginfo) {
		this.pricinginfo = pricinginfo;
	}

	public int getHotel_Id() {
		return hotel_Id;
	}

	public void setHotel_Id(int hotel_Id) {
		this.hotel_Id = hotel_Id;
	}

	public String getHotel_Name() {
		return hotel_Name;
	}

	public void setHotel_Name(String hotel_Name) {
		this.hotel_Name = hotel_Name;
	}

	public String getHotel_Description() {
		return hotel_Description;
	}

	public void setHotel_Description(String hotel_Description) {
		this.hotel_Description = hotel_Description;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getHotel_Address() {
		return hotel_Address;
	}

	public void setHotel_Address(String hotel_Address) {
		this.hotel_Address = hotel_Address;
	}

	public String getHotel_Location() {
		return hotel_Location;
	}

	public void setHotel_Location(String hotel_Location) {
		this.hotel_Location = hotel_Location;
	}

	public String getHotel_City() {
		return hotel_City;
	}

	public void setHotel_City(String hotel_City) {
		this.hotel_City = hotel_City;
	}

	public String getHotel_State() {
		return hotel_State;
	}

	public void setHotel_State(String hotel_State) {
		this.hotel_State = hotel_State;
	}

	public String getHotel_Country() {
		return hotel_Country;
	}

	public void setHotel_Country(String hotel_Country) {
		this.hotel_Country = hotel_Country;
	}

	public String getPin_Code() {
		return pin_Code;
	}

	public void setPin_Code(String pin_Code) {
		this.pin_Code = pin_Code;
	}

	public String getWeb_site() {
		return web_site;
	}

	public void setWeb_site(String web_site) {
		this.web_site = web_site;
	}

	public String getContact_No1() {
		return contact_No1;
	}

	public void setContact_No1(String contact_No1) {
		this.contact_No1 = contact_No1;
	}

	public String getContact_No2() {
		return contact_No2;
	}

	public void setContact_No2(String contact_No2) {
		this.contact_No2 = contact_No2;
	}

	public String getContact_No3() {
		return contact_No3;
	}

	public void setContact_No3(String contact_No3) {
		this.contact_No3 = contact_No3;
	}

}
