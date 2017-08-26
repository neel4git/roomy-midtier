package com.Roomy.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Dao.UserPobyteJdbc;
import com.Roomy.Repository.Hotel_InfoRepository;
import com.Roomy.Repository.Hotel_MasterRepository;
import com.Roomy.Request.Domain.HotelsbyLocationRequest;
import com.Roomy.Response.Domain.HotelsBasedOnCityResponse;
import com.Roomy.Response.Domain.HotelsListByRadius;
import com.Roomy.domain.Hotel_Info;
import com.Roomy.domain.Hotel_Master;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class HotelServices {
	private final static Logger LOGGER = Logger.getLogger(HotelServices.class.getName());
	@Autowired
	Hotel_MasterRepository hotel_MasterRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	Hotel_InfoRepository hotel_InfoRepository;
	private Response responseMessage;
	
	@Autowired
	UserPobyteJdbc userPobyteJdbc;

	@RequestMapping(value = "/getHotels", method = RequestMethod.POST, produces = "application/json")
	public Object getHotelsBasedonCity(@RequestParam(value = "cityName", required = true) String cityName,
			@RequestParam(value = "customerToken") String customerToken) throws JsonProcessingException {
		try {

			LOGGER.info("Hotel Serviec :: getListofHotels " + cityName);
			HotelsBasedOnCityResponse hotelsBasedOnCityResponse = null;
			List<HotelsBasedOnCityResponse> lisOfHotelDetails = new ArrayList<HotelsBasedOnCityResponse>();
			List<Hotel_Master> lisOFHotels = hotel_MasterRepository.getHotelMasterDetails(cityName);
			if (!lisOFHotels.isEmpty()) {
				for (Hotel_Master hotel : lisOFHotels) {
					hotelsBasedOnCityResponse = buildHotelDetailsReponse(hotel);
					lisOfHotelDetails.add(hotelsBasedOnCityResponse);
				}
				responseMessage = new Response(ResponseStatus.SUCCESS_CODE, null, customerToken, lisOfHotelDetails);
			} else {
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.NO_RECORDS_FOUND, customerToken,
						lisOfHotelDetails);
			}
		} catch (Exception e) {
			LOGGER.error("An exception occurred while getting the hotels list" + e);
		}
		return responseMessage;
	}

	@RequestMapping(value = "/getHotelDetails", method = RequestMethod.POST, produces = "application/json")
	public Object getHotelDetails(@RequestParam(value = "hotel_id", required = true) int hotel_id,
			@RequestParam(value = "customerToken") String customerToken) throws JsonProcessingException, SQLException{
		List<Hotel_Info> hotel_InfoList = new ArrayList<Hotel_Info>();
		try {
			Hotel_Info hotel_Info = hotel_InfoRepository.getHotelInfo(hotel_id);
			if (hotel_Info != null) {
				hotel_Info.setAminities("Wi-Fi,AC,Car Parking,Gym,SwimmingPool");
				hotel_InfoList.add(hotel_Info);
				responseMessage = new Response("0001", null, customerToken, hotel_InfoList);
			} else {
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.NO_RECORDS_FOUND, customerToken,
						null);
			}
		} catch (Exception e) {
			LOGGER.error("An exception occurred while getting the hotels specific details" + e);
		}
		return responseMessage;
	}
	
	@RequestMapping(value = "/getHotelsbyLocation", method = RequestMethod.POST, produces = "application/json")
	public Response getHotels(@RequestBody HotelsbyLocationRequest locationRequest) throws JsonProcessingException, SQLException {

		//if (userPobyteJdbc.validateJwtToken(locationRequest.getJwtToken())) {
			List<HotelsListByRadius> hotelsListByRadiusList = new ArrayList<HotelsListByRadius>();
			HotelsListByRadius hotelsListByRadius = new HotelsListByRadius();
			StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("GET_HOTELS_BY_RADIUS");
			sp.registerStoredProcedureParameter("USER_LATITUDE", Float.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("USER_LONGITUDE", Float.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("RADIUS", Integer.class, ParameterMode.IN);

			sp.setParameter("USER_LATITUDE", locationRequest.getUser_Latitude());
			sp.setParameter("USER_LONGITUDE", locationRequest.getUser_Longitude());
			sp.setParameter("RADIUS", 10);
			boolean exist = sp.execute();
			if (exist == true) {
				List<Object[]> resultList = sp.getResultList();
				System.out.println(resultList.size());
				for (Object[] result : resultList) {
					hotelsListByRadius.setHotel_id(result[0]);
					hotelsListByRadius.setHotel_name(result[1]);
					hotelsListByRadius.setHotel_description(result[2]);
					hotelsListByRadius.setLatitude(result[3]);
					hotelsListByRadius.setLongitude(result[4]);
					hotelsListByRadius.setHotel_address(result[5]);
					hotelsListByRadius.setHotel_Location(result[6]);
					hotelsListByRadius.setHotel_City(result[7]);
					hotelsListByRadius.setHotel_State(result[8]);
					hotelsListByRadius.setHotel_Country(result[9]);
					hotelsListByRadius.setPin_Code(result[10]);
					hotelsListByRadius.setContact_No1(result[11]);
					hotelsListByRadius.setContact_No2(result[12]);
					hotelsListByRadius.setContact_No3(result[13]);
					hotelsListByRadius.setHotel_type(result[14]);
					hotelsListByRadius.setMaximum_Occupency(result[15]);
					hotelsListByRadius.setStar_Rating_By_Hotel(result[16]);
					hotelsListByRadiusList.add(hotelsListByRadius);
				}
				System.out.println(hotelsListByRadiusList);
				responseMessage = new Response("0001", null, locationRequest.getJwtToken(),
						hotelsListByRadiusList);
			} else {
				responseMessage = new Response("0010",null , locationRequest.getJwtToken(), "");
			}

		/*} else {
			responseMessage = new Response("0011", null, "", "");

		}
*/		return responseMessage;
	}


	private HotelsBasedOnCityResponse buildHotelDetailsReponse(Hotel_Master hotel) {
		HotelsBasedOnCityResponse hotelsBasedOnCityResponse;
		hotelsBasedOnCityResponse = new HotelsBasedOnCityResponse();
		hotelsBasedOnCityResponse.setHotel_Id(hotel.getHotel_Id());
		hotelsBasedOnCityResponse.setHotelName(hotel.getHotel_Name());
		hotelsBasedOnCityResponse.setAddress(hotel.getHotel_Address());
		hotelsBasedOnCityResponse.setMinCost(hotel.getPricinginfo().get(0).getPricePerMinute());
		hotelsBasedOnCityResponse.setHourCost(hotel.getPricinginfo().get(0).getPricePerMinute() * 60);
		hotelsBasedOnCityResponse.setCity(hotel.getHotel_City());
		hotelsBasedOnCityResponse.setState(hotel.getHotel_State());
		hotelsBasedOnCityResponse.setLattitue(hotel.getLatitude());
		hotelsBasedOnCityResponse.setLongitude(hotel.getLongitude());
		hotelsBasedOnCityResponse.setCurrencyType(hotel.getPricinginfo().get(0).getCurrencyType());
		return hotelsBasedOnCityResponse;
	}

}