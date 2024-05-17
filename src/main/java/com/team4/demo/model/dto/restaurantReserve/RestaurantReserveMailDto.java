package com.team4.demo.model.dto.restaurantReserve;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantReserveMailDto {

	private String customer;
	
	private LocalDate reserveDay;
	
	private LocalTime reserveTime;
	
	
}
