package com.team4.demo.model.dto.menuCart;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuCartItemCheckoutRespDto {

    private String name;

    private Integer quantity;

    private Integer price;

}
