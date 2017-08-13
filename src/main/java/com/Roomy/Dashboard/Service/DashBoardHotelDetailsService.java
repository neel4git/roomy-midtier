package com.Roomy.Dashboard.Service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Repository.Hotel_MasterRepository;
import com.Roomy.Response.Domain.HotelDetails;
import com.Roomy.Service.Conversions.ResponseBuilder;
import com.Roomy.domain.Hotel_Master;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;

/*
 * This is the service used to get the Hotel Details By passing the Hotel ID as Input Param
 */

@RestController
public class DashBoardHotelDetailsService {
	private final static Logger LOGGER = Logger.getLogger(DashBoardHotelDetailsService.class.getName());
	@Autowired
	Hotel_MasterRepository hotel_MasterRepository;
	@Autowired
	ResponseBuilder responseBuilder;
	private Response hotelDetailsResponse;

	@RequestMapping(value = "/getDashBoardHotelDetails", method = RequestMethod.POST, produces = "application/json")
	public Object getHotelDetails(@RequestParam(value = "hotel_id", required = true) int hotel_id,
			@RequestParam(value = "customerToken", required = false) String customerToken) {
		List<Hotel_Master> lisOFHotels = new ArrayList<>();
		HotelDetails hotelDetails = null;
		try {
			LOGGER.info("Getting the Dashboard Hote Details with Id as:: " + hotel_id);
			lisOFHotels = hotel_MasterRepository.getHotelDetailsByID(hotel_id);
			if (!lisOFHotels.isEmpty()) {
				for (Hotel_Master hotel : lisOFHotels) {
					hotelDetails = responseBuilder.getHotelDetails(hotel);
				}
				hotelDetailsResponse = new Response(ResponseStatus.SUCCESS_CODE, null, customerToken, hotelDetails);
			} else {
				hotelDetailsResponse = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.NO_RECORDS_FOUND,
						customerToken, hotelDetails);
			}
		} catch (Exception e) {
			LOGGER.error("An exception occurred while getting the hotels list" + e);
			hotelDetailsResponse = new Response(ResponseStatus.FAILURE_CODE,
					ResponseStatus.DASHBOARD_HOTEL_DETAILS_EXCEPTION, customerToken, hotelDetails);
		}
		return hotelDetailsResponse;
	}
}
