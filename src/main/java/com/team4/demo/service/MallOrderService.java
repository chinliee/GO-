package com.team4.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.dto.mall.FindMallOrderDto;
import com.team4.demo.model.dto.mall.MallOrderDto;
import com.team4.demo.model.entity.MallOrder;
import com.team4.demo.model.repository.MallOrderRepository;

@Service
public class MallOrderService {

	@Autowired
    private final MallOrderRepository mallOrderRepository;

    public MallOrderService(MallOrderRepository mallOrderRepository) {
        this.mallOrderRepository = mallOrderRepository;
    }

    public List<MallOrder> getAllMallOrders() {
        return mallOrderRepository.findAll();
    }
    
    //新增單筆mallOrder
    
	public MallOrder createMallOrder(MallOrderDto mallOrderDto) {
		
		MallOrder MallOrder = new MallOrder();
		MallOrder.setMemberId(mallOrderDto.getMemberId());
		MallOrder.setOrderTotalAmount(mallOrderDto.getOrderTotalAmount());
		MallOrder.setOrderStatus(mallOrderDto.getOrderStatus());
		MallOrder.setOrderTime(mallOrderDto.getOrderTime());
		MallOrder.setPaymentTime(mallOrderDto.getPaymentTime());
		MallOrder.setShippingTime(mallOrderDto.getShippingTime());
		MallOrder.setShippingAddress(mallOrderDto.getShippingAddress());
		
		return mallOrderRepository.save(MallOrder);
	}
	
	//根據會員id查詢此會員所有mallOrder
	
	public List<FindMallOrderDto> findByMember_MemberId(Integer memberId) {
		
		List<MallOrder> mallOrders = mallOrderRepository.findByMember_MemberId(memberId);
		List<FindMallOrderDto> findMallOrderDtos = new ArrayList<>();
		
		for(MallOrder mallOrder : mallOrders) {
			
			FindMallOrderDto findMallOrderDto = new FindMallOrderDto();
			findMallOrderDto.setOrderTotalAmount(mallOrder.getOrderTotalAmount());
			findMallOrderDto.setOrderStatus(mallOrder.getOrderStatus());
			findMallOrderDto.setOrderTime(mallOrder.getOrderTime());
			findMallOrderDto.setPaymentTime(mallOrder.getPaymentTime());
			findMallOrderDto.setShippingTime(mallOrder.getShippingTime());
			findMallOrderDto.setShippingAddress(mallOrder.getShippingAddress());
			
			findMallOrderDtos.add(findMallOrderDto);
			
		}
		
		return findMallOrderDtos;
		
	}
	
	
    //根據訂單編號刪除此訂單
	public void delete(Integer orderId) {
		
		mallOrderRepository.deleteById(orderId);
		
		
	}
	
	
    
}
