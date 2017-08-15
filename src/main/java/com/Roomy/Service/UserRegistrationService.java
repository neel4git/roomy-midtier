package com.Roomy.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Repository.UserRepository;
import com.Roomy.Request.Domain.AutheticateUserRequest;
import com.Roomy.Request.Domain.LoginRequest;
import com.Roomy.Request.Domain.UserRegistrationRequest;
import com.Roomy.Response.Domain.LoginResponse;
import com.Roomy.Util.AESEncryptionUtil;
import com.Roomy.Util.JwtKeyUtil;
import com.Roomy.Util.RoomyUtil;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;
import com.Roomy.domain.SourceKeyRing;
import com.Roomy.domain.UserMaster;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

@RestController
public class UserRegistrationService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	AESEncryptionUtil aESEncryptionUtil;
	private Response response;
	private final static Logger LOGGER = Logger.getLogger(UserRegistrationService.class.getName());

/*	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public Object registerUser(@RequestBody UserMaster userMaster) {
		LOGGER.info("Entered into registerUser Service ");
		int OTPAuth = 0;
		UserMaster userDetailsFromDatabase = null;
		try {
			// Check if the User Already Register with Pobyte Or Not
			userDetailsFromDatabase = userRepository.getUserDetailsByEmailIdOrMobile(userMaster.getEmailAddress(),
					userMaster.getContactNumber());
			if (userDetailsFromDatabase != null) {
				LOGGER.info("User Already registerd in the Pobye Databse with Email is as "
						+ userMaster.getEmailAddress() + "with mobile number as " + userMaster.getContactNumber());
				response = new Response(ResponseStatus.FAILURE_CODE, "User already registered with Pobye", null, null);
			}
			// New user Register with the system
			else {
				userMaster.setLoginPassword(aESEncryptionUtil.encrypt(userMaster.getLoginPassword()));
				OTPAuth = RoomyUtil.generateOTP();
				LOGGER.info("OTP generated for the given customer is " + OTPAuth + "for the user ");
				response = new Response(ResponseStatus.SUCCESS_CODE, null, generateCustomerToken(userMaster, OTPAuth),
						OTPAuth);
			}
		} catch (Exception exception) {
			LOGGER.error("Some Exception occurred in processing the RegisterUser Service", exception);
			return new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.OTP_EXCEPTION_MESSAGE, null, OTPAuth);
		}
		return response;
	}
*/
	/*@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST)
	public Object authenticateUser(@RequestBody AutheticateUserRequest autheticateUserRequest) {
		LOGGER.info("Entered into authenticateUser Service ");
		try {
			SourceKeyRing sourceKeyRing = decryptyToken(autheticateUserRequest.getCustomerToken());
			// if otp didnot matched
			if (autheticateUserRequest.getOtp().equals(sourceKeyRing.getOtp())) {
				response = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.WRONG_OTP_EXCEPTION,
						autheticateUserRequest.getCustomerToken(), null);
			}
			// if OTP issued time is excceded greater than 15 minutes
			else if (RoomyUtil.getOtpIssueTimeDiffrence(sourceKeyRing.getOtpIssuedTime()) > 15) {
				response = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.OTP_EXPIRED_EXCEPTION,
						autheticateUserRequest.getCustomerToken(), null);
			} else {
				UserMaster userMaster = userRepository.save(sourceKeyRing.getUserMaster());
				if (userMaster != null) {
					response = new Response(ResponseStatus.SUCCESS_CODE, "User Registered Sucessfully",
							autheticateUserRequest.getCustomerToken(), null);
				} else {
					response = new Response(ResponseStatus.FAILURE_CODE, "User Already Registered Please login",
							autheticateUserRequest.getCustomerToken(), null);
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Some Exception occurred in processing the authenticateUser Service", exception);
			response = new Response(ResponseStatus.FAILURE_CODE,
					"Some Exception occurred in processing the authenticateUser Service",
					autheticateUserRequest.getCustomerToken(), null);
		}
		return response;
	}*/
/*
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object getCustomerDetails(@RequestBody LoginRequest loginRequest) {
		LOGGER.info("Entered into Login Service  withe the emnail ID" + loginRequest.getEmailId());
		UserMaster userMaster = null;
		String userPassword;
		LoginResponse loginResponse = new LoginResponse();
		try {
			userPassword = aESEncryptionUtil.encrypt(loginRequest.getPassword());
			// User is trying to LOgin with EmailID
			if (loginRequest.getEmailId() != null && loginRequest.getEmailId().trim().length() > 0) {
				userMaster = (UserMaster) userRepository.getUserDetailsByEmailID(loginRequest.getEmailId(),
						userPassword);
			}
			// User is Trying to Login with Mobile
			else {
				userMaster = (UserMaster) userRepository.getUserDetailsByMobileNumber(loginRequest.getMobileNumber(),
						userPassword);
			}
			if (userMaster != null) {
				// Record Found In Database
				loginResponse.setEmailAddress(userMaster.getEmailAddress());
				loginResponse.setContactNumber(userMaster.getContactNumber());
				loginResponse.setFirstName(userMaster.getFirstName());
				loginResponse.setMiddleName(userMaster.getMiddleName());
				loginResponse.setLastName(userMaster.getLastName());
				response = new Response(ResponseStatus.SUCCESS_CODE, "Authenticated User",
						generateCustomerToken(userMaster, 0), loginResponse);

			} else {
				// Record not found
				response = new Response(ResponseStatus.FAILURE_CODE, ResponseStatus.USER_RECORDS_NOT_FOUND, null, null);
			}

		} catch (Exception e) {
			LOGGER.error("Exception occurred in fetching the User Details from the Database", e);
			response = new Response(ResponseStatus.FAILURE_CODE, "Exception Occurred in fecthing the user Details",
					null, null);
		}
		return response;

	}
*/
	/*private String generateCustomerToken(UserRegistrationRequest userRegistrationRequest, int OTPAuth) throws JOSEException {
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
*/}
