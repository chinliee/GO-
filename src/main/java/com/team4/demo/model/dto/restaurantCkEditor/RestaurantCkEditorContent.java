package com.team4.demo.model.dto.restaurantCkEditor;

import java.time.LocalTime;

import jakarta.validation.constraints.NegativeOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCkEditorContent {
	
	private String html;
	
	private String introduce;
	
	private String name;
	
	private String county;
	
	private String district;
	
	private String address;
	
	private String phone;
	
	private LocalTime openTime;
	
	private LocalTime closeTime;
}
