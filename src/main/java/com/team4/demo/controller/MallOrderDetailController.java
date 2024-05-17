package com.team4.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.mall.CreateMallOrderDetail;
import com.team4.demo.model.entity.MallOrderDetail;
import com.team4.demo.service.MallOrderDetailService;

@RestController
@RequestMapping("/mall-order-details")
public class MallOrderDetailController {

	@Autowired
    private MallOrderDetailService mallOrderDetailService;

    public MallOrderDetailController(MallOrderDetailService mallOrderDetailService) {
        this.mallOrderDetailService = mallOrderDetailService;
    }

    @GetMapping
    public List<MallOrderDetail> getAllMallOrderDetails() {
        return mallOrderDetailService.getAllMallOrderDetails();
    }

  //新增單筆mallOrderDetail
    @PostMapping("/create")
	public MallOrderDetail createMallOrderDetail(@RequestBody CreateMallOrderDetail mallOrderDetail) {
		
		return mallOrderDetailService.createMallOrderDetail(mallOrderDetail);
		
		
	}
    
	//刪除單筆mallOrderDetail
    @DeleteMapping("/delete/{id}")
	public void deleteMallOrderDetail(@PathVariable("id") Integer id) {
		
		mallOrderDetailService.deleteMallOrderDetail(id);
	}


}
