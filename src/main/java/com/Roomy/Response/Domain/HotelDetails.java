package com.Roomy.Response.Domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

public class HotelDetails implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	private int hotelId;
	private String hotelName;
	private String logo;
	private String hotelDescription;
	private List<String> amenities;
	private String stars;
	private Float ratePerMin;
	private int MinBooking;
	private Map<String, String> address;
	private String phone;
	private String email;
	private String website;
	private List<String> pics;

	public int getHotelId() {
		return hotelId;
	}

	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getHotelDescription() {
		return hotelDescription;
	}

	public void setHotelDescription(String hotelDescription) {
		this.hotelDescription = hotelDescription;
	}

	public List<String> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<String> amenities) {
		this.amenities = amenities;
	}

	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}

	public Float getRatePerMin() {
		return ratePerMin;
	}

	public void setRatePerMin(Float ratePerMin) {
		this.ratePerMin = ratePerMin;
	}

	public int getMinBooking() {
		return MinBooking;
	}

	public void setMinBooking(int minBooking) {
		MinBooking = minBooking;
	}

	public Map<String, String> getAddress() {
		return address;
	}

	public void setAddress(Map<String, String> address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<String> getPics() {
		return pics;
	}

	public void setPics(List<String> pics) {
		this.pics = pics;
	}

}
