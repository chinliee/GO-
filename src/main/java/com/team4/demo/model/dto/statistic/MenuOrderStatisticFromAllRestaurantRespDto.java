package com.team4.demo.model.dto.statistic;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderStatisticFromAllRestaurantRespDto {

    // 今日餐點訂單銷售數據，包含訂單數量、銷售額
    private MenuOrderSalesNumberRespDto todaySalesNumber;

    // 本月訂單銷售數據
    private MenuOrderSalesNumberRespDto monthSalesNumber;

    // 區間訂單銷售數據
    private MenuOrderSalesNumberRespDto rangeSalesNumber;

    // 查詢時間區間內，以月為單位統計，每個月的訂單總數量、總銷售額
    private List<MenuOrderMonthSalesRespDto> monthSalesList = new ArrayList<>();

    // 查詢時間區間內，根據餐點訂單總銷售額排序，查詢最高的5家餐廳
    private List<MenuOrderTopRestaurantRespDto> topRestaurantBySalesList = new ArrayList<>();

    // 查詢時間區間內，根據餐點訂單總數量排序，查詢最高的5家餐廳
    private List<MenuOrderTopRestaurantRespDto> topRestaurantByOrderCountList = new ArrayList<>();

}
