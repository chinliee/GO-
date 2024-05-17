package com.team4.demo.model.dto.menuOrder;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderForRestaurantRespDto {

    private Integer orderId;

    // 訂單類型，外送 DELIVERY、外帶自取 TAKEOUT
    private String orderType;

    // 訂單狀態
    private String status;

    // 訂單總金額
    private Integer total;

    // 外送費
    private Integer deliveryFee;

    // 平台費
    private Integer platformFee;

    // 平台費
    private Integer subTotal;

    // 付款方式，現金 CASH、信用卡 CREDIT_CARD
    private String paymentType;

    // 預約時間 Long 毫秒數
    private Long reservationTime;

    // 訂單創建時間 Long 毫秒數
    private Long createdTime;

    // 外送地址
    private String deliveryAddress;

    // 會員資料
    private MemberDataDto member;

    // 餐廳資料
    private RestaurantSimpleDto restaurant;

    // 訂單明細資料
    List<OrderDetailDataDto> orderDetailList = new ArrayList<>();

}
