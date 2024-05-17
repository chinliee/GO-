package com.team4.demo.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.demo.model.entity.RestaurantCkEditor;


public interface RestaurantCkEditorRepository extends JpaRepository<RestaurantCkEditor, Integer> {

	List<RestaurantCkEditor> findByRestaurant_restaurantId(Integer restaurantId);
	
	Optional<RestaurantCkEditor>findByRestaurant_RestaurantId(Integer restaurantId);
	
}
