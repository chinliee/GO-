package com.team4.demo.util.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.util.test.*;

@RestController
public class OrderController {
	
	@Autowired
	OrderService orderService;

	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout() {
		String aioCheckOutALLForm = orderService.ecpayCheckout();
		
		return aioCheckOutALLForm;
	}
}
