package com.Roomy.Dashboard.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Dao.UserPobyteJdbc;
import com.Roomy.Service.Conversions.ResponseBuilder;

/*
 * DashBoardCustomerDetails can get the details of the reservations(Customers) of the given hotel based on number of days.
 */

@RestController
public class DashBoardCustomerDetails {

	@Autowired
	UserPobyteJdbc userPobyteJdbc;
	@Autowired
	ResponseBuilder responseBuilder;

	@RequestMapping(value = "/getCustomers", method = RequestMethod.POST, produces = "application/json")
	public Object getDashBoardCustomers(@RequestParam(value = "hotel_id", required = true) int hotel_id,
			@RequestParam(value = "previousBookings", required = false) String previousBookings) {

		return null;
	}
}
