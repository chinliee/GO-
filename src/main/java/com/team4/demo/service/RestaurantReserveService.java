package com.team4.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team4.demo.controller.RestaurantReserveController;

import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveFindAllDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveInsertBackDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveInsertDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveMailDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveUpdateDto;
import com.team4.demo.model.dto.restaurantReserve.RestaurantReserveUpdateFindDto;
import com.team4.demo.model.dto.restaurantReserve.restaurantReserveEcpayDto;
import com.team4.demo.model.entity.Member;
import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.model.entity.RestaurantRecord;
import com.team4.demo.model.entity.RestaurantReserve;
import com.team4.demo.model.entity.RestaurantSeat;
import com.team4.demo.model.repository.MemberRepository;
import com.team4.demo.model.repository.RestaurantRepository;
import com.team4.demo.model.repository.RestaurantReserveRepository;
import com.team4.demo.model.repository.RestaurantSeatRepository;
import com.team4.demo.util.ecpay.payment.integration.AllInOne;
import com.team4.demo.util.ecpay.payment.integration.domain.ATMRequestObj;
import com.team4.demo.util.ecpay.payment.integration.domain.AioCheckOutALL;
import com.team4.demo.util.ecpay.payment.integration.domain.AioCheckOutOneTime;
import com.team4.demo.util.ecpay.payment.integration.domain.InvoiceObj;

@Service
@Transactional
public class RestaurantReserveService {

