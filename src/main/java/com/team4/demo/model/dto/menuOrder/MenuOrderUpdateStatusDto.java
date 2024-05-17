package com.team4.demo.model.dto.menuOrder;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOrderUpdateStatusDto {

    private Integer orderId;

    private String status;

}
