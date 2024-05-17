package com.team4.demo.model.dto.adData;

import java.util.Date;

import com.team4.demo.model.entity.Restaurant;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AdDataCreateDto {

	private Integer adId;
	
	private String location;

	private byte[] photo;
	
	private String url;
	
	private String content;
	
	private Integer days;
	
	private Integer price;
	
	private Long startTime;
	
	private Long endTime;

	private String status;
	
	private Long createdTime;
	
	private Restaurant restaurant;

}
