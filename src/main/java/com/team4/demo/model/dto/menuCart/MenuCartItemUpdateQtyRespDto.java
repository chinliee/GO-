package com.team4.demo.model.dto.menuCart;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCartItemUpdateQtyRespDto {

    private Integer itemId;

    private Integer quantity;

}
