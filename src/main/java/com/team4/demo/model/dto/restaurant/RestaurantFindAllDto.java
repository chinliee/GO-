package com.team4.demo.model.dto.restaurant;

import java.math.BigDecimal;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantFindAllDto {

	private Integer restaurantId;
	

	private String name;
	

	private String uniformNumbers;
	

//	private String password;
	

//	private String salt;
	

	private String county;
	

	private String district;
	

	private String address;
	

	private String phone;
	

//	private BigDecimal longitude;
	

//	private BigDecimal latitude;
	

	private LocalTime openTime;
	

	private LocalTime closeTime;
	

	private String style;
	

	private String introduce;
}
