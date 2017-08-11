package com.Roomy.Dashboard.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Repository.Hotel_MasterRepository;
import com.Roomy.Response.Domain.HotelDetails;
import com.Roomy.Util.Response;
import com.Roomy.Util.ResponseStatus;
import com.Roomy.domain.Hotel_Master;

@RestController
public class DashBoardHotelDetailsService {
	private final static Logger LOGGER = Logger.getLogger(DashBoardHotelDetailsService.class.getName());
	@Autowired
	Hotel_MasterRepository hotel_MasterRepository;
	private Response response;

	@RequestMapping(value = "/getDashBoardHotelDetails", method = RequestMethod.POST, produces = "application/json")
	public Object getHotelDetails(@RequestParam(value = "hotel_id", required = true) int hotel_id,
			@RequestParam(value = "customerToken", required = false) String customerToken) {
		List<Hotel_Master> lisOFHotels = new ArrayList<>();
		HotelDetails hotelDetails = null;
		try {
			LOGGER.info("getting the Dashboard Hote Details with Id as:: " + hotel_id);
			lisOFHotels = hotel_MasterRepository.getHotelDetailsByID(hotel_id);
			if (!lisOFHotels.isEmpty()) {
				for (Hotel_Master hotel : lisOFHotels) {
					hotelDetails = getGotelDetails(hotel);
				}
				response = new Response(ResponseStatus.SUCCESS_CODE, null, customerToken, hotelDetails);
			} else {
				response = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.NO_RECORDS_FOUND, customerToken,
						hotelDetails);
			}
		} catch (Exception e) {
			LOGGER.error("An exception occurred while getting the hotels list" + e);
			response = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.DASHBOARD_HOTEL_DETAILS_EXCEPTION,
					customerToken, hotelDetails);
		}
		return response;
	}

	private HotelDetails getGotelDetails(Hotel_Master hotel) {
		HotelDetails hotelDetails = new HotelDetails();
		hotelDetails.setHotelId(hotel.getHotel_Id());
		hotelDetails.setHotelName(hotel.getHotel_Name());
		hotelDetails.setLogo("");
		hotelDetails.setHotelDescription(hotel.getHotel_Description());
		hotelDetails.setAmenities(null);
		hotelDetails.setStars(hotel.getHotel_Info().getStar_Rating_By_Hotel());
		hotelDetails.setRatePerMin(hotel.getPricinginfo().get(0).getPricePerMinute());
		hotelDetails.setMinBooking(hotel.getHotel_Info().getMaximum_Occupency());
		Map<String, String> address = new HashMap<String, String>();
		address.put("location", hotel.getHotel_Location());
		address.put("landMark", "");
		address.put("city", hotel.getHotel_City());
		address.put("state", hotel.getHotel_City());
		address.put("pin", hotel.getPin_Code());
		hotelDetails.setAddress(address);
		hotelDetails.setPhone(hotel.getContact_No1());
		hotelDetails.setEmail("");
		hotelDetails.setWebsite(hotel.getWeb_site());
		hotelDetails.setPics("");
		return hotelDetails;
	}

}
