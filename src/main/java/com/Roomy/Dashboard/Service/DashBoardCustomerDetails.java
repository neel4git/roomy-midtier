package com.Roomy.Dashboard.Service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Dao.UserPobyteJdbc;
import com.Roomy.Service.Conversions.ResponseBuilder;
import com.Roomy.domain.Response;
import com.Roomy.domain.ResponseStatus;

/*
 * DashBoardCustomerDetails can get the details of the reservations(Customers) of the given hotel based on number of days.
 */

@RestController
public class DashBoardCustomerDetails {
	private final static Logger LOGGER = Logger.getLogger(DashBoardLoginService.class.getName());

	@Autowired
	UserPobyteJdbc userPobyteJdbc;
	@Autowired
	ResponseBuilder responseBuilder;

	@RequestMapping(value = "/getCustomers", method = RequestMethod.POST, produces = "application/json")
	public Object getDashBoardCustomers(@RequestParam(value = "hotel_id", required = true) String hotel_id,
			@RequestParam(value = "previousBookings", required = true) int previousBookingsIntervalDays) {
		Response dashBoardCustomerDetails = null;
		try {
			List<Object[]> dashboardUserDeatils = userPobyteJdbc.gteBookingsHistroy(hotel_id,
					previousBookingsIntervalDays);
			dashBoardCustomerDetails = responseBuilder.buildDashboardCustomerDetails(dashboardUserDeatils);

		} catch (Exception exception) {
			LOGGER.error("Exception occurred while Login into dashboard contact Admin", exception);
			dashBoardCustomerDetails = new Response(ResponseStatus.FAILURE_CODE, "Failure:InactiveOrSuspendedUser",
					null, null);
		}

		return dashBoardCustomerDetails;
	}
}
