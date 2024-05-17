package com.team4.demo.model.dto.menuCart;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCartUpdateRespDto {

    private Integer cartId;

    private String orderType;

    private Integer deliveryFee;

    private Integer platformFee;

}