	@Autowired
	private RestaurantReserveRepository restaurantReserveRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private RestaurantSeatRepository restaurantSeatRepository;


//	餐廳方面查全部
	public List<RestaurantReserveFindAllDto> findAll(Integer restaurantId) {
	
	List<RestaurantReserve> restaurantReserveList=restaurantReserveRepository.findByRestaurant_restaurantId(restaurantId);

	List<RestaurantReserveFindAllDto> restaurantReserveFindAllDtoList = new ArrayList<>();
		
	for(RestaurantReserve restaurantReserve:restaurantReserveList) {
	
	RestaurantReserveFindAllDto reserveFindAllDto = new RestaurantReserveFindAllDto();
	
	reserveFindAllDto.setReservedId(restaurantReserve.getReservedId());
	reserveFindAllDto.setAmount(restaurantReserve.getAmount());
	reserveFindAllDto.setCustomer(restaurantReserve.getCustomer());
	reserveFindAllDto.setReserveDay(restaurantReserve.getReserveDay());
	reserveFindAllDto.setReserveTime(restaurantReserve.getReserveTime());
	reserveFindAllDto.setState(restaurantReserve.getState());
	reserveFindAllDto.setMemberId(restaurantReserve.getMember().getMemberId());
	reserveFindAllDto.setName(restaurantReserve.getMember().getName());
	
	restaurantReserveFindAllDtoList.add(reserveFindAllDto);
	
	}		
	return restaurantReserveFindAllDtoList;
	
	}
//	單筆查詢
	public RestaurantReserveUpdateFindDto findById(Integer reserveId ) {
		RestaurantReserve reserve = restaurantReserveRepository.findById(reserveId).orElse(null);
		
		RestaurantReserveUpdateFindDto dto =  new RestaurantReserveUpdateFindDto();
		
		
		dto.setState(reserve.getState());
		dto.setReserveId(reserve.getReservedId());
		
		return dto;
	}
	
	
	
	
//	新增前台邏輯
	public String insert(RestaurantReserveInsertDto dto) {
	    Integer restaurantId = dto.getRestaurantId();
	    Integer memberId = dto.getMemberId();
	    Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	    Member member = memberRepository.findById(memberId).orElse(null);

	    // 定義座位人數的轉換函數由其他表示方法STRING轉換成INT做邏輯處理
//	    從字串轉換整數
//	    跟要轉換的型別同一個名稱比較保險 xx-> switch(xx)
	    Function<String, Integer> convertTableForToInt = tableFor -> {
	        switch (tableFor) {
	        	case "二人桌":
	        		return 2;
	            case "四人桌":
	                return 4;
	            case "六人桌":
	                return 6;
	            case "八人桌":
	                return 8;
	            case "十人桌":
	                return 10;
	            case "十二人桌":
	                return 12;
	            case "十四人桌":
	                return 14;
	            default:
	                return 0; // 如果無法識別，返回 0 或其他適當的值
	        }
	    };

	    // 查詢相關餐廳的紀錄和座位
	    List<RestaurantRecord> restaurantRecords = restaurant.getRestaurantRecordList();
	    List<RestaurantSeat> restaurantSeats = restaurantSeatRepository.findByRestaurant_restaurantId(restaurantId);
	    // 宣告一筆新的訂單
	    RestaurantReserve restaurantReserve = new RestaurantReserve();

	    for (RestaurantRecord record : restaurantRecords) {
	        if ((dto.getReserveTime().isAfter(record.getStartTime()) || dto.getReserveTime().equals(record.getStartTime())) && dto.getReserveTime().isBefore(record.getEndTime())) {
	            // 檢查預約時間是否在營業時段內
	            for (RestaurantSeat seat : restaurantSeats) {
	            	//檢查餐廳之座位開放日期是否符合需求
	            	if(dto.getReserveDay().isEqual(seat.getOpenDay())) {
	                // 如果座位狀態是 "未預約"，則設置相應的座位
	                if ("未預約".equals(seat.getSeatState())) {
	                    // 檢查座位是否符合要求
	                    if (dto.getCustomer() <= convertTableForToInt.apply(seat.getTableFor())) {
//	                    	檢查座位的時段是否與營業時段符合
	                    	if (seat.getSeatTimePer().equals(record.getTimePer())) {
	                        Integer seatId = seat.getSeatId();
	                        RestaurantSeat restaurantSeat = restaurantSeatRepository.findById(seatId).orElse(null);
	                        restaurantReserve.setReserveDay(dto.getReserveDay());
	                        restaurantReserve.setReserveTime(dto.getReserveTime());
	                        restaurantReserve.setCustomer(dto.getCustomer());
	                        restaurantReserve.setRestaurant(restaurant);
	                        restaurantReserve.setMember(member);
	                        restaurantReserve.setState(dto.getState() != null ? dto.getState() : "已預約");
	                        restaurantReserve.setRestaurantSeat(restaurantSeat);
	                        restaurantReserve.setAmount(dto.getCustomer()*record.getPrice());
	                        // 將座位狀態更改爲已預約
	                        seat.setSeatState("已預約");
	                        restaurantSeatRepository.save(seat);
	                        restaurantReserveRepository.save(restaurantReserve);

	                        return restaurantReserve.getReservedId().toString();
	                    	}
	                     }	                    
	                   }
	            	}
	            }
	        }
	    }
	    return "預約失敗"; // 沒有可用的座位或預約時間不在營業時段內
	}

