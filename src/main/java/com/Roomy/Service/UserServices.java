package com.Roomy.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Dao.UserPobyteJdbc;
import com.Roomy.Request.Domain.AutheticateUserRequest;
import com.Roomy.Request.Domain.LoginRequest;
import com.Roomy.Request.Domain.UserRegistrationRequest;
import com.Roomy.Response.Domain.HotelsListByRadius;
import com.Roomy.Response.Domain.UserDetails;
import com.Roomy.Util.AESEncryptionUtil;
import com.Roomy.Util.JwtKeyUtil;
import com.Roomy.Util.RoomyUtil;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;
import com.Roomy.domain.SourceKeyRing;
import com.Roomy.domain.UserMaster;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

@RestController
public class UserServices {

	public Response responseMessage;

	@Autowired
	EntityManager entityManager;

	@Autowired
	AESEncryptionUtil aESEncryptionUtil;

	public UserPobyteJdbc userPobyteJdbc;

	
	
	
	@RequestMapping(value = "/userRegistration", method = RequestMethod.POST, produces = "application/json")
	public Response userRegistration(@RequestBody UserRegistrationRequest userRegistrationRequest) throws Exception {
		
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("USER_REGISTRATION");
		sp.registerStoredProcedureParameter("REGISTRATION_TYPE", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("EMAIL_ADDRESS", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("CONTACT_NUMBER", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_PASSWORD", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("FIRST_NAME", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("USER_TYPE", String.class, ParameterMode.IN);
				
		sp.setParameter("REGISTRATION_TYPE", userRegistrationRequest.getRegistrationType());
		sp.setParameter("EMAIL_ADDRESS", userRegistrationRequest.getEmailId());
		sp.setParameter("CONTACT_NUMBER", userRegistrationRequest.getConactNumber());
		sp.setParameter("LOGIN_PASSWORD", aESEncryptionUtil.encrypt(userRegistrationRequest.getPasword()));
		sp.setParameter("FIRST_NAME", userRegistrationRequest.getName());
		sp.setParameter("USER_TYPE", userRegistrationRequest.getUserType());
		boolean exist = sp.execute();
		if (exist == true) {
			List<Object[]> resultList = sp.getResultList();
			if (resultList.size() > 0 && resultList.contains("Success")) {
				int OTPAuth = RoomyUtil.generateOTP();
				responseMessage = new Response(ResponseStatus.SUCCESS_CODE, null, generateCustomerToken(userRegistrationRequest, OTPAuth),
						OTPAuth);
				return responseMessage;
			}
			if (resultList.size() > 0 && resultList.contains("Failure:ContactNumberExists")) {
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, "Mobile Number is already register,Please login", null, null);
				return responseMessage;
			}
			if (resultList.size() > 0 && resultList.contains("Failure:EmailAddressExists")) {
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, "Email Id is already register,Please login", null, null);
				return responseMessage;
			}
		}
		return responseMessage;
	}
	
	@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST)
	public Object authenticateUser(@RequestBody AutheticateUserRequest autheticateUserRequest) {
	
		try {
			SourceKeyRing sourceKeyRing = decryptyToken(autheticateUserRequest.getCustomerToken());
			// if otp didnot matched
			if (autheticateUserRequest.getOtp().equals(sourceKeyRing.getOtp())) {
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.WRONG_OTP_EXCEPTION,
						autheticateUserRequest.getCustomerToken(), null);
			}
			// if OTP issued time is excceded greater than 15 minutes
			else if (RoomyUtil.getOtpIssueTimeDiffrence(sourceKeyRing.getOtpIssuedTime()) > 15) {
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.OTP_EXPIRED_EXCEPTION,
						autheticateUserRequest.getCustomerToken(), null);
			} else {
				//sp
				StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("UPDATE_USER_STATUS");
				sp.registerStoredProcedureParameter("CONTACT_NUMBER", String.class, ParameterMode.IN);
				sp.setParameter("CONTACT_NUMBER", sourceKeyRing.getRegistrationRequest().getConactNumber());
				boolean exist = sp.execute();
				if (exist == true) {
					List<Object[]> resultList = sp.getResultList();
					if (resultList.size() > 0 && resultList.contains("Success")) {
					responseMessage = new Response(ResponseStatus.SUCCESS_CODE, "User Registered Sucessfully",
							autheticateUserRequest.getCustomerToken(), null);
				} else {
					responseMessage = new Response(ResponseStatus.FAILURE_CODE, "User Already Registered Please login",
							autheticateUserRequest.getCustomerToken(), null);
				}
			}
			}
		}
		 catch (Exception exception) {
			
			responseMessage = new Response(ResponseStatus.FAILURE_CODE,
					"Some Exception occurred in processing the authenticateUser Service",
					autheticateUserRequest.getCustomerToken(), null);
		}
		return responseMessage;
	}
	
	
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST, produces = "application/json")
	public Response userLogin(@RequestBody LoginRequest loginRequest) throws Exception {

		UserDetails userDetails = null;
		List<Object> response = new ArrayList<>();
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("USER_LOGIN");
		sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_PASSWORD", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_TYPE", String.class, ParameterMode.IN);

		if(loginRequest.getLoginType().equals("CREDENTIALS")){
		if (loginRequest.getMobileNumber() != null) {
			sp.setParameter("LOGIN_DETAIL", loginRequest.getMobileNumber());
		} else {
			sp.setParameter("LOGIN_DETAIL", loginRequest.getEmailId());
		}
		}else{
			sp.setParameter("LOGIN_DETAIL",loginRequest.getJwtToken());
		}
		sp.setParameter("LOGIN_TYPE", loginRequest.getLoginType());
		sp.setParameter("LOGIN_PASSWORD", aESEncryptionUtil.encrypt(loginRequest.getPassword()));

		boolean exist = sp.execute();
		if (exist == true) {
			List<Object[]> resultList = sp.getResultList();
			if (resultList.size() > 0 && resultList.contains("Failure:WrongCredentials")) {
				userDetails = new UserDetails();
				userDetails.setResponse("Failure:WrongCredentials");
				response.add(userDetails);
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, userDetails.getResponse(), null, null);
				return responseMessage;

			} else if (resultList.size() > 0 && resultList.contains("Failure:InactiveOrSuspendedUser")) {
				userDetails = new UserDetails();
				userDetails.setResponse("Failure:InactiveOrSuspendedUser");
				response.add(userDetails);
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, userDetails.getResponse(), null, null);
				return responseMessage;

			} else {

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
					responseMessage = new Response(ResponseStatus.SUCCESS_CODE, null, null, userDetails);
				}

			}

		} else {
			responseMessage = new Response(ResponseStatus.FAILURE_CODE, "No Records found", null, "");
		}

		return responseMessage;
	}

	@RequestMapping(value = "/userLogout", method = RequestMethod.POST, produces = "application/json")
	public Response userLogout(@RequestParam(value = "userID", required = false) int userID,
			@RequestParam(value = "jwtToken") String jwtToken) throws JsonProcessingException, SQLException {

		if (userPobyteJdbc.validateJwtToken(jwtToken)) {
			StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("USER_LOGOUT");
			sp.registerStoredProcedureParameter("USER_ID", Integer.class, ParameterMode.IN);

			sp.setParameter("USER_ID", userID);
			boolean exist = sp.execute();
			if (exist == true) {
				List<Object[]> resultList = sp.getResultList();
				if (resultList.size() > 0 && resultList.contains("Success")) {
					System.out.println("Logout");
				} else {
					responseMessage = new Response(ResponseStatus.SUCCESS_CODE, ResponseStatus.SUCESS_MESSAGE, null,
							exist);
				}

				responseMessage = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.FAILURE_MESSAGE, null,
						exist);
			}

		} else {
			responseMessage = new Response(ResponseStatus.UNAUTH_ACCESS, "UnAuthrorized access,Please login", "", "");
		}

		return responseMessage;
	}

	@RequestMapping(value = "/getHotelsbyLocation", method = RequestMethod.POST, produces = "application/json")
	public Response getHotels(@RequestParam(value = "user_Latitude") float user_Latitude,
			@RequestParam(value = "user_Longitude") float user_Longitude, @RequestParam(value = "radius") int radius,
			@RequestParam(value = "jwtToken") String jwtToken) throws JsonProcessingException, SQLException {

		if (userPobyteJdbc.validateJwtToken(jwtToken)) {
			List<HotelsListByRadius> hotelsListByRadiusList = new ArrayList<HotelsListByRadius>();
			HotelsListByRadius hotelsListByRadius = new HotelsListByRadius();
			StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("GET_HOTELS_BY_RADIUS");
			sp.registerStoredProcedureParameter("USER_LATITUDE", Float.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("USER_LONGITUDE", Float.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("RADIUS", Integer.class, ParameterMode.IN);

			sp.setParameter("USER_LATITUDE", user_Latitude);
			sp.setParameter("USER_LONGITUDE", user_Longitude);
			sp.setParameter("RADIUS", radius);
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
				responseMessage = new Response(ResponseStatus.SUCCESS_CODE, ResponseStatus.SUCESS_MESSAGE, null,
						hotelsListByRadiusList);
			} else {
				responseMessage = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.FAILURE_MESSAGE, null, "");
			}

		} else {
			responseMessage = new Response(ResponseStatus.UNAUTH_ACCESS, "UnAuthrorized access,Please login", "", "");

		}
		return responseMessage;
	}

	@RequestMapping(value = "/updateUserProfile", method = RequestMethod.POST, produces = "application/json")
	public Response updateUserProfile(@RequestBody UserDetails useRDetails,
			@RequestParam(value = "jwtToken") String jwtToken) throws JsonProcessingException, SQLException {

		if (validateJwtToken(jwtToken)) {
			StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("UPDATE_USER_PROFILE");
			sp.registerStoredProcedureParameter("USER_ID", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("FIRST_NAME", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("MIDDLE_NAME", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("LAST_NAME", String.class, ParameterMode.IN);

			sp.setParameter("USER_ID", useRDetails.getUserID());
			sp.setParameter("FIRST_NAME", useRDetails.getFirst_Name());
			sp.setParameter("MIDDLE_NAME", useRDetails.getMidle_Name());
			sp.setParameter("LAST_NAME", useRDetails.getLast_Name());

			boolean exist = sp.execute();

			if (exist == true) {
				List<Object[]> resultList = sp.getResultList();
				if (resultList.size() > 0 && resultList.contains("Success")) {
					responseMessage = new Response(ResponseStatus.SUCCESS_CODE, ResponseStatus.SUCESS_MESSAGE, null,
							exist);
				} else {
					responseMessage = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.FAILURE_MESSAGE, null,
							exist);
				}
			}
		} else {
			responseMessage = new Response(ResponseStatus.UNAUTH_ACCESS, "UnAuthrorized access,Please login", "", "");
		}
		return responseMessage;
	}

	
	@RequestMapping(value = "/getJwtToken", method = RequestMethod.POST, produces = "application/json")
	public Response getJwtToken(@RequestParam(value = "mobileNumber") String mobileNumber)
			throws JsonProcessingException, SQLException {

		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("GET_TOKEN");
		sp.registerStoredProcedureParameter("CONTACT_NUMBER", String.class, ParameterMode.IN);
		System.out.println(mobileNumber);
		if (mobileNumber != null) {
			sp.setParameter("CONTACT_NUMBER", mobileNumber);
		}
		System.out.println(mobileNumber);
		boolean result = sp.execute();
		System.out.println(result);
		String token = "";
		if (result == true) {
			List<Object[]> resultList = sp.getResultList();
			token = resultList.toString();

			System.out.println(token);
			responseMessage = new Response(ResponseStatus.SUCCESS_CODE, ResponseStatus.SUCESS_MESSAGE, token, "");
		} else {
			responseMessage = new Response(ResponseStatus.FAILURE_CODE, "User is not logged in.!", null, "");
		}

		return responseMessage;
	}

	public boolean validateJwtToken(String jwtToken) throws JsonProcessingException, SQLException {

		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("VALIDATE_JWTTOKEN");

		sp.registerStoredProcedureParameter("USER_TOKEN_VALUE", String.class, ParameterMode.IN);

		sp.setParameter("USER_TOKEN_VALUE", jwtToken);
		boolean result = sp.execute();
		if (result == true) {
			List<Object[]> resultList = sp.getResultList();
		}

		return result;
	}
	
	private String generateCustomerToken(UserRegistrationRequest userRegistrationRequest, int OTPAuth) throws JOSEException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		// Set the SourceKeyRing vale to generate the Key
		SourceKeyRing keyRing = new SourceKeyRing();
		UserRegistrationRequest registrationRequest = userRegistrationRequest;
		keyRing.setRegistrationRequest(registrationRequest);
		keyRing.setOtp(OTPAuth);
		keyRing.setOtpIssuedTime(dateFormat.format(date));
		return JwtKeyUtil.createJWT(keyRing);
	}

	private SourceKeyRing decryptyToken(String token)
			throws ParseException, JOSEException, JsonParseException, JsonMappingException, IOException {
		SourceKeyRing sourceKeyRing = null;
		ObjectMapper mapper = new ObjectMapper();
		JWTClaimsSet claims = (JWTClaimsSet) JwtKeyUtil.decryptToken(token);
		Object clms = claims.getClaim("sourceKeyRing");
		if (clms != null) {
			sourceKeyRing = mapper.readValue((mapper.writer().writeValueAsString(clms)), SourceKeyRing.class);
		}
		return sourceKeyRing;
	}


}
