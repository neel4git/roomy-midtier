package com.Roomy.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Repository.UserRepository;
import com.Roomy.Response.DTO.MetaData;
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

	private final static Logger LOGGER = Logger
			.getLogger(UserRegistrationService.class.getName());

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public Object getCustomerDetails(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "userPassword", required = true) String userPassword)
			throws Exception {
		LOGGER.info("Entered into Login Service to get the user Names as "
				+ userName);
		UserMaster userMaster = null;
		try {
			userPassword = aESEncryptionUtil.encrypt(userPassword);
			userMaster = (UserMaster) userRepository.getUserDetails(userName,
					userPassword);
			if (userMaster == null) {
				metaData.setFailureMessage("No userFound with these details");
				return metaData;
			}
		} catch (Exception e) {
			LOGGER.error("Some Exception occurred in processing", e);
			throw new Exception("CustomeDetailsNOt found");
		}
		return userMaster;

	}

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public MetaData getMemberDetailsByPsaId(@RequestBody UserMaster userMaster) {
		LOGGER.info("Entered into registerUser Service ");
		int OTPAuth = 0;
		MetaData metaData = new MetaData();
		try {
			userMaster.setLoginPassword(aESEncryptionUtil.encrypt(userMaster
					.getLoginPassword()));
			OTPAuth = RoomyUtil.generateOTP();
			LOGGER.info("OTP generated for the given customer is " + OTPAuth
					+ "for the user " + userMaster.getUserId());
			metaData.setOtp(OTPAuth);
			metaData.setCustomerToken(generateCustomerToken(userMaster, OTPAuth));
		} catch (JOSEException jOSEException) {
			LOGGER.error("Password decryption error occurred ", jOSEException);
			metaData.setFailureMessage("Token Creation Exception occurred");
			return metaData;
		} catch (Exception exception) {
			LOGGER.error(
					"Some Exception occurred in processing the RegisterUser Service",
					exception);
			metaData.setFailureMessage("Some Exception Occurted while generating the OTP Please contact ADmin");
			return metaData;
		}

		return metaData;
	}

	@RequestMapping(value = "/authenticateUser", method = RequestMethod.GET)
	public MetaData authenticateUser(
			@RequestParam(value = "otp", required = true) String otp,
			@RequestHeader(value = "customerToken") String customertToken) {
		LOGGER.info("Entered into authenticateUser Service ");
		try {
			SourceKeyRing sourceKeyRing = decryptyToken(customertToken);
			MetaData metaData = new MetaData();
			// if otp didnot matched
			if (otp.equals(sourceKeyRing.getOtp())) {
				metaData.setFailureMessage("OTP erntered is wrong please enter the valid otp");
				metaData.setCustomerToken(customertToken);
				return metaData;
			}
			// if OTP issued time is excceded greater than 15 minutes
			else if (RoomyUtil.getOtpIssueTimeDiffrence(sourceKeyRing
					.getOtpIssuedTime()) > 15) {
				metaData.setResponseMessage("Token entered is Expired Please Reegister again");
				metaData.setCustomerToken(customertToken);
				return metaData;
			} else {
				UserMaster userMaster = userRepository.save(sourceKeyRing
						.getUserMaster());
				if (userMaster != null) {
					metaData.setResponseMessage("User Registered Sucessfully");
					metaData.setCustomerToken(customertToken);
					return metaData;
				} else {
					metaData.setFailureMessage("User Already Registered Please login");
					metaData.setCustomerToken(customertToken);
					return metaData;
				}

			}
		} catch (Exception exception) {
			LOGGER.error(
					"Some Exception occurred in processing the authenticateUser Service",
					exception);
			metaData.setFailureMessage("Some Exception Occurred Please contact Admin");
			metaData.setCustomerToken(customertToken);
			return metaData;

		}
	}

	private String generateCustomerToken(UserMaster userMaster, int OTPAuth)
			throws JOSEException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		// Set the SourceKeyRing vale to generate the Key
		SourceKeyRing kerRing = new SourceKeyRing();
		kerRing.setUserMaster(userMaster);
		kerRing.setOtp(OTPAuth);
		kerRing.setOtpIssuedTime(dateFormat.format(date));
		return JwtKeyUtil.createJWT(kerRing);
	}

	private SourceKeyRing decryptyToken(String token) throws ParseException,
			JOSEException, JsonParseException, JsonMappingException,
			IOException {
		SourceKeyRing sourceKeyRing = null;
		ObjectMapper mapper = new ObjectMapper();
		JWTClaimsSet claims = (JWTClaimsSet) JwtKeyUtil.decryptToken(token);
		Object clms = claims.getClaim("sourceKeyRing");
		if (clms != null) {
			sourceKeyRing = mapper.readValue(
					(mapper.writer().writeValueAsString(clms)),
					SourceKeyRing.class);
		}
		return sourceKeyRing;
	}
}
