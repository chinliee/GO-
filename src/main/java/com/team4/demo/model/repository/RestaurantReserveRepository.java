package com.team4.demo.model.repository;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.model.entity.RestaurantReserve;

public interface RestaurantReserveRepository extends JpaRepository<RestaurantReserve, Integer> {

	List<RestaurantReserve> findByRestaurant_restaurantId(Integer restaurantId);
	
	Optional<RestaurantReserve>findByRestaurantSeat_seatState(String state);
	
	Optional<Restaurant> findByRestaurant_RestaurantId(Integer restaurantId);

	List<RestaurantReserve> findByRestaurant_RestaurantIdAndMember_Phone(Integer restaurantId,String phone);
	
	List<RestaurantReserve> findByReserveDay(LocalDate reserveDay);
//	日期前後
	List<RestaurantReserve> findByRestaurant_restaurantIdAndReserveDayBetween(Integer restaurantId, LocalDate startDate, LocalDate endDate);
//	當月電話
	List<RestaurantReserve> findByRestaurant_RestaurantIdAndMember_PhoneAndReserveDayBetween(Integer restaurantId,String phone, LocalDate startMonth, LocalDate endMonth);
//	當月姓名
	List<RestaurantReserve> findByRestaurant_RestaurantIdAndMember_NameAndReserveDayBetween(Integer restaurantId,String Name, LocalDate startMonth, LocalDate endMonth);

	List<RestaurantReserve> findByReserveDayBetween(LocalDate startDay, LocalDate endDay);
	
    List<RestaurantReserve> findByMember_MemberIdAndStateIn(Integer memberId, List<String> states);

//	查詢訂單最小月
	@Query("SELECT MIN(rr.reserveDay) FROM RestaurantReserve rr JOIN rr.member m WHERE m.phone = :phone AND rr.restaurant.restaurantId = :restaurantId")
	LocalDate findEarliestReservationDateByPhone(String phone,Integer restaurantId);
//	查詢訂單最大月
	@Query("SELECT MAX(rr.reserveDay) FROM RestaurantReserve rr JOIN rr.member m WHERE m.phone = :phone AND rr.restaurant.restaurantId = :restaurantId")
	LocalDate findLatestReservationDateByPhone(String phone,Integer restaurantId);	
//	HQL	
	 @Query("SELECT MONTH(r.reserveDay), SUM(r.amount) " +
	           "FROM RestaurantReserve r " +
	           "WHERE r.state = '已結單' AND r.member.memberId = :memberId " +
	           "GROUP BY YEAR(r.reserveDay), MONTH(r.reserveDay) " +
	           "ORDER BY YEAR(r.reserveDay) DESC, MONTH(r.reserveDay)")
	List<Object[]> getMonthlySummary(@Param("memberId") Integer memberId);
	
	 @Query("SELECT MONTH(r.reserveDay), SUM(r.amount) " +
	           "FROM RestaurantReserve r " +
	           "WHERE r.state = '已結單' AND r.restaurant.restaurantId = :restaurantId " +
	           "GROUP BY YEAR(r.reserveDay), MONTH(r.reserveDay) " +
	           "ORDER BY YEAR(r.reserveDay) DESC, MONTH(r.reserveDay)")
	 List<Object[]> getMonthlyResttaurant(@Param("restaurantId") Integer restaurantId);
	
	
		
	List<RestaurantReserve> findByMember_MemberIdAndState(Integer memberId,String state);
	
	List<RestaurantReserve> findByRestaurant_restaurantIdAndState(Integer restaurantId,String state);

	@Query("SELECT MIN(rr.reserveDay) FROM RestaurantReserve rr JOIN rr.member m WHERE m.name = :name AND rr.restaurant.restaurantId = :restaurantId")
	LocalDate findEarliestReservationDateByName(String name ,Integer restaurantId);

	@Query("SELECT MAX(rr.reserveDay) FROM RestaurantReserve rr JOIN rr.member m WHERE m.name = :name AND rr.restaurant.restaurantId = :restaurantId")
	LocalDate findLatestReservationDateByName(String name,Integer restaurantId);
		
	}
 

