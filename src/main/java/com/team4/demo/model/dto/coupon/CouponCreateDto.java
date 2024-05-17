package com.team4.demo.model.dto.coupon;

import lombok.Data;

@Data
public class CouponCreateDto {

	private String code;

	private String type;

	private Double discount;

	private Integer usagePrice;

	private Long startTime;

	private Long endTime;

}
