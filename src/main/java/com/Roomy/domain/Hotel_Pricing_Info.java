package com.Roomy.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PRICING_INFO")
public class Hotel_Pricing_Info {
	@Transient
	private static final long serialVersionUID = -112950002831333869L;

	@Id
	@GeneratedValue
	@Column(name = "HOTEL_ID")
	private int hotelID;

	@Column(name = "AVAILABLE_DATE")
	private Date availableDate;

	@Column(name = "PRICE_PER_MINUTE")
	private float pricePerMinute;

	@Column(name = "MAINTENANCE_PRICE", nullable = true)
	private float maintenancePrice;

	@Column(name = "MINIMUM_DURATION", nullable = true)
	private int minimumDuration;

	@Column(name = "CURRENCY_TYPE", nullable = true)
	private String currencyType;

	@ManyToOne
	@JoinColumn(name = "HOTEL_ID", insertable = false, updatable = false)
	private Hotel_Master hotelMaster;

	public Hotel_Master getHotelMaster() {
		return hotelMaster;
	}

	public void setHotelMaster(Hotel_Master hotelMaster) {
		this.hotelMaster = hotelMaster;
	}

	public int getHotelID() {
		return hotelID;
	}

	public void setHotelID(int hotelID) {
		this.hotelID = hotelID;
	}

	public Date getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
	}

	public float getPricePerMinute() {
		return pricePerMinute;
	}

	public void setPricePerMinute(float pricePerMinute) {
		this.pricePerMinute = pricePerMinute;
	}

	public float getMaintenancePrice() {
		return maintenancePrice;
	}

	public void setMaintenancePrice(float maintenancePrice) {
		this.maintenancePrice = maintenancePrice;
	}

	public int getMinimumDuration() {
		return minimumDuration;
	}

	public void setMinimumDuration(int minimumDuration) {
		this.minimumDuration = minimumDuration;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

}
