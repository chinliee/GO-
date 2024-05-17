package com.team4.demo.model.dto.menuOrder;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOrderSearchForRestaurantDto {

    private Integer restaurantId;

    private String restaurantName;

    // 會員姓名
    private String memberName;

    // 前端搜尋會員 email，但傳送的是會員ID到後端查詢
     private Integer memberId;

    // 訂單狀態
    private String status;

    // 訂單類型，外送 DELIVERY、外帶自取 TAKEOUT
    private String orderType;

    // 複合查詢中，查詢訂單時間，範圍起始時間、結束時間
    private Long orderCreatedTimeStart;
    private Long orderCreatedTimeEnd;

}
