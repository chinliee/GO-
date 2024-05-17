package com.team4.demo.model.dto.statistic;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderMonthSalesRespDto {

    // 年份+月份，前端顯示用
    private String month;

    // 訂單數量
    private Integer orderCount;

    // 銷售額
    private Integer sales;

}
