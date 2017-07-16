package com.Roomy.Response.Domain;

public class HotelsBasedOnCityResponse {

	private int hotel_Id;
	private String hotelName;
	private String address;
	private String city;
	private String state;
	private double lattitue;
	private double longitude;
	private float minCost;
	private float hourCost;
	private String currencyType;

	public int getHotel_Id() {
		return hotel_Id;
	}

	public void setHotel_Id(int hotel_Id) {
		this.hotel_Id = hotel_Id;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

}
