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
import com.Roomy.Response.Domain.LoginResponse;
import com.Roomy.Response.Domain.MetaData;
import com.Roomy.Util.AESEncryptionUtil;
import com.Roomy.Util.JwtKeyUtil;
import com.Roomy.Util.RoomyUtil;
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
	@Autowired
	MetaData metaData;

	private final static Logger LOGGER = Logger.getLogger(UserRegistrationService.class.getName());

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public MetaData registerUser(@RequestBody UserMaster userMaster) {
		LOGGER.info("Entered into registerUser Service ");
		int OTPAuth = 0;
		MetaData metaData = new MetaData();
		try {
			userMaster.setLoginPassword(aESEncryptionUtil.encrypt(userMaster.getLoginPassword()));
			OTPAuth = RoomyUtil.generateOTP();
			LOGGER.info("OTP generated for the given customer is " + OTPAuth + "for the user ");
			metaData.setOtp(OTPAuth);
			metaData.setCustomerToken(generateCustomerToken(userMaster, OTPAuth));
		} catch (JOSEException jOSEException) {
			LOGGER.error("Password decryption error occurred ", jOSEException);
			metaData.setFailureMessage("Token Creation Exception occurred");
			return metaData;
		} catch (Exception exception) {
			LOGGER.error("Some Exception occurred in processing the RegisterUser Service", exception);
			metaData.setFailureMessage("Some Exception Occurted while generating the OTP Please contact ADmin");
			return metaData;
		}
		return metaData;
	}

	@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST)
	public MetaData authenticateUser(@RequestBody AutheticateUserRequest autheticateUserRequest) {
		LOGGER.info("Entered into authenticateUser Service ");
		try {
			SourceKeyRing sourceKeyRing = decryptyToken(autheticateUserRequest.getCustomerToken());
			MetaData metaData = new MetaData();
			// if otp didnot matched
			if (autheticateUserRequest.getOtp().equals(sourceKeyRing.getOtp())) {
				metaData.setFailureMessage("OTP erntered is wrong please enter the valid otp");
				metaData.setCustomerToken(autheticateUserRequest.getCustomerToken());
				return metaData;
			}
			// if OTP issued time is excceded greater than 15 minutes
			else if (RoomyUtil.getOtpIssueTimeDiffrence(sourceKeyRing.getOtpIssuedTime()) > 15) {
				metaData.setResponseMessage("Token entered is Expired Please Reegister again");
				metaData.setCustomerToken(autheticateUserRequest.getCustomerToken());
				return metaData;
			} else {
				UserMaster userMaster = userRepository.save(sourceKeyRing.getUserMaster());
				if (userMaster != null) {
					metaData.setResponseMessage("User Registered Sucessfully");
					metaData.setCustomerToken(autheticateUserRequest.getCustomerToken());
					return metaData;
				} else {
					metaData.setFailureMessage("User Already Registered Please login");
					metaData.setCustomerToken(autheticateUserRequest.getCustomerToken());
					return metaData;
				}

			}
		} catch (Exception exception) {
			LOGGER.error("Some Exception occurred in processing the authenticateUser Service", exception);
			metaData.setFailureMessage("Some Exception Occurred Please contact Admin");
			metaData.setCustomerToken(autheticateUserRequest.getCustomerToken());
			return metaData;

		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginResponse getCustomerDetails(@RequestBody LoginRequest loginRequest) {
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
				loginResponse.setResponseData("Authenticated User");
				loginResponse.setEmailAddress(userMaster.getEmailAddress());
				loginResponse.setContactNumber(userMaster.getContactNumber());
				loginResponse.setFirstName(userMaster.getFirstName());
				loginResponse.setMiddleName(userMaster.getMiddleName());
				loginResponse.setLastName(userMaster.getLastName());
				loginResponse.setCustomerToken(generateCustomerToken(userMaster, 0));

			} else {
				// Record not found
				loginResponse.setResponseData("No records Found with Roomy Please register");

			}

		} catch (Exception e) {
			LOGGER.error("Some Exception occurred in processing", e);
			loginResponse.setResponseData("Some Exception Occurred while fetching the Detials");

		}
		return loginResponse;

	}

	private String generateCustomerToken(UserMaster userMaster, int OTPAuth) throws JOSEException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		// Set the SourceKeyRing vale to generate the Key
		SourceKeyRing keyRing = new SourceKeyRing();
		UserMaster userMasterSourceKeyRing = userMaster;
		keyRing.setUserMaster(userMasterSourceKeyRing);
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