	public String stateUpdate(Integer reservedId,RestaurantReserveUpdateDto dto) {
		
		RestaurantReserve restaurantReserve = restaurantReserveRepository.findById(reservedId).orElse(null);
		
		if (restaurantReserve != null) {
	        if (dto.getState().equals("已取消")) {
	            // 如果狀態為已取消，則更新訂單狀態並將座位狀態設為未預約
	            restaurantReserve.setState(dto.getState());
	            
	            RestaurantSeat restaurantSeat = restaurantReserve.getRestaurantSeat();
	            if (restaurantSeat != null) {
	                restaurantSeat.setSeatState("未預約");
	                restaurantSeatRepository.save(restaurantSeat);
	            }
	            
	            restaurantReserveRepository.save(restaurantReserve);
	            return "修改成功";
	        }  if (dto.getState().equals("已結單")) {
	            // 如果狀態為已結單，則刪除相應的座位
	            RestaurantSeat restaurantSeat = restaurantReserve.getRestaurantSeat();
	            if (restaurantSeat != null) {
	                // 刪除相應的座位
	                restaurantSeatRepository.deleteById(restaurantSeat.getSeatId());
	            }
	            
	            // 刪除訂單
	            restaurantReserve.setState(dto.getState());
	            restaurantReserveRepository.save(restaurantReserve);
	            return "修改成功";
	        } else {
	            return "無效的訂單狀態";
	        }
	    } else {
	        return "找不到對應的訂單";
	    }
	}

//	後台新增邏輯
	public String insertback(RestaurantReserveInsertBackDto dto) {
	    Integer restaurantId = dto.getRestaurantId();
	    
	    Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	    
//	    從手機去抓會員Id 
	    List<Member> members = memberRepository.findByPhone(dto.getPhone());
	    if (!members.isEmpty()) {
//	        在台灣電話不會重複抓取符合資格第一位Id
	       dto.setMemberId(members.get(0).getMemberId());
	       

	    } else {
	        return null;
	    }
	


	    // 定義座位人數的轉換函數由其他表示方法STRING轉換成INT做邏輯處理
//	    從字串轉換整數
//	    跟要轉換的型別同一個名稱比較保險 xx-> switch(xx)
	    Function<String, Integer> convertTableForToInt = tableFor -> {
	        switch (tableFor) {
	        	case "二人桌":
	        		return 2;
	            case "四人桌":
	                return 4;
	            case "六人桌":
	                return 6;
	            case "八人桌":
	                return 8;
	            case "十人桌":
	                return 10;
	            case "十二人桌":
	                return 12;
	            case "十四人桌":
	                return 14;
	            default:
	                return 0; // 如果無法識別，返回 0 或其他適當的值
	        }
	    };

	    // 查詢相關餐廳的紀錄和座位
	    List<RestaurantRecord> restaurantRecords = restaurant.getRestaurantRecordList();
	    List<RestaurantSeat> restaurantSeats = restaurantSeatRepository.findByRestaurant_restaurantId(restaurantId);
	    // 宣告一筆新的訂單
	    RestaurantReserve restaurantReserve = new RestaurantReserve();

	    for (RestaurantRecord record : restaurantRecords) {
	        if ((dto.getReserveTime().isAfter(record.getStartTime()) || dto.getReserveTime().equals(record.getStartTime())) && dto.getReserveTime().isBefore(record.getEndTime())) {
	            // 檢查預約時間是否在營業時段內
	            for (RestaurantSeat seat : restaurantSeats) {
	            	//檢查餐廳之座位開放日期是否符合需求
	            	if(dto.getReserveDay().isEqual(seat.getOpenDay())) {
	                // 如果座位狀態是 "未預約"，則設置相應的座位
	                if ("未預約".equals(seat.getSeatState())) {
	                    // 檢查座位是否符合要求
	                    if (dto.getCustomer() <= convertTableForToInt.apply(seat.getTableFor())) {
//	                    	檢查座位的時段是否與營業時段符合
	                    	if (seat.getSeatTimePer().equals(record.getTimePer())) {
	                        Integer seatId = seat.getSeatId();
	                        RestaurantSeat restaurantSeat = restaurantSeatRepository.findById(seatId).orElse(null);

	                        restaurantReserve.setReserveDay(dto.getReserveDay());
	                        restaurantReserve.setReserveTime(dto.getReserveTime());
	                        restaurantReserve.setCustomer(dto.getCustomer());
	                        restaurantReserve.setRestaurant(restaurant);
	                        restaurantReserve.setMember(members.get(0));
	                        restaurantReserve.setState(dto.getState() != null ? dto.getState() : "已預約");
	                        restaurantReserve.setRestaurantSeat(restaurantSeat);
	                        restaurantReserve.setAmount(dto.getCustomer()*record.getPrice());
	                        // 將座位狀態更改爲已預約
	                        seat.setSeatState("已預約");
	                        restaurantSeatRepository.save(seat);
	                        restaurantReserveRepository.save(restaurantReserve);
	                        return "預約成功";
	                    	}
	                     }	                    
	                   }
	            	}
	            }
	        }
	    }
	    return "預約失敗"; // 沒有可用的座位或預約時間不在營業時段內
	}
//	前後三天
	public List<RestaurantReserveFindAllDto> findByReserveDayThree(Integer restaurantId,LocalDate ReserveDay){
		
	LocalDate startDate = ReserveDay.minusDays(3);
		
	LocalDate endDate = ReserveDay.plusDays(3);
	
	List<RestaurantReserve> restaurantReserveList = restaurantReserveRepository.findByRestaurant_restaurantIdAndReserveDayBetween(restaurantId, startDate, endDate);
		
	List<RestaurantReserveFindAllDto> restaurantReserveFindAllDtoList = new ArrayList<>();
	
	for(RestaurantReserve restaurantReserve:restaurantReserveList) {
	
	RestaurantReserveFindAllDto reserveFindAllDto = new RestaurantReserveFindAllDto();
	
	reserveFindAllDto.setReservedId(restaurantReserve.getReservedId());
	reserveFindAllDto.setAmount(restaurantReserve.getAmount());
	reserveFindAllDto.setCustomer(restaurantReserve.getCustomer());
	reserveFindAllDto.setReserveDay(restaurantReserve.getReserveDay());
	reserveFindAllDto.setReserveTime(restaurantReserve.getReserveTime());
	reserveFindAllDto.setState(restaurantReserve.getState());
	reserveFindAllDto.setMemberId(restaurantReserve.getMember().getMemberId());
	reserveFindAllDto.setName(restaurantReserve.getMember().getName());
	
	restaurantReserveFindAllDtoList.add(reserveFindAllDto);
	
	}		
	return restaurantReserveFindAllDtoList;
		
		
	}
//	單純實現手機尋找會員
	public List<RestaurantReserveFindAllDto> findByPhone(Integer restaurantId, String phone){
		
	List<RestaurantReserve> restaurantReserveList = restaurantReserveRepository.findByRestaurant_RestaurantIdAndMember_Phone(restaurantId,phone);
	
	List<RestaurantReserveFindAllDto> restaurantReserveFindAllDtoList = new ArrayList<>();
	
	for(RestaurantReserve restaurantReserve:restaurantReserveList) {
	
	RestaurantReserveFindAllDto reserveFindAllDto = new RestaurantReserveFindAllDto();
	
	reserveFindAllDto.setReservedId(restaurantReserve.getReservedId());
	reserveFindAllDto.setAmount(restaurantReserve.getAmount());
	reserveFindAllDto.setCustomer(restaurantReserve.getCustomer());
	reserveFindAllDto.setReserveDay(restaurantReserve.getReserveDay());
	reserveFindAllDto.setReserveTime(restaurantReserve.getReserveTime());
	reserveFindAllDto.setState(restaurantReserve.getState());
	reserveFindAllDto.setMemberId(restaurantReserve.getMember().getMemberId());
	reserveFindAllDto.setName(restaurantReserve.getMember().getName());
	
	restaurantReserveFindAllDtoList.add(reserveFindAllDto);
	
	}		
	return restaurantReserveFindAllDtoList;
	}
	
//	用月份查詢及電話查詢
	public List<RestaurantReserveFindAllDto> findByPhoneAndMonth(Integer restaurantId,String phone, Integer month) {
//	最大及最小訂單月
	LocalDate earliestDate = restaurantReserveRepository.findEarliestReservationDateByPhone(phone,restaurantId);
	LocalDate latestDate = restaurantReserveRepository.findLatestReservationDateByPhone(phone,restaurantId);
//	月份未提供就查全部
	LocalDate startDay;
	LocalDate endDay;
	if (month != null) {
//	獲取當年的月及第一天 透過這方法取得上個月最後一天及下個月第一天的範圍
	LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
	startDay = startOfMonth;
	endDay = startOfMonth.plusMonths(1).minusDays(1);
	} else {
// 	使用最早日期和最晚日期查詢
    startDay = earliestDate;
	endDay = latestDate;
	}
	List<RestaurantReserve> restaurantReserveList = restaurantReserveRepository.findByRestaurant_RestaurantIdAndMember_PhoneAndReserveDayBetween(restaurantId,phone, startDay, endDay);

	List<RestaurantReserveFindAllDto> restaurantReserveFindAllDtoList = new ArrayList<>();
	
	for(RestaurantReserve restaurantReserve:restaurantReserveList) {
	
	RestaurantReserveFindAllDto reserveFindAllDto = new RestaurantReserveFindAllDto();
	
	reserveFindAllDto.setReservedId(restaurantReserve.getReservedId());
	reserveFindAllDto.setAmount(restaurantReserve.getAmount());
	reserveFindAllDto.setCustomer(restaurantReserve.getCustomer());
	reserveFindAllDto.setReserveDay(restaurantReserve.getReserveDay());
	reserveFindAllDto.setReserveTime(restaurantReserve.getReserveTime());
	reserveFindAllDto.setState(restaurantReserve.getState());
	reserveFindAllDto.setMemberId(restaurantReserve.getMember().getMemberId());
	reserveFindAllDto.setName(restaurantReserve.getMember().getName());
	
	restaurantReserveFindAllDtoList.add(reserveFindAllDto);
	
	}		
	return restaurantReserveFindAllDtoList;
	
	}
//	用月份查詢及姓名查詢
	@SuppressWarnings("unlikely-arg-type")
	public List<RestaurantReserveFindAllDto> findByNameAndMonth(Integer restaurantId,String name, Integer month) {
	    // 定義查詢範圍的起始日期和結束日期
	    LocalDate startDay;
	    LocalDate endDay;
	    if (month != null && !month.equals("null")) {
	        // 如果指定了月份，則設置起始日期和結束日期為該月份的第一天和最後一天
	        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
	        startDay = startOfMonth;
	        endDay = startOfMonth.plusMonths(1).minusDays(1);
	    } else {
	        // 如果沒有指定月份，則根據姓名查詢的範圍為該用戶的最早預訂日期和最晚預訂日期
	        LocalDate earliestDate = restaurantReserveRepository.findEarliestReservationDateByName(name,restaurantId);
	        LocalDate latestDate = restaurantReserveRepository.findLatestReservationDateByName(name,restaurantId);
	        startDay = earliestDate;
	        endDay = latestDate;
	    }

	    // 根據姓名和日期範圍查詢預訂信息
	    List<RestaurantReserve> restaurantReserveList;
	    if (name != null && !name.isEmpty()) {
	        // 如果指定了姓名，則根據姓名和日期範圍查詢
	        restaurantReserveList = restaurantReserveRepository.findByRestaurant_RestaurantIdAndMember_NameAndReserveDayBetween(restaurantId,name, startDay, endDay);
	    } else {
	        // 如果沒有指定姓名，則僅根據日期範圍查詢
	        restaurantReserveList = restaurantReserveRepository.findByReserveDayBetween(startDay, endDay);
	    }

	    List<RestaurantReserveFindAllDto> restaurantReserveFindAllDtoList = new ArrayList<>();
	    for (RestaurantReserve restaurantReserve : restaurantReserveList) {
	        RestaurantReserveFindAllDto reserveFindAllDto = new RestaurantReserveFindAllDto();
	        reserveFindAllDto.setReservedId(restaurantReserve.getReservedId());
	        reserveFindAllDto.setAmount(restaurantReserve.getAmount());
	        reserveFindAllDto.setCustomer(restaurantReserve.getCustomer());
	        reserveFindAllDto.setReserveDay(restaurantReserve.getReserveDay());
	        reserveFindAllDto.setReserveTime(restaurantReserve.getReserveTime());
	        reserveFindAllDto.setState(restaurantReserve.getState());
	        reserveFindAllDto.setMemberId(restaurantReserve.getMember().getMemberId());
	        reserveFindAllDto.setName(restaurantReserve.getMember().getName());
	        restaurantReserveFindAllDtoList.add(reserveFindAllDto);
	    }

	    return restaurantReserveFindAllDtoList;
	}
	
	
	public List<RestaurantReserveFindAllDto> findByMemberIdForMember(Integer memberId,String state){
		
		List<RestaurantReserve> restaurantReserveList = restaurantReserveRepository.findByMember_MemberIdAndState(memberId,state);
		List<RestaurantReserveFindAllDto> restaurantReserveFindAllDtoList = new ArrayList<>();
		
		for(RestaurantReserve restaurantReserve:restaurantReserveList) {
		
		RestaurantReserveFindAllDto reserveFindAllDto = new RestaurantReserveFindAllDto();
		
		reserveFindAllDto.setReservedId(restaurantReserve.getReservedId());
		reserveFindAllDto.setAmount(restaurantReserve.getAmount());
		reserveFindAllDto.setCustomer(restaurantReserve.getCustomer());
		reserveFindAllDto.setReserveDay(restaurantReserve.getReserveDay());
		reserveFindAllDto.setReserveTime(restaurantReserve.getReserveTime());
		reserveFindAllDto.setState(restaurantReserve.getState());
		reserveFindAllDto.setMemberId(restaurantReserve.getMember().getMemberId());
		reserveFindAllDto.setName(restaurantReserve.getMember().getName());
		reserveFindAllDto.setRestaurantName(restaurantReserve.getRestaurant().getName());
		restaurantReserveFindAllDtoList.add(reserveFindAllDto);
		
		}		
		return restaurantReserveFindAllDtoList;
	}
	
