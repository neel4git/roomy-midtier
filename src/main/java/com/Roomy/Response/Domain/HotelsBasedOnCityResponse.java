package com.Roomy.Response.Domain;

public class HotelsBasedOnCityResponse {
	private int hotelID;
	private String hotelName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private float minCost;
	private float hourCost;
	private double lattitue;
	private double longitude;
	private String category;
	private String img;
	private String currencyType;

	public int getHotelID() {
		return hotelID;
	}

	public void setHotelID(int hotelID) {
		this.hotelID = hotelID;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

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

	public float getMinCost() {
		return minCost;
	}

	public void setMinCost(float minCost) {
		this.minCost = minCost;
	}

	public float getHourCost() {
		return hourCost;
	}

	public void setHourCost(float hourCost) {
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
