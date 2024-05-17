package com.team4.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.dto.mall.CreateMallOrderDetail;
import com.team4.demo.model.entity.MallOrderDetail;
import com.team4.demo.model.repository.MallOrderDetailRepository;

@Service
public class MallOrderDetailService {

	@Autowired
    private final MallOrderDetailRepository mallOrderDetailRepository;

    public MallOrderDetailService(MallOrderDetailRepository mallOrderDetailRepository) {
        this.mallOrderDetailRepository = mallOrderDetailRepository;
    }

    public List<MallOrderDetail> getAllMallOrderDetails() {
        return mallOrderDetailRepository.findAll();
    }
    
    
    //新增單筆mallOrderDetail
	public MallOrderDetail createMallOrderDetail(CreateMallOrderDetail createmallOrderDetailDto) {
		
		MallOrderDetail mallOrderDetail = new MallOrderDetail();
		mallOrderDetail.setOrderId(createmallOrderDetailDto.getOrderId());
		mallOrderDetail.setProductId(createmallOrderDetailDto.getProductId());
		mallOrderDetail.setProductPrice(createmallOrderDetailDto.getProductPrice());
		mallOrderDetail.setQuantity(createmallOrderDetailDto.getQuantity());
		mallOrderDetail.setOrderTime(createmallOrderDetailDto.getOrderTime());
				
		return mallOrderDetailRepository.save(mallOrderDetail);
	}
	
	//刪除單筆mallOrderDetail
	
	public void deleteMallOrderDetail(Integer id) {
		System.out.println("OK" +id);
		
		mallOrderDetailRepository.deleteById(id);
		
		
	}

}