		public List<RestaurantReserveFindAllDto> findByMemberIdForMemberAndStateIn(Integer memberId,List<String> states){
		
	    List<RestaurantReserve> restaurantReserveList = restaurantReserveRepository.findByMember_MemberIdAndStateIn(memberId, states);
		List<RestaurantReserveFindAllDto> restaurantReserveFindAllDtoList = new ArrayList<>();
		
		for(RestaurantReserve restaurantReserve:restaurantReserveList) {
		
		RestaurantReserveFindAllDto reserveFindAllDto = new RestaurantReserveFindAllDto();
		
		reserveFindAllDto.setReservedId(restaurantReserve.getReservedId());
		reserveFindAllDto.setAmount(restaurantReserve.getAmount());
		reserveFindAllDto.setCustomer(restaurantReserve.getCustomer());
		reserveFindAllDto.setReserveDay(restaurantReserve.getReserveDay());
		reserveFindAllDto.setReserveTime(restaurantReserve.getReserveTime());
		reserveFindAllDto.setState(restaurantReserve.getState());
		reserveFindAllDto.setMemberId(restaurantReserve.getMember().getMemberId());
		reserveFindAllDto.setName(restaurantReserve.getMember().getName());
		reserveFindAllDto.setRestaurantName(restaurantReserve.getRestaurant().getName());
		restaurantReserveFindAllDtoList.add(reserveFindAllDto);
		
		}		
		return restaurantReserveFindAllDtoList;
	}
	
	
	
	
	public void delete(Integer id) {
		restaurantReserveRepository.deleteById(id);
	}
			
