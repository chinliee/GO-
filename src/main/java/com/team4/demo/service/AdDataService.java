package com.team4.demo.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.demo.exception.DataNotFoundException;
import com.team4.demo.model.dto.adData.AdDataCreateDto;
import com.team4.demo.model.dto.adData.AdDataFindDto;
import com.team4.demo.model.dto.adData.AdDataUpdateDto;
import com.team4.demo.model.entity.AdData;
import com.team4.demo.model.repository.AdDataRepository;
import com.team4.demo.model.repository.RestaurantRepository;

@Service
@Transactional
public class AdDataService {

	@Autowired
	private AdDataRepository adDataRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	public List<AdDataFindDto> getAllAdDatas() {
		List<AdData> adDatas = adDataRepository.findAll();
		List<AdDataFindDto> dto = new ArrayList<>();
		
		for (AdData adDataFindDto : adDatas) {
			AdDataFindDto finddto = new AdDataFindDto();
			finddto.setAdId(adDataFindDto.getAdId());
			finddto.setLocation(adDataFindDto.getLocation());
			finddto.setPhoto(adDataFindDto.getPhoto());
			finddto.setUrl(adDataFindDto.getUrl());
			finddto.setContent(adDataFindDto.getContent());
			finddto.setDays(adDataFindDto.getDays());
			finddto.setPrice(adDataFindDto.getPrice());
			finddto.setStartTime(adDataFindDto.getStartTime());
			finddto.setEndTime(adDataFindDto.getEndTime());
			finddto.setStatus(adDataFindDto.getStatus());
			finddto.setCreatedTime(adDataFindDto.getCreatedTime());
			finddto.setRestaurantId(adDataFindDto.getRestaurant().getRestaurantId());
			finddto.setName(adDataFindDto.getRestaurant().getName());
			
			dto.add(finddto);
		}
		
		return dto;
	}

	public AdDataFindDto getAdDatasByid(Integer adId) {
		AdData adDataFindDto = adDataRepository.findById(adId)
				.orElseThrow(() -> new DataNotFoundException("Not found AdData with adId = " + adId));
		
		AdDataFindDto finddto = new AdDataFindDto();
		finddto.setAdId(adDataFindDto.getAdId());
		finddto.setLocation(adDataFindDto.getLocation());
		finddto.setPhoto(adDataFindDto.getPhoto());
		finddto.setUrl(adDataFindDto.getUrl());
		finddto.setContent(adDataFindDto.getContent());
		finddto.setDays(adDataFindDto.getDays());
		finddto.setPrice(adDataFindDto.getPrice());
		finddto.setStartTime(adDataFindDto.getStartTime());
		finddto.setEndTime(adDataFindDto.getEndTime());
		finddto.setStatus(adDataFindDto.getStatus());
		finddto.setCreatedTime(adDataFindDto.getCreatedTime());
		finddto.setRestaurantId(adDataFindDto.getRestaurant().getRestaurantId());
		finddto.setName(adDataFindDto.getRestaurant().getName());
		return finddto;
	}
	
	public List<AdDataFindDto> getAllAdDatasByRestaurantId(Integer restaurantId) {
		if (!restaurantRepository.existsById(restaurantId)) {
			throw new DataNotFoundException("Not found Restaurant with restaurantId = " + restaurantId);
		}

		List<AdData> adDatas = adDataRepository.findByRestaurant_RestaurantId(restaurantId);
		List<AdDataFindDto> dto = new ArrayList<>();
		
		for (AdData adDataFindDto : adDatas) {
			AdDataFindDto finddto = new AdDataFindDto();
			finddto.setAdId(adDataFindDto.getAdId());
			finddto.setLocation(adDataFindDto.getLocation());
			finddto.setPhoto(adDataFindDto.getPhoto());
			finddto.setUrl(adDataFindDto.getUrl());
			finddto.setContent(adDataFindDto.getContent());
			finddto.setDays(adDataFindDto.getDays());
			finddto.setPrice(adDataFindDto.getPrice());
			finddto.setStartTime(adDataFindDto.getStartTime());
			finddto.setEndTime(adDataFindDto.getEndTime());
			finddto.setStatus(adDataFindDto.getStatus());
			finddto.setCreatedTime(adDataFindDto.getCreatedTime());
			finddto.setRestaurantId(adDataFindDto.getRestaurant().getRestaurantId());
			finddto.setName(adDataFindDto.getRestaurant().getName());
			
			dto.add(finddto);
		}
		
		return dto;

	}

	public AdData insertAdData(AdDataCreateDto dto) {
		AdData adData = AdData.builder().location(dto.getLocation()).photo(dto.getPhoto()).url(dto.getUrl())
				.content(dto.getContent()).days(dto.getDays()).price(dto.getPrice())
				.startTime(Instant.ofEpochMilli(dto.getStartTime()).atZone(ZoneId.systemDefault()).toLocalDate()).endTime(Instant.ofEpochMilli(dto.getEndTime()).atZone(ZoneId.systemDefault()).toLocalDate()).status(dto.getStatus())
				.createdTime(new Date(dto.getCreatedTime())).restaurant(dto.getRestaurant()).build();

		return adDataRepository.save(adData);

	}

	
	


	public AdData addAdDataToRestaurant(Integer restaurantId, String data,MultipartFile photo) throws IOException {
		byte[] photoBytes = photo.getBytes();

	   
		AdDataCreateDto dto = new ObjectMapper().readValue(data, AdDataCreateDto.class);
		 LocalDate startTime = Instant.ofEpochMilli(dto.getStartTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		    LocalDate endTime = Instant.ofEpochMilli(dto.getEndTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		AdData adData = restaurantRepository.findById(restaurantId).map(restaurant -> {
			AdData adDataEntity = AdData.builder().location(dto.getLocation()).photo(photoBytes).url(dto.getUrl())
					.content(dto.getContent()).days(dto.getDays()).price(dto.getPrice())
					.startTime(startTime).endTime(endTime).status(dto.getStatus())
					.createdTime(new Date(dto.getCreatedTime())).restaurant(restaurant).build();

			return adDataRepository.save(adDataEntity);
		}).orElseThrow(() -> new DataNotFoundException("Not found Restaurant with id = " + restaurantId));

		return adData;

	}
	

	
	

	public AdData updateAdDatas(Integer adId, AdDataUpdateDto dto) {
		AdData adData = adDataRepository.findById(adId)
				.orElseThrow(() -> new DataNotFoundException("adId " + adId + "not found"));

		adData.setLocation(dto.getLocation());
		adData.setPhoto(dto.getPhoto());
		adData.setUrl(dto.getUrl());
		adData.setContent(dto.getContent());
		adData.setDays(dto.getDays());
		adData.setPrice(dto.getPrice());
		adData.setStartTime(Instant.ofEpochMilli(dto.getStartTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		adData.setEndTime(Instant.ofEpochMilli(dto.getEndTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		adData.setStatus(dto.getStatus());
		adData.setCreatedTime(new Date(dto.getCreatedTime()));
		//new Date(dto.getEndTime())
		AdData saveadData = adDataRepository.save(adData);
		return saveadData;
	}

	public void deleteAdDatas(Integer adId) {

		adDataRepository.deleteById(adId);

	}

	public void deleteAllAdDatasOfRestaurant(Integer restaurantId) {
		if (!restaurantRepository.existsById(restaurantId)) {
			throw new DataNotFoundException("Not found Restaurant with restaurantId = " + restaurantId);
		}

		adDataRepository.deleteByRestaurant_RestaurantId(restaurantId);
		
	}
}