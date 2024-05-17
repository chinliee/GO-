package com.team4.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.restaurantCord.RestaurantRecordFindAllDto;
import com.team4.demo.model.dto.restaurantCord.RestaurantRecorddto;
import com.team4.demo.model.dto.restaurantCord.RestaurantRecorddtose;
import com.team4.demo.model.entity.RestaurantRecord;
import com.team4.demo.model.entity.RestaurantReserve;
import com.team4.demo.service.RestaurantRecordService;

@RestController
@CrossOrigin
public class RestaurantRecordController {

	@Autowired
	private RestaurantRecordService restaurantrecordService;

	@GetMapping("/restaurantRecords/{restaurantId}") 
	public List<RestaurantRecordFindAllDto> AllbyID(@PathVariable("restaurantId") Integer restaurantId) {
	
	return restaurantrecordService.FindAllByid(restaurantId);
	}

	@GetMapping("/restaurantrecords/{restaurantrecordid}")
	public RestaurantRecorddtose getRestaurantRecordById(@PathVariable ("restaurantrecordid") Integer restaurantrecordid) {
	Optional<RestaurantRecorddtose> restaurantRecordOptional = restaurantrecordService.getRecordById(restaurantrecordid);
	
	return restaurantRecordOptional.orElse(null);
	}	  	 	
		
	@GetMapping("/restaurantRecordTimeper/records/{restaurantid}")
	public List<RestaurantRecordFindAllDto> getRecordByTimePer(@PathVariable("restaurantid") Integer restaurantId,
													 @RequestParam("timePer") String timePer) {
	        
	return restaurantrecordService.getRecordBytimeper(restaurantId, timePer);			
	}

	@PostMapping("/restaurantRecord")
	public RestaurantRecord Insert(@RequestBody RestaurantRecorddto dto) {
		
	return restaurantrecordService.Recordinsert(dto);
	}	

	@DeleteMapping("/restaurantRecord/{restaurantrecordId}")
	public void DeleteById(@PathVariable("restaurantrecordId") Integer restaurantrecordId) {
		
	restaurantrecordService.deletebyrestaurantrecordid(restaurantrecordId);	
	}
			
	@PutMapping("/restaurantRecord/{restaurantrecordid}")
	public RestaurantRecord Updata(@PathVariable("restaurantrecordid") Integer restaurantrecordid,
												  @RequestBody  RestaurantRecorddto dto ) {
	
	RestaurantRecord restaurantRecord =restaurantrecordService.Recordupdate(restaurantrecordid,dto);
		
	return restaurantRecord ;
	}		
	
	

	
	
	}