package com.team4.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.demo.model.entity.RestaurantRecord;

public interface RestaurantRecordRepository extends JpaRepository<RestaurantRecord, Integer> {
//	各餐廳查詢自家的全部營業紀錄
	List<RestaurantRecord> findByRestaurant_restaurantId(Integer restaurantId);
//  各餐廳單筆修改單筆查詢紀錄 
	List<RestaurantRecord> findByRestaurantrecordIdAndRestaurantIsNotNull(Integer restaurantRecordId);
//	各餐廳之時段查詢
	List<RestaurantRecord> findByRestaurant_restaurantIdAndTimePer(Integer restaurantid,String TimePer);
	
}
 