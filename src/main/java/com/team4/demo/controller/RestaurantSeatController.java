package com.team4.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.restaurantSeat.RestaurantSeatDto;
import com.team4.demo.model.dto.restaurantSeat.RestaurantSeatFindAllDto;
import com.team4.demo.model.dto.restaurantSeat.RestaurantSeatinsertdto;
import com.team4.demo.model.dto.restaurantSeat.RestaurantSeatupdata;
import com.team4.demo.model.entity.RestaurantSeat;
import com.team4.demo.service.RestaurantSeatService;

@RestController
@CrossOrigin
public class RestaurantSeatController {

	@Autowired
	private RestaurantSeatService restaurantSeatService;

	@GetMapping("/restaurantSeat/{restaurantid}")
	public List<RestaurantSeatFindAllDto> FindAllRestaurantSeat(@PathVariable("restaurantid") Integer restaurantid) {

	return restaurantSeatService.FindAll(restaurantid);
	}

	@GetMapping("/restaurantSeat/tableFor/{restaurantid}")
	public List<RestaurantSeatFindAllDto> FindByTableFor(@PathVariable("restaurantid") Integer restaurantId,
			@RequestParam("tableFor") String tableFor) {

	return restaurantSeatService.Findbytable(restaurantId, tableFor);
	}

	@GetMapping("/restaurantSeat/seatState/{restaurantid}")
	public List<RestaurantSeatFindAllDto> FindByState(@PathVariable("restaurantid") Integer restaurantId,
			@RequestParam("seatState") String seatState) {

	return restaurantSeatService.Findbystate(restaurantId, seatState);
	}

	@PostMapping("/restaurantSeat")
	public ResponseEntity<?> insertSeat(@RequestBody RestaurantSeatinsertdto dto) {
	RestaurantSeat restaurantSeat = restaurantSeatService.insertRestaurantSeat(dto);
	if (restaurantSeat != null) {
	return ResponseEntity.status(HttpStatus.CREATED).body(restaurantSeat);
	} else {
	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無法新增餐廳座位");
	}
	}

	@DeleteMapping("/restaurantSeat/{seatId}")
	public void DeletebById(@PathVariable("seatId") Integer seatId) {

	restaurantSeatService.deletebySeatid(seatId);
	}

	@PutMapping("/restaurantSeat/{seatId}")
	public ResponseEntity<?> updateSeat(@PathVariable("seatId") Integer seatId,
			@RequestBody RestaurantSeatupdata dto) {
	RestaurantSeat restaurantSeat = restaurantSeatService.Seattableforupdata(seatId, dto);
	if (restaurantSeat != null) {
	return ResponseEntity.ok(restaurantSeat);
	} 
	else {
	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("更新座位失敗");	
	}
	}
	@GetMapping("/restaurantSeat/update/{seatId}")
	public RestaurantSeatDto getSeatDto(@PathVariable("seatId") Integer seatID) {
	Optional<RestaurantSeatDto> rOptional = restaurantSeatService.getSeatById(seatID);

	return rOptional.orElse(null);
	}

	@GetMapping("/restaurantSeat/seatStateData/{restaurantid}")
	public List<LocalDate> FindByStateDate(@PathVariable("restaurantid") Integer restaurantId) {

	return restaurantSeatService.findOpenDates(restaurantId);
	}

	@GetMapping("/restaurantSeat/seatStateTime/{restaurantid}")
	public List<LocalTime> FindByStateTime(@PathVariable("restaurantid") Integer restaurantId,
										   @RequestParam("openDay") LocalDate openDay,
										   @RequestParam("tableFor") String tableFor) {

	return restaurantSeatService.findbytimepr(restaurantId, openDay,tableFor);
	}

	@GetMapping("/restaurants/openDayFive/{restaurantId}")
	public List<RestaurantSeatFindAllDto> findByDateFive(@PathVariable("restaurantId") Integer restaurantId,
		        											   @RequestParam("openDate") LocalDate date){
	
	return restaurantSeatService.findByDateFive(restaurantId, date);
	
	}

	@GetMapping("/restaurantSeat/timePer/{restaurantId}")
	public List<RestaurantSeatFindAllDto> findbyTimePer(@PathVariable("restaurantId") Integer restaurantId,
										      @RequestParam("timePer") String timePer){
	return  restaurantSeatService.findByTimePer(restaurantId, timePer);		
	}

	@GetMapping("/getPrice/{restaurantId}")
	public ResponseEntity<Integer> getPrice(@PathVariable("restaurantId") Integer restaurantId, 
										    @RequestParam("openDay") LocalDate openDay, 
										    @RequestParam("openTime") LocalTime openTime, 
										    @RequestParam("tableFor") String tableFor){
	
	Integer price = restaurantSeatService.findbytimeprgetprice(restaurantId, openDay, openTime, tableFor);
	    
	if (price != null) {
	return ResponseEntity.ok(price);
	} else {
	return ResponseEntity.notFound().build(); 
	
	}
	}	
	}