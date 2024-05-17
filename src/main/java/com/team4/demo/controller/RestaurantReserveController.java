package com.team4.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveFindAllDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveInsertBackDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveInsertDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveMailDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveUpdateDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveUpdateFindDto;
import com.team4.demo.model.dto.restaurantReserve.restaurantReserveEcpayDto;
import com.team4.demo.service.RestaurantReserveService;

@RestController
@CrossOrigin
public class RestaurantReserveController {

	@Autowired
	private RestaurantReserveService restaurantReserveService;

	@GetMapping("/restaurantReserve/{restaurantId}")
	public List<RestaurantReserveFindAllDto> findAll(@PathVariable("restaurantId") Integer restaurantId){		
		return restaurantReserveService.findAll(restaurantId);
	}

	@PostMapping("/restaurantReserve/insert")
	public String insertMember(@RequestBody RestaurantReserveInsertDto dto) {
				
	return restaurantReserveService.insert(dto);
	}

	@PostMapping("/restaurantReserve/insertBack")
	public String insertBack(@RequestBody RestaurantReserveInsertBackDto dto) {
				
	return restaurantReserveService.insertback(dto);
	}

	@GetMapping("/restaurantReserve/reserveDay/{restaurantId}")
	public List<RestaurantReserveFindAllDto> findbrReserveDayThree(@PathVariable("restaurantId") Integer restaurantId,
																		 @RequestParam("reserveDay") LocalDate reserveDay ){
		
	return restaurantReserveService.findByReserveDayThree(restaurantId, reserveDay);
	
	
	}

	@GetMapping("/restaurantReserve/phone/{restaurantId}")
	public  List<RestaurantReserveFindAllDto>  findbyPhone(@PathVariable("restaurantId") Integer restaurantId,
										 @RequestParam("phone") String phone ){
	
	return restaurantReserveService.findByPhone(restaurantId, phone);
	}

	@GetMapping("/restaurantReserves/findByPhone/{restaurantId}")
	public  List<RestaurantReserveFindAllDto> findByPhoneAndMonth(@PathVariable("restaurantId") Integer restaurantId,
																  @RequestParam("phone") String phone,
	        													  @RequestParam(value = "month", required = false) String month) {	
	Integer monthValue = null;
	if (month != null && !month.isEmpty() && !month.equals("null")) {
	    monthValue = Integer.valueOf(month);
	}
   
	return restaurantReserveService.findByPhoneAndMonth(restaurantId,phone, monthValue);


	}
	
	@GetMapping("/restaurantReserves/findByName/{restaurantId}")
	public List<RestaurantReserveFindAllDto> findByNameAndMonth(@PathVariable("restaurantId") Integer restaurantId,
																@RequestParam(value ="name", required = false) String name,
	        													@RequestParam(value = "month", required = false) String month) {	
	Integer monthValue = null;
	if (month != null && !month.isEmpty() && !month.equals("null")) {
	    monthValue = Integer.valueOf(month);
	}	   
	return restaurantReserveService.findByNameAndMonth(restaurantId,name, monthValue);
	
	}

	@PutMapping("/restaurantStateUpdate/{reserveId}")
	public String stateUpdate(@PathVariable("reserveId") Integer reserveId,@RequestBody RestaurantReserveUpdateDto dto) {
		
	String aaa = restaurantReserveService.stateUpdate(reserveId, dto);
	
	return aaa;
	}

	@GetMapping("/restaurantReserveId/{reserveId}")
	public RestaurantReserveUpdateFindDto findById(@PathVariable("reserveId") Integer reserveId) {
		
	return restaurantReserveService.findById(reserveId);	
	}
	
	@GetMapping("/order/member/{memberId}")
	public List<RestaurantReserveFindAllDto> findByMemberIdForMember(@PathVariable ("memberId") Integer memberId,
														   @RequestParam("state") String state){
		
	return restaurantReserveService.findByMemberIdForMember(memberId,state);
		
	
	}
	@GetMapping("/order/memberState/{memberId}")
	public List<RestaurantReserveFindAllDto> findByMemberIdForMemberAndStateIn(@PathVariable ("memberId") Integer memberId,
														                       @RequestParam("states") List<String> states){
		
	return restaurantReserveService.findByMemberIdForMemberAndStateIn(memberId,states);
		
	
	}
	
	
	
	
	
	
	@GetMapping("/restaurantReserves/restaurantState/{restaurantId}")
	public List<RestaurantReserveFindAllDto> findByState(@PathVariable("restaurantId")Integer restaurantId,
														 @RequestParam("state") String state){
		
	return restaurantReserveService.findByState(restaurantId,state);
		
	
	}
	
	@DeleteMapping("/restaurantReserveDelete/{reservedId}")
	public void delete(@PathVariable("reservedId") Integer reservedId) {
		restaurantReserveService.delete(reservedId);
	}
	
	@PostMapping("/restaurantReserveEmail/{memberId}/{restaurantId}")
	public String email(@PathVariable("memberId") Integer memberId,
						@PathVariable("restaurantId") Integer restaurantId,
						@RequestBody RestaurantReserveMailDto dto  ) {
		
		restaurantReserveService.sendMail(memberId,restaurantId,dto);
		
		return"寄送成功";
		
	}
    @GetMapping("/membermonth/{memberId}")
    public List<Object[]> getMonthlySummary(@PathVariable Integer memberId) {
    return restaurantReserveService.getMonthlySummary(memberId);
    }
        
	@PostMapping("/restaurantReserve/ecpayCheckout/{id}")
	public String ecpayCheckout(@PathVariable("id") Integer reservedId,
								@RequestBody restaurantReserveEcpayDto dto) {
	String aioCheckOutALLForm = restaurantReserveService.ecpayCheckout(dto,reservedId);
			
	return aioCheckOutALLForm;
		}
	}
