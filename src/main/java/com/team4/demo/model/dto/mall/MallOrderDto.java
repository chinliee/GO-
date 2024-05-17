package com.team4.demo.model.dto.mall;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallOrderDto {

	
	
    private Integer memberId;

    private Integer orderTotalAmount;

    private String orderStatus;

    private Date orderTime;

    private Date paymentTime;

    private Date shippingTime;

    private String shippingAddress;
    
    
}
