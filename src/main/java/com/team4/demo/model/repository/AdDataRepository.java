package com.team4.demo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team4.demo.model.entity.AdData;

@Repository
public interface AdDataRepository extends JpaRepository<AdData, Integer> {
	 List<AdData> findByRestaurant_RestaurantId(Integer restaurantId);
	 
	 void deleteByRestaurant_RestaurantId(Integer RestaurantId);
}
