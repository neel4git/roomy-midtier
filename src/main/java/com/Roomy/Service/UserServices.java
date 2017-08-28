package com.Roomy.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.Roomy.Request.Domain.ForgetPassword;
import com.Roomy.Request.Domain.UserRequest;
import com.Roomy.Response.Domain.UserDetails;
import com.Roomy.Util.AESEncryptionUtil;
import com.Roomy.Util.JwtKeyUtil;
import com.Roomy.Util.RoomyUtil;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;
import com.Roomy.domain.SourceKeyRing;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

@RestController
public class UserServices {

	

	@Autowired
	EntityManager entityManager;
	@Autowired
	AESEncryptionUtil aESEncryptionUtil;
	@Autowired
	UserPobyteJdbc userPobyteJdbc;
	public Response responseMessage;
	
	@RequestMapping(value = "/userRegistration", method = RequestMethod.POST, produces = "application/json")
	public Response userRegisterAndAuth(@RequestBody UserRequest userRequest) throws Exception {
		try{
			
			int OTPAuth = RoomyUtil.generateOTP();
			//Sign in Module
			if(userRequest.getAction().equals("SIGNIN")){
				responseMessage = userPobyteJdbc.userLogin(userRequest);
			if(responseMessage.getStatus().equals("0001")){
				if(userRequest.getLoginType().equals("APP") || userRequest.getLoginType().equals("FB") || userRequest.getLoginType().equals("GMAIL")){
				Object result =  responseMessage.getResult();
				String token = generateCustomerToken(userRequest, OTPAuth);
				if (userRequest.getConactNumber() != null) {
					userPobyteJdbc.updateToken(userRequest.getConactNumber(), token);	
				} else {
					userPobyteJdbc.updateToken(userRequest.getEmailId(), token);
				}
				
				responseMessage = new Response("0001", null,token, result);
				}else{
					responseMessage = new Response("0001", null,userRequest.getToken(), responseMessage.getResult());
				}
			}else{
				responseMessage = new Response(responseMessage.getStatus(), null,null, null);
			}
			}
			
			
			
		// Register module
			
		if(userRequest.getAction().equals("SIGNUP")){
		responseMessage = userPobyteJdbc.userRegistration(userRequest);
		if(responseMessage.getStatus().equals("0001")){
			
			int userId = (int)responseMessage.getResult();
			userRequest.setUserId(userId);
			userRequest.setOtp(OTPAuth);
			
				responseMessage = new Response("0009", null, generateCustomerToken(userRequest, OTPAuth),userRequest);	
			
			
		}else{
			if(responseMessage.getStatusMessage().equals("0002")){
				responseMessage = new Response(0002, null, null,null);
			}else{
				if(responseMessage.getStatus().equals("0003")){
					responseMessage = new Response(0003, null, null,null);
				}
			}
		}
		}
		//OTP module
			if(userRequest.getAction().equals("OTP")){
				SourceKeyRing sourceKeyRing = decryptyToken(userRequest.getToken());
				// if otp didnot matched
				if (userRequest.getOtp() != sourceKeyRing.getOtp()) {
					responseMessage = new Response("0007", null,userRequest.getToken(), null);
				}
				// if OTP issued time is excceded greater than 15 minutes
				else if (RoomyUtil.getOtpIssueTimeDiffrence(sourceKeyRing.getOtpIssuedTime()) > 15) {
					responseMessage = new Response("0008", null,userRequest.getToken(), null);
				}else{
					responseMessage =userPobyteJdbc.updateuserStatus(userRequest.getUserId());
					if(responseMessage.getStatus().equals("0001")){
						responseMessage = new Response("0001", null,userRequest.getToken(), userPobyteJdbc.getUserprofileById(userRequest));
					}
				}
			}
			if(userRequest.getAction().equals("RESENDOTP")){
				String token = generateCustomerToken(userRequest, OTPAuth);
				if (userRequest.getConactNumber() != null && userRequest.getConactNumber().trim().length()>0) {
					userPobyteJdbc.updateToken(userRequest.getConactNumber(), token);	
				} else {
					userPobyteJdbc.updateToken(userRequest.getEmailId(), token);
				}
				UserDetails userDetails = userPobyteJdbc.getUserprofileById(userRequest);
				userRequest.setUserId((int) userDetails.getUserID());
				userRequest.setOtp(OTPAuth);
				responseMessage = new Response("0009", null, generateCustomerToken(userRequest, OTPAuth),userRequest);
			}
		
		}catch(Exception e){
			System.out.println("In Userservice Entered into Catch Block");
			System.out.println(e);
			return responseMessage = new Response("0011", null,null, null);
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

	/*@RequestMapping(value = "/getHotelsbyLocation", method = RequestMethod.POST, produces = "application/json")
	public Response getHotels(@RequestBody HotelsbyLocationRequest locationRequest) throws JsonProcessingException, SQLException {

		if (userPobyteJdbc.validateJwtToken(locationRequest.getJwtToken())) {
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

		} else {
			responseMessage = new Response("0011", null, "", "");

		}
		return responseMessage;
	}
*/
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
	
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST, produces = "application/json")
	public Response forgetPassword(@RequestBody ForgetPassword forgetPassword)
			throws JsonProcessingException, SQLException {

		try{
			responseMessage = 	userPobyteJdbc.forgetPassword(forgetPassword);
			
		}catch(Exception e ){
			System.out.printf("Exception in FORGETPASSWORD",e);
			
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
	
	private String generateCustomerToken(UserRequest userRequest, int OTPAuth) throws JOSEException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		// Set the SourceKeyRing vale to generate the Key
		SourceKeyRing keyRing = new SourceKeyRing();
		
		keyRing.setUserRequest(userRequest);
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
