package com.Roomy.Dashboard.Service;

/*
 * This is service for login  into dashboard by passing the email/Phone number and password 
 */
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Dao.UserPobyteJdbc;
import com.Roomy.Request.Domain.LoginRequest;
import com.Roomy.Service.Conversions.ResponseBuilder;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;

@RestController
public class DashBoardLoginService {
	private final static Logger LOGGER = Logger.getLogger(DashBoardLoginService.class.getName());

	@Autowired
	UserPobyteJdbc userPobyteJdbc;
	@Autowired
	ResponseBuilder responseBuilder;

	@RequestMapping(value = "/DashBoardLogin", method = RequestMethod.POST, produces = "application/json")
	public Object getDashBoardUserDetails(@RequestBody LoginRequest loginRequest) throws SQLException {
		LOGGER.info("Entered into getDashBoardUserDetails methof for login into the dashboard with Email as  "
				+ loginRequest.getEmailId());
		Response dashBoardUserDetailsResponse = null;
		try {
			List<Object[]> dashboardUserDeatils = userPobyteJdbc.getDashBoardUserDetails(loginRequest);
			dashBoardUserDetailsResponse = responseBuilder.buildDashBoardLoginResponse(dashboardUserDeatils);
		} catch (Exception exception) {
			LOGGER.error("Exception occurred while Login into dashboard contact Admin", exception);
			dashBoardUserDetailsResponse = new Response(ResponseStatus.FAILURE_CODE, "Failure:InactiveOrSuspendedUser",
					null, null);
		}
		return dashBoardUserDetailsResponse;
	}
}
