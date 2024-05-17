package com.team4.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.demo.model.dto.adData.AdDataCreateDto;
import com.team4.demo.model.dto.adData.AdDataFindDto;
import com.team4.demo.model.dto.adData.AdDataUpdateDto;
import com.team4.demo.model.entity.AdData;
import com.team4.demo.model.repository.AdDataRepository;
import com.team4.demo.model.repository.RestaurantRepository;
import com.team4.demo.service.AdDataService;

@RestController
public class AdDataController {


	@Autowired
	private AdDataService adDataService;


	@GetMapping("/adDatas")
	public ResponseEntity<List<AdDataFindDto>> getAllAdDatas() {

		List<AdDataFindDto> adDatas = adDataService.getAllAdDatas();

		return new ResponseEntity<>(adDatas, HttpStatus.OK);
	}

	@GetMapping("/adDatas/{adId}")
	public ResponseEntity<AdDataFindDto> getAdDatasByid(@PathVariable(value = "adId") Integer adId) {
		AdDataFindDto adData = adDataService.getAdDatasByid(adId);

		return new ResponseEntity<>(adData, HttpStatus.OK);
	}

	@GetMapping("/restaurants/{restaurantId}/adDatas")
	public ResponseEntity<List<AdDataFindDto>> getAllAdDatasByRestaurantId(
			@PathVariable(value = "restaurantId") Integer restaurantId) {
		List<AdDataFindDto> adDatas = adDataService.getAllAdDatasByRestaurantId(restaurantId);
		return new ResponseEntity<>(adDatas, HttpStatus.OK);
	}


	@PostMapping("/restaurants/{restaurantId}/adDatas")
	public ResponseEntity<AdData> addAdDataToRestaurant(@PathVariable(value = "restaurantId") Integer restaurantId,
			@RequestPart("photo") MultipartFile photo, @RequestPart String data) throws IOException {

		
		AdData adData = adDataService.addAdDataToRestaurant(restaurantId, data, photo);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/adDatas/{adId}")
	public ResponseEntity<AdData> updateAdDatas(@PathVariable("adId") Integer adId, @RequestBody AdDataUpdateDto dto) {
		AdData adData = adDataService.updateAdDatas(adId, dto);

		// TODO:adDataRepository.save(adData)
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/adDatas/{adId}")
	public ResponseEntity<HttpStatus> deleteAdDatas(@PathVariable("adId") Integer adId) {

		adDataService.deleteAdDatas(adId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/restaurants/{restaurantId}/adDatas")
	public ResponseEntity<HttpStatus> deleteAllAdDatasOfRestaurant(
			@PathVariable(value = "restaurantId") Integer restaurantId) {
		adDataService.deleteAllAdDatasOfRestaurant(restaurantId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
