package com.team4.demo.model.dto.restaurantCord;

import java.time.LocalTime;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRecordFindAllDto {

	private Integer restaurantrecordId;

	private String timePer;
	
	private LocalTime startTime;
	
	private LocalTime endTime;
	
	private Integer price;
	
	private String note;
}
