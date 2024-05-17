package com.team4.demo.model.dto.menuOrder;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourierDataDto {

    private Integer courierId;

    private String name;

    private BigDecimal longitude;

    private BigDecimal latitude;

}
