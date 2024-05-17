package com.team4.demo.model.dto.restaurantCord;


import java.time.LocalTime;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRecorddto {

	private Integer restaurantId;	
	
	private String timePer;
	
//	private String dayPer;
	
	private LocalTime startTime;
	
	private LocalTime endTime;
	
	private Integer price;
	
	private String note;


}
