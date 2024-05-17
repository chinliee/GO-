package com.team4.demo.model.dto.restaurantSeat;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSeatFindAllDto {

	private Integer seatId;
	
	private String tableFor;
	
	private String seatTimePer;
	
	private LocalTime openTime;
	
	private LocalDate openDay;
	
	private String seatState;
}
