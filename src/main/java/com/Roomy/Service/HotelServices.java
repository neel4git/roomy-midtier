package com.Roomy.Service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Repository.Hotel_InfoRepository;
import com.Roomy.Repository.Hotel_MasterRepository;
import com.Roomy.Response.Domain.HotelsBasedOnCityResponse;
import com.Roomy.Util.Message;
import com.Roomy.Util.ResponseBodyResult;
import com.Roomy.domain.Hotel_Info;
import com.Roomy.domain.Hotel_Master;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class HotelServices {

	private final static Logger LOGGER = Logger.getLogger(HotelServices.class
			.getName());

	@Autowired
	Hotel_MasterRepository hotel_MasterRepository;

	@Autowired
	Hotel_InfoRepository hotel_InfoRepository;

	ResponseBodyResult responseBodyResult = new ResponseBodyResult();

	@RequestMapping(value = "/getHotels", method = RequestMethod.POST, produces = "application/json")
	public Object getHotelsBasedonCity(
			@RequestParam(value = "cityName", required = true) String cityName,
			@RequestParam(value = "customerToken") String customerToken)
			throws JsonProcessingException {
		try {

			LOGGER.info("Hotel Serviec :: getListofHotels " + cityName);
			HotelsBasedOnCityResponse hotelsBasedOnCityResponse = null;
			List<HotelsBasedOnCityResponse> lisOfHotelDetails = new ArrayList<HotelsBasedOnCityResponse>();
			List<Hotel_Master> lisOFHotels = hotel_MasterRepository
					.getHotelMasterDetails(cityName);
			if (!lisOFHotels.isEmpty()) {
				for (Hotel_Master hotel : lisOFHotels) {
					hotelsBasedOnCityResponse = buildHotelDetailsReponse(hotel);
					lisOfHotelDetails.add(hotelsBasedOnCityResponse);
				}
				responseBodyResult = new ResponseBodyResult(
						Message.SUCCESS_CODE, null, customerToken,
						lisOfHotelDetails);
			} else {
				responseBodyResult = new ResponseBodyResult(
						Message.FAILURE_CODE, Message.NO_RECORDS_FOUND,
						customerToken, lisOfHotelDetails);
			}
		} catch (Exception e) {
			System.out.println("List of hotels :: Exception " + e);
		}
		return responseBodyResult;
	}

	private HotelsBasedOnCityResponse buildHotelDetailsReponse(
			Hotel_Master hotel) {
		HotelsBasedOnCityResponse hotelsBasedOnCityResponse;
		hotelsBasedOnCityResponse = new HotelsBasedOnCityResponse();
		hotelsBasedOnCityResponse.setHotel_Id(hotel.getHotel_Id());
		hotelsBasedOnCityResponse.setHotelName(hotel.getHotel_Name());
		hotelsBasedOnCityResponse.setAddress(hotel.getHotel_Address());
		hotelsBasedOnCityResponse.setMinCost(hotel.getPricinginfo().get(0)
				.getPricePerMinute());
		hotelsBasedOnCityResponse.setHourCost(hotel.getPricinginfo().get(0)
				.getPricePerMinute() * 60);
		hotelsBasedOnCityResponse.setCity(hotel.getHotel_City());
		hotelsBasedOnCityResponse.setState(hotel.getHotel_State());
		hotelsBasedOnCityResponse.setLattitue(hotel.getLatitude());
		hotelsBasedOnCityResponse.setLongitude(hotel.getLongitude());
		hotelsBasedOnCityResponse.setCurrencyType(hotel.getPricinginfo().get(0)
				.getCurrencyType());
		return hotelsBasedOnCityResponse;
	}

	@RequestMapping(value = "/getHotelDetails", method = RequestMethod.POST, produces = "application/json")
	public Object getHotelDetails(
			@RequestParam(value = "hotel_id", required = true) int hotel_id,
			@RequestParam(value = "customerToken") String customerToken) {
		List<Hotel_Info> hotel_InfoList = new ArrayList<Hotel_Info>();
		try {
			Hotel_Info hotel_Info = hotel_InfoRepository.getHotelInfo(hotel_id);
			if (hotel_Info != null) {
				hotel_Info
						.setAminities("Wi-Fi,AC,Car Parking,Gym,SwimmingPool");
				hotel_InfoList.add(hotel_Info);
				responseBodyResult = new ResponseBodyResult(
						Message.SUCCESS_CODE, null, customerToken,
						hotel_InfoList);
			} else {
				responseBodyResult = new ResponseBodyResult(
						Message.FAILURE_CODE, Message.NO_RECORDS_FOUND,
						customerToken, null);
			}
		} catch (Exception e) {
			System.out.println("Hotel Details :: Exception" + e);
		}
		return responseBodyResult;
	}

}