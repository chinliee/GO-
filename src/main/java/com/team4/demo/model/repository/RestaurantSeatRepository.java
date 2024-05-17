package com.team4.demo.model.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.model.entity.RestaurantSeat;

public interface RestaurantSeatRepository extends JpaRepository<RestaurantSeat, Integer> {
	// 查詢自家餐廳全部座位
	List<RestaurantSeat> findByRestaurant_restaurantId(Integer restaurantId);

	// 餐廳自家單筆修改(查詢) 複合查詢
	List<RestaurantSeat> findByRestaurant_restaurantIdAndSeatId(Integer restaurantId, Integer seatId);

	// 餐廳自家查詢預約狀態
	List<RestaurantSeat> findByRestaurant_restaurantIdAndSeatState(Integer restaurantId, String seatState);

	// 餐廳自家查詢幾人桌 Containing模糊查詢
	List<RestaurantSeat> findByRestaurant_restaurantIdAndTableForContaining(Integer restaurantId, String tableFor);
	
	List<RestaurantSeat> findByRestaurant_RestaurantIdAndSeatStateAndOpenDayAndTableFor(Integer restaurantId, String seatState, LocalDate openDay,String tableFor);

//  查時段
	List<RestaurantSeat> findByRestaurant_restaurantIdAndSeatTimePer(Integer restaurantId,String seatTimePer);
//	日期前後
	List<RestaurantSeat> findByRestaurant_restaurantIdAndOpenDayBetween(Integer restaurantId, LocalDate startDate, LocalDate endDate);
	
	List<RestaurantSeat>  findByRestaurant_CountyAndRestaurant_StyleAndOpenDayAndTableForAndSeatState(String county, String style,LocalDate openDay,String tableFor, String SeatState);
	
	List<RestaurantSeat> findByRestaurantAndOpenDayAndTableFor(Restaurant restaurant, LocalDate openDay, String tableFor);
	
	List<RestaurantSeat> findByRestaurant_RestaurantIdAndSeatStateAndOpenDayAndOpenTimeAndTableFor(Integer restaurantId, String seatState, LocalDate openDay,LocalTime openTime,String tableFor);

	List<RestaurantSeat> findByRestaurant_CountyAndRestaurant_Style(String county, String style);
}
