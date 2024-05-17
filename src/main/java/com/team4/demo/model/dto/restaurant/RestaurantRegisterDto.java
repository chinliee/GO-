package com.team4.demo.model.dto.restaurant;

import org.hibernate.internal.build.AllowNonPortable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllowNonPortable
public class RestaurantRegisterDto {

	private String name;

	private String uniformNumbers;
	
	private String password;

	private String salt;
	
	private String county;
	
	private String district;
	
	private String address;
	
	private String phone;
	

}
