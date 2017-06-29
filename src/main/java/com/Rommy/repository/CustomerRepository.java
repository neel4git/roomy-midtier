package com.Rommy.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.Rommy.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query("SELECT customer FROM Customer customer  WHERE customer.customerName=?1 and customer.customerPassword=?2")
	Customer getCustomerDetails(String userName, String userPassword);

}
