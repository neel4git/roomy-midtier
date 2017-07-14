package com.Roomy.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.Roomy.domain.Hotel_Master;

public interface Hotel_MasterRepository extends CrudRepository<Hotel_Master, Long> {

	@Query("SELECT hotelMaster FROM Hotel_Master hotelMaster  WHERE hotelMaster.hotel_City=?1")
	List<Hotel_Master> getHotelMasterDetails(String hotel_City);

}
