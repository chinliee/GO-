package com.team4.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team4.demo.model.dto.restaurantPhoto.RestaurantPhotoDto;
import com.team4.demo.model.dto.restaurantPhoto.RestaurantPhotoFindDTO;
import com.team4.demo.model.entity.RestaurantPhoto;
import com.team4.demo.service.RestaurantPhotoService;

@RestController
@CrossOrigin
public class RestaurantPhotoController {

	@Autowired
	private RestaurantPhotoService restaurantPhotoService;

	@PostMapping("/save1")
	public String saveimage2(@RequestPart MultipartFile image, @RequestParam("restaurantId") Integer restaurantId) throws Exception {
	RestaurantPhotoDto dto = new RestaurantPhotoDto();
	dto.setRestuarntId(restaurantId);	
	dto.setPhotoFile(image.getBytes()); 
	restaurantPhotoService.savefile(dto);
	    
	return "OK";
	}

	@GetMapping("/image/{id}")
	public String getImageBase64(@PathVariable Integer id) {
	
	String base64image = restaurantPhotoService.getimageByid(id);
	
	return base64image;		
	}

	@GetMapping("/restaurantPhotoMain/{restaurantId}")
	public String getphotoMain(@PathVariable("restaurantId") Integer restaurantId ) {
		
	String	base64MainImage = restaurantPhotoService.photomain(restaurantId);
		
	return base64MainImage;	
	}
		
	@GetMapping("/restaurantPhotoFindAll")
	public List<RestaurantPhotoFindDTO> FindAllPhoto(){
		
	return restaurantPhotoService.FindAllMainPhoto(); 	
	}
	
	@GetMapping("/restaurantPhoto/findByCountyAndStyle")
	public List<RestaurantPhotoFindDTO> findByStyle(@RequestParam String county,
									         @RequestParam String style,
									         @RequestParam(required = false) LocalDate openDay,
									         @RequestParam(required = false) String tableFor) {
	List<RestaurantPhotoFindDTO> photos = restaurantPhotoService.findByCountyAndStyleAndOpenDayAndTableForAndSeatState(county, style, openDay, tableFor);
	return photos;
	}		
	}
