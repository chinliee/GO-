package com.team4.demo.model.dto.adData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.team4.demo.model.entity.Restaurant;

import lombok.Data;

@Data
public class AdDataFindDto {
	
	private Integer adId;
	
	private String location;

	private byte[] photo;
	
	private String url;
	
	private String content;
	
	private Integer days;
	
	private Integer price;
	
	private LocalDate startTime;
	
	private LocalDate endTime;

	private String status;
	
	private Date createdTime;
	
	private Integer restaurantId;
	
	private String name;
}
