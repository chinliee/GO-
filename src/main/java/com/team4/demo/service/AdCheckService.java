package com.team4.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.team4.demo.exception.DataNotFoundException;
import com.team4.demo.model.entity.AdCheck;
import com.team4.demo.model.repository.AdCheckRepository;

@Service
@Transactional
public class AdCheckService {
	@Autowired
	private AdCheckRepository adCheckRepository;
	
	public AdCheck getAdCheckById(Integer adCheckId) {
		AdCheck adCheck = adCheckRepository.findById(adCheckId)
				.orElseThrow(() -> new DataNotFoundException("Not found AdCheck with adCheckId = " + adCheckId));

		return adCheck;
	}
	
	public void deleteAdCheck(Integer adCheckId) {
		adCheckRepository.deleteById(adCheckId);
	}
}
