package com.team4.demo.model.dto.restaurant;

import java.math.BigDecimal;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUpdateDto {

	
	
	
	private String name;
	

	private String uniformNumbers;
	
	
	private String password;
	
	
	private String salt;
	
	
	private String county;
	
	
	private String district;
	
	
	private String address;
	
	
	private String phone;
	
	
	private BigDecimal longitude;
	

	private BigDecimal latitude;
	
	
	private LocalTime openTime;
	
	
	private LocalTime closeTime;
	
	
	private String style;
}
