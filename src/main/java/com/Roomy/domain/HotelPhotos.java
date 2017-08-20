package com.Roomy.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity(name = "HOTEL_PHOTOS")
public class HotelPhotos implements Serializable {

	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@Column(name = "HOTEL_ID", unique = true)
	private int hotel_Id;

	@Column(name = "PHOTO_CATEGORY")
	private String photoCategory;

	@Column(name = "PHOTO_DESCRIPTION")
	private String photoDecription;

	@Column(name = "PHOTO")
	private String picUrl;

	@ManyToOne
	@JoinColumn(name = "HOTEL_ID", insertable = false, updatable = false)
	private Hotel_Master hotelMaster;

	public Hotel_Master getHotelMaster() {
		return hotelMaster;
	}

	public void setHotelMaster(Hotel_Master hotelMaster) {
		this.hotelMaster = hotelMaster;
	}

	public int getHotel_Id() {
		return hotel_Id;
	}

	public void setHotel_Id(int hotel_Id) {
		this.hotel_Id = hotel_Id;
	}

	public String getPhotoCategory() {
		return photoCategory;
	}

	public void setPhotoCategory(String photoCategory) {
		this.photoCategory = photoCategory;
	}

	public String getPhotoDecription() {
		return photoDecription;
	}

	public void setPhotoDecription(String photoDecription) {
		this.photoDecription = photoDecription;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
