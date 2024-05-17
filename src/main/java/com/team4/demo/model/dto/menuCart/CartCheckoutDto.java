package com.team4.demo.model.dto.menuCart;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartCheckoutDto {

    // 餐點購物車資料
    private Integer cartId;

    private String orderType;

    private Integer deliveryFee;

    private Integer platformFee;

    // 餐點明細
    private List<MenuCartItemCheckoutRespDto> itemList;

}
