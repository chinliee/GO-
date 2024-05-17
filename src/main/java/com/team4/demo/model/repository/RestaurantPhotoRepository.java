package com.team4.demo.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.demo.model.entity.RestaurantPhoto;

public interface RestaurantPhotoRepository extends JpaRepository<RestaurantPhoto, Integer> {

	Optional<RestaurantPhoto> findByRestaurant_restaurantId(Integer restaurantId);
	
	RestaurantPhoto findByRestaurantRestaurantIdAndPhotoMain(Integer restaurantId, Integer photoMain);

	List<RestaurantPhoto>  findByPhotoMain(Integer photoMain);

    List<RestaurantPhoto> findByRestaurant_CountyAndRestaurant_Style(String county, String style);

    List<RestaurantPhoto> findByRestaurant_RestaurantId(Integer restaurantId);
}
