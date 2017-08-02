package com.Roomy.Dashboard.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Response.Domain.UseRDetails;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class DashBoardLoginService {

	@Autowired
	EntityManager entityManager;

	@RequestMapping(value = "/DashBoardLogin", method = RequestMethod.POST, produces = "application/json")
	public Object getDashBoardUserDetails(@RequestParam(value = "emailID", required = false) String emailAddress,
			@RequestParam(value = "contactNumber", required = false) String contactNumber,
			@RequestParam(value = "password") String password) throws JsonProcessingException, SQLException {

		UseRDetails useRDetails = null;
		List<Object> response = new ArrayList<>();
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("GET_USER_DETAILS");
		sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_PASSWORD", String.class, ParameterMode.IN);

		if (contactNumber != null) {
			sp.setParameter("LOGIN_DETAIL", contactNumber);
		} else {
			sp.setParameter("LOGIN_DETAIL", emailAddress);
		}
		sp.setParameter("LOGIN_PASSWORD", password);

		boolean exist = sp.execute();
		if (exist == true) {
			List<Object[]> resultList = sp.getResultList();
			if (resultList.size() > 0 && resultList.contains("Failure:WrongCredentials")) {
				useRDetails = new UseRDetails();
				useRDetails.setResponse("Failure:WrongCredentials");
				response.add(useRDetails);
				return response;

			} else if (resultList.size() > 0 && resultList.contains("Failure:InactiveOrSuspendedUser")) {
				useRDetails = new UseRDetails();
				useRDetails.setResponse("Failure:InactiveOrSuspendedUser");
				response.add(useRDetails);
				return response;

			} else {
				Map<String, String> resultMap = new HashMap<String, String>(resultList.size());
				for (Object[] result : resultList) {
					useRDetails = new UseRDetails();
					useRDetails.setResponse(result[0]);
					useRDetails.setUserID(result[1]);
					useRDetails.setContactNumber(result[2]);
					useRDetails.setEmailAddress(result[3]);
					useRDetails.setFirst_Name(result[4]);
					useRDetails.setMidle_Name(result[5]);
					useRDetails.setLast_Name(result[6]);
					useRDetails.setUser_type(result[7]);
					useRDetails.setMemberShip_type(result[8]);
					useRDetails.setIdentityCardType(result[9]);
					useRDetails.setIdentityCardNumber(result[10]);
					useRDetails.setCompanyName(result[11]);
					useRDetails.setEmergencyContactNumber1(result[12]);
					useRDetails.setEmergencyContactNumber2(result[13]);
					useRDetails.setDateOfBirth(result[14]);
					useRDetails.setCityPrefrence(result[15]);
					useRDetails.setSmsNotificationPrefrences(result[16]);
					useRDetails.setEmailNotificationPrefrences(result[17]);
					useRDetails.setUserStatus(result[18]);
					useRDetails.setUserTokenValue(result[19]);

					response.add(useRDetails);
				}

			}

		} else {
			throw new RuntimeException("No result set(s) returned from the stored procedure");
		}
		return response;
	}

}
