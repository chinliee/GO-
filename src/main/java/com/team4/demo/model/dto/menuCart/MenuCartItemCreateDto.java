package com.team4.demo.model.dto.menuCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCartItemCreateDto {

    private Integer memberId;

    private Integer restaurantId;

    private String orderType;

    private Integer deliveryFee;

    private Integer platformFee;

    private Integer menuId;

    private Integer quantity;

}
