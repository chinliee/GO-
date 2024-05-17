package com.team4.demo.model.dto.menuOrder;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantSimpleDto {

    private Integer restaurantId;

    private String name;

    private String address;

}
