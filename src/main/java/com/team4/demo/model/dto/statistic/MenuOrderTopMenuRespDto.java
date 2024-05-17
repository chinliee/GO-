package com.team4.demo.model.dto.statistic;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderTopMenuRespDto {

    private Integer menuId;

    private String menuName;

    // 餐點總銷售額
    private Integer sales;

    // 餐點銷售量
    private Integer quantity;

}
