package com.team4.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.mall.FindMallOrderDto;
import com.team4.demo.model.dto.mall.MallOrderDto;
import com.team4.demo.model.dto.response.ResponseDto;
import com.team4.demo.model.entity.MallOrder;
import com.team4.demo.service.MallOrderService;
import com.team4.demo.util.linepay.LinePayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mall-order")
@CrossOrigin(origins = "http://localhost:8080")
public class MallOrderController {

	@Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private LinePayService linePayService;
	
	
    public MallOrderController(MallOrderService mallOrderService) {
        this.mallOrderService = mallOrderService;
    }

    @GetMapping
    public List<MallOrder> getAllMallOrders() {
        return mallOrderService.getAllMallOrders();
    }
    
    // 新增單筆商城訂單
    @PostMapping("create")
    public ResponseEntity<?> createMallOrder(@RequestBody MallOrderDto mallOrderDto) {
    	
	    
    	System.out.println(mallOrderDto);
    	MallOrder mallOrder = mallOrderService.createMallOrder(mallOrderDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.value(), mallOrder));
	}
    
    
    
	//根據會員id查詢此會員所有mallOrder
    @GetMapping("/member/{memberId}")
	public List<FindMallOrderDto> findByMember_MemberId(@PathVariable("memberId") Integer memberId) {
		
		return mallOrderService.findByMember_MemberId(memberId);
		
	}
    
    //根據訂單編號刪除此訂單
	@DeleteMapping("/delete/{orderId}")
	public void delete(@PathVariable("orderId") Integer orderId) {
		
		mallOrderService.delete(orderId);
	}
    
	
	 // 測試，取得 Line Pay 網頁付款連結
    @GetMapping("/linepay")
    public ResponseEntity<ResponseDto> getLinePayWebPaymentUrl() {
		log.info("MenuOrderController - getLinePayWebPaymentUrl ... ");
        String confirmUrl = "http://127.0.0.1:5502/member-menu-order.html";
        String cancelUrl = "http://127.0.0.1:5502/menu-checkout.html";
        String webPaymentUrl = linePayService.getWebPaymentUrl(500, 824, confirmUrl, cancelUrl, "各式各樣的訂單");
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), webPaymentUrl));
    }
    
    
}
