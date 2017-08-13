package com.Roomy.Service.Conversions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.Roomy.Response.Domain.HotelDetails;
import com.Roomy.Response.Domain.UserDetails;
import com.Roomy.domain.Hotel_Master;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;

@Component
public class ResponseBuilder {

	public HotelDetails getHotelDetails(Hotel_Master hotel) {
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

	public Response buildDashBoardLoginResponse(List<Object[]> dashboardUserDeatils) {
		UserDetails userDetails = new UserDetails();
		HotelDetails hotelDetails = new HotelDetails();
		Response dashBoardUserDetailsResponse;
		List<HotelDetails> hotelDetailsList = new ArrayList<>();
		if (dashboardUserDeatils == null) {
			dashBoardUserDetailsResponse = new Response(ResponseStatus.FAILURE_CODE,
					ResponseStatus.USER_RECORDS_NOT_FOUND, null, userDetails);
			return dashBoardUserDetailsResponse;
		} else if (dashboardUserDeatils.size() > 0 && dashboardUserDeatils.contains("Failure:WrongCredentials")) {
			dashBoardUserDetailsResponse = new Response(ResponseStatus.FAILURE_CODE, "Failure:WrongCredentials", null,
					userDetails);
			return dashBoardUserDetailsResponse;

		} else if (dashboardUserDeatils.size() > 0
				&& dashboardUserDeatils.contains("Failure:InactiveOrSuspendedUser")) {
			dashBoardUserDetailsResponse = new Response(ResponseStatus.FAILURE_CODE, "Failure:InactiveOrSuspendedUser",
					null, userDetails);
			return dashBoardUserDetailsResponse;
		} else {
			Object[] uderDetails = dashboardUserDeatils.get(0);
			// User Details
			userDetails.setResponse(uderDetails[0]);
			userDetails.setUserID(uderDetails[1]);
			userDetails.setContactNumber(uderDetails[2]);
			userDetails.setEmailAddress(uderDetails[3]);
			userDetails.setFirst_Name(uderDetails[4]);
			userDetails.setMidle_Name(uderDetails[5]);
			userDetails.setLast_Name(uderDetails[6]);
			userDetails.setUser_type(uderDetails[7]);
			userDetails.setUserTokenValue("");
			for (Object[] result : dashboardUserDeatils) {
				// Hotel Detail Associated for that User
				hotelDetails = new HotelDetails();
				hotelDetails.setHotelId(Integer.parseInt(result[21].toString()));
				hotelDetails.setHotelName(result[22].toString());
				hotelDetailsList.add(hotelDetails);
			}
			// Fill the hotel details for the givev User
			userDetails.setListOfHotels(hotelDetailsList);
		}
		// need to build the jwtToken
		dashBoardUserDetailsResponse = new Response(ResponseStatus.SUCCESS_CODE, null, null, userDetails);
		return dashBoardUserDetailsResponse;
	}
}