	public List<RestaurantReserveFindAllDto> findByState(Integer restaurantId,String state){
		
	List<RestaurantReserve> restaurantReservelList = restaurantReserveRepository.findByRestaurant_restaurantIdAndState(restaurantId,state);
	
	List<RestaurantReserveFindAllDto> reserveFindAllDtoList = new ArrayList<>();
	
	for(RestaurantReserve restaurantReserve : restaurantReservelList) {
		
	RestaurantReserveFindAllDto reserveFindAllDto = new RestaurantReserveFindAllDto();
	
	reserveFindAllDto.setReservedId(restaurantReserve.getReservedId());
	reserveFindAllDto.setAmount(restaurantReserve.getAmount());
	reserveFindAllDto.setCustomer(restaurantReserve.getCustomer());
	reserveFindAllDto.setReserveDay(restaurantReserve.getReserveDay());
	reserveFindAllDto.setReserveTime(restaurantReserve.getReserveTime());
	reserveFindAllDto.setState(restaurantReserve.getState());
	reserveFindAllDto.setMemberId(restaurantReserve.getMember().getMemberId());
	reserveFindAllDto.setName(restaurantReserve.getMember().getName());
	
	reserveFindAllDtoList.add(reserveFindAllDto);
		
		
	}
	return reserveFindAllDtoList;
		
	}
	
	
	public String sendMail(Integer memberId,Integer restaurantId,RestaurantReserveMailDto dto) {
		   
	Member member = memberRepository.findById(memberId).orElse(null);		
		
	String to = member.getEmail();
	
	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
	
	String restaurantname = restaurant.getName();
	
	String messageMail = "覓食GO\n日期：" + dto.getReserveDay() +"\n時間：" 
            + dto.getReserveTime() +"\n人數:" 
            + dto.getCustomer()+"人\n\n感謝您的預約\n屆時請準時\n我們期待在這個時間爲您提供優質的服務！";
	
	
	
	// 設置屬性
	Properties props = new Properties();
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.port", "587");

	// 取得屬性
	Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	protected PasswordAuthentication getPasswordAuthentication() {
	return new PasswordAuthentication("信箱", "信箱");
	}
	});

	try {
	            // 宣告MimeMessage 來裝session
	            MimeMessage message = new MimeMessage(session);

	           
	            message.setFrom(new InternetAddress("信箱"));

	            
	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	            // 主旨
	            message.setSubject(restaurantname +"餐廳預約通知");

	            // 內容
	            message.setText(messageMail);

	            // 寄送內容
	            Transport.send(message);
	        } catch (MessagingException mex) {
	           mex.printStackTrace();
	        }
	        return "OK";
	    }
	
		public String ecpayCheckout(restaurantReserveEcpayDto dto,Integer id) {
		
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		
		LocalDateTime now = LocalDateTime.now();
		 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	    String formattedDateTime = now.format(formatter);
		       
		AllInOne all = new AllInOne("");
		
		AioCheckOutALL obj = new AioCheckOutALL();
//		隨機編號
		obj.setMerchantTradeNo(uuId);
//		時間
		obj.setMerchantTradeDate(formattedDateTime);
//		金額
		obj.setTotalAmount(dto.getAmount());
		obj.setStoreID("12131231");
		obj.setTradeDesc("訂單金額");
		obj.setItemName(dto.getRestaurantName()+"訂單金額");
		obj.setReturnURL("http://211.23.128.214:5000");
		obj.setNeedExtraPaidInfo("N");
		obj.setInvoiceMark("測試用");

		String form = all.aioCheckOut(obj, null);
		System.out.println(obj.getPaymentInfoURL());
		RestaurantReserve reserve = restaurantReserveRepository.findById(id).orElse(null);
		
		
			reserve.setState("已付款");
			restaurantReserveRepository.save(reserve);


		
		return form;
		
		
	}

		
	
	
	
	public List<Object[]> getMonthlySummary(Integer memberId) {
        return restaurantReserveRepository.getMonthlySummary(memberId);
    }
	


}

