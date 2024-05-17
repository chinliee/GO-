package com.team4.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.dto.restaurantCord.RestaurantRecordFindAllDto;
import com.team4.demo.model.dto.restaurantCord.RestaurantRecorddto;
import com.team4.demo.model.dto.restaurantCord.RestaurantRecorddtose;
import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.model.entity.RestaurantRecord;
import com.team4.demo.model.repository.RestaurantRecordRepository;
import com.team4.demo.model.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RestaurantRecordService {

	@Autowired
	private RestaurantRecordRepository restaurantRecordRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;

	public List<RestaurantRecordFindAllDto> FindAllByid(Integer restaurantId){
	List<RestaurantRecord> restaurantRecordslList = restaurantRecordRepository.findByRestaurant_restaurantId(restaurantId);
	
	List<RestaurantRecordFindAllDto> restaurantRecordFindAllDtosList = new ArrayList<>();
	
	for(RestaurantRecord restaurantRecord : restaurantRecordslList) {
	
	RestaurantRecordFindAllDto restaurantRecordFindAllDto = new RestaurantRecordFindAllDto();
	
	restaurantRecordFindAllDto.setRestaurantrecordId(restaurantRecord.getRestaurantrecordId());
	restaurantRecordFindAllDto.setTimePer(restaurantRecord.getTimePer());
	restaurantRecordFindAllDto.setStartTime(restaurantRecord.getStartTime());
	restaurantRecordFindAllDto.setEndTime(restaurantRecord.getEndTime());
	restaurantRecordFindAllDto.setPrice(restaurantRecord.getPrice());
	restaurantRecordFindAllDto.setNote(restaurantRecord.getNote());
	
	restaurantRecordFindAllDtosList.add(restaurantRecordFindAllDto);
	}
	
	return restaurantRecordFindAllDtosList;
	}
			
	public RestaurantRecord Recordinsert(RestaurantRecorddto dto) {
	RestaurantRecord restaurantRecord = new  RestaurantRecord();
	Integer restaurantId = dto.getRestaurantId();
	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
		 
	restaurantRecord.setRestaurant(restaurant);
	restaurantRecord.setTimePer(dto.getTimePer());
	restaurantRecord.setStartTime(dto.getStartTime());
	restaurantRecord.setEndTime(dto.getEndTime());
	restaurantRecord.setPrice(dto.getPrice());
	restaurantRecord.setNote(dto.getNote());
		 
	return restaurantRecordRepository.save(restaurantRecord);		 
	}
	

	public RestaurantRecord Recordupdate(Integer restaurantrecordid,RestaurantRecorddto dto) {
	RestaurantRecord restaurantRecord = new  RestaurantRecord();		 
	Integer restaurantId = dto.getRestaurantId();
	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	restaurantRecord.setRestaurant(restaurant);
	restaurantRecord.setRestaurantrecordId(restaurantrecordid);
	restaurantRecord.setTimePer(dto.getTimePer());
	restaurantRecord.setStartTime(dto.getStartTime());
	restaurantRecord.setEndTime(dto.getEndTime());
	restaurantRecord.setPrice(dto.getPrice());
	restaurantRecord.setNote(dto.getNote());
		
	return restaurantRecordRepository.save(restaurantRecord);
	}	  

	public List<RestaurantRecordFindAllDto> getRecordBytimeper(Integer Restaurantid,String timeper) {
	
	List<RestaurantRecord> restaurantRecordlList = restaurantRecordRepository
				.findByRestaurant_restaurantIdAndTimePer(Restaurantid, timeper);
	List<RestaurantRecordFindAllDto> restaurantRecordFindAllDtosList = new ArrayList<>();
	
	for(RestaurantRecord restaurantRecord : restaurantRecordlList) {
	
	RestaurantRecordFindAllDto restaurantRecordFindAllDto = new RestaurantRecordFindAllDto();
	
	restaurantRecordFindAllDto.setRestaurantrecordId(restaurantRecord.getRestaurantrecordId());
	restaurantRecordFindAllDto.setTimePer(restaurantRecord.getTimePer());
	restaurantRecordFindAllDto.setStartTime(restaurantRecord.getStartTime());
	restaurantRecordFindAllDto.setEndTime(restaurantRecord.getEndTime());
	restaurantRecordFindAllDto.setPrice(restaurantRecord.getPrice());
	restaurantRecordFindAllDto.setNote(restaurantRecord.getNote());
	
	restaurantRecordFindAllDtosList.add(restaurantRecordFindAllDto);
	}
	
	
	
	return restaurantRecordFindAllDtosList;	
	}

	public void deletebyrestaurantrecordid(Integer restaurantrecordid) {
	
	restaurantRecordRepository.deleteById(restaurantrecordid);
	}

	private RestaurantRecorddtose mapToDTO(RestaurantRecord restaurantRecord) {
	RestaurantRecorddtose dto = new RestaurantRecorddtose();

   dto.setRestaurantrecordId(restaurantRecord.getRestaurantrecordId());
   dto.setTimePer(restaurantRecord.getTimePer());
   dto.setStartTime(restaurantRecord.getStartTime());
   dto.setEndTime(restaurantRecord.getEndTime());
   dto.setPrice(restaurantRecord.getPrice());
   dto.setNote(restaurantRecord.getNote());
   dto.setRestaurantId(restaurantRecord.getRestaurant().getRestaurantId());
   
   return dto;
   }
	
	public Optional<RestaurantRecorddtose> getRecordById(Integer restaurantRecordId) {
	Optional<RestaurantRecord> restaurantRecordOptional = restaurantRecordRepository.findById(restaurantRecordId);

	return restaurantRecordOptional.map(this::mapToDTO);
	}	 	
	}
