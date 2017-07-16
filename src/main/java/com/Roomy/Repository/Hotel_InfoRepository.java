package com.Roomy.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.Roomy.domain.Hotel_Info;

public interface Hotel_InfoRepository extends CrudRepository<Hotel_Info, Long> {

		
	@Query("SELECT hotelInfo FROM Hotel_Info hotelInfo  WHERE hotelInfo.hotel_Id=?1")
	Hotel_Info getHotelInfo(int hotel_id);

}
