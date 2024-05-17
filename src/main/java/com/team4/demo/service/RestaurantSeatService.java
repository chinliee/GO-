package com.team4.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.demo.model.dto.restaurantSeat.RestaurantSeatDto;
import com.team4.demo.model.dto.restaurantSeat.RestaurantSeatinsertdto;
import com.team4.demo.model.dto.restaurantSeat.RestaurantSeatupdata;
import com.team4.demo.model.dto.restaurantSeat.RestaurantSeatFindAllDto;
import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.model.entity.RestaurantRecord;
import com.team4.demo.model.entity.RestaurantSeat;
import com.team4.demo.model.repository.RestaurantRecordRepository;
import com.team4.demo.model.repository.RestaurantRepository;
import com.team4.demo.model.repository.RestaurantSeatRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RestaurantSeatService {

	@Autowired
	private RestaurantSeatRepository restaurantSeatRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private RestaurantRecordRepository restaurantRecordRepository;

	// 新增座位
	public RestaurantSeat insertRestaurantSeat(RestaurantSeatinsertdto dto) {
		RestaurantSeat restaurantSeat = new RestaurantSeat();
		Integer restaurantId = dto.getRestaurantId();
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

		List<RestaurantRecord> restaurantRecordList = restaurant.getRestaurantRecordList();

		Integer getrecordId = null;
		for (RestaurantRecord record : restaurantRecordList) {
			if (dto.getSeatTimePer().equals(record.getTimePer())) {
				System.out.println(2);
				if((dto.getOpenTime().isAfter(record.getStartTime())|| dto.getOpenTime().equals(record.getStartTime()))&&dto.getOpenTime().isBefore(record.getEndTime())) {
					System.out.println(1);
					getrecordId = record.getRestaurantrecordId();
				}
			}
		}

		if (getrecordId != null) {

			restaurantSeat.setSeatTimePer(dto.getSeatTimePer());
			restaurantSeat.setRestaurant(restaurant);
			restaurantSeat.setTableFor(dto.getTableFor());
			restaurantSeat.setOpenTime(dto.getOpenTime());
			restaurantSeat.setOpenDay(dto.getOpenDay());
			// 設置默認值為未預約
			restaurantSeat.setSeatState(dto.getSeatState() != null ? dto.getSeatState() : "未預約");

			RestaurantRecord restaurantRecord = restaurantRecordRepository.findById(getrecordId).orElse(null);
			restaurantSeat.setRestaurantRecord(restaurantRecord);

			return restaurantSeatRepository.save(restaurantSeat);
		} else {
			return null;
		}

	}

	// 刪除
	public void deletebySeatid(Integer restaurantseatid) {

		restaurantSeatRepository.deleteById(restaurantseatid);
	}

	// 自家餐廳查詢全部
	public List<RestaurantSeatFindAllDto> FindAll(Integer restaurantid) {
		List<RestaurantSeat> restaurantSeatList = restaurantSeatRepository
				.findByRestaurant_restaurantId(restaurantid);
		List<RestaurantSeatFindAllDto> restaurantSeatFindAllDtoList = new ArrayList<>();
		
		for(RestaurantSeat restaurantSeat : restaurantSeatList) {
		
		RestaurantSeatFindAllDto restaurantSeatFindAllDto = new RestaurantSeatFindAllDto();
		
		restaurantSeatFindAllDto.setSeatId(restaurantSeat.getSeatId());
		restaurantSeatFindAllDto.setOpenDay(restaurantSeat.getOpenDay());
		restaurantSeatFindAllDto.setOpenTime(restaurantSeat.getOpenTime());
		restaurantSeatFindAllDto.setSeatState(restaurantSeat.getSeatState());
		restaurantSeatFindAllDto.setTableFor(restaurantSeat.getTableFor());
		restaurantSeatFindAllDto.setSeatTimePer(restaurantSeat.getSeatTimePer());
			
		restaurantSeatFindAllDtoList.add(restaurantSeatFindAllDto);
		
		}
		
		

		return restaurantSeatFindAllDtoList;
	}

	// 自家餐廳幾人桌查詢
	public List<RestaurantSeatFindAllDto> Findbytable(Integer restaurantId, String tableFor) {
		List<RestaurantSeat> restaurantSeatList = restaurantSeatRepository
				.findByRestaurant_restaurantIdAndTableForContaining(restaurantId, tableFor);
		List<RestaurantSeatFindAllDto> restaurantSeatFindAllDtoList = new ArrayList<>();
		
		for(RestaurantSeat restaurantSeat : restaurantSeatList) {
		
		RestaurantSeatFindAllDto restaurantSeatFindAllDto = new RestaurantSeatFindAllDto();
		
		restaurantSeatFindAllDto.setSeatId(restaurantSeat.getSeatId());
		restaurantSeatFindAllDto.setOpenDay(restaurantSeat.getOpenDay());
		restaurantSeatFindAllDto.setOpenTime(restaurantSeat.getOpenTime());
		restaurantSeatFindAllDto.setSeatState(restaurantSeat.getSeatState());
		restaurantSeatFindAllDto.setTableFor(restaurantSeat.getTableFor());
		restaurantSeatFindAllDto.setSeatTimePer(restaurantSeat.getSeatTimePer());
			
		restaurantSeatFindAllDtoList.add(restaurantSeatFindAllDto);
		
		}
		
		

		return restaurantSeatFindAllDtoList;
	}

	// 自家餐廳預約狀態
	public List<RestaurantSeatFindAllDto> Findbystate(Integer restaurantId, String seatState) {
		List<RestaurantSeat> restaurantSeatList = restaurantSeatRepository
				.findByRestaurant_restaurantIdAndSeatState(restaurantId, seatState);
		List<RestaurantSeatFindAllDto> restaurantSeatFindAllDtoList = new ArrayList<>();
		
		for(RestaurantSeat restaurantSeat : restaurantSeatList) {
		
		RestaurantSeatFindAllDto restaurantSeatFindAllDto = new RestaurantSeatFindAllDto();
		
		restaurantSeatFindAllDto.setSeatId(restaurantSeat.getSeatId());
		restaurantSeatFindAllDto.setOpenDay(restaurantSeat.getOpenDay());
		restaurantSeatFindAllDto.setOpenTime(restaurantSeat.getOpenTime());
		restaurantSeatFindAllDto.setSeatState(restaurantSeat.getSeatState());
		restaurantSeatFindAllDto.setTableFor(restaurantSeat.getTableFor());
		restaurantSeatFindAllDto.setSeatTimePer(restaurantSeat.getSeatTimePer());
			
		restaurantSeatFindAllDtoList.add(restaurantSeatFindAllDto);
		
		}
		
		

		return restaurantSeatFindAllDtoList;
	}
//	搜尋前後五天
	public List<RestaurantSeatFindAllDto> findByDateFive(Integer restaurantId, LocalDate openDay) {
// 		前後五天日期範圍
//		減
        LocalDate startDate = openDay.minusDays(5);
//      加
        LocalDate endDate = openDay.plusDays(5);
        
        List<RestaurantSeat> restaurantSeatList =restaurantSeatRepository.findByRestaurant_restaurantIdAndOpenDayBetween(restaurantId, startDate, endDate);
       
        List<RestaurantSeatFindAllDto> restaurantSeatFindAllDtoList = new ArrayList<>();

		for(RestaurantSeat restaurantSeat : restaurantSeatList) {
		
		RestaurantSeatFindAllDto restaurantSeatFindAllDto = new RestaurantSeatFindAllDto();
		
		restaurantSeatFindAllDto.setSeatId(restaurantSeat.getSeatId());
		restaurantSeatFindAllDto.setOpenDay(restaurantSeat.getOpenDay());
		restaurantSeatFindAllDto.setOpenTime(restaurantSeat.getOpenTime());
		restaurantSeatFindAllDto.setSeatState(restaurantSeat.getSeatState());
		restaurantSeatFindAllDto.setTableFor(restaurantSeat.getTableFor());
		restaurantSeatFindAllDto.setSeatTimePer(restaurantSeat.getSeatTimePer());
			
		restaurantSeatFindAllDtoList.add(restaurantSeatFindAllDto);
		
		}
		
		

		return restaurantSeatFindAllDtoList;
    }
	

	public List<RestaurantSeatFindAllDto> findByTimePer(Integer restaurantId,String timePer){
		
	List<RestaurantSeat> restaurantSeatList = restaurantSeatRepository
			.findByRestaurant_restaurantIdAndSeatTimePer(restaurantId, timePer);
	
	List<RestaurantSeatFindAllDto> restaurantSeatFindAllDtoList = new ArrayList<>();

		for(RestaurantSeat restaurantSeat : restaurantSeatList) {
		
		RestaurantSeatFindAllDto restaurantSeatFindAllDto = new RestaurantSeatFindAllDto();
		
		restaurantSeatFindAllDto.setSeatId(restaurantSeat.getSeatId());
		restaurantSeatFindAllDto.setOpenDay(restaurantSeat.getOpenDay());
		restaurantSeatFindAllDto.setOpenTime(restaurantSeat.getOpenTime());
		restaurantSeatFindAllDto.setSeatState(restaurantSeat.getSeatState());
		restaurantSeatFindAllDto.setTableFor(restaurantSeat.getTableFor());
		restaurantSeatFindAllDto.setSeatTimePer(restaurantSeat.getSeatTimePer());
			
		restaurantSeatFindAllDtoList.add(restaurantSeatFindAllDto);
		
		}
		
		

		return restaurantSeatFindAllDtoList;

	}
	

	// 餐廳自家單筆修改(查詢)
	// 映射
	// mapToDTO以Rest)aurantRecord作為參數
	private RestaurantSeatDto mapToDTO(RestaurantSeat restaurantSeat) {
		RestaurantSeatDto dto = new RestaurantSeatDto();
		// 將RestaurantSeatd映射到RestaurantSeatDto
		dto.setSeatId(restaurantSeat.getSeatId());
		dto.setTableFor(restaurantSeat.getTableFor());
		dto.setSeatState(restaurantSeat.getSeatState());
		dto.setSeatTimePer(restaurantSeat.getSeatTimePer());
		dto.setOpenDay(restaurantSeat.getOpenDay());
		dto.setOpenTime(restaurantSeat.getOpenTime());
		// 透過此方法來獲取外鍵
		// dto.setRestaurantRecordId(restaurantSeat.getRestaurantRecord().getRestaurantrecordId());
		// dto.setRestaurantId(restaurantSeat.getRestaurant().getRestaurantId());
		return dto;
	}

	public Optional<RestaurantSeatDto> getSeatById(Integer seatId) {
		Optional<RestaurantSeat> restaurantRecordOptional = restaurantSeatRepository.findById(seatId);
		// 如果找到紀錄mapToDTO的方法會被請求
		return restaurantRecordOptional.map(this::mapToDTO);
	}

	// 修改
	public RestaurantSeat Seattableforupdata(Integer seatId, RestaurantSeatupdata dto) {
		RestaurantSeat restaurantSeat = restaurantSeatRepository
				.findById(seatId).orElse(null);
		Integer restaurantId = dto.getRestaurantId();

		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
		// 透過ORM找出餐廳的營業紀錄
		List<RestaurantRecord> restaurantRecordList = restaurant.getRestaurantRecordList();

		boolean getTimePer = false;
		// 修改的值是否存在於營業表單中
		for (RestaurantRecord record : restaurantRecordList) {
			if (dto.getSeatTimePer().equals(record.getTimePer())) {
				if((dto.getOpenTime().isAfter(record.getStartTime())|| dto.getOpenTime().equals(record.getStartTime()))&&dto.getOpenTime().isBefore(record.getEndTime())) {				
				getTimePer = true;
				break;
				}
			}
		}
		System.out.println(getTimePer);
		if (getTimePer) {
			restaurantSeat.setOpenTime(dto.getOpenTime());
			restaurantSeat.setSeatTimePer(dto.getSeatTimePer());
			restaurantSeat.setRestaurant(restaurant);
			restaurantSeat.setSeatId(seatId);
			restaurantSeat.setTableFor(dto.getTableFor());
			restaurantSeat.setSeatState(dto.getSeatState());
			restaurantSeat.setOpenDay(dto.getOpenDay());
			return restaurantSeatRepository.save(restaurantSeat);
		} else {

			return null;
		}
	}
//	顯示未預約的日期
	public List<LocalDate> findOpenDates(Integer restaurantId) {
		List<RestaurantSeat> restaurantSeatList = restaurantSeatRepository
				.findByRestaurant_restaurantIdAndSeatState(restaurantId, "未預約");

		Set<LocalDate> uniqueDates = new HashSet<>();
		for (RestaurantSeat seat : restaurantSeatList) {
			if (seat.getOpenDay() != null) {
				uniqueDates.add(seat.getOpenDay());
			}
		}
		List<LocalDate> openDate = new ArrayList<>(uniqueDates);

		return openDate;
	}
//	顯示時間
	public List<LocalTime> findbytimepr(Integer restaurantId, LocalDate openDay,String tableFor){
		 // 根據餐廳ID、座位狀態、開放日期和桌號查找座位時段
				
        List<RestaurantSeat> seats = restaurantSeatRepository.findByRestaurant_RestaurantIdAndSeatStateAndOpenDayAndTableFor(restaurantId, "未預約", openDay, tableFor);
        
        Set<LocalTime> uniqueStartTimes = new HashSet<>(); // 使用集合來存儲唯一的開始時間
        
        // 對於每個座位，找到相關的營業時段，並添加開始時間到 uniqueStartTimes 集合中
        for (RestaurantSeat seat : seats) {             
                uniqueStartTimes.add(seat.getOpenTime());      
        }    
        // 將唯一的開始時間轉換為列表
        List<LocalTime> startTimes = new ArrayList<>(uniqueStartTimes);
        
        return startTimes;
    }
	public Integer findbytimeprgetprice(Integer restaurantId, LocalDate openDay,LocalTime opentime ,String tableFor) {
		
		List<RestaurantSeat> seats = restaurantSeatRepository.findByRestaurant_RestaurantIdAndSeatStateAndOpenDayAndTableFor(restaurantId, "未預約", openDay, tableFor);	    

		
		for (RestaurantSeat seat : seats) {

			System.out.println(seat.getRestaurantRecord().getTimePer());
	       
			if (seat.getOpenTime().equals(opentime)) {

	    		System.out.println(seat.getRestaurantRecord().getRestaurantrecordId());
	    		
	            String seatTimePer = seat.getSeatTimePer();

	            String recordTimePer = seat.getRestaurantRecord().getTimePer();
	           	            
	            if (seatTimePer.equals(recordTimePer)) {
	                return seat.getRestaurantRecord().getPrice();
	            }
	        }
	    }
	    
	    return null;
	}
	
	}
