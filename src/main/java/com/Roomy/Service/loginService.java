package com.Roomy.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Response.DTO.MetaData;
import com.Roomy.Util.AESEncryptionUtil;
import com.Roomy.Util.MailUtil;
import com.Roomy.domain.CustomerMaster;
import com.Roomy.repository.CustomerRepository;

@RestController
public class loginService {

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	AESEncryptionUtil aESEncryptionUtil;
	@Autowired
	MetaData metaData;
	@Autowired
	MailUtil mailUtil;

	@RequestMapping(value = "/getCustomerDetails", method = RequestMethod.GET)
	public CustomerMaster getCustomerDetails(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "userPassword", required = true) String userPassword)
			throws Exception {
		CustomerMaster customerMaster = null;
		try {
			userPassword = aESEncryptionUtil.encrypt(userPassword);
			customerMaster = (CustomerMaster) customerRepository
					.getCustomerDetails(userName, userPassword);

		} catch (Exception e) {
			throw new Exception("CustomeDetailsNOt found");

		}
		return customerMaster;

	}

	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public MetaData getMemberDetailsByPsaId(
			@RequestBody CustomerMaster customerMaster) {
		CustomerMaster customerDeatils = null;
		if (customerMaster.getCustomerName() != null
				&& customerMaster.getCustomerPassword() != null) {
			// Step1 :morph the password usingAES alogorith and save to DB
			try {
				customerMaster.setCustomerPassword(aESEncryptionUtil
						.encrypt(customerMaster.getCustomerName()));
			} catch (Exception e) {
				metaData.setCode("500");
				metaData.setFailureMessage("Password Decryption faile");
			}
			customerDeatils = (CustomerMaster) customerRepository
					.save(customerMaster);
		}
		if (customerDeatils != null) {
			// send the Welcome Mail back to User
			mailUtil.generateCustomeWelcomeMail(customerDeatils.getEmail_Id(),
					"Welcome To Roomy", "WelcomeMail");
			metaData.setCode("200");
			metaData.setFailureMessage("Record Sucessfully saved");

		} else {
			metaData.setCode("400");
			metaData.setFailureMessage("Record Sucessfully Not saved Please contact Adminstrator");
		}
		return metaData;

	}

}
