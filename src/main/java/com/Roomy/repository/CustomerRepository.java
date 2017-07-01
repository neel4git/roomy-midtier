package com.Roomy.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.Roomy.domain.CustomerMaster;

public interface CustomerRepository extends
		CrudRepository<CustomerMaster, Long> {

	@Query("SELECT cst FROM Customer_Master cst  WHERE cst.customerName=?1 and cst.customerPassword=?2")
	CustomerMaster getCustomerDetails(String userName, String userPassword);

	@SuppressWarnings("unchecked")
	@Transactional
	public CustomerMaster save(CustomerMaster customerMaster);

}
