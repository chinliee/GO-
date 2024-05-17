package com.team4.demo.model.dto.adCheck;

import com.team4.demo.model.entity.AdData;


import lombok.Data;

@Data
public class AdCheckUpdateDto {
	
	private Integer adCheckId;

	private Integer adId;

	private Integer checkCount;


    private AdData adData;
}
