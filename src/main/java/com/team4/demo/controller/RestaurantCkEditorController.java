package com.team4.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.restaurantCkEditor.RestaurantCkEditorContent;
import com.team4.demo.model.dto.restaurantCkEditor.RestaurantCkEditorInsertDto;
import com.team4.demo.model.entity.RestaurantCkEditor;
import com.team4.demo.service.RestaurantCkEditorService;


@RestController
@CrossOrigin
public class RestaurantCkEditorController {
	 
	@Autowired
	    private RestaurantCkEditorService restaurantCkEditorService;
	
		@PostMapping("/ckEditorSave/{restaurantId}")
	    public String ckEditorSaveUpdate(@PathVariable("restaurantId") Integer restaurantId,
	    								 @RequestBody RestaurantCkEditorInsertDto dto) {
		
		restaurantCkEditorService.ckEditorSaveUpdate(restaurantId, dto);
		
		return "Content saved successfully!";
	    }	
		
		@GetMapping("/ckEditorlogin/{restaurantId}")
		public List<RestaurantCkEditorContent> loginHtml(@PathVariable("restaurantId") Integer restaurantId) {
						
		return restaurantCkEditorService.findByRestaurantId(restaurantId);
		}
		
		@GetMapping("/ckEditorcheck/{restaurantId}")
		public ResponseEntity<?> findOrCreateRestaurantCkEditor(@PathVariable Integer restaurantId) {
		RestaurantCkEditor restaurantCkEditor = restaurantCkEditorService.findOrCreate(restaurantId);
		        
		if (restaurantCkEditor != null) {
		return ResponseEntity.ok(restaurantCkEditor);
		} else {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RestaurantCkEditor not found or couldn't be created.");
		}
		}
			
}
