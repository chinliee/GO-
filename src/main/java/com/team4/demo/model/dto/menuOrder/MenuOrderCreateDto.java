package com.team4.demo.model.dto.menuOrder;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderCreateDto {

    private Integer cartId;

    private String deliveryAddress;

    private String paymentType;

    private String reservationTime; // 預定時間的字串，格式 yyyy-MM-dd HH:mm

    // 第三方支付 (LINE Pay、綠界金流)，支付成功後要導向的頁面
    public String confirmUrl;

    // 第三方支付，支付取消後要導向的頁面
    public String cancelUrl;

}
