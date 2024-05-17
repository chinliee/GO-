package com.team4.demo.model.dto.restaurant;

import java.math.BigDecimal;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUpdateInfoDto {

	
	private LocalTime openTime;

	private LocalTime closeTime;
	
	private String style;
}
