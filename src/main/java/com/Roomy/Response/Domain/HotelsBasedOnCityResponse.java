package com.Roomy.Response.Domain;

public class HotelsBasedOnCityResponse {	
	private String hotelName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String category;
	private String minCost;
	private String hourCost;
	private String img;
	private double lattitue;
	private double longitude;

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMinCost() {
		return minCost;
	}

	public void setMinCost(String minCost) {
		this.minCost = minCost;
	}

	public String getHourCost() {
		return hourCost;
	}

	public void setHourCost(String hourCost) {
		this.hourCost = hourCost;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public double getLattitue() {
		return lattitue;
	}

	public void setLattitue(double lattitue) {
		this.lattitue = lattitue;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
