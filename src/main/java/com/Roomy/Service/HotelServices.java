package com.Roomy.Service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Repository.Hotel_MasterRepository;
import com.Roomy.Response.Domain.HotelsBasedOnCityResponse;
import com.Roomy.Response.Domain.MetaDataHoteResponse;
import com.Roomy.domain.Hotel_Master;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class HotelServices {

	private final static Logger LOGGER = Logger.getLogger(HotelServices.class.getName());

	@Autowired
	Hotel_MasterRepository hotel_MasterRepository;

	@RequestMapping(value = "/getListofHotelsByCity", method = RequestMethod.GET, produces = "application/json")
	public Object getHotelsBasedonCity(
			@RequestParam(value = "cityName", required = true) String cityName)
			throws JsonProcessingException, JSONException {
		LOGGER.info("Entered into getListofHotelsByCity for fetching the list of hotels by cityname as "
				+ cityName);
		HotelsBasedOnCityResponse hotelsBasedOnCityResponse = null;
		MetaDataHoteResponse metaDataHoteResponse = null;
		List<HotelsBasedOnCityResponse> lisOfHotelDetails = new ArrayList<HotelsBasedOnCityResponse>();
		List<Hotel_Master> lisOFHotels = hotel_MasterRepository
				.getHotelMasterDetails(cityName);
		if (!lisOFHotels.isEmpty()) {
			for (Hotel_Master hotel : lisOFHotels) {
				hotelsBasedOnCityResponse = new HotelsBasedOnCityResponse();
				hotelsBasedOnCityResponse.setHotelName(hotel.getHotel_Name());
				hotelsBasedOnCityResponse.setAddress1(hotel.getHotel_Address());
				hotelsBasedOnCityResponse.setCity(hotel.getHotel_City());
				hotelsBasedOnCityResponse.setState(hotel.getHotel_State());
				hotelsBasedOnCityResponse.setLattitue(hotel.getLatitude());
				hotelsBasedOnCityResponse.setLongitude(hotel.getLongitude());
				lisOfHotelDetails.add(hotelsBasedOnCityResponse);
			}
			return lisOfHotelDetails;
		}
		// No Details found for the given Hotel
		else {
			metaDataHoteResponse = new MetaDataHoteResponse();
			metaDataHoteResponse.setStatusCode("400");
			metaDataHoteResponse.setFailureMessage("No Details Found for the Given City");
		}

		return metaDataHoteResponse;
	}
}