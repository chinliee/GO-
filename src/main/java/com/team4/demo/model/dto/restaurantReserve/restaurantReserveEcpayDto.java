package com.team4.demo.model.dto.restaurantReserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class restaurantReserveEcpayDto {

	private String amount;
	
	private String restaurantName;
}
