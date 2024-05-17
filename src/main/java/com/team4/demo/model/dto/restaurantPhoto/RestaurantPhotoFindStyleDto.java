package com.team4.demo.model.dto.restaurantPhoto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPhotoFindStyleDto {

	private String county;
	
	private String style;
	
	private LocalDate openDay;
	
	private String tableFor;
	
	
	
}
