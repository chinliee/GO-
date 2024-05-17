package com.team4.demo.model.dto.coupon;


import java.util.List;

import com.team4.demo.model.entity.Member;

import lombok.Data;


@Data
public class CouponUpdateDto {
	
	private Integer couponId;
	
	private String code;
	
	private String type;
	
	private Double discount;
	
	private Integer usagePrice;
	
	private Long startTime;
	
	private Long endTime;
	
	private List<Member> members;
	
}
