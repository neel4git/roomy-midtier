package com.Roomy.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Roomy.Request.Domain.ForgetPassword;
import com.Roomy.Request.Domain.LoginRequest;
import com.Roomy.Request.Domain.UserRequest;
import com.Roomy.Response.Domain.UserDetails;
import com.Roomy.Util.AESEncryptionUtil;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class UserPobyteJdbc {
	private final static Logger LOGGER = Logger.getLogger(UserPobyteJdbc.class.getName());
	@Autowired
	EntityManager entityManager;
	@Autowired
	AESEncryptionUtil aESEncryptionUtil;
	public Response responseMessage;
	public String pB_code = "";

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

	public Response userRegistration(UserRequest userRequest) throws Exception {
		try {
			
			StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("USER_REGISTRATION");
			sp.registerStoredProcedureParameter("EMAIL_ADDRESS", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("CONTACT_NUMBER", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("LOGIN_PASSWORD", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("FIRST_NAME", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("MIDDLE_NAME", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("LAST_NAME", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("USER_TYPE", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("GENDER", String.class, ParameterMode.IN);

			sp.setParameter("EMAIL_ADDRESS", userRequest.getEmailId());
			sp.setParameter("CONTACT_NUMBER", userRequest.getConactNumber());
			sp.setParameter("LOGIN_PASSWORD", aESEncryptionUtil.encrypt(userRequest.getPassword()));
			sp.setParameter("FIRST_NAME", userRequest.getName());
			sp.setParameter("MIDDLE_NAME", "");
			sp.setParameter("LAST_NAME", "");
			sp.setParameter("USER_TYPE", "Normal");
			sp.setParameter("GENDER", userRequest.getGender());

			boolean exist = sp.execute();
			if (exist == true) {
				List<Object[]> resultList = sp.getResultList();

				if (resultList.size() > 0 && resultList.contains("Failure:ContactNumberExists")) {
					responseMessage = new Response("0002", null);// Contact Number already exit
				}
				if (resultList.size() > 0 && resultList.contains("Failure:EmailAddressExists")) {
					responseMessage = new Response("0003", null);// Email Id already Exist
				}
				if (resultList.size() > 0) {
					responseMessage = new Response("0001", resultList.get(0)); // Success
				}
			}
		} catch (Exception e) {
			LOGGER.info("UserpobytJDBC :: userRegistration", e);
		}
		return responseMessage;
	}

	public Response updateuserStatus(int userId) {
		try {

			StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("UPDATE_USER_STATUS");
			sp.registerStoredProcedureParameter("USERID", Integer.class, ParameterMode.IN);
			sp.setParameter("USERID", userId);
			boolean exist = sp.execute();
			if (exist == true) {
				List<Object[]> resultList = sp.getResultList();
				if (resultList.size() > 0 && resultList.contains("Success")) {
					responseMessage = new Response("0001", null);// Successfully updated
				} else {
					responseMessage = new Response("0004", null);// Not updated successfully
				}
			}
		} catch (Exception e) {
			LOGGER.info("UserpobytJDBC :: updateuserStatus", e);
		}
		return responseMessage;
	}
	

	public Response updateToken(String result,String token) {
		try {

			StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("UPDATE_USER_TOKEN");
			sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("TOKEN", String.class, ParameterMode.IN);
			sp.setParameter("LOGIN_DETAIL", result);
			sp.setParameter("TOKEN", token);
			
			boolean exist = sp.execute();
			if (exist == true) {
				List<Object[]> resultList = sp.getResultList();
				if (resultList.size() > 0 && resultList.contains("Success")) {
					responseMessage = new Response("0001", null);// Successfully updated
				} else {
					responseMessage = new Response("0004", null);// Not updated successfully
				}
			}
		} catch (Exception e) {
			LOGGER.info("UserpobytJDBC :: updateuserStatus", e);
		}
		return responseMessage;
	}

	
	public Response userLogin(UserRequest userRequest) throws Exception {

		UserDetails userDetails = null;
		List<Object> response = new ArrayList<>();
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("USER_LOGIN");
		sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_PASSWORD", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_TYPE", String.class, ParameterMode.IN);

		if (userRequest.getLoginType().equals("APP") || userRequest.getLoginType().equals("FB") || userRequest.getLoginType().equals("GMAIL")) {
			if (userRequest.getConactNumber() != null && userRequest.getConactNumber().trim().length() > 0) {
				sp.setParameter("LOGIN_DETAIL", userRequest.getConactNumber());
			} else {
				sp.setParameter("LOGIN_DETAIL", userRequest.getEmailId());
			}
			if (userRequest.getLoginType().equals("APP")){
				sp.setParameter("LOGIN_PASSWORD", aESEncryptionUtil.encrypt(userRequest.getPassword()));	
			}else{
				sp.setParameter("LOGIN_PASSWORD","");
			}
			
		}

		if (userRequest.getLoginType().equals("TOKEN")) {

			sp.setParameter("LOGIN_DETAIL", userRequest.getToken());
			sp.setParameter("LOGIN_PASSWORD", "");
		}

		sp.setParameter("LOGIN_TYPE", userRequest.getLoginType());
		boolean exist = sp.execute();
		if (exist == true) {
			List<Object[]> resultList = sp.getResultList();
			if (resultList.size() > 0 && resultList.contains("USERISNOTFOUND")) {
				responseMessage = new Response("0013", null); // User is not Exist 
			}			
			
			if (resultList.size() > 0 && resultList.contains("Failure:WrongCredentials")) {
				responseMessage = new Response("0005", null); // Invalid Username or password

			}
			if (resultList.size() > 0 && resultList.contains("Failure:InactiveOrSuspendedUser")) {
				responseMessage = new Response("0006", null);// Inactive or suspended User
			} 
			if(resultList.size() > 0)
			{
				for (Object[] result : resultList) {
					userDetails = new UserDetails();
					userDetails.setResponse(result[0]);
					userDetails.setUserID(result[1]);
					userDetails.setContactNumber(result[2]);
					userDetails.setEmailAddress(result[3]);
					userDetails.setFirst_Name(result[4]);
					userDetails.setMidle_Name(result[5]);
					userDetails.setLast_Name(result[6]);
					userDetails.setUser_type(result[7]);
					userDetails.setMemberShip_type(result[8]);
					userDetails.setIdentityCardType(result[9]);
					userDetails.setIdentityCardNumber(result[10]);
					userDetails.setCompanyName(result[11]);
					userDetails.setEmergencyContactNumber1(result[12]);
					userDetails.setEmergencyContactNumber2(result[13]);
					userDetails.setDateOfBirth(result[14]);
					userDetails.setCityPrefrence(result[15]);
					userDetails.setSmsNotificationPrefrences(result[16]);
					userDetails.setEmailNotificationPrefrences(result[17]);
					userDetails.setUserStatus(result[18]);
					userDetails.setUserTokenValue(result[19]);

					response.add(userDetails);
					responseMessage = new Response("0001", userDetails);
				}

			}

		}

		return responseMessage;
	}

	public UserDetails getUserprofileById(UserRequest userRequest) {
		UserDetails userDetails = null;
		List<Object> response = new ArrayList<>();
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("GET_USER_PROFILE_BY_ID");
		sp.registerStoredProcedureParameter("USER_ID", Integer.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class, ParameterMode.IN);
		if(userRequest.getUserId() > 0){
			sp.setParameter("USER_ID", userRequest.getUserId());
			sp.setParameter("LOGIN_DETAIL", "");
		}else{
			if (userRequest.getConactNumber() != null && userRequest.getConactNumber().trim().length() > 0) {
				sp.setParameter("LOGIN_DETAIL", userRequest.getConactNumber());
			} else {
				sp.setParameter("LOGIN_DETAIL", userRequest.getEmailId());
			}
			sp.setParameter("USER_ID", 0);
		}

		
		boolean exist = sp.execute();
		if (exist == true) {
			List<Object[]> resultList = sp.getResultList();
			for (Object[] result : resultList) {
				userDetails = new UserDetails();
				userDetails.setUserID(result[0]);
				userDetails.setContactNumber(result[1]);
				userDetails.setEmailAddress(result[2]);
				userDetails.setFirst_Name(result[3]);
				userDetails.setMidle_Name(result[4]);
				userDetails.setLast_Name(result[5]);
				userDetails.setUser_type(result[6]);
				userDetails.setMemberShip_type(result[7]);
				userDetails.setIdentityCardType(result[8]);
				userDetails.setIdentityCardNumber(result[9]);
				userDetails.setCompanyName(result[10]);
				userDetails.setEmergencyContactNumber1(result[11]);
				userDetails.setEmergencyContactNumber2(result[12]);
				userDetails.setDateOfBirth(result[13]);
				userDetails.setCityPrefrence(result[14]);
				userDetails.setSmsNotificationPrefrences(result[15]);
				userDetails.setEmailNotificationPrefrences(result[16]);
				userDetails.setUserStatus(result[17]);
				userDetails.setUserTokenValue(result[18]);

				response.add(userDetails);

			}
		}
		return userDetails;
	}
	
	public Response forgetPassword(ForgetPassword forgetPassword)
			 {
try{
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("UPDATE_USER_PASSWORD");
		sp.registerStoredProcedureParameter("PASSWORD", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class, ParameterMode.IN);
		
		if (forgetPassword.getMobilenNumber() !=null && forgetPassword.getMobilenNumber().trim().length() > 0) {
			sp.setParameter("LOGIN_DETAIL", forgetPassword.getMobilenNumber());
		} else {
			sp.setParameter("LOGIN_DETAIL", forgetPassword.getEmailId());
		}
		sp.setParameter("PASSWORD", aESEncryptionUtil.encrypt(forgetPassword.getPassword()));
		
		boolean result = sp.execute();
		if (result == true) {
			List<Object[]> resultList = sp.getResultList();
			if (resultList.size() > 0 && resultList.contains("Success")) {
				responseMessage = new Response("0001", null);// Successfully updated
			} else {
				responseMessage = new Response("0012", null);// User ID Is not exist
			}
			
				}
}catch(Exception e){
	LOGGER.info("FORget password  exception",e);
	
}
		return responseMessage;
	}

}
