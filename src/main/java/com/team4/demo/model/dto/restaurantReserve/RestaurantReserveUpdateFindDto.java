package com.team4.demo.model.dto.restaurantReserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestaurantReserveUpdateFindDto {
	
	private Integer reserveId;
	
	private String state;
}
