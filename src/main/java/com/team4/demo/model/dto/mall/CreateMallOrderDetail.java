package com.team4.demo.model.dto.mall;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMallOrderDetail {

	private Integer orderId;

    private Integer productId;

    private Integer productPrice;

    private Integer quantity;

    private Date orderTime;
	
    
	
	
	
}
