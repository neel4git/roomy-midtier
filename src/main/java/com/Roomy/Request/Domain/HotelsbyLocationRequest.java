package com.Roomy.Request.Domain;

public class HotelsbyLocationRequest {
	private float user_Latitude;
	private float user_Longitude;
	private String jwtToken;
	public float getUser_Latitude() {
		return user_Latitude;
	}
	public void setUser_Latitude(float user_Latitude) {
		this.user_Latitude = user_Latitude;
	}
	public float getUser_Longitude() {
		return user_Longitude;
	}
	public void setUser_Longitude(float user_Longitude) {
		this.user_Longitude = user_Longitude;
	}
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	

}
