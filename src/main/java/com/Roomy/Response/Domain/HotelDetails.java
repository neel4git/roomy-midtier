package com.Roomy.Response.Domain;

import java.io.Serializable;

import javax.persistence.Transient;

public class HotelDetails implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	private String hotelId;
	private String hotelName;

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

}
