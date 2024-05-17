package com.team4.demo.model.dto.restaurantReserve;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantReserveInsertDto {

	private LocalDate reserveDay;
	
	private LocalTime reserveTime;
	
	private Integer customer;
	
	private String state;
	
	private Integer amount;
	
	private Integer restaurantId;
	
	private Integer memberId;
	
//	private String timePer;
	
	private Integer seatId;
}
