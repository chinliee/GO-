package com.team4.demo.util.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.team4.demo.util.ecpay.payment.integration.AllInOne;
import com.team4.demo.util.ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class OrderService {

	public String ecpayCheckout() {
		
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		
		LocalDateTime now = LocalDateTime.now();
		 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	    String formattedDateTime = now.format(formatter);
		
	    System.out.println(formattedDateTime);
        
		AllInOne all = new AllInOne("");
		
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate(formattedDateTime);
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		obj.setReturnURL("http://211.23.128.214:5000");
		obj.setNeedExtraPaidInfo("N");
		String form = all.aioCheckOut(obj, null);
		
		return form;
	}
}
