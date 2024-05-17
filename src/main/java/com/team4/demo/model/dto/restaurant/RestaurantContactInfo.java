package com.team4.demo.model.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantContactInfo {
private String county;
	
private String district;
	
private String address;
	
private String phone;
}
