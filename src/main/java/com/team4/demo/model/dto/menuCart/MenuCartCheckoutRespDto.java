package com.team4.demo.model.dto.menuCart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuCartCheckoutRespDto {

    private MemberCheckoutDto member;

    private RestaurantCheckoutDto restaurant;

    private CartCheckoutDto cart;

}
