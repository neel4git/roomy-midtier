package com.Roomy.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.Roomy.domain.UserMaster;

public interface UserRepository extends CrudRepository<UserMaster, Long> {

	@Query("SELECT usermst FROM User_Master usermst  WHERE usermst.userId=?1 and usermst.loginPassword=?2")
	UserMaster getUserDetails(String userId, String loginPassword);

	@SuppressWarnings("unchecked")
	@Transactional
	public UserMaster save(UserMaster usermaster);

}
