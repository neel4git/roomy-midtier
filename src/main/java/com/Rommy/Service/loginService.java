package com.Rommy.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Rommy.domain.Customer;
import com.Rommy.repository.CustomerRepository;

@RestController
@RequestMapping("/getUserDetails")
public class loginService {

	@Autowired
	CustomerRepository customerRepository;

	@RequestMapping(method = RequestMethod.GET)
	public String getMemberDetailsByPsaId(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String userPassword) {

		Customer user = (Customer) customerRepository.getCustomerDetails(
				userName, userPassword);

		if (user == null) {
			return "invalid" + userName;
		} else {
			return "Hellow" + userName;
		}

	}
}
