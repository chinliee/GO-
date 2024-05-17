package com.team4.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.exception.DataNotFoundException;
import com.team4.demo.model.dto.adCheck.AdCheckUpdateDto;
import com.team4.demo.model.entity.AdCheck;
import com.team4.demo.model.repository.AdCheckRepository;
import com.team4.demo.service.AdCheckService;

@RestController
public class AdCheckController {

	@Autowired
	private AdCheckService adCheckService;

	@GetMapping("/adchecks/{adCheckId}")
	public ResponseEntity<AdCheck> getAdCheckById(@PathVariable(value = "adCheckId") Integer adCheckId) {
		AdCheck adCheck = adCheckService.getAdCheckById(adCheckId);
		return new ResponseEntity<>(adCheck, HttpStatus.OK);
	}

	@DeleteMapping("/adchecks/{adCheckId}")
	public ResponseEntity<HttpStatus> deleteAdCheck(@PathVariable("adCheckId") Integer adCheckId) {
		adCheckService.deleteAdCheck(adCheckId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
