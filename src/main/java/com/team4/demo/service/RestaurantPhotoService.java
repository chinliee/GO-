package com.team4.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.dto.restaurantPhoto.RestaurantPhotoDto;
import com.team4.demo.model.dto.restaurantPhoto.RestaurantPhotoFindDTO;
import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.model.entity.RestaurantPhoto;
import com.team4.demo.model.entity.RestaurantSeat;
import com.team4.demo.model.repository.RestaurantPhotoRepository;
import com.team4.demo.model.repository.RestaurantRepository;
import com.team4.demo.model.repository.RestaurantSeatRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RestaurantPhotoService {

	@Autowired
	private RestaurantPhotoRepository restaurantPhotoRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private RestaurantSeatRepository restaurantSeatRepository;
	

	public String savefile(RestaurantPhotoDto dto) {
	Integer restaurantId = dto.getRestuarntId();
	Optional<RestaurantPhoto> rOptional = restaurantPhotoRepository.findByRestaurant_restaurantId(restaurantId);
	    
	if (rOptional.isEmpty()) {

	RestaurantPhoto restaurantPhoto = new RestaurantPhoto();
	restaurantPhoto.setPhotoFile(dto.getPhotoFile());
	restaurantPhoto.setPhotoMain(1);
	        
	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	if (restaurant != null) {
	restaurantPhoto.setRestaurant(restaurant);
	}
	        
	restaurantPhotoRepository.save(restaurantPhoto);

	} else {

	RestaurantPhoto existingPhoto = rOptional.get();
	if (existingPhoto.getPhotoMain() == 1) {
	existingPhoto.setPhotoFile(dto.getPhotoFile());	            
	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	if (restaurant != null) {
	existingPhoto.setRestaurant(restaurant);
	}
	restaurantPhotoRepository.save(existingPhoto);
	}
	}
	return "圖片已更新";
	}

	public String getimageByid(Integer id) {
	RestaurantPhoto restaurantPhoto = restaurantPhotoRepository.findById(id).orElse(null);				
	byte[] photo = restaurantPhoto.getPhotoFile();
	return Base64.getEncoder().encodeToString(photo);
		
	}
	

	public String photomain(Integer restaurantId) {
	Optional<RestaurantPhoto> rOptional = restaurantPhotoRepository.findByRestaurant_restaurantId(restaurantId);

	if (rOptional.isPresent()) {
	RestaurantPhoto restaurantPhoto = rOptional.get();
	if (restaurantPhoto.getPhotoMain() == 1) {	        	
	byte[] photo = restaurantPhoto.getPhotoFile();

	return Base64.getEncoder().encodeToString(photo);      
	}
	}	    
	return null;
	}

	public List<RestaurantPhotoFindDTO> FindAllMainPhoto(){
	Integer photoMain = 1;
	List<RestaurantPhoto> restaurantPhotolList =restaurantPhotoRepository
			.findByPhotoMain(photoMain);
		
	 List<RestaurantPhotoFindDTO> dtoList = new ArrayList<>();
	   for (RestaurantPhoto photo : restaurantPhotolList) {
	        RestaurantPhotoFindDTO dto = new RestaurantPhotoFindDTO();
	        dto.setPhotoFile(photo.getPhotoFile());
	        dto.setRestaurantId(photo.getRestaurant().getRestaurantId());
	        dto.setName(photo.getRestaurant().getName());
	        dto.setStyle(photo.getRestaurant().getStyle());
	        dtoList.add(dto);
	    }
	
	return	dtoList;		
	}

	public List<RestaurantPhotoFindDTO> findByCountyAndStyleAndOpenDayAndTableForAndSeatState(String county, String style, LocalDate openDay, String tableFor) {
	    List<RestaurantPhotoFindDTO> dtos = new ArrayList<>();

	    if (county != null && style != null && openDay != null && tableFor != null) {
	    	List<RestaurantSeat> seats = restaurantSeatRepository.findByRestaurant_CountyAndRestaurant_StyleAndOpenDayAndTableForAndSeatState(county, style, openDay, tableFor, "未預約");
	        Set<Integer> restaurantIds = new HashSet<>();

	        for (RestaurantSeat seat : seats) {
	            restaurantIds.add(seat.getRestaurant().getRestaurantId());
	        }

	        for (Integer restaurantId : restaurantIds) {
	            List<RestaurantPhoto> photos = restaurantPhotoRepository.findByRestaurant_RestaurantId(restaurantId);
	            
	            for (RestaurantPhoto photo : photos) {

	            	RestaurantPhotoFindDTO dto = new RestaurantPhotoFindDTO();
	                dto.setRestaurantId(photo.getRestaurant().getRestaurantId());
	                dto.setName(photo.getRestaurant().getName());
	                dto.setPhotoFile(photo.getPhotoFile());
	                dto.setStyle(photo.getRestaurant().getStyle());
	                dtos.add(dto);
	            }
	        }
	    } else {
	        List<RestaurantPhoto> photos = restaurantPhotoRepository.findByRestaurant_CountyAndRestaurant_Style(county, style);
	        System.out.println(2);
	        for (RestaurantPhoto photo : photos) {
	        	RestaurantPhotoFindDTO dto = new RestaurantPhotoFindDTO();
	        	   dto.setRestaurantId(photo.getRestaurant().getRestaurantId());
	                dto.setName(photo.getRestaurant().getName());
	                dto.setPhotoFile(photo.getPhotoFile());
	                dto.setStyle(photo.getRestaurant().getStyle());
	            dtos.add(dto);
	        }
	    }

	    return dtos;
	}
	
	}
