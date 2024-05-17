package com.team4.demo.model.dto.restaurantSeat;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSeatinsertdto {

	private Integer restaurantrecordId;
	
	private Integer restaurantId;
	
	private String tableFor;
	
	private String seatState;

	private LocalDate openDay;

	private String seatTimePer;
	
	private LocalTime openTime;
}
