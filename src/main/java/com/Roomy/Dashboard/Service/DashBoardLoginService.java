package com.Roomy.Dashboard.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Request.Domain.LoginRequest;
import com.Roomy.Response.Domain.HotelDetails;
import com.Roomy.Response.Domain.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class DashBoardLoginService {

	@Autowired
	EntityManager entityManager;

	@RequestMapping(value = "/DashBoardLogin", method = RequestMethod.POST, produces = "application/json")
	public Object getDashBoardUserDetails(@RequestBody LoginRequest loginRequest)
			throws JsonProcessingException, SQLException {

		UserDetails userDetails = null;
		HotelDetails hotelDetails = null;
		List<HotelDetails> hotelDetailsList = new ArrayList<>();
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("USER_LOGIN_DASHBOARD");
		sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_PASSWORD", String.class, ParameterMode.IN);

		if (loginRequest.getMobileNumber() != null) {
			sp.setParameter("LOGIN_DETAIL", loginRequest.getMobileNumber());
		} else {
			sp.setParameter("LOGIN_DETAIL", loginRequest.getEmailId());
		}
		sp.setParameter("LOGIN_PASSWORD", loginRequest.getPassword());

		boolean exist = sp.execute();
		if (exist == true) {
			List<Object[]> resultList = sp.getResultList();
			if (resultList.size() > 0 && resultList.contains("Failure:WrongCredentials")) {
				userDetails = new UserDetails();
				userDetails.setResponse("Failure:WrongCredentials");
				return userDetails;

			} else if (resultList.size() > 0 && resultList.contains("Failure:InactiveOrSuspendedUser")) {
				userDetails = new UserDetails();
				userDetails.setResponse("Failure:InactiveOrSuspendedUser");
				return userDetails;

			} else {
				Map<String, String> resultMap = new HashMap<String, String>(resultList.size());
				Object[] uderDetails = resultList.get(0);
				userDetails = new UserDetails();
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
				for (Object[] result : resultList) {
					// Hotel Detail Associated for that User
					hotelDetails = new HotelDetails();
					hotelDetails.setHotelId(result[21].toString());
					hotelDetails.setHotelName(result[22].toString());
					hotelDetailsList.add(hotelDetails);
				}
				// Fill the hotel details for the givev User
				userDetails.setListOfHotels(hotelDetailsList);
			}

		} else {
			userDetails.setResponse("No resuluts found for the given user name and password");
		}
		return userDetails;
	}

}
