package com.team4.demo.model.dto.menuOrder;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderDataForMemberRespDto {

    // 餐廳資訊
    private RestaurantDataDto restaurant;

    // 外送員資訊
    private CourierDataDto courier;

    // 訂單資訊 MenuOrder
    private OrderDataDto order;

    // 訂單明細資訊 MenuOrderDetail
    private List<OrderDetailDataDto> orderDetailList = new ArrayList<>();

}
