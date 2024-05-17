package com.team4.demo.model.dto.menuOrder;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDataDto {

    private Integer orderDetailId;

    private Integer menuId;

    private String menuName;

    private Integer price;

    private Integer quantity;

}
