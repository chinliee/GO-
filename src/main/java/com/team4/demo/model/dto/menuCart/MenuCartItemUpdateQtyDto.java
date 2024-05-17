package com.team4.demo.model.dto.menuCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuCartItemUpdateQtyDto {

    private Integer itemId;

    private Integer quantity;

}
