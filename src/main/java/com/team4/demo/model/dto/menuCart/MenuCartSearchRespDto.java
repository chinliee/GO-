package com.team4.demo.model.dto.menuCart;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCartSearchRespDto {

    private Integer cartId;

    private String orderType;

    private Integer deliveryFee;

    private Integer platformFee;

    private List<MenuCartItemSearchRespDto> itemList;

}
