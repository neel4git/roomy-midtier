package com.Roomy.Dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Roomy.Request.Domain.LoginRequest;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class UserPobyteJdbc {
	private final static Logger LOGGER = Logger.getLogger(UserPobyteJdbc.class.getName());
	@Autowired
	EntityManager entityManager;
	public Response responseMessage;

	/*
	 * This is the method used to validate the Toekn present for the Given User
	 */
	public boolean validateJwtToken(String jwtToken) throws SQLException {
		boolean tokenExist = Boolean.FALSE;
		try {
			StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("VALIDATE_JWTTOKEN");
			sp.registerStoredProcedureParameter("USER_TOKEN_VALUE", String.class, ParameterMode.IN);
			sp.setParameter("USER_TOKEN_VALUE", jwtToken);
			tokenExist = sp.execute();
		} catch (Exception sqlexception) {
			LOGGER.error("An exception occurred while validating the Token in the Database ", sqlexception);
			throw new SQLException(sqlexception.getMessage());
		}
		return tokenExist;
	}

	public Response getJwtToken(String mobileNumber) throws JsonProcessingException, SQLException {
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("GET_TOKEN");
		sp.registerStoredProcedureParameter("CONTACT_NUMBER", String.class, ParameterMode.IN);
		System.out.println(mobileNumber);
		if (mobileNumber != null) {
			sp.setParameter("CONTACT_NUMBER", mobileNumber);
		}
		System.out.println(mobileNumber);
		boolean result = sp.execute();
		System.out.println(result);
		if (result == true) {
			String jwtToken = "";
			List<Object[]> resultList = sp.getResultList();
			System.out.println(resultList.size());
			for (Object[] results : resultList) {
				jwtToken = results[0].toString();
			}

			responseMessage = new Response(ResponseStatus.SUCCESS_CODE, ResponseStatus.SUCESS_MESSAGE, jwtToken, "");
		} else {
			responseMessage = new Response(ResponseStatus.FAILURE_CODE, "User is logged in.!", null, "");
		}

		return responseMessage;
	}

	/*
	 * DAO method to allow the user to login into the Dashboard
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getDashBoardUserDetails(LoginRequest loginRequest) throws SQLException {
		boolean userFound = Boolean.FALSE;
		List<Object[]> dashboardUserDetails = null;
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("USER_LOGIN_DASHBOARD");
		sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_PASSWORD", String.class, ParameterMode.IN);
		/*
		 * if Mobile Number is missing Take the EmailID
		 */
		if (loginRequest.getMobileNumber() != null && loginRequest.getMobileNumber().trim().length() > 0) {
			sp.setParameter("LOGIN_DETAIL", loginRequest.getMobileNumber());
		} else {
			sp.setParameter("LOGIN_DETAIL", loginRequest.getEmailId());
		}
		sp.setParameter("LOGIN_PASSWORD", loginRequest.getPassword());
		try {
			userFound = sp.execute();
			if (userFound) {
				dashboardUserDetails = sp.getResultList();
			}
		} catch (Exception exception) {
			LOGGER.error("An exception occurred while login into the dashbaord ", exception);
			throw new SQLException(exception.getMessage());
		}
		return dashboardUserDetails;
	}

	/*
	 * This is the method used to validate the Toekn present for the Given User
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> gteBookingsHistroy(String hotelID, int lastBookingsinDays) throws SQLException {
		LOGGER.info("Entered into gteBookingsHistroy");
		boolean bookingsFound = Boolean.FALSE;
		List<Object[]> bookingsHistroy = null;
		StoredProcedureQuery bookingsHistroySP = entityManager.createStoredProcedureQuery("GET_RESERVATIONS_BY_HOTEL");
		bookingsHistroySP.registerStoredProcedureParameter("HOTEL_ID", String.class, ParameterMode.IN);
		bookingsHistroySP.registerStoredProcedureParameter("INTERVAL_DAYS", Integer.class, ParameterMode.IN);
		bookingsHistroySP.setParameter("HOTEL_ID", hotelID);
		bookingsHistroySP.setParameter("INTERVAL_DAYS", lastBookingsinDays);
		try {
			bookingsFound = bookingsHistroySP.execute();
			if (bookingsFound) {
				bookingsHistroy = bookingsHistroySP.getResultList();
			}
		} catch (Exception exception) {
			LOGGER.error("An exception occurred while getting the bookings histroy in DashBoardService ", exception);
			throw new SQLException(exception.getMessage());
		}
		return bookingsHistroy;
	}

}
