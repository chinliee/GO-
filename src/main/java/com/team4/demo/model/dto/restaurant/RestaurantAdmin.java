package com.team4.demo.model.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAdmin {

	private Integer restaurantId;
	
	private String name;
	
	private String uniformNumbers;
	
	private String password;
	
	private String county;
	
	private String district;
	
	
}
