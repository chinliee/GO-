package com.team4.demo.model.dto.statistic;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderSalesNumberRespDto {

    // 訂單數量
    private Integer orderCount;

    // 銷售額
    private Integer sales;

}
