package com.Roomy.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity(name = "Hotel_Info")
public class Hotel_Info implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "hotel_Info")
	private Hotel_Master hotel_Master;

	@Id
	@Column(name = "Hotel_Id", unique = true)
	private int hotel_Id;

	@Column(name = "Hotel_Type")
	private String hotel_type;

	@Column(name = "MAXIMUM_OCCUPENCY")
	private int maximum_Occupency;

	@Column(name = "STAR_RATING_BY_HOTEL")
	private String star_Rating_By_Hotel;

	@Column(name = "HOTEL_PIC_PATH")
	private String hotelLogo;

	public String getHotelLogo() {
		return hotelLogo;
	}

	public void setHotelLogo(String hotelLogo) {
		this.hotelLogo = hotelLogo;
	}

	@Transient
	private String aminities;

	public String getAminities() {
		return aminities;
	}

	public void setAminities(String aminities) {
		this.aminities = aminities;
	}

	public int getHotel_Id() {
		return hotel_Id;
	}

	public void setHotel_Id(int hotel_Id) {
		this.hotel_Id = hotel_Id;
	}

	public String getHotel_type() {
		return hotel_type;
	}

	public void setHotel_type(String hotel_type) {
		this.hotel_type = hotel_type;
	}

	public int getMaximum_Occupency() {
		return maximum_Occupency;
	}

	public void setMaximum_Occupency(int maximum_Occupency) {
		this.maximum_Occupency = maximum_Occupency;
	}

	public String getStar_Rating_By_Hotel() {
		return star_Rating_By_Hotel;
	}

	public void setStar_Rating_By_Hotel(String star_Rating_By_Hotel) {
		this.star_Rating_By_Hotel = star_Rating_By_Hotel;
	}

}
