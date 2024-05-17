package com.team4.demo.model.dto.restaurantReserve;

import java.time.LocalDate;
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
public class RestaurantReserveFindAllDto {

	private Integer reservedId;
			
	private LocalDate reserveDay;
	
	private LocalTime reserveTime;
	
	private Integer customer;
	
	private String state;
	
	private Integer amount;
	
	private String name;
	
	private Integer memberId;
	
	private String restaurantName;
}
