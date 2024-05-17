package com.team4.demo.model.dto.menuCart;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberCheckoutDto {

    private Integer memberId;

    // 會員的預設地址，給前端 Google Map 外送位置的預設使用
    private String address;

    // 會員預設信用卡資訊
    private String creditCardInfo;

}
