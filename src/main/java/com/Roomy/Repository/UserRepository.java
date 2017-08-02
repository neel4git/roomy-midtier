package com.Roomy.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.Roomy.domain.UserMaster;

public interface UserRepository extends CrudRepository<UserMaster, Long> {

	/*
	 * @Query("SELECT usermst FROM User_Master usermst  WHERE usermst.contactNumber=?1 and usermst.loginPassword=?2"
	 * ) UserMaster getUserDetailsByMobileNumber(String contactNumber, String
	 * loginPassword);
	 */
	@Query("SELECT usermst FROM User_Master usermst  WHERE (usermst.emailAddress=?1 and usermst.loginPassword=?2) or (usermst.emailAddress=?1 or  usermst.contactNumber=?3)")
	UserMaster getUserDetails(String emailD, String loginPassword, String mobileNumber);

	@Query("SELECT usermst FROM User_Master usermst  WHERE usermst.emailAddress=?1 or  usermst.contactNumber=?2")
	UserMaster getUserDetailsByEmailIdOrMobile(String emailID, String mobileNumber);

	@SuppressWarnings("unchecked")
	@Transactional
	public UserMaster save(UserMaster usermaster);

	@Query("SELECT a from User_Master a WHERE a.contactNumber=?1")
	public UserMaster getUserProfileDetails(String mobileNumber);

}
