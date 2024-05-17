package com.team4.demo.model.dto.statistic;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderTopRestaurantRespDto {

    private Integer restaurantId;

    private String restaurantName;

    private Integer orderCount;

    private Integer sales;

}
