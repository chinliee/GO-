package com.team4.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.dto.restaurantCkEditor.RestaurantCkEditorContent;
import com.team4.demo.model.dto.restaurantCkEditor.RestaurantCkEditorInsertDto;
import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.model.entity.RestaurantCkEditor;
import com.team4.demo.model.repository.RestaurantCkEditorRepository;
import com.team4.demo.model.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RestaurantCkEditorService {

	@Autowired
	private RestaurantCkEditorRepository restaurantCkEditorRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;

	public String ckEditorSaveUpdate(Integer restaurantId, RestaurantCkEditorInsertDto dto) {
	Optional<RestaurantCkEditor> rOptional = restaurantCkEditorRepository.findByRestaurant_RestaurantId(restaurantId);
	   		
	if (rOptional.isEmpty()) {
	RestaurantCkEditor restaurantCkEditor = new RestaurantCkEditor();   
	restaurantCkEditor.setHtml(dto.getHtml());
	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	if (restaurant != null) {
	restaurantCkEditor.setRestaurant(restaurant);
	}
	restaurantCkEditorRepository.save(restaurantCkEditor);
	} else {
	RestaurantCkEditor existCkEditor = rOptional.get();
	existCkEditor.setHtml(dto.getHtml());
	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	if (restaurant != null) {
	existCkEditor.setRestaurant(restaurant);
	}
	restaurantCkEditorRepository.save(existCkEditor);
	}
	    
	return "已更新";           
	}
//	顯示
	public List<RestaurantCkEditorContent> findByRestaurantId(Integer restaurantId) {
		 
	List<RestaurantCkEditor> restaurantCkEditorsList = restaurantCkEditorRepository.findByRestaurant_restaurantId(restaurantId);
	
	List<RestaurantCkEditorContent> ckEditorContentsList = new ArrayList<>();
	
	for(RestaurantCkEditor ckEditor : restaurantCkEditorsList) {
	
	RestaurantCkEditorContent restaurantCkEditorContent = new RestaurantCkEditorContent();
	
	restaurantCkEditorContent.setHtml(ckEditor.getHtml());
	restaurantCkEditorContent.setName(ckEditor.getRestaurant().getName());
	restaurantCkEditorContent.setIntroduce(ckEditor.getRestaurant().getIntroduce());
	restaurantCkEditorContent.setCounty(ckEditor.getRestaurant().getCounty());
	restaurantCkEditorContent.setDistrict(ckEditor.getRestaurant().getDistrict());
	restaurantCkEditorContent.setAddress(ckEditor.getRestaurant().getAddress());
	restaurantCkEditorContent.setPhone(ckEditor.getRestaurant().getPhone());
	restaurantCkEditorContent.setOpenTime(ckEditor.getRestaurant().getOpenTime());
	restaurantCkEditorContent.setCloseTime(ckEditor.getRestaurant().getCloseTime());

	ckEditorContentsList.add(restaurantCkEditorContent);
		
	}
	
	

	
	return ckEditorContentsList;
	}

	    
	public RestaurantCkEditor findOrCreate(Integer restaurantId) {
	Optional<RestaurantCkEditor> rOptional = restaurantCkEditorRepository.findByRestaurant_RestaurantId(restaurantId);

	if (rOptional.isEmpty()) {
	RestaurantCkEditor restaurantCkEditor = new RestaurantCkEditor();
	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	if (restaurant != null) {
	restaurantCkEditor.setRestaurant(restaurant);
	restaurantCkEditor.setHtml("<p>請編輯你的頁面<p>");
	restaurantCkEditorRepository.save(restaurantCkEditor);
	return restaurantCkEditor;
	} else {       
	 
	return null;
	}
	} else {
	
	return rOptional.get();
	}
	}	 	
	}
