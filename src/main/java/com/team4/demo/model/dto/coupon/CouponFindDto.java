package com.team4.demo.model.dto.coupon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.team4.demo.model.entity.Member;
import com.team4.demo.model.entity.Restaurant;

import lombok.Data;

@Data
public class CouponFindDto {
	
	private Integer couponId;
	
	private String code;

	private String type;

	private Double discount;

	private Integer usagePrice;

	private Date startTime;

	private Date endTime;
	
//	private List<Integer> memberId;
//	
//	private List<String> email;
//
//	private List<String> name;
//	
//	private List<Date> registeration_date;
	
	

}
